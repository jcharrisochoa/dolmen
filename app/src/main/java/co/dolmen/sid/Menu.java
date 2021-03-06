package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import co.dolmen.sid.utilidades.HandleListenLocation;
import co.dolmen.sid.utilidades.MiBaseDatos;
import co.dolmen.sid.utilidades.MiLocalizacion;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class Menu extends AppCompatActivity {

    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idCenso;

    ImageButton btnPerfil;
    ImageButton btnSalir;
    ImageButton btnCensoTecnico;
    ImageButton btnReporteDano;
    ImageButton btnCrearActividad;
    ImageButton btnActualizarElemento;
    ImageButton btnCrearElemento;
    ImageButton btnCensoCarga;
    ImageButton btnMisActividades;
    ImageButton btnMiStock;

    SharedPreferences config;
    TextView txtNombreMunicipio;
    TextView txtNombreProceso;
    TextView txtNombreContrato;

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    AlertDialog.Builder alert;

    MiBaseDatos miBaseDatos;
    public LocationManager ubicacion;
    private boolean gpsListener;
    static MiLocalizacion miLocalizacion;

    public void getLocation(Location lo){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle(getText(R.string.titulo_menu));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(miLocalizacion == null) {
                miLocalizacion = new MiLocalizacion(getApplicationContext());
                if(miLocalizacion.estadoGPS()) {
                    ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                    ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, miLocalizacion);
                    //gpsListener = true;
                }
            }
        }


        conn = new BaseDatos(Menu.this);
        database = conn.getReadableDatabase();
        miBaseDatos = new MiBaseDatos(database);

        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        config = getSharedPreferences("config", MODE_PRIVATE);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso   = config.getString("nombreProceso", "");
        nombreContrato  = config.getString("nombreContrato", "");


        btnPerfil           = findViewById(R.id.btn_perfil);
        btnSalir            = findViewById(R.id.btn_salir);
        btnReporteDano      = findViewById(R.id.btn_reportar_dano);
        btnCrearActividad   = findViewById(R.id.btn_crear_actividad);
        btnActualizarElemento = findViewById(R.id.btn_actualizar_elemento);
        btnCrearElemento      = findViewById(R.id.btn_crear_elemento);
        btnCensoCarga       = findViewById(R.id.btn_censo_carga);
        btnCensoTecnico     = findViewById(R.id.btn_censo_tecnico);
        btnMisActividades   = findViewById(R.id.btn_mis_actividades);
        btnMiStock          = findViewById(R.id.btn_mi_stock);

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
                String mensaje = "";
                Cursor cursorCenso = miBaseDatos.censoDB.consultarTodo();
                Cursor cursor = miBaseDatos.actividadOperativaDB.porSincronizar();

                if(cursor.getCount()>0){
                    mensaje = "Tiene "+cursor.getCount()+" actividad(es) por sincrinozar\n\n";
                }

                if(cursorCenso.getCount()>0){
                    mensaje = "Tiene "+cursorCenso.getCount()+" actividad(es) de Censo por sincrinozar\n\n";
                }

                if(!mensaje.isEmpty()) {
                    alert.setMessage(mensaje + getText(R.string.alert_cerrar_sesion));
                }
                else{
                    alert.setMessage(R.string.alert_cerrar_sesion);
                }

                alert.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        try {
                            miBaseDatos.eliminarDatos();
                            config.edit().clear().commit();
                            Intent intent = new Intent(Menu.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            Menu.this.finish();
                        }catch (SQLException e){
                            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
                alert.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                    }
                });
                alert.create().show();

            }
        });

        btnCensoTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(Menu.this,SubMenuCensoTecnico.class);
                Intent i = new Intent(Menu.this,CensoTecnico.class);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnCensoCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] items = {"Centro Distribución","Secuencial"};

                final int[] selected = new int[1];

                new MaterialAlertDialogBuilder(Menu.this)
                        .setTitle(R.string.tipo_recorrido)
                        .setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                selected[0] = which;

                                /*dialog.dismiss();
                                ProgressDialog progress = new ProgressDialog(Menu.this);
                                progress.setCancelable(false);
                                progress.setTitle(R.string.titulo_alerta);
                                progress.setIcon(R.drawable.icon_info);
                                progress.setMessage(getString(R.string.cargando));
                                switch (which){
                                    case 0:
                                        progress.show();
                                        break;
                                    case 1:
                                        progress.show();
                                        Intent i = new Intent(Menu.this,CensoCarga.class);
                                        startActivity(i);
                                        Menu.this.finish();
                                        break;
                                }*/
                            }
                        })
                        .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ProgressDialog progress = new ProgressDialog(Menu.this);
                                progress.setCancelable(false);
                                progress.setTitle(R.string.titulo_alerta);
                                progress.setIcon(R.drawable.icon_info);
                                progress.setMessage(getString(R.string.cargando));
                                Intent i;
                                switch (selected[0]){
                                    case 0:
                                        progress.show();
                                        i = new Intent(Menu.this,ListaTransformador.class);
                                        startActivity(i);
                                        Menu.this.finish();
                                        break;
                                    case 1:
                                        progress.show();
                                        i = new Intent(Menu.this,CensoCarga.class);
                                        startActivity(i);
                                        Menu.this.finish();
                                        break;
                                    default:
                                        dialog.dismiss();
                                }
                            }
                        })
                        .setCancelable(false)
                .create().show();
            }
        });

        btnReporteDano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ReporteDano.class);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,GenerarActividadOperativa.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnActualizarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ActualizarElemento.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnCrearElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,CrearElemento.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnMisActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, ListaActividad.class);
                //i.putExtra("LOCATION","");
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnMiStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, ListaStock.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
    }
}
