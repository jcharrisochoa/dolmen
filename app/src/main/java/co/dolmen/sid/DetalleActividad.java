package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.text.LineBreaker;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.modelo.MovimientoArticuloDB;
import co.dolmen.sid.utilidades.MiLocalizacion;

public class DetalleActividad extends AppCompatActivity {

    private FloatingActionButton btnCancelar;
    private FloatingActionButton btnEnSitio;

    private TextView txtMunicipio;
    private TextView txtActividad;
    private TextView txtFchActividad;
    private TextView txtPrograma;
    private TextView txtTipoOperacion;
    private TextView txtReporte;
    private TextView txtET;
    private TextView txtProceso;
    private TextView txtVehiculo;
    private TextView txtElemento;
    private TextView txtMobiliario;
    private TextView txtReferencia;
    private TextView txtBarrio;
    private TextView txtDireccion;
    private TextView txtEjecucion;
    private TextView txtEstado;
    private TextView txtObservacion;
    private TextView txtLatitud;
    private TextView txtLongitud;
    private TextView txtSincronizada;

    private LinearLayout layoutMaterial;
    private ActividadOperativa actividadOperativa;
    public LocationManager ubicacion;
    private boolean gpsListener;
    AlertDialog.Builder alert;
    MiLocalizacion miLocalizacion;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);

        conn = new BaseDatos(this);
        database = conn.getReadableDatabase();

        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        Intent i = getIntent();
        actividadOperativa = (ActividadOperativa)i.getSerializableExtra("actividadOperativa");
        setTitle(getString(R.string.titulo_actividad)+" No "+actividadOperativa.getIdActividad());


        btnEnSitio     = findViewById(R.id.fab_en_sitio);
        btnCancelar     = findViewById(R.id.fab_cancelar);

        txtMunicipio    = findViewById(R.id.txt_municipio);
        //txtActividad    = findViewById(R.id.txt_actividad);
        txtFchActividad = findViewById(R.id.txt_fch_actividad);
        txtPrograma     = findViewById(R.id.txt_programa);
        txtTipoOperacion= findViewById(R.id.txt_tipo_operacion);
        txtReporte      = findViewById(R.id.txt_reporte);
        txtET           = findViewById(R.id.txt_et);
        txtProceso      = findViewById(R.id.txt_proceso);
        txtVehiculo     = findViewById(R.id.txt_vehiculo);
        txtElemento     = findViewById(R.id.txt_elemento);
        txtMobiliario   = findViewById(R.id.txt_mobiliario);
        txtReferencia   = findViewById(R.id.txt_referencia);
        txtBarrio       = findViewById(R.id.txt_barrio);
        txtDireccion    = findViewById(R.id.txt_direccion);
        txtEjecucion    = findViewById(R.id.txt_fch_ejecucion);
        txtEstado       = findViewById(R.id.txt_estado);
        txtObservacion  = findViewById(R.id.txt_observacion);
        txtLatitud      = findViewById(R.id.txt_latitud);
        txtLongitud      = findViewById(R.id.txt_longitud);
        txtSincronizada = findViewById(R.id.txt_sincronizada);

        layoutMaterial    = findViewById(R.id.layout_material);

        setDetalle(actividadOperativa);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            miLocalizacion = new MiLocalizacion(getApplicationContext());
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, miLocalizacion);
            gpsListener  = true;
        }

        if(actividadOperativa.getEstadoActividad().getId()==2){
            btnEnSitio.hide();
        }

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
                if(!estadoGPS()){
                    alert.setTitle(getString(R.string.titulo_alerta));
                    alert.setMessage(getString(R.string.alert_gps_deshabilitado));
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();
                }
                else {
                    actividadOperativa.setFechaEnSitio(new Date());
                    if(activarCoordenadas()) {
                        Log.d("programacion","Lat:"+miLocalizacion.getLatitud());

                        actividadOperativa.setLatitud(miLocalizacion.getLatitud());
                        actividadOperativa.setLongitud(miLocalizacion.getLongitud());
                        if (miLocalizacion.getLatitud() == 0 && miLocalizacion.getLongitud() == 0) {
                            alert.setMessage("No fue posible capturar la geolocalización.\n ¿Quiere Continuar?");
                            alert.setNegativeButton(getText(R.string.btn_cancelar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.setPositiveButton(getText(R.string.btn_aceptar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(DetalleActividad.this, EjecutaActividad.class);
                                    i.putExtra("actividadOperativa", actividadOperativa);
                                    startActivity(i);
                                    DetalleActividad.this.finish();
                                }
                            });
                            alert.create().show();
                        } else {
                            Intent i = new Intent(DetalleActividad.this, EjecutaActividad.class);
                            i.putExtra("actividadOperativa", actividadOperativa);
                            startActivity(i);
                            DetalleActividad.this.finish();
                        }
                    }
                }
            }
        });

    }

    private void setDetalle(ActividadOperativa  ao){
        txtMunicipio.setText(ao.getBarrio().getDescripcion());
       //txtActividad.setText(String.valueOf(ao.getIdActividad()));
        txtFchActividad.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ao.getFechaActividad()));
        txtPrograma.setText(String.valueOf(ao.getPrograma().getId()));
        txtTipoOperacion.setText(ao.getTipoActividad().getDescripcion());
        txtET.setText(ao.getEt());
        //txtReporte.setText();
        txtProceso.setText(ao.getProcesoSgc().getDescripcion());
        txtVehiculo.setText(ao.getEquipo().getSerial());
        txtElemento.setText(ao.getElemento().getElemento_no());
        txtMobiliario.setText(ao.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(ao.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());
        txtBarrio.setText(ao.getBarrio().getNombreBarrio());
        txtDireccion.setText(ao.getDireccion());
        if(ao.getFechaEjecucion() != null)
            txtEjecucion.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ao.getFechaEjecucion()));
        txtEstado.setText(ao.getEstadoActividad().getDescripcion());
        if(ao.getEstadoActividad().getId()!=2){
            txtEstado.setTextColor(ContextCompat.getColor(txtEstado.getContext(), R.color.colorAccent));
        }
        else{
            txtEstado.setTextColor(ContextCompat.getColor(txtEstado.getContext(), R.color.colorVerifed));
        }
        txtObservacion.setText(ao.getObservacion());
        txtLatitud.setText(String.valueOf(ao.getLatitud()));
        txtLongitud.setText(String.valueOf(ao.getLongitud()));
        txtSincronizada.setText((ao.getPendienteSincronizar().contentEquals("S"))?getString(R.string.NO):getString(R.string.SI));

        //--Materiales
        MovimientoArticuloDB movimientoArticuloDB = new MovimientoArticuloDB(database);
        Cursor cursor = movimientoArticuloDB.consultarTodo(actividadOperativa.getIdActividad());
        tablaMateriales(cursor);

    }

    public boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return (ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    private boolean activarCoordenadas() {
        boolean sw = true;
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        int PERMISSIONS_REQUEST_LOCATION = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            sw = false;

        }
        else {
            if(!gpsListener) {
                //Log.d("programacion","activo");
                //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, miLocalizacion);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                    miLocalizacion = new MiLocalizacion(getApplicationContext());
                    ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, miLocalizacion);
                    gpsListener  = true;
                }

            }
            else{
                ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, miLocalizacion);
            }
        }
        return sw;
    }

    private void tablaMateriales(Cursor cursor){
        LayoutInflater layoutInflater;
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                layoutInflater = LayoutInflater.from(this);
                View view = layoutInflater.inflate(R.layout.view_item_articulos,null);
                TextView tipoStock = view.findViewById(R.id.txt_tipo_stock);
                TextView movimiento = view.findViewById(R.id.txt_tipo_movimiento);
                TextView cantidad   = view.findViewById(R.id.txt_cantidad);
                TextView articulo   = view.findViewById(R.id.txt_descripcion_articulo);

                tipoStock.setText(cursor.getString(cursor.getColumnIndex("tipo_stock")));
                movimiento.setText(cursor.getString(cursor.getColumnIndex("movimiento")));
                cantidad.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("cantidad"))));
                articulo.setText("("+String.valueOf(cursor.getInt(cursor.getColumnIndex("id_articulo")))+") : "+cursor.getString(cursor.getColumnIndex("articulo")));
                layoutMaterial.addView(view);
            }
        }

    }
}
