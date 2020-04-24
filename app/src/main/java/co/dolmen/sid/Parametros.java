package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoEstructura;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoTension;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
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
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
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
        txt_porcentaje_carga = findViewById(R.id.txt_porcentaje_carga);
        txt_nombre_tipo_descarga = findViewById(R.id.txt_nombre_tipo_descarga);
        cargarParametros(config.getInt("id_usuario",0));
        db = new BaseDatos(Parametros.this);
        database = db.getWritableDatabase();
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
                actualizarBaseDatosParamtros();
                Intent i = new Intent(Parametros.this,ConfigurarArea.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText(getText(R.string.alert_error_ejecucion)+ " Code:"+statusCode);
            }
        });

    }
    public void actualizarBaseDatosParamtros(){
        int progress = 0;
        progressBar.setProgress(0);
        txt_porcentaje_carga.setText("0%");
        txt_nombre_tipo_descarga.setText(getText(R.string.actualizando_base_datos));
        try{
            json = new JSONObject(new String(responseBodyTmp));
            JSONObject parametros = json.getJSONObject("parametros");


            //--Censo Asignado
            JSONArray arrayCenso = parametros.getJSONArray("censo");
            CensoAsignadoDB censoAsignadoDB = new CensoAsignadoDB(database);
            for (int i = 0;i<arrayCenso.length();i++){
                JSONObject jObjectCenso = arrayCenso.getJSONObject(i);
                censoAsignadoDB.setId(jObjectCenso.getInt("id"));
                censoAsignadoDB.setId_municipio(jObjectCenso.getInt("id_municipio"));
                censoAsignadoDB.setId_proceso_sgc(jObjectCenso.getInt("id_proceso_sgc"));
                censoAsignadoDB.agregarDatos(censoAsignadoDB);

                progress = (int)Math.round((double)(i+1)/arrayCenso.length()*100);
                progressBar.incrementProgressBy(progress);
                txt_porcentaje_carga.setText("Actualizando Censo "+progress+"%");
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
                progressBar.incrementProgressBy(progress);
                txt_porcentaje_carga.setText("Actualizando Municipio "+progress+"%");
            }

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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipologia "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Mobiliario "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Referencia Mobiliario "+progress+"%");
            }

            /*new Thread(new Runnable() {
                int i = 0;
                public void run() {
                    while (i < 100) {
                        i += 1;
                        // Update the progress bar and display the current value in text view
                        hdlr.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(i);
                                txt_porcentaje_carga.setText(i+"/"+progressBar.getMax());
                            }
                        });
                        try {
                            // Sleep for 100 milliseconds to show the progress slowly.
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();*/

            //--Barrio
            BarrioDB barrioDB = new BarrioDB(database);
            JSONArray arrayBarrio = parametros.getJSONArray("barrio");
            for (int i = 0;i<arrayBarrio.length();i++){
                JSONObject jObjectBarrio = arrayBarrio.getJSONObject(i);
                barrioDB.setIdBarrio(jObjectBarrio.getInt("id"));
                barrioDB.setId(jObjectBarrio.getInt("id_municipio"));
                barrioDB.setNombreBarrio(jObjectBarrio.getString("descripcion"));
                barrioDB.agregarDatos(barrioDB);
                progress = (int)Math.round((double)(i+1)/arrayBarrio.length()*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Barrios "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Proceso SGC "+progress+"%");
            }

            //--Proceso SGC
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Contratos "+progress+"%");
            }

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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Entidad Clase Via "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Estado Mobiliario "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Estado Actividad "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Vatiaje "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Unidad Medida "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Red "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Poste "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Interseccion "+progress+"%");
            }

            //--Tipo Espacio
            TipoEspacioDB tipoEspacioDB = new TipoEspacioDB(database);
            JSONArray arrayTipoEspacio = parametros.getJSONArray("tipo_espacio");
            for (int i = 0;i<arrayTipoEspacio.length();i++){
                JSONObject jObjectTipoEspacio = arrayTipoEspacio.getJSONObject(i);
                tipoEspacioDB.setId(jObjectTipoEspacio.getInt("id"));
                tipoEspacioDB.setDescripcion(jObjectTipoEspacio.getString("descripcion"));
                tipoEspacioDB.agregarDatos(tipoEspacioDB);
                progress = (int)Math.round((double)(i+1)/arrayTipoEspacio.length()*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Espacio "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Tension "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Retenida Poste "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Norma Construccion Poste "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Estructura "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Norma Construccion Red "+progress+"%");
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
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Tipo Reporte Dano "+progress+"%");
            }

            //--Tipo Programa
            ProgramaDB programaDB = new ProgramaDB(database);
            JSONArray arrayPrograma = parametros.getJSONArray("programausuario");
            for (int i = 0;i<arrayPrograma.length();i++){
                JSONObject jObjectPrograma = arrayPrograma.getJSONObject(i);
                programaDB.setId(jObjectPrograma.getInt("id"));
                ProcesoSgc proceso = new ProcesoSgc();
                proceso.setId(jObjectPrograma.getInt("id_proceso_sgc"));
                Municipio municipio = new Municipio();
                municipio.setId(jObjectPrograma.getInt("id_municipio"));
                programaDB.setProcesoSgc(proceso);
                programaDB.setMunicipio(municipio);
                programaDB.agregarDatos(programaDB);

                progress = (int)Math.round((double)(i+1)/arrayPrograma.length()*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Programa "+progress+"%");
            }

            //--Elementos--
            ElementoDB elementoDB = new ElementoDB(database);
            JSONArray arrayElemento = parametros.getJSONArray("elemento_usuario");
            for (int i = 0;i<arrayElemento.length();i++){

                JSONObject jObjectElemento = arrayElemento.getJSONObject(i);

                elementoDB.setId(jObjectElemento.getInt("id_elemento"));
                elementoDB.setElemento_no(jObjectElemento.getString("elemento_no"));
                elementoDB.setDireccion(jObjectElemento.getString("direccion"));

                Barrio barrio = new Barrio();
                barrio.setIdBarrio(jObjectElemento.getInt("id_barrio"));
                barrio.setId(jObjectElemento.getInt("id_municipio"));
                elementoDB.setBarrio(barrio);

                ProcesoSgc procesoSgc = new ProcesoSgc();
                procesoSgc.setId(jObjectElemento.getInt("id_proceso_sgc"));
                elementoDB.setProcesoSgc(procesoSgc);

                ReferenciaMobiliario referenciaMobiliario = new ReferenciaMobiliario();
                referenciaMobiliario.setId(jObjectElemento.getInt("id_tipologia"));
                referenciaMobiliario.setIdMobiliario(jObjectElemento.getInt("id_mobiliario"));
                referenciaMobiliario.setIdReferenciaMobiliario(jObjectElemento.getInt("id_referencia"));
                elementoDB.setReferenciaMobiliario(referenciaMobiliario);

                EstadoMobiliario estadoMobiliarioElem = new EstadoMobiliario();
                estadoMobiliarioElem.setIdEstadoMobiliario(jObjectElemento.getInt("id_estado_mobiliario"));
                elementoDB.setEstadoMobiliario(estadoMobiliarioElem);

                elementoDB.agregarDatos(elementoDB);

                progress = (int)Math.round((double)(i+1)/arrayElemento.length()*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando elementos "+progress+"%");

            }
            database.close();
        }catch (JSONException e){
            e.getMessage();
        }
    }

}
