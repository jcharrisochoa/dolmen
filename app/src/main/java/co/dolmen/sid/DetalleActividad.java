package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

import co.dolmen.sid.entidad.ActividadOperativa;

public class DetalleActividad extends AppCompatActivity implements Serializable {

    private Button btnCancelar;
    private Button btnEjecutar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);
        Intent i = getIntent();

        ActividadOperativa actividadOperativa = (ActividadOperativa)i.getSerializableExtra("actividadOperativa");
        setTitle(getString(R.string.titulo_actividad)+" No "+actividadOperativa.getIdActividad());

        btnEjecutar           = findViewById(R.id.btn_ejecutar);
        btnCancelar            = findViewById(R.id.btn_cancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalleActividad.this, ListaActividad.class);
                startActivity(i);
                DetalleActividad.this.finish();
            }
        });

    }
}