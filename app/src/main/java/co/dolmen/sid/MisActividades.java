package co.dolmen.sid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

class MisActividade {

    private ProgressBar progressBar;
    private TextView titulo;
    private TextView porcentaje;
    private Context context;
    private int contenLenght =  0;
    private int progress = 0;
    private Integer id_usuario;
    byte[] responseBodyTmp;
    private boolean notifcador;

    public MisActividade(ProgressBar progressBar, TextView titulo, TextView porcentaje, Context context,Integer id_usuario){
        this.progressBar = progressBar;
        this.titulo = titulo;
        this.porcentaje = porcentaje;
        this.context = context;
        this.id_usuario = id_usuario;
    }

    public void setMaxprogressBar(int max){
        progressBar.setMax(max);
    }

    private void setNotificador(boolean notifcador){
        this.notifcador = notifcador;
    }
    public boolean getNotificador(){
        return this.notifcador;
    }

    public void consultarActividades(){
        String urlConsultarActividad = ServicioWeb.urlConsultarActividad;
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_usuario",id_usuario.toString());
        requestParams.add("aho","2020");
        client.setTimeout(180000);
        RequestHandle GET = client.get(urlConsultarActividad, requestParams,new AsyncHttpResponseHandler() {
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
                porcentaje.setText(progress+"%");
            }

            @Override
            public void onStart() {
                super.onStart();
                progressBar.setProgress(0);
                porcentaje.setText("0%");
                titulo.setText(context.getText(R.string.descargando_actividades));
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.d("programacion","datos "+new String(responseBody));
                responseBodyTmp = responseBody;
                AlmacenarBaseDatos almacenarBaseDatos = new AlmacenarBaseDatos();
                almacenarBaseDatos.execute().getStatus();
                Log.d("notificador","stado "+almacenarBaseDatos.getStatus());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("programacion","error "+responseBody.toString());
                progressBar.setProgress(0);
                porcentaje.setText(context.getText(R.string.alert_error_ejecucion)+ " Code:"+statusCode);
            }
        });
    }

    private class AlmacenarBaseDatos extends AsyncTask<Void,Integer,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                JSONObject json = new JSONObject(new String(responseBodyTmp));
                JSONArray arrayProgramacion = json.getJSONArray("programacion");
                for (int i = 0;i<arrayProgramacion.length();i++) {
                    JSONObject jObjectProgramacion = arrayProgramacion.getJSONObject(i);
                    progress = (int)Math.round((double)(i+1)/arrayProgramacion.length()*100);
                    publishProgress(progress, R.string.titulo_programa);
                    Log.d("programacion","->:"+jObjectProgramacion.getInt("programa"));
                    Log.d("programacion","->programacion:"+progress+"%");
                    Thread.sleep(100);
                }
            }
            catch (JSONException | InterruptedException e){
                e.getMessage();
            }
            //
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            porcentaje.setText("0%");
            titulo.setText(context.getText(R.string.actualizando_base_datos));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                Toast.makeText(context, "Actualizacion de actividades Finalizada!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("values", String.valueOf(values[0]));
            int progreso = values[0].intValue();
            progressBar.setProgress(progreso);
            porcentaje.setText(context.getText(values[1])+" "+progreso+"%");
        }

        public AlmacenarBaseDatos() {
            super();
        }
    }
}