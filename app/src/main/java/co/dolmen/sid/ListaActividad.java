package co.dolmen.sid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Bodega;
import co.dolmen.sid.entidad.Calibre;
import co.dolmen.sid.entidad.CentroCosto;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.ControlEncendido;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Equipo;
import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.Programa;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoActividad;
import co.dolmen.sid.entidad.TipoBalasto;
import co.dolmen.sid.entidad.TipoBaseFotocelda;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.entidad.TipoEscenario;
import co.dolmen.sid.entidad.TipoInstalacionRed;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoReporteDano;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArchivoActividadDB;
import co.dolmen.sid.modelo.MovimientoArticuloDB;
import co.dolmen.sid.utilidades.AdapterData;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ListaActividad extends AppCompatActivity  {

    private SharedPreferences config;
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;
    private AlertDialog.Builder alert;

    private ArrayList<ActividadOperativa> actividadOperativaArrayList;
    private RecyclerView recyclerView;
    AdapterData adapterData;

    private FloatingActionButton fabCancelar;
    private ImageButton btnBuscarActividad;
    private ImageButton btnCancelarBusqueda;

    private Switch swBuscarPorElemento;
    private Switch swBuscarPorDireccion;
    private Switch swBuscarPorActividad;

    private EditText txtDatoBusqueda;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;
    private int idDefaultBodega;
    private  int contenLenght =  0;
    private MovimientoArticuloDB movimientoArticuloDB;
    private ActividadOperativaDB actividadOperativaDB;
    private ArchivoActividadDB archivoActividadDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad);
        setTitle(R.string.btn_mis_actividades);

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        idDefaultBodega     = config.getInt("id_bodega", 0);
        /*nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");*/

        conn = new BaseDatos(ListaActividad.this);
        database = conn.getReadableDatabase();

        actividadOperativaDB = new ActividadOperativaDB(database);

        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        actividadOperativaArrayList = new ArrayList<ActividadOperativa>();

        swBuscarPorElemento     = findViewById(R.id.sw_buscar_elemento);
        swBuscarPorDireccion    = findViewById(R.id.sw_buscar_direccion);
        swBuscarPorActividad    = findViewById(R.id.sw_buscar_actividad);

        fabCancelar             = findViewById(R.id.fab_cancelar);
        txtDatoBusqueda         = findViewById(R.id.txt_dato_busqueda);

        btnBuscarActividad      = findViewById(R.id.btn_buscar_actividad);
        btnCancelarBusqueda     = findViewById(R.id.btn_cancelar_busqueda);

        recyclerView = findViewById(R.id.rw_actividades);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        );

        try {
            consultarActividad(database);
        } catch (ParseException e) {
            Log.d("Error",e.getMessage());
        }
        adapterData = new AdapterData(actividadOperativaArrayList);

        adapterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaActividad.this,DetalleActividad.class);
                ActividadOperativa actividadOperativa  =  actividadOperativaArrayList.get(recyclerView.getChildAdapterPosition(v));
                i.putExtra("actividadOperativa",actividadOperativa);
                startActivity(i);
                ListaActividad.this.finish();
               // v.setBackgroundColor(R.color.colorAccent);
            }
        });

        recyclerView.setAdapter(adapterData);

        txtDatoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        btnBuscarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        btnCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDatoBusqueda.setText("");
                swBuscarPorElemento.setChecked(true);
                swBuscarPorDireccion.setChecked(false);
                swBuscarPorActividad.setChecked(false);
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Cancelar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                database.close();
                Intent i = new Intent(ListaActividad.this, Menu.class);
                startActivity(i);
                ListaActividad.this.finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mis_actividades,menu);
        return true; //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sincronizar:
                sincronizarAtendidas();
                break;
        }
        return true;//super.onOptionsItemSelected(item);
    }

    private void filter(String dato, boolean elemento, boolean direccion, boolean actividad){
        ArrayList<ActividadOperativa> filterActividadOperativa = new ArrayList<ActividadOperativa>();

        for(ActividadOperativa a:actividadOperativaArrayList){
            if(elemento) {
                if (a.getElemento().getElemento_no().contains(dato)) {
                    filterActividadOperativa.add(a);
                }
            }
            if(direccion) {
                if (a.getDireccion().toUpperCase().contains(dato.toUpperCase())) {
                    filterActividadOperativa.add(a);
                }
            }
            if(actividad) {
                if (String.valueOf(a.getIdActividad()).contains(dato)) {
                    filterActividadOperativa.add(a);
                }
            }
        }

        adapterData.filterList(filterActividadOperativa);
    }

    public void consultarActividad(SQLiteDatabase database) throws ParseException {
        actividadOperativaArrayList.clear();
        ActividadOperativa actividadOperativa;
        try {
            Cursor cursor = actividadOperativaDB.consultarTodo(idDefaultMunicipio, idDefaultProceso);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    Barrio barrio = new Barrio();
                    barrio.setId(cursor.getInt(cursor.getColumnIndex("id_municipio")));
                    barrio.setDescripcion(cursor.getString(cursor.getColumnIndex("municipio")));
                    //barrio.setIdBarrio(cursor.getInt(cursor.getColumnIndex("id_barrio")));
                    barrio.setIdBarrio(0);
                    barrio.setNombreBarrio(cursor.getString(cursor.getColumnIndex("barrio")));

                    Elemento elemento = new Elemento();
                    elemento.setId(cursor.getInt(cursor.getColumnIndex("id_elemento")));
                    if (cursor.getString(cursor.getColumnIndex("elemento_no")) == null) {
                        elemento.setElemento_no("");
                        elemento.setTipologia(new Tipologia());
                        elemento.setMobiliario(new Mobiliario());
                        elemento.setReferenciaMobiliario(new ReferenciaMobiliario());
                        elemento.setEstadoMobiliario(new EstadoMobiliario());
                        elemento.setTipoBalasto(new TipoBalasto());
                        elemento.setNormaConstruccionPoste(new NormaConstruccionPoste());
                        elemento.setTipoBaseFotocelda(new TipoBaseFotocelda());
                        elemento.setTipoBrazo(new TipoBrazo());
                        elemento.setControlEncendido(new ControlEncendido());
                        elemento.setTipoRed(new TipoRed());
                        elemento.setTipoInstalacionRed(new TipoInstalacionRed());
                        elemento.setTipoEscenario(new TipoEscenario());
                        elemento.setClaseVia(new ClaseVia());
                        elemento.setCalibre(new Calibre());
                        elemento.setZona("U");
                        elemento.setSector("N");
                        elemento.setAnchoVia(0);
                        elemento.setInterdistancia(0);
                        elemento.setPosteNo("");
                        elemento.setTransformadorExclusivo(true);
                        elemento.setPosteExclusivo(true);
                        elemento.setPotenciaTransformador(0);
                        elemento.setPlacaMT("");
                        elemento.setPlacaCT("");
                    } else {
                        elemento.setElemento_no(cursor.getString(cursor.getColumnIndex("elemento_no")));
                        elemento.setTipologia(
                                new Tipologia(
                                        cursor.getInt(cursor.getColumnIndex("id_tipologia")),
                                        cursor.getString(cursor.getColumnIndex("tipologia"))
                                )
                        );
                        elemento.setMobiliario(new Mobiliario(
                                cursor.getInt(cursor.getColumnIndex("id_mobiliario")),
                                cursor.getString(cursor.getColumnIndex("mobiliario"))
                        ));

                        elemento.setReferenciaMobiliario(
                                new ReferenciaMobiliario(
                                        cursor.getInt(cursor.getColumnIndex("id_referencia")),
                                        cursor.getString(cursor.getColumnIndex("referencia"))
                                )
                        );
                        elemento.setEstadoMobiliario(new EstadoMobiliario(
                                cursor.getInt(cursor.getColumnIndex("id_estado_mobiliario")),
                                cursor.getString(cursor.getColumnIndex("estado_mobiliario"))
                        ));

                        elemento.setTipoBalasto(new TipoBalasto(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_balasto")),
                                cursor.getString(cursor.getColumnIndex("tipo_balasto"))
                        ));

                        elemento.setNormaConstruccionPoste(new NormaConstruccionPoste(
                                cursor.getInt(cursor.getColumnIndex("id_norma_construccion_poste")),
                                cursor.getString(cursor.getColumnIndex("norma_construccion_poste")),
                                new TipoPoste(
                                        cursor.getInt(cursor.getColumnIndex("id_tipo_poste")),
                                        cursor.getString(cursor.getColumnIndex("tipo_poste"))
                                )
                        ));

                        elemento.setTipoBaseFotocelda(new TipoBaseFotocelda(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_base_fotocelda")),
                                cursor.getString(cursor.getColumnIndex("tipo_base_fotocelda"))
                        ));

                        elemento.setTipoBrazo(new TipoBrazo(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_brazo")),
                                cursor.getString(cursor.getColumnIndex("tipo_brazo"))
                        ));

                         elemento.setControlEncendido(new ControlEncendido(
                                 cursor.getInt(cursor.getColumnIndex("id_control_encendido")),
                                 cursor.getString(cursor.getColumnIndex("control_encendido"))
                         ));
                        elemento.setTipoRed(new TipoRed(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_red")),
                                cursor.getString(cursor.getColumnIndex("tipo_red"))
                        ));

                        elemento.setTipoInstalacionRed(new TipoInstalacionRed(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_instalacion_red_alimentacion")),
                                cursor.getString(cursor.getColumnIndex("tipo_instalacion_red"))
                        ));

                        elemento.setTipoEscenario(new TipoEscenario(
                                cursor.getInt(cursor.getColumnIndex("id_tipo_escenario")),
                                cursor.getString(cursor.getColumnIndex("tipo_escenario"))
                        ));

                        elemento.setClaseVia(new ClaseVia(
                                cursor.getInt(cursor.getColumnIndex("id_clase_via")),
                                cursor.getString(cursor.getColumnIndex("clase_via"))
                        ));

                        elemento.setCalibre(new Calibre(
                                cursor.getInt(cursor.getColumnIndex("id_calibre_conductores")),
                                cursor.getString(cursor.getColumnIndex("calibre_conductor"))
                        ));

                        elemento.setZona(cursor.getString(cursor.getColumnIndex("zona")));
                        elemento.setSector(cursor.getString(cursor.getColumnIndex("sector")));
                        elemento.setAnchoVia(cursor.getInt(cursor.getColumnIndex("ancho_via")));
                        elemento.setInterdistancia(cursor.getInt(cursor.getColumnIndex("interdistancia")));
                        elemento.setPosteNo(cursor.getString(cursor.getColumnIndex("poste_no")));
                        elemento.setTransformadorExclusivo((cursor.getString(cursor.getColumnIndex("transformador_compartido")).contentEquals("N"))?true:false);
                        elemento.setPosteExclusivo((cursor.getString(cursor.getColumnIndex("estructura_soporte_compartida")).contentEquals("N"))?true:false);
                        elemento.setPotenciaTransformador(cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                        elemento.setDireccion(cursor.getString(cursor.getColumnIndex("direccion_elemento")));
                        elemento.setPlacaMT(cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                        elemento.setPlacaCT(cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));

                    }
                    ProcesoSgc procesoSgc = new ProcesoSgc();
                    procesoSgc.setId(cursor.getInt(cursor.getColumnIndex("id_proceso_sgc")));
                    procesoSgc.setDescripcion(cursor.getString(cursor.getColumnIndex("proceso")));

                    EstadoActividad estadoActividad = new EstadoActividad();
                    estadoActividad.setId(cursor.getInt(cursor.getColumnIndex("id_estado_actividad")));
                    estadoActividad.setDescripcion(cursor.getString(cursor.getColumnIndex("estado_actividad")));

                    TipoActividad tipoActividad = new TipoActividad();
                    tipoActividad.setId(cursor.getInt(cursor.getColumnIndex("id_tipo_operacion")));
                    tipoActividad.setDescripcion(cursor.getString(cursor.getColumnIndex("tipo_operacion")));

                    Equipo equipo = new Equipo(
                            cursor.getInt(cursor.getColumnIndex("id_equipo")),
                            cursor.getString(cursor.getColumnIndex("codigo")),
                            cursor.getString(cursor.getColumnIndex("serial"))
                    );

                    CentroCosto centroCosto = new CentroCosto();
                    centroCosto.setIdCentroCosto(cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                    centroCosto.setDescripcionCentroCosto(cursor.getString(cursor.getColumnIndex("centro_costo")));

                    TipoReporteDano tipoReporteDano = new TipoReporteDano();
                    tipoReporteDano.setId(cursor.getInt(cursor.getColumnIndex("id_tipo_reporte_dano")));
                    tipoReporteDano.setDescripcion(cursor.getString(cursor.getColumnIndex("tipo_reporte_dano")));

                    Programa programa = new Programa();
                    programa.setId(cursor.getInt(cursor.getColumnIndex("id_programa")));
                    programa.setDescripcion(cursor.getString(cursor.getColumnIndex("programa")));
                    programa.setProcesoSgc(procesoSgc);
                    programa.setFechaPrograma(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("fch_programa"))));
                    programa.setMunicipio((Municipio) barrio);

                    actividadOperativa = new ActividadOperativa(
                            cursor.getInt(cursor.getColumnIndex("id_actividad")),
                            cursor.getInt(cursor.getColumnIndex("id_espacio_publicitario")),
                            programa,
                            procesoSgc,
                            elemento,
                            centroCosto,
                            barrio,
                            estadoActividad,
                            tipoReporteDano,
                            tipoActividad,
                            equipo,
                            new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("fch_programa"))),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(cursor.getColumnIndex("fch_actividad"))),
                            cursor.getString(cursor.getColumnIndex("direccion")),
                            cursor.getString(cursor.getColumnIndex("et")),
                            cursor.getString(cursor.getColumnIndex("usuario_programa_actividad"))
                    );

                    if(cursor.getString(cursor.getColumnIndex("fch_ejecucion"))!=null) {
                        actividadOperativa.setFechaEjecucion(new SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(cursor.getString(cursor.getColumnIndex("fch_ejecucion"))));
                    }

                    if(cursor.getString(cursor.getColumnIndex("fch_en_sitio"))!=null) {
                        actividadOperativa.setFechaEnSitio(new SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(cursor.getString(cursor.getColumnIndex("fch_en_sitio"))));
                    }

                    actividadOperativa.setObservacion(cursor.getString(cursor.getColumnIndex("observacion")));
                    actividadOperativa.setLatitud(cursor.getDouble(cursor.getColumnIndex("latitud")));
                    actividadOperativa.setLongitud(cursor.getDouble(cursor.getColumnIndex("longitud")));
                    actividadOperativa.setPendienteSincronizar(cursor.getString(cursor.getColumnIndex("pendiente_sincronizar")));
                    actividadOperativa.setElementoNoEncontrado(cursor.getString(cursor.getColumnIndex("elemento_no_encontrado")));
                    actividadOperativa.setAfectadoPorVandalismo(cursor.getString(cursor.getColumnIndex("afectado_por_vandalismo")));
                    actividadOperativaArrayList.add(actividadOperativa);
                }
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void sincronizarAtendidas(){
        ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Sincronizar sincronizar = new Sincronizar(this);
            sincronizar.execute();
        } else {
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_conexion);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
    }

    private JSONArray armarJSON(){
        JSONArray principal = new JSONArray();
        Cursor cursor;
        ArchivoActividadDB archivoActividadDB = new ArchivoActividadDB(database);
        MovimientoArticuloDB movimientoArticuloDB = new MovimientoArticuloDB(database);

        for(ActividadOperativa atendidaPendiente:actividadOperativaArrayList) {
            if (atendidaPendiente.getPendienteSincronizar().contentEquals("S") && atendidaPendiente.getEstadoActividad().getId() == 2) { //Filtra las atendidas pendientes por sincronizar
                //Log.d(Constantes.TAG,"List->"+atendidaPendiente.getIdActividad()+",Sincronizada->"+atendidaPendiente.getPendienteSincronizar());
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id_usuario", idUsuario);
                    jsonObject.put("id_actividad", atendidaPendiente.getIdActividad());
                    jsonObject.put("fch_ejecucion", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(atendidaPendiente.getFechaEjecucion()));
                    jsonObject.put("fecha_hora_llegada", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(atendidaPendiente.getFechaEnSitio()));
                    jsonObject.put("id_elemento", atendidaPendiente.getElemento().getId());
                    jsonObject.put("id_bodega", idDefaultBodega);
                    jsonObject.put("id_centro_costo", atendidaPendiente.getCentroCosto().getIdCentroCosto());
                    jsonObject.put("latitud", atendidaPendiente.getLatitud());
                    jsonObject.put("longitud", atendidaPendiente.getLongitud());
                    jsonObject.put("elemento_no_encontrado", atendidaPendiente.isElementoNoEncontrado());
                    jsonObject.put("afectado_por_vandalismo", atendidaPendiente.isAfectadoPorVandalismo());
                    jsonObject.put("id_barrio", atendidaPendiente.getBarrio().getIdBarrio());
                    jsonObject.put("barrio",atendidaPendiente.getBarrio().getNombreBarrio());
                    jsonObject.put("direccion", atendidaPendiente.getDireccion());
                    jsonObject.put("id_tipo_actividad", atendidaPendiente.getTipoActividad().getId());
                    jsonObject.put("id_estado_actividad", atendidaPendiente.getEstadoActividad().getId());
                    jsonObject.put("id_equipo", atendidaPendiente.getEquipo().getIdEquipo());
                    jsonObject.put("observacion", atendidaPendiente.getObservacion());

                    //--Fotos Antes
                    JSONArray jsonArrayFotoAntes = new JSONArray();
                    cursor = archivoActividadDB.consultarTodo(atendidaPendiente.getIdActividad(),"A");
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            jsonArrayFotoAntes.put(cursor.getString(cursor.getColumnIndex("archivo")));
                            //jsonArrayFotoAntes.put("textAntes");
                        }
                    }
                    cursor.close();
                    jsonObject.put("foto_antes", jsonArrayFotoAntes);

                    //--Fotos Despues
                    JSONArray jsonArrayFotoDespues = new JSONArray();
                    cursor = archivoActividadDB.consultarTodo(atendidaPendiente.getIdActividad(),"D");
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            jsonArrayFotoDespues.put(cursor.getString(cursor.getColumnIndex("archivo")));
                            //jsonArrayFotoDespues.put("textDespues");
                        }
                    }
                    cursor.close();
                    jsonObject.put("foto_despues", jsonArrayFotoDespues);

                    //--Materiales
                    JSONArray jsonArrayInst     = new JSONArray();
                    JSONArray jsonArrayDesUtil  = new JSONArray();
                    JSONArray jsonArrayDesUtilInst = new JSONArray();
                    JSONArray jsonArrayDesNoUtil = new JSONArray();
                    JSONArray jsonArrayPNC      = new JSONArray();
                    JSONArray jsonArrayAsig     = new JSONArray();

                    cursor = movimientoArticuloDB.consultarTodo(atendidaPendiente.getIdActividad());
                    if(cursor.getCount()>0){
                        while (cursor.moveToNext()){
                            switch (cursor.getInt(cursor.getColumnIndex("id_tipo_stock"))){
                                case 1://stock
                                    JSONObject jsonInst = new JSONObject();
                                    jsonInst.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                    jsonInst.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                    jsonInst.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                    jsonInst.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                    jsonInst.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                    jsonInst.put("movimiento", cursor.getString(cursor.getColumnIndex("movimiento")));
                                    jsonArrayInst.put(jsonInst);
                                    break;
                                case 2:
                                    JSONObject jsonPNC = new JSONObject();
                                    jsonPNC.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                    jsonPNC.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                    jsonPNC.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                    jsonPNC.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                    jsonPNC.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                    jsonPNC.put("movimiento", cursor.getString(cursor.getColumnIndex("movimiento")));
                                    jsonArrayPNC.put(jsonPNC);
                                    break;
                                case 3:
                                    if(cursor.getString(cursor.getColumnIndex("movimiento")).contentEquals(getString(R.string.movimiento_salida))){ //Movimiento Negativo
                                        JSONObject jsonDesUtilInst = new JSONObject();
                                        jsonDesUtilInst.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                        jsonDesUtilInst.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                        jsonDesUtilInst.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                        jsonDesUtilInst.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                        jsonDesUtilInst.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                        jsonDesUtilInst.put("movimiento", cursor.getInt(cursor.getColumnIndex("movimiento")));
                                        jsonArrayDesUtilInst.put(jsonDesUtilInst);
                                    }
                                    else {
                                        JSONObject jsonDesUtil = new JSONObject();
                                        jsonDesUtil.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                        jsonDesUtil.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                        jsonDesUtil.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                        jsonDesUtil.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                        jsonDesUtil.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                        jsonDesUtil.put("movimiento", cursor.getInt(cursor.getColumnIndex("movimiento")));
                                        jsonArrayDesUtil.put(jsonDesUtil);
                                    }
                                    break;
                                case 4:
                                    JSONObject jsonDesNoUtil = new JSONObject();
                                    jsonDesNoUtil.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                    jsonDesNoUtil.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                    jsonDesNoUtil.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                    jsonDesNoUtil.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                    jsonDesNoUtil.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                    jsonDesNoUtil.put("movimiento", cursor.getInt(cursor.getColumnIndex("movimiento")));
                                    jsonArrayDesNoUtil.put(jsonDesNoUtil);
                                    break;
                                case 5:
                                    JSONObject jsonAsig = new JSONObject();
                                    jsonAsig.put("id_bodega",cursor.getInt(cursor.getColumnIndex("id_bodega")));
                                    jsonAsig.put("id_centro_costo",cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                                    jsonAsig.put("id_tipo_stock", cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                                    jsonAsig.put("id_articulo", cursor.getInt(cursor.getColumnIndex("id_articulo")));
                                    jsonAsig.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad"))); ///cuando es un valor con decimales se van decimales equivocados
                                    jsonAsig.put("movimiento", cursor.getInt(cursor.getColumnIndex("movimiento")));
                                    jsonArrayAsig.put(jsonAsig);
                                    break;
                            }
                        }
                    }
                    cursor.close();
                    jsonObject.put("inst", jsonArrayInst);
                    jsonObject.put("desmutil", jsonArrayDesUtil);
                    jsonObject.put("desmutinst", jsonArrayDesUtilInst);
                    jsonObject.put("desmnoutil", jsonArrayDesNoUtil);
                    jsonObject.put("pnc", jsonArrayPNC);
                    jsonObject.put("asig", jsonArrayAsig);


                    //--Elemento
                    JSONObject jsonElemento = new JSONObject();
                    jsonElemento.put("id_usuario", idUsuario);
                    jsonElemento.put("fch_actualizacion",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(atendidaPendiente.getFechaEjecucion()));
                    jsonElemento.put("id_elemento",atendidaPendiente.getElemento().getId());
                    jsonElemento.put("elemento_no",atendidaPendiente.getElemento().getElemento_no());
                    jsonElemento.put("id_barrio", atendidaPendiente.getBarrio().getIdBarrio());
                    jsonElemento.put("direccion", atendidaPendiente.getDireccion());
                    jsonElemento.put("id_tipo_balasto", atendidaPendiente.getElemento().getTipoBalasto().getIdTipoBalasto());
                    jsonElemento.put("id_tipo_base_fotocelda", atendidaPendiente.getElemento().getTipoBaseFotocelda().getidTipoBaseFotocelda());
                    jsonElemento.put("id_tipo_brazo", atendidaPendiente.getElemento().getTipoBrazo().getidTipoBrazo());
                    jsonElemento.put("id_control_encendido", atendidaPendiente.getElemento().getControlEncendido().getidControlEncendido());
                    jsonElemento.put("id_estado_mobiliario", atendidaPendiente.getElemento().getEstadoMobiliario().getIdEstadoMobiliario());
                    jsonElemento.put("id_tipo_escenario", atendidaPendiente.getElemento().getTipoEscenario().getId());
                    jsonElemento.put("id_clase_via", atendidaPendiente.getElemento().getClaseVia().getId());
                    jsonElemento.put("id_tipo_poste", atendidaPendiente.getElemento().getNormaConstruccionPoste().getTipoPoste().getId());
                    jsonElemento.put("id_norma_construccion_poste", atendidaPendiente.getElemento().getNormaConstruccionPoste().getId());
                    jsonElemento.put("id_calibre", atendidaPendiente.getElemento().getCalibre().getId_calibre());
                    jsonElemento.put("id_tipo_instalacion_red", atendidaPendiente.getElemento().getTipoInstalacionRed().getidTipoInstalacionRed());
                    jsonElemento.put("id_tipo_red", atendidaPendiente.getElemento().getTipoRed().getId());
                    jsonElemento.put("zona", atendidaPendiente.getElemento().getZona());
                    jsonElemento.put("sector", atendidaPendiente.getElemento().getSector());
                    jsonElemento.put("latitud", atendidaPendiente.getElemento().getLatitud());
                    jsonElemento.put("longitud", atendidaPendiente.getElemento().getLongitud());
                    jsonElemento.put("ancho_via", atendidaPendiente.getElemento().getAnchoVia());
                    jsonElemento.put("poste_no", atendidaPendiente.getElemento().getPosteNo());
                    jsonElemento.put("interdistancia", atendidaPendiente.getElemento().getInterdistancia());

                    jsonElemento.put("poste_exclusivo_ap", atendidaPendiente.getElemento().isPosteExclusivo());
                    jsonElemento.put("potencia_transformador", atendidaPendiente.getElemento().getPotenciaTransformador());
                    jsonElemento.put("placa_mt_transformador", atendidaPendiente.getElemento().getPlacaMT());
                    jsonElemento.put("placa_ct_transformador", atendidaPendiente.getElemento().getPlacaCT());
                    jsonElemento.put("transformador_exclusivo_ap", atendidaPendiente.getElemento().isTransformadorExclusivo());
                    jsonElemento.put("foto",atendidaPendiente.getElemento().getEncodeStringFoto());

                    jsonObject.put("info_elemento",jsonElemento);

                    principal.put(jsonObject);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        return principal;
    }

    class Sincronizar extends AsyncTask<Void,Integer,Boolean> {
        JSONArray jsonArray;
        ProgressDialog dialog;
        public Sincronizar(Activity activity){
            dialog = new ProgressDialog(activity);
            dialog.setCancelable(false);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fabCancelar.setEnabled(false);
            fabCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            dialog.setMessage("Recopilando datos.");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonArray = armarJSON();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                //Toast.makeText(getApplicationContext(), "Recopilación de datos Finalizado", Toast.LENGTH_SHORT).show();
                dialog.hide();
                //Log.d(Constantes.TAG,"JSON->"+jsonArray.toString()+"Count:"+jsonArray.length());
                if(jsonArray.length()==0){
                    alert.setMessage(getText(R.string.alert_sin_datos_por_sincronizar));
                    alert.setNeutralButton(R.string.btn_aceptar,new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fabCancelar.setEnabled(true);
                            fabCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                        }
                    });
                    alert.create().show();
                }
                else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    StringEntity jsonParams = new StringEntity(jsonArray.toString(), "UTF-8");
                    jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.setTimeout(Constantes.TIMEOUT);

                    RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlGuardarActividad, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                            super.onPreProcessResponse(instance, response);
                            Header[] headers = response.getAllHeaders();
                            for (Header header : headers) {
                                if (header.getName().equalsIgnoreCase("content-length")) {
                                    String value = header.getValue();
                                    contenLenght = Integer.valueOf(value);
                                }
                            }
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            dialog.setMessage("Procesnado Info en el server");
                            dialog.show();
                            //progress = (int)Math.round(((double)bytesWritten/(double)contenLenght)*100);
                            //progressBar.setProgress(progress);
                            //txt_porcentaje_carga.setText(progress+"%");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String respuesta = new String(responseBody);
                            Log.d(Constantes.TAG, respuesta);
                            int pos = -1;
                            try {
                                JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                JSONArray jsonArrayLog = jsonResponse.getJSONArray("log");
                                archivoActividadDB = new ArchivoActividadDB(database);
                                movimientoArticuloDB = new MovimientoArticuloDB(database);
                                for (int i = 0; i < jsonArrayLog.length(); i++) {

                                    //opcion eliminar registro;
                                /*if(jsonArrayLog.getJSONObject(i).getBoolean("sw")) {
                                    archivoActividadDB.eliminarDatos(jsonArrayLog.getJSONObject(i).getInt("id_actividad"));
                                    movimientoArticuloDB.eliminarDatos(jsonArrayLog.getJSONObject(i).getInt("id_actividad"));
                                    actividadOperativaDB.eliminarDatos(jsonArrayLog.getJSONObject(i).getInt("id_actividad"));
                                    pos = adapterData.getPositionItem(jsonArrayLog.getJSONObject(i).getInt("id_actividad"));
                                    adapterData.removeItem(pos);
                                }*/

                                    //opcion actualizar registro
                                    if (jsonArrayLog.getJSONObject(i).getBoolean("sw")) {
                                        pos = adapterData.getPositionItem(jsonArrayLog.getJSONObject(i).getInt("id_actividad"));
                                        actividadOperativaArrayList.get(pos).setPendienteSincronizar("N");
                                        actividadOperativaDB.actualizarDatos(actividadOperativaArrayList.get(pos));
                                        adapterData.notifySetChange();
                                    }
                                    else{
                                        //pantalla con los movitos del poque no se sincronizó
                                    }

                                }

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            fabCancelar.setEnabled(true);
                            fabCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            String respuesta = new String(responseBody);
                            Log.d(Constantes.TAG, respuesta);
                            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Código: " + statusCode + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                            fabCancelar.setEnabled(true);
                            fabCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                        }

                        @Override
                        public void onUserException(Throwable error) {
                            super.onUserException(error);
                            fabCancelar.setEnabled(true);
                            fabCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(Constantes.TAG, error.getMessage());
                        }
                    });
                }
            }
        }
    }
}

