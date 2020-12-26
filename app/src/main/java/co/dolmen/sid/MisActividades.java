package co.dolmen.sid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.CentroCosto;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Equipo;
import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.Programa;
import co.dolmen.sid.entidad.TipoActividad;
import co.dolmen.sid.entidad.TipoReporteDano;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.utilidades.HandleTaskResponse;
import co.dolmen.sid.utilidades.ResponseHandle;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

class MisActividades {

    private ProgressBar progressBar;
    private TextView titulo;
    private TextView porcentaje;
    private Context context;
    private int contenLenght =  0;
    private int progress = 0;
    private Integer id_usuario;
    SQLiteOpenHelper db;
    SQLiteDatabase database;

    public MisActividades(ProgressBar progressBar, TextView titulo, TextView porcentaje, Context context,Integer id_usuario){
        this.progressBar = progressBar;
        this.titulo = titulo;
        this.porcentaje = porcentaje;
        this.context = context;
        this.id_usuario = id_usuario;
        db = new BaseDatos(context);
        database = db.getWritableDatabase();
    }

    public void setMaxprogressBar(int max){
        progressBar.setMax(max);
    }

    public void consultarActividades(final ResponseHandle callback){
        String urlConsultarActividad = ServicioWeb.urlConsultarActividad;
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_usuario",id_usuario.toString());
        //requestParams.add("aho","2020");
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
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                //Log.d("programacion","datos "+new String(responseBody));
                AlmacenarBaseDatos almacenarBaseDatos = new AlmacenarBaseDatos(context, responseBody, new HandleTaskResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        //Log.d("programacion","Programacion Escrita");
                        callback.onSuccess(responseBody);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }
                });
                almacenarBaseDatos.execute();

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

        private HandleTaskResponse<Boolean> mCallBack;
        private Context mContext;
        public Exception mException;
        private byte[] mResponseBody;

        public AlmacenarBaseDatos(Context context, byte[] responseBody, HandleTaskResponse callback) {
            mCallBack = callback;
            mContext = context;
            mResponseBody = responseBody;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                JSONObject json = new JSONObject(new String(mResponseBody));
                JSONArray arrayProgramacion = json.getJSONArray("programacion");
                ProgramaDB programaDB = new ProgramaDB(database);
                ActividadOperativaDB actividadOperativaDB = new ActividadOperativaDB(database);

                for (int i = 0;i<arrayProgramacion.length();i++) {
                    JSONObject jObjectProgramacion = arrayProgramacion.getJSONObject(i);

                    programaDB.setId(jObjectProgramacion.getInt("programa"));
                    programaDB.setDescripcion(jObjectProgramacion.getString("descripcion_programa"));
                    programaDB.setFechaPrograma(new SimpleDateFormat("yyyy-mm-dd").parse(jObjectProgramacion.getString("fecha_programa")));

                    ProcesoSgc procesoPrograma = new ProcesoSgc();
                    procesoPrograma.setId(jObjectProgramacion.getInt("id_proceso_sgc"));
                    programaDB.setProcesoSgc(procesoPrograma);

                    Municipio municipioPrograma = new Municipio();
                    municipioPrograma.setId(jObjectProgramacion.getInt("id_municipio"));
                    programaDB.setMunicipio(municipioPrograma);

                    programaDB.agregarDatos(programaDB);

                    JSONArray arrayActividad = jObjectProgramacion.getJSONArray("actividad");
                    for (int j=0;j<arrayActividad.length();j++){
                        JSONObject jObjectActividad = arrayActividad.getJSONObject(j);
                        Log.d("Programacion","Programa->"+jObjectProgramacion.getInt("programa")+" Actividad->"+jObjectActividad.getInt("id_actividad"));

                        progress = (int)Math.round((double)(j+1)/arrayActividad.length()*100);
                        publishProgress(progress, jObjectProgramacion.getInt("programa"));

                        Elemento elemento = new Elemento();
                        elemento.setId(jObjectActividad.getInt("id_elemento"));
                        elemento.setElemento_no("elemento");

                        CentroCosto centroCosto = new CentroCosto();
                        centroCosto.setIdCentroCosto(jObjectActividad.getInt("id_centro_costo"));
                        centroCosto.setDescripcionCentroCosto(jObjectActividad.getString("centro_costo"));

                        Barrio barrio = new Barrio();
                        //barrio.setIdBarrio(jObjectActividad.getInt("id_barrio"));
                        barrio.setNombreBarrio(jObjectActividad.getString("barrio"));

                        EstadoActividad estadoActividad = new EstadoActividad();
                        estadoActividad.setId(jObjectActividad.getInt("id_estado_actividad"));
                        estadoActividad.setDescripcion(jObjectActividad.getString("estado_actividad"));

                        Equipo equipo = new Equipo();
                        equipo.setIdEquipo(jObjectActividad.getInt("id_vehiculo"));
                        equipo.setSerial(jObjectActividad.getString("placa_vehiculo"));

                        TipoReporteDano tipoReporteDano = new TipoReporteDano();
                        tipoReporteDano.setId(jObjectActividad.getInt("id_tipo_reporte_dano"));
                        tipoReporteDano.setDescripcion(jObjectActividad.getString("tipo_reporte_dano"));

                        TipoActividad tipoActividad = new TipoActividad();
                        tipoActividad.setId(jObjectActividad.getInt("id_tipo_operacion"));
                        tipoActividad.setDescripcion(jObjectActividad.getString("tipo_operacion"));

                        ActividadOperativa actividadOperativa = new ActividadOperativa(
                                jObjectActividad.getInt("id_actividad"),
                                jObjectActividad.getInt("id_espacio_publicitario"),
                                (Programa) programaDB,
                                procesoPrograma,
                                elemento,
                                centroCosto,
                                barrio,
                                estadoActividad,
                                tipoReporteDano,
                                tipoActividad,
                                equipo,
                                programaDB.getFechaPrograma(),
                                new SimpleDateFormat("yyyy-mm-dd H:m:s").parse(jObjectActividad.getString("fch_actividad")),
                                jObjectActividad.getString("direccion"),
                                jObjectActividad.getString("et"),
                                jObjectActividad.getString("usuario_programa_actividad")
                        );
                        actividadOperativaDB.agregarDatos(actividadOperativa);
                        //Thread.sleep(200);
                    }
                }
                return true;
            }
            catch (JSONException  | ParseException  e){
                e.getMessage();
                Log.d("Error","Error->"+e.getMessage());
                return false;
            }

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
            if (mCallBack != null) {
                if (mException == null) {
                    mCallBack.onSuccess(result);
                } else {
                    mCallBack.onFailure(mException);
                }
            }
            /*if(result) {
                Toast.makeText(context, "Actualizacion de actividades Finalizada!", Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("values", String.valueOf(values[0]));
            int progreso = values[0].intValue();
            progressBar.setProgress(progreso);
            porcentaje.setText("Prog No:("+values[1]+") "+progreso+"%");
        }
    }
}
