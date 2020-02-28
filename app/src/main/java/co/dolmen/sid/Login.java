package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private String urlLogin = ServicioWeb.urlLogin;
    boolean salida;

    AlertDialog.Builder alert;
    EditText txtUsuario;
    EditText txtClave;
    Button btnIngresar;
    Button btnCancelar;
    SharedPreferences config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setTitle(R.string.titulo_actividad_login);

        alert = new AlertDialog.Builder(this);

        txtUsuario  = findViewById(R.id.txt_usuario);
        txtClave    = findViewById(R.id.txt_clave);
        btnIngresar = findViewById(R.id.btn_ingresar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        btnIngresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(validarFrm()){

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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
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
}
