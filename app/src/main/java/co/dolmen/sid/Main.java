package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends AppCompatActivity {
    //SQLiteOpenHelper db;
    //SQLiteDatabase database;
    SharedPreferences config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //db = new BaseDatos(Main.this);
        //database = db.getWritableDatabase();

        config = getSharedPreferences("config",MODE_PRIVATE);
        Boolean userLogged      = config.getBoolean("usuario_logueado",false);
        String nombre_usuario   = config.getString("nombre_usuario","");
        String usuario          = config.getString("usuario","");
        Integer id_usuario      = config.getInt("id_usuario",0);
        Integer id_bodega       = config.getInt("id_bodega",0);

        Timer t = new Timer();
        if(userLogged){
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    //Constantes.OLD_VERSION_BASEDATOS = database.getVersion();
                    Intent i;
                    int idDefaultProceso = config.getInt("id_proceso", 0);
                    int idDefaultContrato = config.getInt("id_contrato", 0);
                    int idDefaultMunicipio = config.getInt("id_municipio", 0);
                    //int idDefaultBodega = config.getInt("id_bodega", 0);
                    if(idDefaultProceso!= 0 && idDefaultContrato!=0 && idDefaultMunicipio!=0 ){
                        i = new Intent(Main.this,Menu.class);
                    }
                    else{
                        i = new Intent(Main.this,ConfigurarArea.class);
                    }
                    startActivity(i);
                    finish();
                }
            },1000);
        }
        else{
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(Main.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }
    }
}
