package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import co.dolmen.sid.entidad.ActividadOperativa;

public class DetalleActividad extends AppCompatActivity {

    private FloatingActionButton btnCancelar;
    private FloatingActionButton btnEnSitio;

    private TextView txtMunicipio;
    private TextView txtActividad;
    private TextView txtFchActividad;
    private TextView txtPrograma;
    private TextView txtTipoOperacion;
    private TextView txtReporte;
    private TextView txtProceso;
    private TextView txtVehiculo;
    private TextView txtElemento;
    private TextView txtMobiliario;
    private TextView txtReferencia;
    private TextView txtBarrio;
    private TextView txtDireccion;

    private ActividadOperativa actividadOperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);
        Intent i = getIntent();

        actividadOperativa = (ActividadOperativa)i.getSerializableExtra("actividadOperativa");
        setTitle(getString(R.string.titulo_actividad)+" No "+actividadOperativa.getIdActividad());

        btnEnSitio     = findViewById(R.id.fab_en_sitio);
        btnCancelar     = findViewById(R.id.fab_cancelar);

        txtMunicipio    = findViewById(R.id.txt_municipio);
        txtActividad    = findViewById(R.id.txt_actividad);
        txtFchActividad = findViewById(R.id.txt_fch_actividad);
        txtPrograma     = findViewById(R.id.txt_programa);
        txtTipoOperacion= findViewById(R.id.txt_tipo_operacion);
        txtReporte      = findViewById(R.id.txt_reporte);
        txtProceso      = findViewById(R.id.txt_proceso);
        txtVehiculo     = findViewById(R.id.txt_vehiculo);
        txtElemento     = findViewById(R.id.txt_elemento);
        txtMobiliario   = findViewById(R.id.txt_mobiliario);
        txtReferencia   = findViewById(R.id.txt_referencia);
        txtBarrio       = findViewById(R.id.txt_barrio);
        txtDireccion    = findViewById(R.id.txt_direccion);

        setDetalle(actividadOperativa);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalleActividad.this, ListaActividad.class);
                startActivity(i);
                DetalleActividad.this.finish();
            }
        });

        btnEnSitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetalleActividad.this,EjecutaActividad.class);
                i.putExtra("actividadOperativa",actividadOperativa);
                startActivity(i);
                DetalleActividad.this.finish();

            }
        });

    }

    private void setDetalle(ActividadOperativa  ao){
        txtMunicipio.setText(ao.getBarrio().getDescripcion());
        txtActividad.setText(String.valueOf(ao.getIdActividad()));
        txtFchActividad.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ao.getFechaActividad()));
        txtPrograma.setText(String.valueOf(ao.getPrograma().getId()));
        txtTipoOperacion.setText(ao.getTipoActividad().getDescripcion());
        //txtReporte.setText(ao.getR);
        txtProceso.setText(ao.getProcesoSgc().getDescripcion());
        txtVehiculo.setText(ao.getEquipo().getSerial());
        txtElemento.setText(ao.getElemento().getElemento_no());
        txtMobiliario.setText(ao.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(ao.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());
        txtBarrio.setText(ao.getBarrio().getNombreBarrio());
        txtDireccion.setText(ao.getDireccion());
    }

}
