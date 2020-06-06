package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubMenuCensoTecnico extends AppCompatActivity {


    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idUsuario;

    SQLiteOpenHelper conn;
    private CensoDB censoDB;
    private CensoTipoArmadoDB censoTipoArmadoDB;
    private CensoArchivoDB censoArchivoDB;
    Cursor cursor;
    Cursor cursorTipoArmado;
    Cursor cursorCensoArchivo;

    SQLiteDatabase database;
    AlertDialog.Builder alert;
    AlertDialog.Builder alertLogs;

    Button btnRegistrarElemento;
    Button btnSincronizar;
    Button btnCancelar;
    SharedPreferences config;
    TextView txtNombreMunicipio;
    TextView txtNombreProceso;
    TextView txtNombreContrato;
    TextView txtMensaje;
    ProgressBar progressBar;
    EditText txtLog;
    int cant = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu_censo_tecnico);

        setTitle(getText(R.string.titulo_menu_censo_tecnico));

        alert = new AlertDialog.Builder(this);

        conn = new BaseDatos(SubMenuCensoTecnico.this);
        database = conn.getReadableDatabase();


        try {
            censoDB = new CensoDB(database);
            censoTipoArmadoDB = new CensoTipoArmadoDB(database);
            censoArchivoDB = new CensoArchivoDB(database);
            cursor = censoDB.consultarTodo();
            cursorCensoArchivo = censoArchivoDB.consultarTodo();
            cant = cursor.getCount();
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
        }


        config = getSharedPreferences("config", MODE_PRIVATE);
        nombreMunicipio     = config.getString("nombreMunicipio", "");
        nombreProceso       = config.getString("nombreProceso", "");
        nombreContrato      = config.getString("nombreContrato", "");
        idUsuario           = config.getInt("id_usuario",0);

        btnRegistrarElemento = findViewById(R.id.btn_registrar_elemento);
        btnSincronizar = findViewById(R.id.btn_sincronizar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        progressBar = findViewById(R.id.progressBarSincronizar);

        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);
        //txtLog              = findViewById(R.id.txt_log);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

        progressBar.setVisibility(View.INVISIBLE);

        btnRegistrarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubMenuCensoTecnico.this,CensoTecnico.class);
                startActivity(i);
                SubMenuCensoTecnico.this.finish();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubMenuCensoTecnico.this,Menu.class);
                startActivity(i);
                SubMenuCensoTecnico.this.finish();
            }
        });
        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sincronizar();
            }
        });
        btnSincronizar.setText(getText(R.string.btn_sincronizar)+" ("+cant+")");
    }

    private void sincronizar(){
        setButton(false);
        final String tag = "Log:\n";
        ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
            JSONObject principal = new JSONObject();
            if (censoDB.consultarTodo().getCount() > 0) {
                try {
                    principal.put("json",armarJson());
                   // Log.d("JSON",principal.toString());
                    final AsyncHttpClient client = new AsyncHttpClient();
                    StringEntity jsonParams = new StringEntity(principal.toString(), "UTF-8");
                    jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.setTimeout(Constantes.TIMEOUT);

                    RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlSincronizarCensoTecnico, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String respuesta = new String(responseBody);
                            String logs = tag;
                            //Log.d("JSON-RESPONSE:",respuesta);
                            try {
                                JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                /*alert.setTitle(R.string.titulo_alerta);
                                alert.setMessage(jsonResponse.getString("mensaje"));
                                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setButton(true);
                                        dialogInterface.cancel();
                                    }
                                });
                                alert.create().show();*/
                                JSONArray jArrayLog = jsonResponse.getJSONArray("log");
                                for (int i=0;i<jArrayLog.length();i++){
                                    JSONObject jLog = jArrayLog.getJSONObject(i);
                                    jLog.getInt("id");
                                    jLog.getInt("id_censo");
                                    jLog.getInt("mobiliario");
                                    jLog.getString("mensaje");
                                    jLog.getBoolean("procesar");
                                    logs = logs + "Mobiliario No: "+jLog.getInt("mobiliario")+","+jLog.getString("mensaje")+"\n";
                                    if (jLog.getBoolean("procesar")){
                                        censoArchivoDB.eliminarDatos(jLog.getInt("id"));
                                        censoTipoArmadoDB.eliminarDatos(jLog.getInt("id"));
                                        censoDB.eliminarDatos(jLog.getInt("id"));
                                    }
                                }
                                visualizarLogs(logs,jsonResponse.getString("mensaje"));
                                btnSincronizar.setText(getText(R.string.btn_sincronizar)+" ("+censoDB.consultarTodo().getCount()+")");
                                setButton(true);

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            //setButton(true);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            String respuesta = new String(responseBody);
                            Log.d("JSON-RESPONSE-ERROR:",respuesta);
                            Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                            setButton(true);
                        }

                        @Override
                        public void onUserException(Throwable error) {
                            super.onUserException(error);
                            Log.d("JSON-RESPONSE:",error.getMessage());
                        }
                    });


                } catch (JSONException e){
                    Toast.makeText(getApplicationContext(),"Error generando empaquetando los datos",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    setButton(true);
                }
            }
            else {
                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(R.string.alert_sin_datos_por_sincronizar);
                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setButton(true);
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        }
        else{
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_conexion);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setButton(true);
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
    }

    private void setButton(boolean estado){
        btnRegistrarElemento.setEnabled(estado);
        btnCancelar.setEnabled(estado);
        btnSincronizar.setEnabled(estado);

        if(estado) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private JSONArray armarJson() throws JSONException{
        JSONArray datos = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                jsonObject.put("id_usuario", idUsuario);
                jsonObject.put("id_municipio", cursor.getInt(cursor.getColumnIndex("id_municipio")));
                jsonObject.put("id_barrio", cursor.getInt(cursor.getColumnIndex("id_barrio")));
                jsonObject.put("id_tipologia", cursor.getInt(cursor.getColumnIndex("id_tipologia")));
                jsonObject.put("id_mobiliario", cursor.getInt(cursor.getColumnIndex("id_mobiliario")));
                jsonObject.put("id_referencia", cursor.getInt(cursor.getColumnIndex("id_referencia")));
                jsonObject.put("id_estado_mobiliario", cursor.getInt(cursor.getColumnIndex("id_estado_mobiliario")));
                jsonObject.put("longitud", cursor.getFloat(cursor.getColumnIndex("longitud")));
                jsonObject.put("latitud", cursor.getFloat(cursor.getColumnIndex("latitud")));
                jsonObject.put("direccion", cursor.getString(cursor.getColumnIndex("direccion")));
                jsonObject.put("fch_registro", cursor.getString(cursor.getColumnIndex("fch_registro")));
                jsonObject.put("observacion", cursor.getString(cursor.getColumnIndex("observacion")));
                jsonObject.put("id_censo", cursor.getInt(cursor.getColumnIndex("id_censo")));
                jsonObject.put("id_elemento", cursor.getInt(cursor.getColumnIndex("id_elemento")));
                jsonObject.put("mobiliario_no", cursor.getInt(cursor.getColumnIndex("mobiliario_no")));
                jsonObject.put("numero_mobiliario_visible", cursor.getString(cursor.getColumnIndex("numero_mobiliario_visible")));
                jsonObject.put("mobiliario_en_sitio", cursor.getString(cursor.getColumnIndex("mobiliario_en_sitio")));
                jsonObject.put("id_sentido", 0);
                jsonObject.put("cantidad", 1);
                jsonObject.put("id_tipo_poste", cursor.getInt(cursor.getColumnIndex("id_tipo_poste")));
                jsonObject.put("id_norma_construccion_poste", cursor.getInt(cursor.getColumnIndex("id_norma_construccion_poste")));
                jsonObject.put("id_tipo_red", cursor.getInt(cursor.getColumnIndex("id_tipo_red")));
                jsonObject.put("poste_no", cursor.getString(cursor.getColumnIndex("poste_no")));
                jsonObject.put("interdistancia", cursor.getInt(cursor.getColumnIndex("interdistancia")));
                jsonObject.put("puesta_a_tierra", cursor.getString(cursor.getColumnIndex("puesta_a_tierra")));
                jsonObject.put("poste_exclusivo_ap", cursor.getString(cursor.getColumnIndex("poste_exclusivo_ap")));
                jsonObject.put("id_tipo_retenida", cursor.getInt(cursor.getColumnIndex("id_tipo_retenida")));
                jsonObject.put("id_clase_via", cursor.getInt(cursor.getColumnIndex("id_clase_via")));
                jsonObject.put("serial_medidor", 0);
                jsonObject.put("lectura_medidor", 0);
                jsonObject.put("potencia_transformador", cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                jsonObject.put("placa_mt_transformador", cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                jsonObject.put("placa_ct_transformador", cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));
                jsonObject.put("poste_buen_estado", cursor.getString(cursor.getColumnIndex("poste_buen_estado")));
                jsonObject.put("sector", cursor.getString(cursor.getColumnIndex("sector")));
                jsonObject.put("zona", cursor.getString(cursor.getColumnIndex("zona")));
                jsonObject.put("id_tipo_escenario", cursor.getString(cursor.getColumnIndex("id_tipo_escenario")));
                jsonObject.put("mobiliario_buen_estado", cursor.getString(cursor.getColumnIndex("mobiliario_buen_estado")));
                jsonObject.put("tipo_propietario_transformador", cursor.getString(cursor.getColumnIndex("tipo_propietario_transformador")));
                jsonObject.put("brazo_mal_estado",cursor.getString(cursor.getColumnIndex("brazo_mal_estado")));
                jsonObject.put("visor_mal_estado",cursor.getString(cursor.getColumnIndex("visor_mal_estado")));
                jsonObject.put("mobiliario_mal_posicionado",cursor.getString(cursor.getColumnIndex("mobiliario_mal_posicionado")));
                jsonObject.put("mobiliario_obsoleto",cursor.getString(cursor.getColumnIndex("mobiliario_obsoleto")));
                jsonObject.put("mobiliario_sin_bombillo",cursor.getString(cursor.getColumnIndex("mobiliario_sin_bombillo")));

                //--Tipo Armado
                JSONArray jsonArrayTipoArmado = new JSONArray();
                cursorTipoArmado = censoTipoArmadoDB.consultarTodo(cursor.getInt(cursor.getColumnIndex("id")));
                if (cursorTipoArmado.moveToFirst()) {
                    do {
                        JSONObject jsonTipoArmado = new JSONObject();
                        jsonTipoArmado.put("id_tipo_red",cursorTipoArmado.getInt(cursorTipoArmado.getColumnIndex("id_tipo_red")));
                        jsonTipoArmado.put("id_norma_construccion",cursorTipoArmado.getInt(cursorTipoArmado.getColumnIndex("id_norma_construccion_red")));
                        jsonArrayTipoArmado.put(jsonTipoArmado);
                    } while (cursorTipoArmado.moveToNext());
                }
                cursorTipoArmado.close();
                jsonObject.put("tipo_armado",jsonArrayTipoArmado);

                //--Imagenes
                JSONArray jsonArrayFoto = new JSONArray();
                cursorCensoArchivo = censoArchivoDB.consultarTodo(cursor.getInt(cursor.getColumnIndex("id")));
                if (cursorCensoArchivo.moveToFirst()) {
                    do {
                        jsonArrayFoto.put(cursorCensoArchivo.getString(cursorCensoArchivo.getColumnIndex("archivo")));
                    } while (cursorCensoArchivo.moveToNext());
                }
                jsonObject.put("foto",jsonArrayFoto);

                datos.put(jsonObject);
            } while (cursor.moveToNext());
        }

        return datos;
    }

    private void visualizarLogs(String logs,String msg){
        alertLogs = new AlertDialog.Builder(this);
        View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.visualizar_logs,null);
        txtLog         = content.findViewById(R.id.txt_log);
        txtLog.setEnabled(false);
        txtLog.setText(msg+"\n\n"+logs);
        alertLogs.setTitle(R.string.titulo_alerta);
        alert.setMessage(msg);
        alertLogs.setView(content);
        alertLogs.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setButton(true);
                dialogInterface.cancel();
            }
        });

        alertLogs.create().setCancelable(false);
        alertLogs.create().show();
    }
}

