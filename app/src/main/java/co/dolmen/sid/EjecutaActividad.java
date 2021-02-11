package co.dolmen.sid;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.ArchivoActividad;
import co.dolmen.sid.entidad.Articulo;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Bodega;
import co.dolmen.sid.entidad.Calibre;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.ControlEncendido;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.modelo.ElementoDesmontadoDB;
import co.dolmen.sid.entidad.Equipo;
import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.Stock;
import co.dolmen.sid.entidad.TipoActividad;
import co.dolmen.sid.entidad.TipoBalasto;
import co.dolmen.sid.entidad.TipoBaseFotocelda;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.entidad.TipoEscenario;
import co.dolmen.sid.entidad.TipoInstalacionRed;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoStock;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArchivoActividadDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.MovimientoArticuloDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.modelo.VatiajeDesmontadoDB;
import co.dolmen.sid.utilidades.MiLocalizacion;
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

    AlertDialog.Builder alert;

    //--Fragment
    private FragmentFotoAntes fragmentFotoAntes;
    private FragmentFotoDespues fragmentFotoDespues;
    private FragmentMateriales fragmentMateriales;
    private FragmentInformacion fragmentInformacion;
    private FragmentElemento fragmentElemento;

    private ProgressBar progressGuardarActividad;
    private FloatingActionButton fabGuardar;
    private FloatingActionButton fabCancelar;

    private ArrayList<MovimientoArticulo> movimientoArticulos;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idDefaultBodega;

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


        progressGuardarActividad = findViewById(R.id.progress_guardar_actividad);
        progressGuardarActividad.setVisibility(View.INVISIBLE);

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        idDefaultBodega     = config.getInt("id_bodega", 0);

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

    private void actualizarObjeto(){

        //--Actualizar Pojo Actividad operativa--
        actividadOperativa.setFechaEjecucion(new Date());
        actividadOperativa.setDireccion(fragmentInformacion.editDireccion.getText().toString());
        actividadOperativa.setObservacion(fragmentInformacion.editObservacion.getText().toString());
        actividadOperativa.setAfectadoPorVandalismo((fragmentInformacion.swVandalismo.isChecked())?"S":"N");
        actividadOperativa.setElementoNoEncontrado((fragmentInformacion.swElementoNoEncontrado.isChecked())?"S":"N");

        Equipo equipo = actividadOperativa.getEquipo();
        equipo.setIdEquipo(fragmentInformacion.vehiculoList.get(fragmentInformacion.sltVehiculo.getSelectedItemPosition()).getId());
        equipo.setSerial(fragmentInformacion.vehiculoList.get(fragmentInformacion.sltVehiculo.getSelectedItemPosition()).getDescripcion());

        EstadoActividad estadoActividad = actividadOperativa.getEstadoActividad();
        estadoActividad.setId(fragmentInformacion.estadoActividadList.get(fragmentInformacion.sltEstadoActividad.getSelectedItemPosition()).getId());
        estadoActividad.setDescripcion(fragmentInformacion.estadoActividadList.get(fragmentInformacion.sltEstadoActividad.getSelectedItemPosition()).getDescripcion());

        TipoActividad tipoActividad = actividadOperativa.getTipoActividad();
        tipoActividad.setId(fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId());
        tipoActividad.setDescripcion(fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getDescripcion());

        Barrio barrio = actividadOperativa.getBarrio();
        barrio.setIdBarrio(fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getId());
        barrio.setNombreBarrio(fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getDescripcion());
        barrio.setId(idDefaultMunicipio);

        if(!fragmentElemento.txtLatitud.getText().toString().isEmpty() && actividadOperativa.getLatitud() == 0)
            actividadOperativa.setLatitud(Float.parseFloat(fragmentElemento.txtLatitud.getText().toString()));

        if(!fragmentElemento.txtLongitud.getText().toString().isEmpty() && actividadOperativa.getLongitud() == 0)
            actividadOperativa.setLongitud(Float.parseFloat(fragmentElemento.txtLongitud.getText().toString()));

        //--Actualiza Objeto Elemento
        Elemento elemento = actividadOperativa.getElemento();
        elemento.setDireccion(fragmentInformacion.editDireccion.getText().toString());
        elemento.setBarrio(barrio);
        elemento.setMunicipio( (Municipio) barrio);
        elemento.setTipoBalasto(
                new TipoBalasto(
                        fragmentElemento.tipoBalastoList.get(fragmentElemento.sltTipoBalasto.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoBalastoList.get(fragmentElemento.sltTipoBalasto.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setTipoBaseFotocelda(
                new TipoBaseFotocelda(
                        fragmentElemento.tipoBaseFotoceldaList.get(fragmentElemento.sltTipoBaseFotocelda.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoBaseFotoceldaList.get(fragmentElemento.sltTipoBaseFotocelda.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setTipoBrazo(
                new TipoBrazo(
                        fragmentElemento.tipoBrazoList.get(fragmentElemento.sltTipoBrazo.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoBrazoList.get(fragmentElemento.sltTipoBrazo.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setControlEncendido(
                new ControlEncendido(
                        fragmentElemento.controlEncendidoList.get(fragmentElemento.sltControlEncendido.getSelectedItemPosition()).getId(),
                        fragmentElemento.controlEncendidoList.get(fragmentElemento.sltControlEncendido.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setEstadoMobiliario(
                new EstadoMobiliario(
                        fragmentElemento.estadoMobiliarioList.get(fragmentElemento.sltEstadoMobiliario.getSelectedItemPosition()).getId(),
                        fragmentElemento.estadoMobiliarioList.get(fragmentElemento.sltEstadoMobiliario.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setZona(fragmentElemento.zona);
        elemento.setSector(fragmentElemento.sector);

        elemento.setTipoEscenario(
                new TipoEscenario(
                        fragmentElemento.tipoEscenarioList.get(fragmentElemento.sltTipoEscenario.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoEscenarioList.get(fragmentElemento.sltTipoEscenario.getSelectedItemPosition()).getDescripcion()
                )
        );

        Float latitud = (fragmentElemento.txtLatitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(fragmentElemento.txtLatitud.getText().toString());
        Float longitud = (fragmentElemento.txtLongitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(fragmentElemento.txtLongitud.getText().toString());
        elemento.setLatitud(latitud);
        elemento.setLongitud(longitud);

        elemento.setClaseVia(
                new ClaseVia(
                        fragmentElemento.claseViaList.get(fragmentElemento.sltClaseVia.getSelectedItemPosition()).getId(),
                        fragmentElemento.claseViaList.get(fragmentElemento.sltClaseVia.getSelectedItemPosition()).getDescripcion()
                )
        );

        Integer anchoVia = (fragmentElemento.txtAnchoVia.getText().toString().isEmpty()) ? 0 : Integer.parseInt(fragmentElemento.txtAnchoVia.getText().toString());
        elemento.setAnchoVia(anchoVia);

        elemento.setNormaConstruccionPoste(
                new NormaConstruccionPoste(
                        fragmentElemento.normaConstruccionPosteList.get(fragmentElemento.sltNormaConstruccionPoste.getSelectedItemPosition()).getId(),
                        fragmentElemento.normaConstruccionPosteList.get(fragmentElemento.sltNormaConstruccionPoste.getSelectedItemPosition()).getDescripcion(),
                        new TipoPoste(
                                fragmentElemento.tipoPosteList.get(fragmentElemento.sltTipoPoste.getSelectedItemPosition()).getId(),
                                fragmentElemento.tipoPosteList.get(fragmentElemento.sltTipoPoste.getSelectedItemPosition()).getDescripcion()
                        )
                )
        );
        elemento.setPosteExclusivo(fragmentElemento.swPosteExclusivoAp.isChecked());

        elemento.setPosteNo(fragmentElemento.txtPosteNo.getText().toString());

        Integer interdistancia = (fragmentElemento.txtInterdistancia.getText().toString().isEmpty()) ? 0 : Integer.parseInt(fragmentElemento.txtInterdistancia.getText().toString());
        elemento.setInterdistancia(interdistancia);

        Double potencia = (fragmentElemento.txtPotenciaTransformador.getText().toString().isEmpty()) ? 0 : Double.parseDouble(fragmentElemento.txtPotenciaTransformador.getText().toString());
        elemento.setPotenciaTransformador(potencia);

        elemento.setPlacaCT(fragmentElemento.txtCtTransformador.getText().toString());
        elemento.setPlacaMT(fragmentElemento.txtMtTransformador.getText().toString());
        elemento.setTransformadorExclusivo(fragmentElemento.swTranformadorExclusivoAP.isChecked());
        elemento.setCalibre(
                new Calibre(
                        fragmentElemento.calibreList.get(fragmentElemento.sltCalibreConexionElemento.getSelectedItemPosition()).getId(),
                        fragmentElemento.calibreList.get(fragmentElemento.sltCalibreConexionElemento.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setTipoInstalacionRed(
                new TipoInstalacionRed(
                        fragmentElemento.tipoInstalacionRedList.get(fragmentElemento.sltTipoInstalacionRed.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoInstalacionRedList.get(fragmentElemento.sltTipoInstalacionRed.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setTipoRed(
                new TipoRed(
                        fragmentElemento.tipoRedList.get(fragmentElemento.sltTipoRed.getSelectedItemPosition()).getId(),
                        fragmentElemento.tipoRedList.get(fragmentElemento.sltTipoRed.getSelectedItemPosition()).getDescripcion()
                )
        );
        elemento.setEncodeStringFoto(fragmentFotoDespues.encodeStringFoto_1);
        actividadOperativa.setElemento(elemento);

        //--Fotos Antes
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoAntes.encodeStringFoto_1, "A"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoAntes.encodeStringFoto_2, "A"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoAntes.encodeStringFoto_3, "A"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoAntes.encodeStringFoto_4, "A"));
        //--Fotos Despues
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoDespues.encodeStringFoto_1, "D"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoDespues.encodeStringFoto_2, "D"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoDespues.encodeStringFoto_3, "D"));
        actividadOperativa.agregarArchivoActividad(new ArchivoActividad(actividadOperativa.getIdActividad(),fragmentFotoDespues.encodeStringFoto_4, "D"));

        //--Elementos desmontados
        actividadOperativa.setElementosDesmontadosList(fragmentInformacion.desmontadoList);

        //--Vatiaje Desmontado
        actividadOperativa.setVatiajeDesmontadoList(fragmentInformacion.vatiajeDesmontadoList);

    }

    private void guardar(final View view){
        if(validarFotoAntes()){
            if(validarFotoDespues()){
                if(validarInfo(view)){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        guardarFormulario('R');
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
                                guardarFormulario('L');
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
            if(fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getId()==0){
                Snackbar.make(view, "Barrio es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return false;
            }
            else{
                if(fragmentInformacion.editDireccion.getText().toString().trim()==""){
                    Snackbar.make(view, "Direccion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return false;
                }
                else{
                    if(fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==0){
                        Snackbar.make(view, "Tipo Operacion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        return false;
                    }
                    else{
                        if(fragmentInformacion.estadoActividadList.get(fragmentInformacion.sltEstadoActividad.getSelectedItemPosition()).getId()==0){
                            Snackbar.make(view, "Estado Actividad es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            return false;
                        }
                        else{
                            if(fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==239
                            || fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==220
                            || fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==4
                            || fragmentInformacion.tipoActividadList.get(fragmentInformacion.sltTipoActividad.getSelectedItemPosition()).getId()==135){
                                if(actividadOperativa.getElemento().getId()==0){
                                    Snackbar.make(view, "Numero de elemento es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    return false;
                                }
                                else{
                                    if(fragmentElemento.tipoBalastoList.get(fragmentElemento.sltTipoBalasto.getSelectedItemPosition()).getId()==0){
                                        Snackbar.make(view, "Tipo Balasto Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        return false;
                                    }
                                    else{
                                        if(fragmentElemento.tipoBaseFotoceldaList.get(fragmentElemento.sltTipoBaseFotocelda.getSelectedItemPosition()).getId()==0){
                                            Snackbar.make(view, "Tipo Base Fotocelda Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            return false;
                                        }
                                        else{
                                            if(fragmentElemento.tipoBrazoList.get(fragmentElemento.sltTipoBrazo.getSelectedItemPosition()).getId()==0){
                                                Snackbar.make(view, "Tipo Soporte Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                return false;
                                            }
                                            else{
                                                if(fragmentElemento.controlEncendidoList.get(fragmentElemento.sltControlEncendido.getSelectedItemPosition()).getId()==0){
                                                    Snackbar.make(view, "Control de Encendido Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                    return false;
                                                }
                                                else{
                                                    if(fragmentElemento.estadoMobiliarioList.get(fragmentElemento.sltEstadoMobiliario.getSelectedItemPosition()).getId()==0){
                                                        Snackbar.make(view, "Funcionamiento del Mobiliario Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                        return false;
                                                    }
                                                    else{
                                                        if(fragmentElemento.tipoEscenarioList.get(fragmentElemento.sltTipoEscenario.getSelectedItemPosition()).getId()==0){
                                                            Snackbar.make(view, "Tipo Espacio Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                            return false;
                                                        }
                                                        else{
                                                            if(fragmentElemento.claseViaList.get(fragmentElemento.sltClaseVia.getSelectedItemPosition()).getId()==0){
                                                                Snackbar.make(view, "Clase via Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                return false;
                                                            }
                                                            else{
                                                                if(fragmentElemento.tipoPosteList.get(fragmentElemento.sltTipoPoste.getSelectedItemPosition()).getId()==0){
                                                                    Snackbar.make(view, "Tipo poste Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                    return false;
                                                                }
                                                                else{
                                                                    if(fragmentElemento.normaConstruccionPosteList.get(fragmentElemento.sltNormaConstruccionPoste.getSelectedItemPosition()).getId()==0){
                                                                        Snackbar.make(view, "Norma Construccion Poste Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                        return false;
                                                                    }
                                                                    else{
                                                                        if(fragmentElemento.calibreList.get(fragmentElemento.sltCalibreConexionElemento.getSelectedItemPosition()).getId()==0){
                                                                            Snackbar.make(view, "Calibre Conductor Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                            return false;
                                                                        }
                                                                        else{
                                                                            if(fragmentElemento.tipoInstalacionRedList.get(fragmentElemento.sltTipoInstalacionRed.getSelectedItemPosition()).getId()==0){
                                                                                Snackbar.make(view, "Tipo Instalación Red Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                                return false;
                                                                            }
                                                                            else{
                                                                                if(fragmentElemento.tipoRedList.get(fragmentElemento.sltTipoRed.getSelectedItemPosition()).getId()==0){
                                                                                    Snackbar.make(view, "Tipo Red Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                                    return false;
                                                                                }
                                                                                else{
                                                                                    if(Integer.parseInt(fragmentElemento.txtAnchoVia.getText().toString())==0){
                                                                                        Snackbar.make(view, "Ancho Via Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                                                        return false;
                                                                                    }
                                                                                    else{
                                                                                        if(Integer.parseInt(fragmentElemento.txtInterdistancia.getText().toString())==0){
                                                                                            Snackbar.make(view, "Interdistancia Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }

    private void guardarFormulario(char tipoAlmacenamiento){
        enableButton(false);
        switch (tipoAlmacenamiento) {
            case 'L':
                almacenarDatosLocal();
                break;
            case 'R':
                almacenarDatosEnRemoto();
                break;
        }
    }

    private void almacenarDatosLocal(){
        actualizarObjeto();
        actividadOperativa.setPendienteSincronizar("S");
        try {
            progressGuardarActividad.setVisibility(View.VISIBLE);
            ActividadOperativaDB actividadOperativaDB = new ActividadOperativaDB(database);
            ArchivoActividadDB archivoActividadDB = new ArchivoActividadDB(database);
            ElementoDesmontadoDB    elementoDesmontadoDB = new ElementoDesmontadoDB(database);
            MovimientoArticuloDB movimientoArticuloDB = new MovimientoArticuloDB(database);
            VatiajeDesmontadoDB vatiajeDesmontadoDB = new VatiajeDesmontadoDB(database);
            ElementoDB elementoDB = new ElementoDB(database);

            //--Actualizar actividad
            actividadOperativaDB.actualizarDatos(actividadOperativa);

            //--Guardar Fotos
            Iterator<ArchivoActividad> archivoActividadIterator = actividadOperativa.getArchivoActividad().iterator();
            ArchivoActividad tmpArchivoActividad;
            while (archivoActividadIterator.hasNext()) {
                tmpArchivoActividad = archivoActividadIterator.next();
                if(!tmpArchivoActividad.getArchivo().isEmpty()) {
                    if (!archivoActividadDB.agregarDatos(tmpArchivoActividad)) {
                        Toast.makeText(getApplicationContext(),"Error guardado las imagenes",Toast.LENGTH_LONG).show();
                    }
                }
            }

            //--Guardar Movimientos de Inventario
            Iterator<MovimientoArticulo> movimientoArticuloIterator = fragmentMateriales.movimientoArticuloArrayList.iterator();
            while (movimientoArticuloIterator.hasNext()) {
                if(!movimientoArticuloDB.agregarDatos(movimientoArticuloIterator.next()))
                    Toast.makeText(getApplicationContext(),"Error guardado los movimientos de inventario",Toast.LENGTH_LONG).show();
            }

            //--Relacionar los elementos desmontados
            Iterator<Elemento> desmontadoIterator = actividadOperativa.getElementosDesmontadosList().iterator();
            while(desmontadoIterator.hasNext()){
                if(!elementoDesmontadoDB.agregarDatos(actividadOperativa.getIdActividad(),desmontadoIterator.next().getId()))
                    Toast.makeText(getApplicationContext(),"Error relacionando los elementos desmontados",Toast.LENGTH_LONG).show();
            }

            //--Relacionar los vatiaje desmontados
            Iterator<Integer> vatiajeDesmontadoIterator = actividadOperativa.getVatiajeDesmontadoList().iterator();
            while(vatiajeDesmontadoIterator.hasNext()){
                if(!vatiajeDesmontadoDB.agregarDatos(actividadOperativa.getIdActividad(),vatiajeDesmontadoIterator.next().intValue()))
                    Toast.makeText(getApplicationContext(),"Error relacionando los vatiajes desmontados",Toast.LENGTH_LONG).show();
            }

            //--Actualiza el stock
            actulizarStock();

            //--Actualizar elemento
            elementoDB.actualizarDatos(actividadOperativa.getElemento());
            alert.setMessage(R.string.alert_almacenamiento_local);
            alert.setNeutralButton("Aceptar",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(EjecutaActividad.this,ListaActividad.class);
                    startActivity(i);
                    EjecutaActividad.this.finish();
                }
            });
            alert.create().show();

        }catch (SQLException e){
            progressGuardarActividad.setVisibility(View.INVISIBLE);
            enableButton(true);
            Toast.makeText(getApplicationContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void actulizarStock(){
        int index = 0;
        StockDB stockDB = new StockDB(database);
        float cantidad = 0;
        Cursor cursor;
        while(index < fragmentMateriales.movimientoArticuloArrayList.size()){
            Bodega bodega = new Bodega();
            bodega.setIdBodega(idDefaultBodega);
            Articulo articulo = new Articulo(fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo(),fragmentMateriales.movimientoArticuloArrayList.get(index).getArticulo());
            TipoStock tipoStock = new TipoStock(fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock(),fragmentMateriales.movimientoArticuloArrayList.get(index).getTipo_stock());
            cantidad = (fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento().contentEquals(getText(R.string.movimiento_entrada)))?fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad():fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()*(-1);
            Stock stock = new Stock(bodega,actividadOperativa.getCentroCosto(),articulo,tipoStock,cantidad);
            cursor = stockDB.consultarTodo(idDefaultBodega,fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo(),fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock(),actividadOperativa.getCentroCosto().getIdCentroCosto());
            if(cursor.getCount()>0) {
               //--------------------Caso PNC------------------
                if(tipoStock.getId() == 2){ //pnc
                    //actualizar mi stock normal

                    /*Stock stockTmp = stock;
                    stockTmp.setTipoStock(
                            new TipoStock(1,"STOCK")
                    );
                    stockTmp.setCantidad(cantidad*(-1));*/

                    Stock stockTmp = new Stock(
                            bodega,
                            actividadOperativa.getCentroCosto(),
                            articulo,new TipoStock(1,"STOCK"),
                            cantidad*(-1)
                    );
                    //Log.d(Constantes.TAG,"INVENT Descontar PNC stock:"+stockTmp.getTipoStock().getDescripcion()+",Cantidad:"+stockTmp.getCantidad()+"");
                    stockDB.actualizarDatos(stockTmp);
                }
                //--------------------------------------------
                stockDB.actualizarDatos(stock);
                //Log.d(Constantes.TAG,"INVENT Agregar:"+stock.getTipoStock().getDescripcion()+",Cantidad:"+stock.getCantidad());
            }
            else {
                stockDB.agregarDatos(stock);
            }
            index++;
        }
    }

    private void almacenarDatosEnRemoto() {
        actualizarObjeto();

        AsyncHttpClient client = new AsyncHttpClient();

        try {
            JSONObject jsonObject = new JSONObject();
            //--Informacion actividad
            jsonObject.put("id_usuario", idUsuario);
            jsonObject.put("id_actividad",actividadOperativa.getIdActividad());
            jsonObject.put("fch_ejecucion",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEjecucion()));
            jsonObject.put("fecha_hora_llegada",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEnSitio()));
            jsonObject.put("id_elemento",actividadOperativa.getElemento().getId());
            jsonObject.put("id_bodega",idDefaultBodega);
            jsonObject.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
            jsonObject.put("latitud",actividadOperativa.getLatitud());
            jsonObject.put("longitud",actividadOperativa.getLongitud());
            jsonObject.put("elemento_no_encontrado", actividadOperativa.isElementoNoEncontrado());
            jsonObject.put("afectado_por_vandalismo", actividadOperativa.isAfectadoPorVandalismo());
            jsonObject.put("id_barrio", actividadOperativa.getBarrio().getIdBarrio());
            jsonObject.put("barrio", fragmentInformacion.barrioList.get(fragmentInformacion.sltBarrio.getSelectedItemPosition()).getDescripcion());
            jsonObject.put("direccion", actividadOperativa.getDireccion());
            jsonObject.put("id_tipo_actividad", actividadOperativa.getTipoActividad().getId());
            jsonObject.put("id_estado_actividad", actividadOperativa.getEstadoActividad().getId());
            jsonObject.put("id_equipo", actividadOperativa.getEquipo().getIdEquipo());
            jsonObject.put("observacion", actividadOperativa.getObservacion());

            //--Elementos desmontados relacionados
            JSONArray jsonArrayDesmontado = new JSONArray();
            Iterator<Elemento> desmontadoIterator = actividadOperativa.getElementosDesmontadosList().iterator();
            while(desmontadoIterator.hasNext()) {
                jsonArrayDesmontado.put(desmontadoIterator.next().getId());
            }
            jsonObject.put("elemento_desmontado", jsonArrayDesmontado);


            //--Vatiaje Desmontado
            JSONArray jsonArrayVatiajeDesmontado = new JSONArray();
            Iterator<Integer> vatiajeDesmontadoIterator = actividadOperativa.getVatiajeDesmontadoList().iterator();
            while(vatiajeDesmontadoIterator.hasNext()) {
                jsonArrayVatiajeDesmontado.put(vatiajeDesmontadoIterator.next().intValue());
            }
            jsonObject.put("vatiaje_desmontado", jsonArrayDesmontado);

            //--Fotos Antes
            JSONArray jsonArrayFotoAntes = new JSONArray();
            for(int f=0;f<4;f++) {
                jsonArrayFotoAntes.put(actividadOperativa.getArchivoActividad().get(f).getArchivo());
            }
            jsonObject.put("foto_antes", jsonArrayFotoAntes);

            //--Fotos Despues
            JSONArray jsonArrayFotoDespues = new JSONArray();
            for(int f=4;f<8;f++) {
                jsonArrayFotoDespues.put(actividadOperativa.getArchivoActividad().get(f).getArchivo());
            }
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
                        jsonInst.put("id_bodega",idDefaultBodega);
                        jsonInst.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
                        jsonInst.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonInst.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonInst.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonInst.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayInst.put(jsonInst);
                        break;
                    case 2:
                        JSONObject jsonPNC = new JSONObject();
                        jsonPNC.put("id_bodega",idDefaultBodega);
                        jsonPNC.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
                        jsonPNC.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonPNC.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonPNC.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonPNC.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayPNC.put(jsonPNC);
                        break;
                    case 3:
                        if(fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento().toString().contentEquals(getString(R.string.movimiento_salida))){ //Movimiento Negativo
                            JSONObject jsonDesUtilInst = new JSONObject();
                            jsonDesUtilInst.put("id_bodega",idDefaultBodega);
                            jsonDesUtilInst.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
                            jsonDesUtilInst.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                            jsonDesUtilInst.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                            jsonDesUtilInst.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                            jsonDesUtilInst.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                            jsonArrayDesUtilInst.put(jsonDesUtilInst);
                        }
                        else {
                            JSONObject jsonDesUtil = new JSONObject();
                            jsonDesUtil.put("id_bodega",idDefaultBodega);
                            jsonDesUtil.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
                            jsonDesUtil.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                            jsonDesUtil.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                            jsonDesUtil.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                            jsonDesUtil.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                            jsonArrayDesUtil.put(jsonDesUtil);
                        }
                        break;
                    case 4:
                        JSONObject jsonDesNoUtil = new JSONObject();
                        jsonDesNoUtil.put("id_bodega",idDefaultBodega);
                        jsonDesNoUtil.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
                        jsonDesNoUtil.put("id_tipo_stock", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_tipo_stock());
                        jsonDesNoUtil.put("id_articulo", fragmentMateriales.movimientoArticuloArrayList.get(index).getId_articulo());
                        jsonDesNoUtil.put("cantidad", fragmentMateriales.movimientoArticuloArrayList.get(index).getCantidad()); ///cuando es un valor con decimales se van decimales equivo
                        jsonDesNoUtil.put("movimiento", fragmentMateriales.movimientoArticuloArrayList.get(index).getMovimiento());
                        jsonArrayDesNoUtil.put(jsonDesNoUtil);
                        break;
                    case 5:
                        JSONObject jsonAsig = new JSONObject();
                        jsonAsig.put("id_bodega",idDefaultBodega);
                        jsonAsig.put("id_centro_costo",actividadOperativa.getCentroCosto().getIdCentroCosto());
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


            //--Elemento
            JSONObject jsonElemento = new JSONObject();
            jsonElemento.put("id_usuario", idUsuario);
            jsonElemento.put("fch_actualizacion",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(actividadOperativa.getFechaEjecucion()));
            jsonElemento.put("id_elemento",actividadOperativa.getElemento().getId());
            jsonElemento.put("elemento_no",actividadOperativa.getElemento().getElemento_no());
            jsonElemento.put("id_barrio", actividadOperativa.getElemento().getBarrio().getIdBarrio());
            jsonElemento.put("direccion", actividadOperativa.getDireccion());
            jsonElemento.put("id_tipo_balasto", actividadOperativa.getElemento().getTipoBalasto().getIdTipoBalasto());
            jsonElemento.put("id_tipo_base_fotocelda", actividadOperativa.getElemento().getTipoBaseFotocelda().getidTipoBaseFotocelda());
            jsonElemento.put("id_tipo_brazo", actividadOperativa.getElemento().getTipoBrazo().getidTipoBrazo());
            jsonElemento.put("id_control_encendido", actividadOperativa.getElemento().getControlEncendido().getidControlEncendido());
            jsonElemento.put("id_estado_mobiliario", actividadOperativa.getElemento().getEstadoMobiliario().getIdEstadoMobiliario());
            jsonElemento.put("id_tipo_escenario", actividadOperativa.getElemento().getTipoEscenario().getId());
            jsonElemento.put("id_clase_via", actividadOperativa.getElemento().getClaseVia().getId());
            jsonElemento.put("id_tipo_poste", actividadOperativa.getElemento().getNormaConstruccionPoste().getTipoPoste().getId());
            jsonElemento.put("id_norma_construccion_poste", actividadOperativa.getElemento().getNormaConstruccionPoste().getId());
            jsonElemento.put("id_calibre", actividadOperativa.getElemento().getCalibre().getId_calibre());
            jsonElemento.put("id_tipo_instalacion_red", actividadOperativa.getElemento().getTipoInstalacionRed().getidTipoInstalacionRed());
            jsonElemento.put("id_tipo_red", actividadOperativa.getElemento().getTipoRed().getId());
            jsonElemento.put("zona", actividadOperativa.getElemento().getZona());
            jsonElemento.put("sector", actividadOperativa.getElemento().getSector());
            jsonElemento.put("latitud", actividadOperativa.getElemento().getLatitud());
            jsonElemento.put("longitud", actividadOperativa.getElemento().getLongitud());
            jsonElemento.put("ancho_via", actividadOperativa.getElemento().getAnchoVia());
            jsonElemento.put("poste_no", actividadOperativa.getElemento().getPosteNo());
            jsonElemento.put("interdistancia", actividadOperativa.getElemento().getInterdistancia());

            jsonElemento.put("poste_exclusivo_ap", fragmentElemento.swPosteExclusivoAp.isChecked());
            jsonElemento.put("potencia_transformador", actividadOperativa.getElemento().getPotenciaTransformador());
            jsonElemento.put("placa_mt_transformador", fragmentElemento.txtMtTransformador.getText());
            jsonElemento.put("placa_ct_transformador", fragmentElemento.txtCtTransformador.getText());
            jsonElemento.put("transformador_exclusivo_ap", fragmentElemento.swTranformadorExclusivoAP.isChecked());
            jsonElemento.put("foto",actividadOperativa.getElemento().getEncodeStringFoto());

            jsonObject.put("info_elemento",jsonElemento);

            JSONArray principal = new JSONArray();
            principal.put(jsonObject);
            Log.d("JSON","->"+jsonObject.toString());

            client = new AsyncHttpClient();
            StringEntity jsonParams = new StringEntity(principal.toString(), "UTF-8");
            jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.setTimeout(Constantes.TIMEOUT);
            RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlGuardarActividad, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    progressGuardarActividad.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String respuesta = new String(responseBody);
                    progressGuardarActividad.setVisibility(View.INVISIBLE);
                    Log.d(Constantes.TAG, respuesta);
                    try {
                        JSONObject jsonResponse = new JSONObject(new String(responseBody));
                        JSONArray jsonLog = jsonResponse.getJSONArray("log");

                        //Log.d(Constantes.TAG, String.valueOf(jsonLog));
                        //if(jsonResponse.getBoolean("sw")) {
                        if(jsonLog.getJSONObject(0).getBoolean("sw")){
                            /*
                            actualiza stock
                            1. actualizar stock con valores +
                            2. actualizar stock con valores -
                             */
                            actividadOperativa.setPendienteSincronizar("N");
                            ActividadOperativaDB actividadOperativaDB = new ActividadOperativaDB(database);
                            ArchivoActividadDB archivoActividadDB = new ArchivoActividadDB(database);
                            ElementoDesmontadoDB    elementoDesmontadoDB = new ElementoDesmontadoDB(database);
                            MovimientoArticuloDB movimientoArticuloDB = new MovimientoArticuloDB(database);
                            VatiajeDesmontadoDB vatiajeDesmontadoDB = new VatiajeDesmontadoDB(database);

                            ElementoDB elementoDB = new ElementoDB(database);

                            //--Actualiza Actividad
                            actividadOperativaDB.actualizarDatos(actividadOperativa);

                            //--Actuliza Elementos
                            elementoDB.actualizarDatos(actividadOperativa.getElemento());

                            //--Guardar Fotos
                            Iterator<ArchivoActividad> archivoActividadIterator = actividadOperativa.getArchivoActividad().iterator();
                            ArchivoActividad tmpArchivoActividad;
                            while (archivoActividadIterator.hasNext()) {
                                tmpArchivoActividad = archivoActividadIterator.next();
                                if(!tmpArchivoActividad.getArchivo().isEmpty()) {
                                    if (!archivoActividadDB.agregarDatos(tmpArchivoActividad)) {
                                        Toast.makeText(getApplicationContext(),"Error guardado las imagenes",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            //--Guardar Movimientos de Inventario
                            Iterator<MovimientoArticulo> movimientoArticuloIterator = fragmentMateriales.movimientoArticuloArrayList.iterator();
                            while (movimientoArticuloIterator.hasNext()) {
                                if(!movimientoArticuloDB.agregarDatos(movimientoArticuloIterator.next()))
                                    Toast.makeText(getApplicationContext(),"Error guardado los movimientos de inventario",Toast.LENGTH_LONG).show();
                            }

                            //--Relacionar los elementos desmontados
                            Iterator<Elemento> desmontadoIterator = actividadOperativa.getElementosDesmontadosList().iterator();
                            while(desmontadoIterator.hasNext()){
                                if(!elementoDesmontadoDB.agregarDatos(actividadOperativa.getIdActividad(),desmontadoIterator.next().getId()))
                                    Toast.makeText(getApplicationContext(),"Error relacionando los elementos desmontados",Toast.LENGTH_LONG).show();
                            }

                            //--Relacionar los vatiaje desmontados
                            Iterator<Integer> vatiajeDesmontadoIterator = actividadOperativa.getVatiajeDesmontadoList().iterator();
                            while(vatiajeDesmontadoIterator.hasNext()){
                                if(!vatiajeDesmontadoDB.agregarDatos(actividadOperativa.getIdActividad(),vatiajeDesmontadoIterator.next().intValue()))
                                    Toast.makeText(getApplicationContext(),"Error relacionando los vatiajes desmontados",Toast.LENGTH_LONG).show();
                            }

                            //--Actualiza el stock
                            actulizarStock();

                            alert.setMessage(jsonResponse.getString("Mensaje"));
                            alert.setNeutralButton("Aceptar",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(EjecutaActividad.this,ListaActividad.class);
                                    startActivity(i);
                                    EjecutaActividad.this.finish();
                                }
                            });
                            alert.create().show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), respuesta, Toast.LENGTH_LONG).show();
                            enableButton(true);
                        }
                        enableButton(true);
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
                    progressGuardarActividad.setVisibility(View.INVISIBLE);
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
        viewPager.setEnabled(estado);
    }

}