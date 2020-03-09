package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
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
        client.setTimeout(150000);

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
                int progress = (int)Math.round(((double)bytesWritten/(double)contenLenght)*100);
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
                //--LLamado a funcion que carga la programacion;
                actualizarBaseDatosParamtros();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseBodyTmp = responseBody;
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
        Log.d("responseBody",""+new String(responseBodyTmp));
        try{
            json = new JSONObject(new String(responseBodyTmp));
            JSONObject parametros = json.getJSONObject("parametros");

            //--Municipio
            MunicipioDB municipioDB = new MunicipioDB(database);
            JSONArray arrayMunicipio = parametros.getJSONArray("municipiousuario");
            for (int i = 0;i<arrayMunicipio.length();i++){
                JSONObject jObjectMunicipio = arrayMunicipio.getJSONObject(i);
                municipioDB.setId(jObjectMunicipio.getInt("id"));
                municipioDB.setDescripcion(jObjectMunicipio.getString("descripcion"));
                municipioDB.agregarDatos(municipioDB);
                progress = (int)Math.round((double)(i+1)/arrayMunicipio.length()*100);
                progressBar.setProgress(progress);
                txt_porcentaje_carga.setText("Actualizando Municipio "+progress+"%");
            }

            //--Barrio
            /*
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
            }*/

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
                //Log.d("progreso->",""+progress);
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

            db.close();
        }catch (JSONException e){
            e.getMessage();
        }
    }
}
