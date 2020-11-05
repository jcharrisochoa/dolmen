package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Contrato;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoEstructura;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoTension;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ProveedorDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.SentidoDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoConductorElectricoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.modelo.UnidadMedidaDB;
import co.dolmen.sid.modelo.VatiajeDB;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class Parametros extends AppCompatActivity {
    SharedPreferences config;
    private ProgressBar progressBar;
    private String urlParametros = ServicioWeb.urlParametros;
    private int contenLenght =  0;
    TextView txt_porcentaje_carga;
    TextView txt_nombre_tipo_descarga;
    SQLiteOpenHelper db;
    SQLiteDatabase database;
    JSONObject json;
    byte[] responseBodyTmp;
    int progress;

    private Handler hdlr = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        config = getSharedPreferences("config",MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        txt_porcentaje_carga = findViewById(R.id.txt_porcentaje_carga);
        txt_nombre_tipo_descarga = findViewById(R.id.txt_nombre_tipo_descarga);
        cargarParametros(config.getInt("id_usuario",0));
        db = new BaseDatos(Parametros.this);
        database = db.getWritableDatabase();
        Constantes.OLD_VERSION_BASEDATOS = database.getVersion();
        //Log.d("versiondb","db:"+Constantes.OLD_VERSION_BASEDATOS);

    }

    private void cargarParametros(final Integer id_usuario){
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.add("id_usuario",id_usuario.toString());
        client.setTimeout(180000);

        RequestHandle GET = client.get(urlParametros, requestParams, new AsyncHttpResponseHandler() {
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
                progress = (int)Math.round(((double)bytesWritten/(double)contenLenght)*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText(progress+"%");
            }

            @Override
            public void onStart() {
                super.onStart();
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText("0%");
                txt_nombre_tipo_descarga.setText(getText(R.string.descargando_configuracion));
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseBodyTmp = responseBody;
                EscribirBD escribir = new EscribirBD();
                escribir.execute();
               /* Intent i = new Intent(Parametros.this,ConfigurarArea.class);
                startActivity(i);
                finish();*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
               // String respuesta = new String(responseBody);
                Log.d("resultado","error "+responseBody);
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText(getText(R.string.alert_error_ejecucion)+ " Code:"+statusCode);
            }
        });

    }

    class EscribirBD extends AsyncTask<Void,Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            txt_porcentaje_carga.setText("0%");
            txt_nombre_tipo_descarga.setText(getText(R.string.actualizando_base_datos));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("values", String.valueOf(values[0]));
            int progreso = values[0].intValue();
            progressBar.setProgress(progreso);
            txt_porcentaje_carga.setText(getText(values[1])+" "+progreso+"%");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                json = new JSONObject(new String(responseBodyTmp));
                JSONObject parametros = json.getJSONObject("parametros");
                ElementoDB elementoDB = new ElementoDB(database);
                JSONArray arrayElemento = parametros.getJSONArray("elemento_usuario");

                //--Censo Asignado
                JSONArray arrayCenso = parametros.getJSONArray("censo");
                CensoAsignadoDB censoAsignadoDB = new CensoAsignadoDB(database);
                for (int i = 0;i<arrayCenso.length();i++){
                    JSONObject jObjectCenso = arrayCenso.getJSONObject(i);
                    censoAsignadoDB.setId(jObjectCenso.getInt("id"));
                    censoAsignadoDB.setId_municipio(jObjectCenso.getInt("id_municipio"));
                    censoAsignadoDB.setId_proceso_sgc(jObjectCenso.getInt("id_proceso_sgc"));
                    censoAsignadoDB.setTipo(jObjectCenso.getString("tipo"));
                    censoAsignadoDB.agregarDatos(censoAsignadoDB);
                    progress = (int)Math.round((double)(i+1)/arrayCenso.length()*100);
                    publishProgress(progress, R.string.titulo_censo_asignado);
                    Log.d("parametros","->censo:"+progress+"%");
                }

                //--Municipio
                MunicipioDB municipioDB = new MunicipioDB(database);
                JSONArray arrayMunicipio = parametros.getJSONArray("municipiousuario");
                for (int i = 0;i<arrayMunicipio.length();i++){
                    JSONObject jObjectMunicipio = arrayMunicipio.getJSONObject(i);
                    municipioDB.setId(jObjectMunicipio.getInt("id"));
                    municipioDB.setDescripcion(jObjectMunicipio.getString("descripcion"));
                    municipioDB.agregarDatos(municipioDB);
                    progress = (int)Math.round((double)(i+1)/arrayMunicipio.length()*100);
                    publishProgress(progress, R.string.municipio);
                    Log.d("parametros","->municipio:"+progress+"%");
                }

                //--Barrio
                BarrioDB barrioDB = new BarrioDB(database);
                JSONArray arrayBarrio = parametros.getJSONArray("barrio");
                barrioDB.iniciarTransaccion();
                for (int i = 0;i<arrayBarrio.length();i++){
                    JSONObject jObjectBarrio = arrayBarrio.getJSONObject(i);
                    barrioDB.setIdBarrio(jObjectBarrio.getInt("id"));
                    barrioDB.setId(jObjectBarrio.getInt("id_municipio"));
                    barrioDB.setNombreBarrio(jObjectBarrio.getString("descripcion"));
                    barrioDB.agregarDatos(barrioDB);
                    progress = (int)Math.round((double)(i+1)/arrayBarrio.length()*100);
                    publishProgress(progress, R.string.titulo_barrio);
                    Log.d("parametros","->Barrio:"+progress+"%");
                }
                barrioDB.finalizarTransaccion();

                //--Tipologia
                TipologiaDB tipologiaDB = new TipologiaDB(database);
                JSONArray arrayTipologia = parametros.getJSONArray("tipologia");
                for (int i = 0;i<arrayTipologia.length();i++){
                    JSONObject jObjectTipologia = arrayTipologia.getJSONObject(i);
                    tipologiaDB.setIdTipologia(jObjectTipologia.getInt("id"));
                    tipologiaDB.setId(jObjectTipologia.getInt("id_proceso_sgc"));
                    tipologiaDB.setDescripcionTipologia(jObjectTipologia.getString("descripcion"));
                    tipologiaDB.agregarDatos(tipologiaDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipologia.length()*100);
                    publishProgress(progress, R.string.titulo_tipologia);
                    Log.d("parametros","->tipologia:"+progress+"%");
                }

                //--Mobiliario
                MobiliarioDB mobiliarioDB = new MobiliarioDB(database);
                JSONArray arrayMobiliario = parametros.getJSONArray("mobiliario");
                for (int i = 0;i<arrayMobiliario.length();i++){
                    JSONObject jObjectMobiliario = arrayMobiliario.getJSONObject(i);
                    mobiliarioDB.setIdMobiliario(jObjectMobiliario.getInt("id"));
                    mobiliarioDB.setId(jObjectMobiliario.getInt("id_tipologia"));
                    mobiliarioDB.setDescripcionMobiliario(jObjectMobiliario.getString("descripcion"));
                    mobiliarioDB.agregarDatos(mobiliarioDB);
                    progress = (int)Math.round((double)(i+1)/arrayMobiliario.length()*100);
                    publishProgress(progress, R.string.titulo_mobiliario);
                    Log.d("parametros","->Mobiliario:"+progress+"%");
                }

                //--Referencia Mobiliario
                ReferenciaMobiliarioDB referenciaMobiliarioDB = new ReferenciaMobiliarioDB(database);
                JSONArray arrayReferenciaMobiliario = parametros.getJSONArray("referencia");
                for (int i = 0;i<arrayReferenciaMobiliario.length();i++){
                    JSONObject jObjectReferenciaMobiliario = arrayReferenciaMobiliario.getJSONObject(i);
                    referenciaMobiliarioDB.setIdReferenciaMobiliario(jObjectReferenciaMobiliario.getInt("id"));
                    referenciaMobiliarioDB.setId(jObjectReferenciaMobiliario.getInt("id_mobiliario"));
                    referenciaMobiliarioDB.setDescripcionReferenciaMobiliario(jObjectReferenciaMobiliario.getString("descripcion"));
                    referenciaMobiliarioDB.agregarDatos(referenciaMobiliarioDB);
                    progress = (int)Math.round((double)(i+1)/arrayReferenciaMobiliario.length()*100);
                    publishProgress(progress, R.string.titulo_referencia);
                    Log.d("parametros","->Referencia:"+progress+"%");
                }

                //--Proceso SGC
                ProcesoSgcDB procesoSgcDB = new ProcesoSgcDB(database);
                JSONArray arrayProcesoSgc = parametros.getJSONArray("procesousuario");
                for (int i = 0;i<arrayProcesoSgc.length();i++){
                    JSONObject jObjectProcesoSgc = arrayProcesoSgc.getJSONObject(i);
                    procesoSgcDB.setId(jObjectProcesoSgc.getInt("id"));
                    procesoSgcDB.setDescripcion(jObjectProcesoSgc.getString("descripcion"));
                    procesoSgcDB.agregarDatos(procesoSgcDB);
                    progress = (int)Math.round((double)(i+1)/arrayProcesoSgc.length()*100);
                    publishProgress(progress, R.string.titulo_proceso);
                    Log.d("parametros","->Proceso:"+progress+"%");
                }

                //--Contrato
                ContratoDB contratoDB = new ContratoDB(database);
                JSONArray arrayContrato = parametros.getJSONArray("contrato");
                for (int i = 0;i<arrayContrato.length();i++){
                    JSONObject jObjectContrato = arrayContrato.getJSONObject(i);
                    contratoDB.setId(jObjectContrato.getInt("id"));
                    contratoDB.setDescripcion(jObjectContrato.getString("descripcion"));
                    //--
                    Municipio municipioContrato = new Municipio();
                    municipioContrato.setId(jObjectContrato.getInt("id_municipio"));
                    contratoDB.setMunicipio(municipioContrato);
                    //--
                    ProcesoSgc procesoContrato = new ProcesoSgc();
                    procesoContrato.setId(jObjectContrato.getInt("id_proceso_sgc"));
                    contratoDB.setProcesoSgc(procesoContrato);

                    //--
                    contratoDB.agregarDatos(contratoDB);
                    progress = (int)Math.round((double)(i+1)/arrayContrato.length()*100);
                    publishProgress(progress, R.string.titulo_contrato);
                    Log.d("parametros","->Contrato:"+progress+"%");
                }

                //Acta Contrato
                ActaContratoDB actaContratoDB = new ActaContratoDB(database);
                JSONArray arrayActa = parametros.getJSONArray("acta");
                actaContratoDB.iniciarTransaccion();
                for (int i = 0;i<arrayActa.length();i++){
                    JSONObject jObjectActa = arrayActa.getJSONObject(i);

                    Contrato contratoActa = new Contrato();
                    contratoActa.setId(jObjectActa.getInt("id_contrato"));

                    actaContratoDB.setIdActa(jObjectActa.getInt("id"));
                    actaContratoDB.setContrato(contratoActa);
                    actaContratoDB.setDescripcionActa(jObjectActa.getString("descripcion"));
                    actaContratoDB.agregarDatos(actaContratoDB);
                    progress = (int)Math.round((double)(i+1)/arrayActa.length()*100);
                    publishProgress(progress, R.string.titulo_acta_contrato);
                    Log.d("parametros","->Acta:"+progress+"%");
                }
                actaContratoDB.finalizarTransaccion();

                //--Clase Via
                ClaseViaDB claseViaDB = new ClaseViaDB(database);
                JSONArray arrayClaseVia = parametros.getJSONArray("clasevia");
                for (int i = 0;i<arrayClaseVia.length();i++){
                    JSONObject jObjectClaseVia = arrayClaseVia.getJSONObject(i);
                    claseViaDB.setId(jObjectClaseVia.getInt("id"));
                    claseViaDB.setDescripcion(jObjectClaseVia.getString("descripcion"));
                    claseViaDB.setAbreviatura(jObjectClaseVia.getString("abreviatura"));
                    claseViaDB.agregarDatos(claseViaDB);
                    progress = (int)Math.round((double)(i+1)/arrayClaseVia.length()*100);
                    publishProgress(progress, R.string.titulo_clase_via);
                    Log.d("parametros","->Clase via:"+progress+"%");
                }

                //--Estado Mobiliario
                EstadoMobiliarioDB estadoMobiliarioDB = new EstadoMobiliarioDB(database);
                JSONArray arrayEstadoMobiliario = parametros.getJSONArray("estado");
                for (int i = 0;i<arrayEstadoMobiliario.length();i++){
                    JSONObject jObjectEstadoMobiliario = arrayEstadoMobiliario.getJSONObject(i);
                    estadoMobiliarioDB.setIdEstadoMobiliario(jObjectEstadoMobiliario.getInt("id"));
                    estadoMobiliarioDB.setId(jObjectEstadoMobiliario.getInt("id_proceso_sgc"));
                    estadoMobiliarioDB.setDescripcionEstadoMobiliario(jObjectEstadoMobiliario.getString("descripcion"));
                    estadoMobiliarioDB.agregarDatos(estadoMobiliarioDB);
                    progress = (int)Math.round((double)(i+1)/arrayEstadoMobiliario.length()*100);
                    publishProgress(progress, R.string.titulo_estado_elemento);
                    Log.d("parametros","->Estado Mobiliario:"+progress+"%");
                }

                //--Estado Actividad
                EstadoActividadDB estadoActividadDB = new EstadoActividadDB(database);
                JSONArray arrayEstadoActividad = parametros.getJSONArray("estado_actividad");
                for (int i = 0;i<arrayEstadoActividad.length();i++){
                    JSONObject jObjectEstadoActividad = arrayEstadoActividad.getJSONObject(i);
                    estadoActividadDB.setId(jObjectEstadoActividad.getInt("id"));
                    estadoActividadDB.setDescripcion(jObjectEstadoActividad.getString("descripcion"));
                    estadoActividadDB.agregarDatos(estadoActividadDB);
                    progress = (int)Math.round((double)(i+1)/arrayEstadoActividad.length()*100);
                    publishProgress(progress, R.string.titulo_estado_actividad);
                    Log.d("parametros","->Estado Actividad:"+progress+"%");
                }

                //--Vatiaje
                VatiajeDB vatiajeDB = new VatiajeDB(database);
                JSONArray arrayVatiaje = parametros.getJSONArray("vatiaje");
                for (int i = 0;i<arrayVatiaje.length();i++){
                    JSONObject jObjectVatiaje = arrayVatiaje.getJSONObject(i);
                    vatiajeDB.setId(jObjectVatiaje.getInt("id"));
                    vatiajeDB.setDescripcion(jObjectVatiaje.getString("descripcion"));
                    vatiajeDB.agregarDatos(vatiajeDB);
                    progress = (int)Math.round((double)(i+1)/arrayVatiaje.length()*100);
                    publishProgress(progress, R.string.titulo_vatiaje);
                    Log.d("parametros","->Vatiaje:"+progress+"%");
                }

                //--Unidad Media
                UnidadMedidaDB unidadMedidaDB = new UnidadMedidaDB(database);
                JSONArray arrayUnidadMedida = parametros.getJSONArray("unidad");
                for (int i = 0;i<arrayUnidadMedida.length();i++){
                    JSONObject jObjectUnidadMedida = arrayUnidadMedida.getJSONObject(i);
                    unidadMedidaDB.setId(jObjectUnidadMedida.getInt("id"));
                    unidadMedidaDB.setDescripcion(jObjectUnidadMedida.getString("descripcion"));
                    unidadMedidaDB.agregarDatos(unidadMedidaDB);
                    progress = (int)Math.round((double)(i+1)/arrayUnidadMedida.length()*100);
                    publishProgress(progress, R.string.titulo_unidad_medida);
                    Log.d("parametros","->Unidad Medida:"+progress+"%");
                }

                //--Tipo Stock
                TipoStockDB tipoStockDB = new TipoStockDB(database);
                JSONArray arrayTipoStock = parametros.getJSONArray("tipo_stock");
                for (int i = 0;i<arrayTipoStock.length();i++){
                    JSONObject jObjectTipoStock = arrayTipoStock.getJSONObject(i);
                    tipoStockDB.setId(jObjectTipoStock.getInt("id"));
                    tipoStockDB.setDescripcion(jObjectTipoStock.getString("descripcion"));
                    tipoStockDB.agregarDatos(tipoStockDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoStock.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_stock);
                    Log.d("parametros","->Tipo Stock:"+progress+"%");
                }

                //--Tipo Red
                TipoRedDB tipoRedDB = new TipoRedDB(database);
                JSONArray arrayTipoRed = parametros.getJSONArray("tipored");
                for (int i = 0;i<arrayTipoRed.length();i++){
                    JSONObject jObjectTipoRed = arrayTipoRed.getJSONObject(i);
                    tipoRedDB.setId(jObjectTipoRed.getInt("id"));
                    tipoRedDB.setDescripcion(jObjectTipoRed.getString("descripcion"));
                    tipoRedDB.agregarDatos(tipoRedDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoRed.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_red);
                    Log.d("parametros","->Tipo Red:"+progress+"%");
                }

                //--Tipo Poste
                TipoPosteDB tipoPosteDB = new TipoPosteDB(database);
                JSONArray arrayTipoPoste = parametros.getJSONArray("tipoposte");
                for (int i = 0;i<arrayTipoPoste.length();i++){
                    JSONObject jObjectTipoPoste = arrayTipoPoste.getJSONObject(i);
                    tipoPosteDB.setId(jObjectTipoPoste.getInt("id"));
                    tipoPosteDB.setDescripcion(jObjectTipoPoste.getString("descripcion"));
                    tipoPosteDB.agregarDatos(tipoPosteDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoPoste.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_poste);
                    Log.d("parametros","->Tipo Poste:"+progress+"%");
                }

                //--Tipo Interseccion
                TipoInterseccionDB tipoInterseccionDB = new TipoInterseccionDB(database);
                JSONArray arrayTipoInterseccion = parametros.getJSONArray("tipo_interseccion");
                for (int i = 0;i<arrayTipoInterseccion.length();i++){
                    JSONObject jObjectTipoInterseccion = arrayTipoInterseccion.getJSONObject(i);
                    tipoInterseccionDB.setId(jObjectTipoInterseccion.getInt("id"));
                    tipoInterseccionDB.setDescripcion(jObjectTipoInterseccion.getString("descripcion"));
                    tipoInterseccionDB.setAbreviatura(jObjectTipoInterseccion.getString("abreviatura"));
                    tipoInterseccionDB.agregarDatos(tipoInterseccionDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoInterseccion.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_interseccion);
                    Log.d("parametros","->Tipo Interseccion:"+progress+"%");
                }

                TipoEspacioDB tipoEspacioDB = new TipoEspacioDB(database);
                JSONArray arrayTipoEspacio = parametros.getJSONArray("tipo_espacio");
                for (int i = 0;i<arrayTipoEspacio.length();i++){
                    JSONObject jObjectTipoEspacio = arrayTipoEspacio.getJSONObject(i);
                    tipoEspacioDB.setId(jObjectTipoEspacio.getInt("id"));
                    tipoEspacioDB.setDescripcion(jObjectTipoEspacio.getString("descripcion"));
                    tipoEspacioDB.agregarDatos(tipoEspacioDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoEspacio.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_espacio);
                    Log.d("parametros","->Tipo Espacio:"+progress+"%");
                }

                //--Tipo Tension
                TipoTensionDB tipoTensionDB = new TipoTensionDB(database);
                JSONArray arrayTipoTension = parametros.getJSONArray("tipo_tension");
                for (int i = 0;i<arrayTipoTension.length();i++){
                    JSONObject jObjectTipoTension = arrayTipoTension.getJSONObject(i);
                    tipoTensionDB.setId(jObjectTipoTension.getInt("id"));
                    tipoTensionDB.setDescripcion(jObjectTipoTension.getString("descripcion"));
                    tipoTensionDB.agregarDatos(tipoTensionDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoTension.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_tension);
                    Log.d("parametros","->Tipo Tension:"+progress+"%");
                }

                //Retenida Poste
                RetenidaPosteDB retenidaPosteDB = new RetenidaPosteDB(database);
                JSONArray arrayRetenidaPoste = parametros.getJSONArray("retenida_poste");
                for (int i = 0;i<arrayRetenidaPoste.length();i++){
                    JSONObject jObjectRetenidaPoste = arrayRetenidaPoste.getJSONObject(i);
                    retenidaPosteDB.setId(jObjectRetenidaPoste.getInt("id"));
                    retenidaPosteDB.setDescripcion(jObjectRetenidaPoste.getString("descripcion"));
                    retenidaPosteDB.setNorma(jObjectRetenidaPoste.getString("norma"));
                    retenidaPosteDB.agregarDatos(retenidaPosteDB);
                    progress = (int)Math.round((double)(i+1)/arrayRetenidaPoste.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_retenida);
                    Log.d("parametros","->Retenida:"+progress+"%");
                }

                //--Norma Construccion Poste
                NormaConstruccionPosteDB normaConstruccionPosteDB = new NormaConstruccionPosteDB(database);
                JSONArray arrayNormaConstruccionPoste = parametros.getJSONArray("norma_construccion_poste");
                for (int i = 0;i<arrayNormaConstruccionPoste.length();i++){
                    JSONObject jObjectNormaConstruccionPoste = arrayNormaConstruccionPoste.getJSONObject(i);
                    normaConstruccionPosteDB.setId(jObjectNormaConstruccionPoste.getInt("id"));
                    normaConstruccionPosteDB.setDescripcion(jObjectNormaConstruccionPoste.getString("descripcion"));
                    //--
                    TipoPoste tipoPoste = new TipoPoste();
                    tipoPoste.setId(jObjectNormaConstruccionPoste.getInt("id_tipo_poste"));
                    normaConstruccionPosteDB.setTipoPoste(tipoPoste);
                    //--
                    normaConstruccionPosteDB.agregarDatos(normaConstruccionPosteDB);
                    progress = (int)Math.round((double)(i+1)/arrayNormaConstruccionPoste.length()*100);
                    publishProgress(progress, R.string.titulo_norma_construccion_poste);
                    Log.d("parametros","->Norma Poste:"+progress+"%");
                }

                //--Tipo Estructura
                TipoEstructuraDB tipoEstructuraDB = new TipoEstructuraDB(database);
                JSONArray arrayTipoEstructura = parametros.getJSONArray("tipo_estructura");
                for (int i = 0;i<arrayTipoEstructura.length();i++){
                    JSONObject jObjectTipoEstructura = arrayTipoEstructura.getJSONObject(i);
                    tipoEstructuraDB.setId(jObjectTipoEstructura.getInt("id"));
                    tipoEstructuraDB.setDescripcion(jObjectTipoEstructura.getString("descripcion"));
                    //--
                    TipoTension tipoTension = new TipoTension();
                    tipoTension.setId(jObjectTipoEstructura.getInt("id_tipo_tension"));
                    tipoEstructuraDB.setTipoTension(tipoTension);
                    //--
                    tipoEstructuraDB.agregarDatos(tipoEstructuraDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoEstructura.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_estructura);
                    Log.d("parametros","->Tipo Estructura:"+progress+"%");
                }

                //--Norma Construccion Red
                NormaConstruccionRedDB normaConstruccionRedDB = new NormaConstruccionRedDB(database);
                JSONArray arrayNormaConstruccionRed = parametros.getJSONArray("norma_construccion_red");
                for (int i = 0;i<arrayNormaConstruccionRed.length();i++){
                    JSONObject jObjectNormaConstruccionRed = arrayNormaConstruccionRed.getJSONObject(i);
                    normaConstruccionRedDB.setId(jObjectNormaConstruccionRed.getInt("id"));
                    normaConstruccionRedDB.setDescripcion(jObjectNormaConstruccionRed.getString("descripcion"));
                    normaConstruccionRedDB.setNorma(jObjectNormaConstruccionRed.getString("norma"));
                    //--
                    TipoEstructura tipoEstructura = new TipoEstructura();
                    tipoEstructura.setId(jObjectNormaConstruccionRed.getInt("id_tipo_estructura"));
                    normaConstruccionRedDB.setTipoEstructura(tipoEstructura);
                    //--
                    normaConstruccionRedDB.agregarDatos(normaConstruccionRedDB);
                    progress = (int)Math.round((double)(i+1)/arrayNormaConstruccionRed.length()*100);
                    publishProgress(progress, R.string.titulo_norma_construccion_red);
                    Log.d("parametros","->Norma Red:"+progress+"%");
                }

                //--Tipo Reporte Dano
                TipoReporteDanoDB tipoReporteDanoDB = new TipoReporteDanoDB(database);
                JSONArray arrayTipoReporteDano = parametros.getJSONArray("tiporeporte");
                for (int i = 0;i<arrayTipoReporteDano.length();i++){
                    JSONObject jObjectTipoReporteDano = arrayTipoReporteDano.getJSONObject(i);
                    ProcesoSgc procesoSgcReporte = new ProcesoSgc();
                    procesoSgcReporte.setId(jObjectTipoReporteDano.getInt("id_proceso_sgc"));

                    tipoReporteDanoDB.setId(jObjectTipoReporteDano.getInt("id"));
                    tipoReporteDanoDB.setProcesoSgc(procesoSgcReporte);
                    tipoReporteDanoDB.setDescripcion(jObjectTipoReporteDano.getString("descripcion"));
                    tipoReporteDanoDB.agregarDatos(tipoReporteDanoDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoReporteDano.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_reporte);
                    Log.d("parametros","->Tipo Reporte Dano:"+progress+"%");
                }

                //--Tipo Actividad Operativa
                TipoActividadDB tipoActividadDB = new TipoActividadDB(database);
                JSONArray arrayTipoActividad = parametros.getJSONArray("tipo_operacion");
                for (int i = 0;i<arrayTipoActividad.length();i++){
                    JSONObject jObjectTipoActividad = arrayTipoActividad.getJSONObject(i);

                    ProcesoSgc procesoSgcTipoAct = new ProcesoSgc();
                    procesoSgcTipoAct.setId(jObjectTipoActividad.getInt("id_proceso_sgc"));

                    tipoActividadDB.setId(jObjectTipoActividad.getInt("id"));
                    tipoActividadDB.setProcesoSgc(procesoSgcTipoAct);
                    tipoActividadDB.setDescripcion(jObjectTipoActividad.getString("descripcion"));
                    tipoActividadDB.agregarDatos(tipoActividadDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoActividad.length()*100);
                    publishProgress(progress, R.string.titulo_tipo_actividad);
                    Log.d("parametros","->Tipo Actividad Operativa:"+progress+"%");
                }

                //Sentido
                SentidoDB sentidoDB = new SentidoDB(database);
                JSONArray arraySentido = parametros.getJSONArray("sentido_espacio");
                for (int i = 0;i<arraySentido.length();i++){
                    JSONObject jObjectSentido = arraySentido.getJSONObject(i);
                    sentidoDB.setId(jObjectSentido.getInt("id"));
                    sentidoDB.setDescripcion(jObjectSentido.getString("descripcion"));
                    sentidoDB.agregarDatos(sentidoDB);
                    progress = (int)Math.round((double)(i+1)/arraySentido.length()*100);
                    publishProgress(progress, R.string.titulo_sentido);
                    Log.d("parametros","->Sentido:"+progress+"%");
                }

                //--Tipo Escenario
                TipoEscenarioDB tipoEscenarioDB = new TipoEscenarioDB(database);
                JSONArray arrayEscenario = parametros.getJSONArray("tipo_escenario");
                for (int i = 0;i<arrayEscenario.length();i++){
                    JSONObject jObjectEscenario = arrayEscenario.getJSONObject(i);
                    tipoEscenarioDB.setId(jObjectEscenario.getInt("id"));
                    tipoEscenarioDB.setDescripcion(jObjectEscenario.getString("descripcion"));
                    tipoEscenarioDB.agregarDatos(tipoEscenarioDB);
                    progress = (int)Math.round((double)(i+1)/arrayEscenario.length()*100);
                    publishProgress(progress,R.string.titulo_tipo_escenario);
                    Log.d("parametros","->Escenario:"+progress+"%");
                }

                //--Tipo Conductor Electrico
                TipoConductorElectricoDB tipoConductorElectricoDB = new TipoConductorElectricoDB(database);
                JSONArray arrayTipoConductor = parametros.getJSONArray("tipo_conductor_electrico");
                for (int i = 0;i<arrayTipoConductor.length();i++){
                    JSONObject jObjectConductor = arrayTipoConductor.getJSONObject(i);
                    tipoConductorElectricoDB.setId(jObjectConductor.getInt("id"));
                    tipoConductorElectricoDB.setDescripcion(jObjectConductor.getString("descripcion"));
                    tipoConductorElectricoDB.agregarDatos(tipoConductorElectricoDB);
                    progress = (int)Math.round((double)(i+1)/arrayTipoConductor.length()*100);
                    publishProgress(progress,R.string.titulo_conductor_electrico);
                    Log.d("parametros","->Conductor Electrico:"+progress+"%");
                }

                //--Calibre
                CalibreDB calibreDB = new CalibreDB(database);
                JSONArray arrayCalibre = parametros.getJSONArray("calibre");
                for (int i = 0;i<arrayCalibre.length();i++){
                    JSONObject jObjectCalibre = arrayCalibre.getJSONObject(i);
                    calibreDB.setId_calibre(jObjectCalibre.getInt("id"));
                    calibreDB.setDescripcion(jObjectCalibre.getString("descripcion"));
                    calibreDB.agregarDatos(calibreDB);
                    progress = (int)Math.round((double)(i+1)/arrayCalibre.length()*100);
                    publishProgress(progress,R.string.titulo_calibre);
                    Log.d("parametros","->Tipo Tension:"+progress+"%");
                }

                //Proveedor
                ProveedorDB proveedorDB = new ProveedorDB(database);
                JSONArray arrayProveedor = parametros.getJSONArray("proveedor");
                proveedorDB.iniciarTransaccion();
                for (int i = 0;i<arrayProveedor.length();i++){
                    JSONObject jObjectProveedor = arrayProveedor.getJSONObject(i);
                    proveedorDB.setId(jObjectProveedor.getInt("id"));
                    proveedorDB.setNombre(jObjectProveedor.getString("descripcion"));
                    proveedorDB.agregarDatos(proveedorDB);
                    progress = (int)Math.round((double)(i+1)/arrayProveedor.length()*100);
                    publishProgress(progress,R.string.titulo_proveedor);
                    Log.d("parametros","->Proveedor:"+progress+"%");
                }
                proveedorDB.finalizarTransaccion();

                //--Articulo
                ArticuloDB articuloDB = new ArticuloDB(database);
                JSONArray arrayArticulo = parametros.getJSONArray("inventario");
                for (int i = 0;i<arrayArticulo.length();i++){
                    JSONObject jObjectArticulo = arrayArticulo.getJSONObject(i);
                    articuloDB.setId(jObjectArticulo.getInt("id"));
                    articuloDB.setDescripcion(jObjectArticulo.getString("descripcion"));
                    articuloDB.agregarDatos(articuloDB);
                    progress = (int)Math.round((double)(i+1)/arrayArticulo.length()*100);
                    publishProgress(progress,R.string.titulo_articulo);
                    Log.d("parametros","->Articulo:"+progress+"%");
                }

                //Elementos
                elementoDB.iniciarTransaccion();
                for (int i = 0;i<arrayElemento.length();i++){

                    JSONObject jObjectElemento = arrayElemento.getJSONObject(i);
                    try {
                        elementoDB.agregarDatos(
                                jObjectElemento.getInt("id_elemento"),
                                jObjectElemento.getString("elemento_no"),
                                jObjectElemento.getString("direccion"),
                                jObjectElemento.getInt("id_municipio"),
                                jObjectElemento.getInt("id_barrio"),
                                jObjectElemento.getInt("id_proceso_sgc"),
                                jObjectElemento.getInt("id_tipologia"),
                                jObjectElemento.getInt("id_mobiliario"),
                                jObjectElemento.getInt("id_referencia"),
                                jObjectElemento.getInt("id_estado_mobiliario"));
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    progress = (int)Math.round((double)(i+1)/arrayElemento.length()*100);
                    publishProgress(progress,R.string.titulo_elemento);
                    Log.d("parametros","->Elementos:"+progress+"%");

                }
                elementoDB.finalizarTransaccion();

                //--Programa
                ProgramaDB programaDB = new ProgramaDB(database);
                JSONArray arrayPrograma = parametros.getJSONArray("programausuario");
                for (int i = 0;i<arrayPrograma.length();i++){
                    JSONObject jObjectPrograma = arrayPrograma.getJSONObject(i);
                    programaDB.setId(jObjectPrograma.getInt("id"));
                    ProcesoSgc procesoPrograma = new ProcesoSgc();
                    procesoPrograma.setId(jObjectPrograma.getInt("id_proceso_sgc"));
                    Municipio municipioPrograma = new Municipio();
                    municipioPrograma.setId(jObjectPrograma.getInt("id_municipio"));
                    programaDB.setProcesoSgc(procesoPrograma);
                    programaDB.setMunicipio(municipioPrograma);
                    programaDB.agregarDatos(programaDB);

                    progress = (int)Math.round((double)(i+1)/arrayPrograma.length()*100);
                    publishProgress(progress,R.string.titulo_programa);
                    Log.d("parametros","->Programa:"+progress+"%");
                }


                database.close();
            } catch (JSONException e) {
                e.getMessage();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(Parametros.this, "Actualización de la configuracion finalizada!", Toast.LENGTH_SHORT).show();
                MisActividade misActividade = new MisActividade(progressBar,txt_nombre_tipo_descarga,txt_porcentaje_carga,Parametros.this,config.getInt("id_usuario",0));
                misActividade.consultarActividades();

                if(misActividade.getNotificador()) {
                    Intent i = new Intent(Parametros.this,ConfigurarArea.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

}
