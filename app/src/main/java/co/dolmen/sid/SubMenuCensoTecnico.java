package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubMenuCensoTecnico extends AppCompatActivity {

    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idCenso;

    Button btnRegistrarElemento;
    Button btnSincronizar;
    Button btnCancelar;
    SharedPreferences config;
    TextView txtNombreMunicipio;
    TextView txtNombreProceso;
    TextView txtNombreContrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu_censo_tecnico);

        config = getSharedPreferences("config", MODE_PRIVATE);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");

        btnRegistrarElemento = findViewById(R.id.btn_registrar_elemento);
        btnSincronizar = findViewById(R.id.btn_sincronizar);
        btnCancelar = findViewById(R.id.btn_cancelar);


        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

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
                Toast.makeText(getApplicationContext(),"Sincronizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
