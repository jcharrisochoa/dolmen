package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends AppCompatActivity {
    //SQLiteOpenHelper db;
    //SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //db = new BaseDatos(Main.this);
        //database = db.getWritableDatabase();

        SharedPreferences config = getSharedPreferences("config",MODE_PRIVATE);
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
                    Intent i = new Intent(Main.this,ConfigurarArea.class);
                    startActivity(i);
                    finish();
                }
            },3000);
        }
        else{
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(Main.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }
    }
}
