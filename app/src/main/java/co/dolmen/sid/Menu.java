package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button btnPerfil;
    Button btnSalir;
    Button btnCensoTecnico;
    SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        config = getSharedPreferences("config", MODE_PRIVATE);

        btnPerfil = findViewById(R.id.btn_perfil);
        btnSalir = findViewById(R.id.btn_salir);
        btnCensoTecnico = findViewById(R.id.btn_censo_tecnico);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ConfigurarArea.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                config.edit().clear().commit();
                Intent i = new Intent(Menu.this,Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnCensoTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,CensoTecnico.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
    }
}
