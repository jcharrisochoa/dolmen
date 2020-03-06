package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class Parametros extends AppCompatActivity {
    SharedPreferences config;
    private ProgressBar progressBar;
    private String urlParametros = ServicioWeb.urlParametros;
    private int contenLenght =  0;
    TextView txt_porcentaje_carga;
    TextView txt_nombre_tipo_descarga;
    BaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        config = getSharedPreferences("config",MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        txt_porcentaje_carga = findViewById(R.id.txt_porcentaje_carga);
        txt_nombre_tipo_descarga = findViewById(R.id.txt_nombre_tipo_descarga);

        cargarParametros(config.getInt("id_usuario",0));

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
                //progressBar.setProgress(0);
                //txt_porcentaje_carga.setText("0%");
                //--LLamado a funcion que carga la programacion;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                txt_porcentaje_carga.setText("Completado");
                /*Intent i = new Intent(Parametros.this, Web.class);
                startActivity(i);
                finish();*/
                db = new BaseDatos(Parametros.this);
                db.onUpgrade(db.getWritableDatabase(),Constantes.VERSION_BASEDATOS,3);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setProgress(0);
                txt_porcentaje_carga.setText(getText(R.string.alert_error_ejecucion));
            }
        });
    }
}
