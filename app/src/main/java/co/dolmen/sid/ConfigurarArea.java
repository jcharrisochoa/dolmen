package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import co.dolmen.sid.entidad.Equipo;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArchivoActividadDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EquipoDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoBalastoDB;
import co.dolmen.sid.modelo.TipoBaseFotoceldaDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInstalacionRedDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import co.dolmen.sid.utilidades.MiBaseDatos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ConfigurarArea extends AppCompatActivity {

    private String nombreUsuario;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int setDefaultPositionMunicipio;
    private int idDefaultProceso;
    private int setDefaultPositionProceso;
    private int idDefaultContrato;
    private int setDefaultPositionContrato = 0;
    private int idDefaultBodega;
    private int setDefaultPositionBodega = 0;
    private boolean salida;

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    FloatingActionButton btnSiguiente;
    FloatingActionButton btnCerrarSesion;

    AlertDialog.Builder alert;
    AlertDialog.Builder alertConfirm;
    Spinner sltMunicipio;
    Spinner sltProceso;
    Spinner sltContrato;
    Spinner sltBodega;
    TextView tvNombreUsuario;
    //--Objetos List
    List<DataSpinner> municipioList;
    List<DataSpinner> procesoList;
    List<DataSpinner> contratoList;
    List<DataSpinner> bodegaList;

    MiBaseDatos miBaseDatos;
    public LocationManager ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_area);
        conn = new BaseDatos(ConfigurarArea.this);
        database = conn.getReadableDatabase();
        miBaseDatos = new MiBaseDatos(database);
        //Log.d("versiondb","db:"+database.getVersion() +"!="+ Constantes.OLD_VERSION_BASEDATOS);

        /*if(database.getVersion() != Constantes.OLD_VERSION_BASEDATOS){
            Intent i = new Intent(ConfigurarArea.this, Parametros.class);
            startActivity(i);
            finish();
        }*/


        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        alertConfirm = new AlertDialog.Builder(this);
        alertConfirm.setCancelable(false);
        alertConfirm.setTitle(R.string.titulo_alerta);
        alertConfirm.setIcon(R.drawable.icon_problem);

        btnSiguiente = findViewById(R.id.fab_siguiente);
        btnCerrarSesion = findViewById(R.id.fab_cerrar_sesion);
        //-
        sltMunicipio    = findViewById(R.id.sltMunicipio);
        sltProceso      = findViewById(R.id.sltProceso);
        sltContrato     = findViewById(R.id.sltContrato);
        sltBodega       = findViewById(R.id.sltBodega);
        tvNombreUsuario = findViewById(R.id.nombre_usuario);

        //--Preferencias--
        config = getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editar = config.edit();
        editar.putBoolean("usuario_logueado",true);
        editar.commit();

        nombreUsuario       = config.getString("nombre_usuario", "");
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultMunicipio  = config.getInt("id_municipio",0);
        idDefaultProceso    = config.getInt("id_proceso",0);
        idDefaultContrato   = config.getInt("id_contrato",0);
        idDefaultBodega     = config.getInt("id_bodega",0);


        tvNombreUsuario.setText(nombreUsuario);

        sltMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarContrato(database,municipioList.get(i).getId(),procesoList.get(sltProceso.getSelectedItemPosition()).getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sltProceso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarContrato(database,municipioList.get(sltMunicipio.getSelectedItemPosition()).getId(),procesoList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PERMISSIONS_REQUEST_LOCATION = 0;
                if(validarFrm()){
                    //Log.d("GidDefaultContrato",""+contratoList.get(sltContrato.getSelectedItemPosition()).getId());
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
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ConfigurarArea.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSIONS_REQUEST_LOCATION);
                        }
                        else {
                            SharedPreferences.Editor editar = config.edit();
                            editar.putBoolean("config", true);
                            editar.putInt("id_municipio", municipioList.get(sltMunicipio.getSelectedItemPosition()).getId());
                            editar.putInt("id_proceso", procesoList.get(sltProceso.getSelectedItemPosition()).getId());
                            editar.putInt("id_contrato", contratoList.get(sltContrato.getSelectedItemPosition()).getId());
                            editar.putString("nombreMunicipio", municipioList.get(sltMunicipio.getSelectedItemPosition()).getDescripcion());
                            editar.putString("nombreProceso", procesoList.get(sltProceso.getSelectedItemPosition()).getDescripcion());
                            editar.putString("nombreContrato", contratoList.get(sltContrato.getSelectedItemPosition()).getDescripcion());
                            editar.putInt("id_bodega", bodegaList.get(sltBodega.getSelectedItemPosition()).getId());
                            editar.commit();
                            database.close();
                            Intent i = new Intent(ConfigurarArea.this, Menu.class);
                            startActivity(i);
                            finish();
                        }
                    }
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
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = miBaseDatos.actividadOperativaDB.porSincronizar();
                if(cursor.getCount()>0){
                    alertConfirm.setMessage("Tiene "+cursor.getCount()+" actividad(es) por sincrinozar\n\n"+getText(R.string.alert_cerrar_sesion));
                }
                else{
                    alertConfirm.setMessage(R.string.alert_cerrar_sesion);
                }


                alertConfirm.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        try {
                            miBaseDatos.eliminarDatos();
                            config.edit().clear().commit();
                            Intent intent = new Intent(ConfigurarArea.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            ConfigurarArea.this.finish();
                        }catch (SQLException e){
                            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
                alertConfirm.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                    }
                });
                alertConfirm.create().show();
            }
        });
        cargarMunicipio(database);
        cargarProceso(database);
        cargarBodega(database);
    }

    private boolean validarFrm() {
        if (municipioList.get(sltMunicipio.getSelectedItemPosition()).getId() == 0) {
            alert.setMessage(getString(R.string.alert_municipio));
            this.salida = false;
        } else {
            if (procesoList.get(sltProceso.getSelectedItemPosition()).getId() == 0) {
                alert.setMessage(getString(R.string.alert_proceso));
                this.salida = false;
            } else {
                if (contratoList.get(sltContrato.getSelectedItemPosition()).getId() == 0) {
                    alert.setMessage(getString(R.string.alert_contrato));
                    this.salida = false;
                }
                else{
                    this.salida = true;
                }
            }
        }
        return this.salida;
    }

    private void cargarMunicipio(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        municipioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        MunicipioDB municipioDB = new MunicipioDB(sqLiteDatabase);
        Cursor cursor = municipioDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        municipioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if(idDefaultMunicipio == 0){
            setDefaultPositionMunicipio = 0;
        }
        if (cursor.moveToFirst()) {
            do {
                i++;
                dataSpinner = new DataSpinner(cursor.getInt(0),cursor.getString(1).toUpperCase());
                municipioList.add(dataSpinner);
                labels.add(cursor.getString(1).toUpperCase());
                if(idDefaultMunicipio == cursor.getInt(0)){
                    setDefaultPositionMunicipio = i;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltMunicipio.setAdapter(dataAdapter);
        sltMunicipio.setSelection(setDefaultPositionMunicipio);
    }

    private void cargarProceso(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        procesoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ProcesoSgcDB procesoSgcDB = new ProcesoSgcDB(sqLiteDatabase);
        Cursor cursor = procesoSgcDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        procesoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());

        if (cursor.moveToFirst()) {
            do {
                i++;
                dataSpinner = new DataSpinner(cursor.getInt(0),cursor.getString(1).toUpperCase());
                procesoList.add(dataSpinner);
                labels.add(cursor.getString(1).toUpperCase());
                if(idDefaultProceso == cursor.getInt(0)){
                    setDefaultPositionProceso = i;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltProceso.setAdapter(dataAdapter);
        sltProceso.setSelection(setDefaultPositionProceso);
    }

    private void cargarContrato(SQLiteDatabase sqLiteDatabase,int id_municipio,int id_proceso_sgc) {
        contratoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        if(id_municipio == 0 || id_proceso_sgc==0){
            DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
            contratoList.add(dataSpinner);
            labels.add(getText(R.string.seleccione).toString());
        }
        else {
            ContratoDB contratoDB = new ContratoDB(sqLiteDatabase);
            Cursor cursor = contratoDB.consultarTodo(id_municipio, id_proceso_sgc);
            DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
            contratoList.add(dataSpinner);
            labels.add(getText(R.string.seleccione).toString());
            Log.d ("Count",""+cursor.getCount());
            if (cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        i++;
                        dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(3).toUpperCase());
                        contratoList.add(dataSpinner);
                        labels.add(cursor.getString(3).toUpperCase());
                        if (idDefaultContrato == cursor.getInt(0)) {
                            setDefaultPositionContrato = i;
                        }
                    } while (cursor.moveToNext());
                }
            }
            else{
                setDefaultPositionContrato = i;
            }
            cursor.close();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltContrato.setAdapter(dataAdapter);
        sltContrato.setSelection(setDefaultPositionContrato);
    }

    private void cargarBodega(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        bodegaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        BodegaDB bodegaDB = new BodegaDB(sqLiteDatabase);
        Cursor cursor = bodegaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        bodegaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if(idDefaultBodega == 0){
            setDefaultPositionBodega = 0;
        }
        if (cursor.moveToFirst()) {
            do {
                i++;
                dataSpinner = new DataSpinner(cursor.getInt(0),cursor.getString(1).toUpperCase());
                bodegaList.add(dataSpinner);
                labels.add(cursor.getString(1).toUpperCase());
                if(idDefaultBodega == cursor.getInt(0)){
                    setDefaultPositionBodega = i;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltBodega.setAdapter(dataAdapter);
        sltBodega.setSelection(setDefaultPositionBodega);
    }

    public boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return (ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }
}
