package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    private String urlLogin = ServicioWeb.urlLogin;
    boolean salida;
    AlertDialog.Builder alert;
    EditText txtUsuario;
    EditText txtClave;
    Button btnIngresar;
    Button btnSalir;
    SharedPreferences config;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        config = getSharedPreferences("config",MODE_PRIVATE);

        alert = new AlertDialog.Builder(this);
        //--
        txtUsuario  = findViewById(R.id.txt_usuario);
        txtClave    = findViewById(R.id.txt_clave);
        btnIngresar = findViewById(R.id.btn_ingresar);
        btnSalir = findViewById(R.id.btn_salir);
        progressBar = findViewById(R.id.progressLogin);
        //--
        progressBar.setVisibility(View.INVISIBLE);
        
        btnIngresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(validarFrm()){
                            servicioConsultarUsuario(view);
                        }
                        else{
                            alert.setTitle(R.string.titulo_alerta);
                            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            alert.create().show();
                        }
                    }
                }
        );
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                finishAffinity();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean validarFrm(){
        if(txtUsuario.getText().toString().trim().length() == 0){
            alert.setMessage(R.string.alert_usuario);
            this.salida = false;
        }
        else{
            if(txtClave.getText().toString().trim().length() == 0){
                alert.setMessage(R.string.alert_clave);
                this.salida = false;
            }
            else{
                this.salida = true;
            }
        }
        return this.salida;
    }
    private void setlockFrm(boolean state){
        txtUsuario.setEnabled(state);
        txtClave.setEnabled(state);
        btnIngresar.setEnabled(state);
        btnSalir.setEnabled(state);
    }
    public void servicioConsultarUsuario(View v){
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.add("usuario",txtUsuario.getText().toString());
        requestParams.add("clave", txtClave.getText().toString());
        client.setTimeout(150000);
        RequestHandle GET = client.get(urlLogin, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setlockFrm(false);
                progressBar.setVisibility(View.VISIBLE);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String mensaje = "";
                /*
                Estados devuelto por recurson json
                    0:Usuario y,o clave Incorrecta
                    1:OK
                    2:Debe llenar los campos de usuario y clave
                    3:Error , consultando la informacion en la BD
                */
                if (statusCode == 200) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        String estado           = json.getString("estado");
                        mensaje          = json.getString("mensaje");
                        String nombre_usuario   = json.getString("nombre");
                        String usuario          = json.getString("usuario");
                        Integer id_usuario      = json.getInt("id_usuario");
                        Integer id_bodega       = json.getInt("id_bodega");

                        if(!estado.contentEquals("1")){
                            alert.setMessage(mensaje);
                            alert.setTitle(R.string.titulo_alerta);
                            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            alert.create().show();
                        }
                        else{
                            SharedPreferences.Editor editar = config.edit();
                            editar.putBoolean("usuario_logueado",true);
                            editar.putString("nombre_usuario", nombre_usuario);
                            editar.putString("usuario", usuario);
                            editar.putInt("id_usuario",id_usuario);
                            editar.putInt("id_bodega",id_bodega);
                            editar.commit();
                            Intent i = new Intent(Login.this, Parametros.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        alert.setMessage(getString(R.string.alert_error_ejecucion)+ "\n"+e.getMessage());
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        setlockFrm(true);
                        JSONObject json = new JSONObject(new String(responseBody));
                        mensaje          = json.getString("mensaje");
                        alert.setMessage(mensaje+ "\n Código Error Http:"+statusCode);
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //aplica para todos los codigos 4xx,5xx,3xx http
                progressBar.setVisibility(View.INVISIBLE);
                setlockFrm(true);
                alert.setMessage(getString(R.string.alert_servicio_no_encontrado)+ "\n"+urlLogin);
                alert.setTitle(R.string.titulo_alerta);
                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        });
        //Log.d("msg","test");
    }
}
