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
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
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
    byte[] responseBody;

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
                actualizarBaseDatos();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                int progress = 0;
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText("0%");
                txt_nombre_tipo_descarga.setText(getText(R.string.actualizando_base_datos));
                Log.d("responseBody",""+new String(responseBody));
                try{
                    json = new JSONObject(new String(responseBody));
                    JSONObject parametros = json.getJSONObject("parametros");

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
                    /*EstadoMobiliarioDB estadoMobiliarioDB = new EstadoMobiliarioDB(database);
                    JSONArray arrayEstadoMobiliario = parametros.getJSONArray("estado");
                    for (int i = 0;i<arrayEstadoMobiliario.length();i++){
                        JSONObject jObjectEstadoMobiliario = arrayEstadoMobiliario.getJSONObject(i);
                        estadoMobiliarioDB.setId(jObjectEstadoMobiliario.getInt("id"));
                        estadoMobiliarioDB.setDescripcion(jObjectEstadoMobiliario.getString("descripcion"));
                        estadoMobiliarioDB.agregarDatos(estadoMobiliarioDB);
                        progress = (int)Math.round((double)(i+1)/arrayEstadoMobiliario.length()*100);
                        progressBar.setProgress(progress);
                        txt_porcentaje_carga.setText("Actualizando Estado Mobiliario "+progress+"%");
                    }*/

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
                    db.close();
                }catch (JSONException e){
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText(getText(R.string.alert_error_ejecucion));
            }
        });
    }
    public void actualizarBaseDatos(){

    }
}
