package co.dolmen.sid;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.utilidades.DataSpinner;
import co.dolmen.sid.utilidades.ViewPagerAdapter;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class EjecutaActividad extends AppCompatActivity {

    private ActividadOperativa actividadOperativa;
    private TextView txtTituloTabActividad;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String TAG = "programacion";

    AlertDialog.Builder alert;

    //--Fragment
    private FragmentFotoAntes fragmentFotoAntes;
    private FragmentFotoDespues fragmentFotoDespues;
    private FragmentMateriales fragmentMateriales;
    private FragmentInformacion fragmentInformacion;
    private FragmentElemento fragmentElemento;

    private FloatingActionButton fabGuardar;
    private FloatingActionButton fabCancelar;

    private ArrayList<DataSpinner> barrioActividad;
    private ArrayList<MovimientoArticulo> movimientoArticulos;
    private ArrayList<DataSpinner> tipoActividad;
    private ArrayList<DataSpinner> estadoActividad;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecuta_actividad);

        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        conn = new BaseDatos(EjecutaActividad.this);
        database = conn.getReadableDatabase();

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);

        Intent i = getIntent();
        actividadOperativa = (ActividadOperativa)i.getSerializableExtra("actividadOperativa");

        txtTituloTabActividad  = findViewById(R.id.txt_titulo_tab_actividad);
        txtTituloTabActividad.setText("Act No "+actividadOperativa.getIdActividad()+" / Elem No "+actividadOperativa.getElemento().getElemento_no());

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        Bundle bundle = new Bundle();
        bundle.putSerializable("actividadOperativa",actividadOperativa);

        fragmentFotoAntes   = new FragmentFotoAntes();
        fragmentFotoDespues = new FragmentFotoDespues();
        fragmentMateriales  = new FragmentMateriales();
        fragmentInformacion = new FragmentInformacion();
        fragmentElemento    = new FragmentElemento();
        fragmentFotoAntes.setArguments(bundle);
        fragmentFotoDespues.setArguments(bundle);
        fragmentInformacion.setArguments(bundle);
        fragmentMateriales.setArguments(bundle);
        fragmentElemento.setArguments(bundle);

        tabLayout.setupWithViewPager(viewPager);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fragmentFotoAntes,"Antes");
        viewPagerAdapter.addFragment(fragmentFotoDespues,"Despues");
        viewPagerAdapter.addFragment(fragmentMateriales,"Material");
        viewPagerAdapter.addFragment(fragmentInformacion,"Info");
        viewPagerAdapter.addFragment(fragmentElemento,"Elem");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_material);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_info);
        tabLayout.getTabAt(4).setIcon(R.drawable.icon_marker);

        //--
        fabGuardar = findViewById(R.id.fab_guardar);
        fabCancelar = findViewById(R.id.fab_cancelar);

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar(view);
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Cancelar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(R.string.alert_cancelar_actividad);
                alert.setPositiveButton(R.string.btn_aceptar,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(EjecutaActividad.this,ListaActividad.class);
                        i.putExtra("actividadOperativa",actividadOperativa);
                        startActivity(i);
                        EjecutaActividad.this.finish();
                    }
                });
                alert.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.create().show();
            }
        });
    }

    private void guardar(final View view){
        //--
        barrioActividad     = fragmentInformacion.barrioList;
        tipoActividad       = fragmentInformacion.tipoActividadList;
        estadoActividad     = fragmentInformacion.estadoActividadList;
        movimientoArticulos = fragmentMateriales.movimientoArticuloArrayList;

        actividadOperativa.setFechaEjecucion(new Date());


        if(validarFotoAntes()){
            if(validarFotoDespues()){
                if(validarInfo(view)){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        guardarFormulario('R', database);
                        //Snackbar.make(view, "Guardar Remoto", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        //
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                //Snackbar.make(view, "Guardar local", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                guardarFormulario('L', database);
                            }
                        });
                        alert.create().show();
                    }
                }
            }
            else{
                Snackbar.make(view, "Foto despues es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        else{
            Snackbar.make(view, "Foto antes es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private boolean validarFotoAntes(){
        if (fragmentFotoAntes.encodeStringFoto_1.isEmpty() &&  fragmentFotoAntes.encodeStringFoto_2.isEmpty() &&
                fragmentFotoAntes.encodeStringFoto_3.isEmpty() && fragmentFotoAntes.encodeStringFoto_4.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validarFotoDespues(){
        if (fragmentFotoDespues.encodeStringFoto_1.isEmpty() &&  fragmentFotoDespues.encodeStringFoto_2.isEmpty() &&
                fragmentFotoDespues.encodeStringFoto_3.isEmpty() && fragmentFotoDespues.encodeStringFoto_4.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validarInfo(View view){
        if(actividadOperativa.getElemento().getId()==0){
            Snackbar.make(view, "Elemento es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        else{
            if(barrioActividad.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getId()==0){
                Snackbar.make(view, "Barrio es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return false;
            }
            else{
                if(fragmentInformacion.editDireccion.getText().toString().trim()==""){
                    Snackbar.make(view, "Direccion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return false;
                }
                else{
                    if(tipoActividad.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==0){
                        Snackbar.make(view, "Tipo Operacion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        return false;
                    }
                    else{
                        if(estadoActividad.get(fragmentInformacion.sltEstadoActividad.getSelectedItemPosition()).getId()==0){
                            Snackbar.make(view, "Estado Actividad es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }
            }
        }
    }

    private void guardarFormulario(char tipoAlmacenamiento, SQLiteDatabase sqLiteDatabase){
        enableButton(false);
        switch (tipoAlmacenamiento) {
            case 'L':
                almacenarDatosLocal(sqLiteDatabase);
                break;
            case 'R':
                almacenarDatosEnRemoto();
                break;
        }
    }

    private void almacenarDatosLocal(SQLiteDatabase sqLiteDatabase) {

    }

    private void almacenarDatosEnRemoto() {
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_usuario", idUsuario);
            jsonObject.put("id_actividad",actividadOperativa.getIdActividad());
            jsonObject.put("fch_ejecucion",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEjecucion()));
            jsonObject.put("fecha_hora_llegada",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEnSitio()));
            jsonObject.put("id_elemento",actividadOperativa.getElemento().getId());

            //--Fotos Antes
            JSONArray jsonArrayFotoAntes = new JSONArray();
            //jsonArrayFotoAntes.put(fragmentFotoAntes.encodeStringFoto_1);
            jsonArrayFotoAntes.put(fragmentFotoAntes.encodeStringFoto_2);
            jsonArrayFotoAntes.put(fragmentFotoAntes.encodeStringFoto_3);
            jsonArrayFotoAntes.put(fragmentFotoAntes.encodeStringFoto_4);
            jsonObject.put("foto_antes", jsonArrayFotoAntes);


            //--Fotos Despues
            JSONArray jsonArrayFotoDespues = new JSONArray();
            //jsonArrayFotoDespues.put(fragmentFotoDespues.encodeStringFoto_1);
            jsonArrayFotoDespues.put(fragmentFotoDespues.encodeStringFoto_2);
            jsonArrayFotoDespues.put(fragmentFotoDespues.encodeStringFoto_3);
            jsonArrayFotoDespues.put(fragmentFotoDespues.encodeStringFoto_4);
            jsonObject.put("foto_despues", jsonArrayFotoDespues);

            //--Materiales
            JSONArray jsonArrayInst     = new JSONArray();
            JSONArray jsonArrayDesUtil  = new JSONArray();
            JSONArray jsonArrayDesUtilInst = new JSONArray();
            JSONArray jsonArrayDesNoUtil = new JSONArray();
            JSONArray jsonArrayPNC      = new JSONArray();
            JSONArray jsonArrayAsig     = new JSONArray();
            int index = 0;
            while(index < fragmentMateriales.movimientoArticuloArrayList.size()){
                switch (fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock()){
                    case 1://stock
                        JSONObject jsonInst = new JSONObject();
                        jsonInst.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonInst.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonInst.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonInst.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayInst.put(jsonInst);
                        break;
                    case 2:
                        JSONObject jsonPNC = new JSONObject();
                        jsonPNC.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonPNC.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonPNC.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonPNC.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayPNC.put(jsonPNC);
                        break;
                    case 3:
                        if(fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento().toString().contentEquals(getString(R.string.movimiento_salida))){ //Movimiento Negativo
                            JSONObject jsonDesUtilInst = new JSONObject();
                            jsonDesUtilInst.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                            jsonDesUtilInst.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                            jsonDesUtilInst.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                            jsonDesUtilInst.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                            jsonArrayDesUtilInst.put(jsonDesUtilInst);
                        }
                        else {
                            JSONObject jsonDesUtil = new JSONObject();
                            jsonDesUtil.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                            jsonDesUtil.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                            jsonDesUtil.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                            jsonDesUtil.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                            jsonArrayDesUtil.put(jsonDesUtil);
                        }
                        break;
                    case 4:
                        JSONObject jsonDesNoUtil = new JSONObject();
                        jsonDesNoUtil.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonDesNoUtil.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonDesNoUtil.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonDesNoUtil.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayDesNoUtil.put(jsonDesNoUtil);
                        break;
                    case 5:
                        JSONObject jsonAsig = new JSONObject();
                        jsonAsig.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonAsig.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonAsig.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonAsig.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayAsig.put(jsonAsig);
                        break;
                }
                index++;
            }
            jsonObject.put("inst", jsonArrayInst);
            jsonObject.put("desmutil", jsonArrayDesUtil);
            jsonObject.put("desmutinst", jsonArrayDesUtilInst);
            jsonObject.put("desmnoutil", jsonArrayDesNoUtil);
            jsonObject.put("pnc", jsonArrayPNC);
            jsonObject.put("asig", jsonArrayAsig);

            //--Informacion actividad
            jsonObject.put("elemento_no_encontrado", fragmentInformacion.swElementoNoEncontrado.isChecked());
            jsonObject.put("afectado_por_vandalismo", fragmentInformacion.swVandalismo.isChecked());
            jsonObject.put("id_barrio", fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getId());
            jsonObject.put("barrio", fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getDescripcion());
            jsonObject.put("direccion", fragmentInformacion.editDireccion.getText());
            jsonObject.put("id_tipo_actividad", fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId());
            jsonObject.put("id_estado_actividad", fragmentInformacion.estadoActividadList.get(fragmentInformacion.sltEstadoActividad.getSelectedItemPosition()).getId());
            jsonObject.put("observacion", fragmentInformacion.editObservacion.getText());

            //--Elemento
            JSONObject jsonElemento = new JSONObject();
            jsonElemento.put("id_usuario", idUsuario);
            jsonElemento.put("fch_actualizacion",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEjecucion()));
            jsonElemento.put("id_elemento",actividadOperativa.getElemento().getId());
            jsonElemento.put("elemento_no",actividadOperativa.getElemento().getElemento_no());
            jsonElemento.put("id_barrio", fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getId());
            jsonElemento.put("direccion", fragmentInformacion.editDireccion.getText());
            jsonElemento.put("id_tipo_balasto", fragmentElemento.tipoBalastoList.get(fragmentElemento.sltTipoBalasto.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_base_fotocelda", fragmentElemento.tipoBaseFotoceldaList.get(fragmentElemento.sltTipoBaseFotocelda.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_brazo", fragmentElemento.tipoBrazoList.get(fragmentElemento.sltTipoBrazo.getSelectedItemPosition()).getId());
            jsonElemento.put("id_control_encendido", fragmentElemento.controlEncendidoList.get(fragmentElemento.sltControlEncendido.getSelectedItemPosition()).getId());
            jsonElemento.put("id_estado_mobiliario", fragmentElemento.estadoMobiliarioList.get(fragmentElemento.sltEstadoMobiliario.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_escenario", fragmentElemento.tipoEscenarioList.get(fragmentElemento.sltTipoEscenario.getSelectedItemPosition()).getId());
            jsonElemento.put("id_clase_via", fragmentElemento.claseViaList.get(fragmentElemento.sltClaseVia.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_poste", fragmentElemento.tipoPosteList.get(fragmentElemento.sltTipoPoste.getSelectedItemPosition()).getId());
            jsonElemento.put("id_norma_construccion_poste", fragmentElemento.normaConstruccionPosteList.get(fragmentElemento.sltNormaConstruccionPoste.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_balasto", fragmentElemento.tipoBalastoList.get(fragmentElemento.sltTipoBalasto.getSelectedItemPosition()).getId());
            jsonElemento.put("id_calibre", fragmentElemento.calibreList.get( fragmentElemento.sltCalibreConexionElemento.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_instalacion_red", fragmentElemento.tipoInstalacionRedList.get(fragmentElemento.sltTipoInstalacionRed.getSelectedItemPosition()).getId());
            jsonElemento.put("id_tipo_red", fragmentElemento.tipoRedList.get(fragmentElemento.sltTipoRed.getSelectedItemPosition()).getId());
            jsonElemento.put("zona", fragmentElemento.zona);
            jsonElemento.put("sector", fragmentElemento.sector);
            jsonElemento.put("latitud", fragmentElemento.txtLatitud.getText());
            jsonElemento.put("longitud", fragmentElemento.txtLongitud.getText());
            jsonElemento.put("ancho_via", fragmentElemento.txtAnchoVia.getText());
            jsonElemento.put("poste_no", fragmentElemento.txtPosteNo.getText());
            jsonElemento.put("interdistancia", fragmentElemento.txtInterdistancia.getText());
            jsonElemento.put("poste_exclusivo_ap", fragmentElemento.swPosteExclusivoAp.isChecked());
            jsonElemento.put("potencia_transformador", fragmentElemento.txtPotenciaTransformador.getText());
            jsonElemento.put("placa_mt_transformador", fragmentElemento.txtMtTransformador.getText());
            jsonElemento.put("placa_ct_transformador", fragmentElemento.txtCtTransformador.getText());
            jsonElemento.put("transformador_exclusivo_ap", fragmentElemento.swTranformadorExclusivoAP.isChecked());
            //jsonElemento.put("foto",fragmentFotoDespues.encodeStringFoto_1);

            jsonObject.put("info_elemento",jsonElemento);

            JSONArray principal = new JSONArray();
            principal.put(jsonObject);
            Log.d("JSON","->"+principal.toString());

            client = new AsyncHttpClient();
            StringEntity jsonParams = new StringEntity(principal.toString(), "UTF-8");
            jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.setTimeout(Constantes.TIMEOUT);
            RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlGuardarActividad, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String respuesta = new String(responseBody);
                    Log.d("JSON-RESPONSE:", respuesta);
                    try {
                        JSONObject jsonResponse = new JSONObject(new String(responseBody));
                        /*
                        actualiza stock
                        cambia estado actividad en bd movil
                        retorna a listado de acti
                        listado de actividades no debe permitirce dar click a actividades ejecutadas
                         */
                        Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_LONG).show();
                        enableButton(true);  //--Quitar al terminar de probar
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"ERROR, JSON RESPONSE:"+e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Error", e.getMessage());
                        enableButton(true);
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String respuesta = new String(responseBody);
                    Log.d("ONFAILURE:",respuesta);
                    Toast.makeText(getApplicationContext(),"ONFAILURE: "+getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                    enableButton(true);
                }

            });

        }catch (JSONException e){
            Toast.makeText(getApplicationContext(),"JSONException:"+e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Error",e.getMessage());
            enableButton(true);
        }

    }

    private void enableButton(boolean estado){
        fabGuardar.setEnabled(estado);
        fabCancelar.setEnabled(estado);
    }

}