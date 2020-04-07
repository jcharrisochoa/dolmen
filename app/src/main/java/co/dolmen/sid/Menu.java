package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idCenso;

    Button btnPerfil;
    Button btnSalir;
    Button btnCensoTecnico;
    SharedPreferences config;
    TextView txtNombreMunicipio;
    TextView txtNombreProceso;
    TextView txtNombreContrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        config = getSharedPreferences("config", MODE_PRIVATE);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");

        btnPerfil = findViewById(R.id.btn_perfil);
        btnSalir = findViewById(R.id.btn_salir);

        btnCensoTecnico = findViewById(R.id.btn_censo_tecnico);
        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

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
