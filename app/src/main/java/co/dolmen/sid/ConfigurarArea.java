package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.utilidades.DataSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean salida;

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    Button btnSiguiente;
    Button btnSalir;
    AlertDialog.Builder alert;
    Spinner sltMunicipio;
    Spinner sltProceso;
    Spinner sltContrato;
    TextView tvNombreUsuario;
    //--Objetos List
    List<DataSpinner> municipioList;
    List<DataSpinner> procesoList;
    List<DataSpinner> contratoList;

    private CensoDB censoDB;
    private CensoTipoArmadoDB censoTipoArmadoDB;
    private CensoArchivoDB censoArchivoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_area);
        conn = new BaseDatos(ConfigurarArea.this);
        database = conn.getReadableDatabase();


        censoDB = new CensoDB(database);
        censoTipoArmadoDB = new CensoTipoArmadoDB(database);
        censoArchivoDB = new CensoArchivoDB(database);

        alert = new AlertDialog.Builder(this);

        btnSiguiente = findViewById(R.id.btn_siguiente);
        btnSalir =  findViewById(R.id.btn_salir);
        //-
        sltMunicipio    = findViewById(R.id.sltMunicipio);
        sltProceso      = findViewById(R.id.sltProceso);
        sltContrato     = findViewById(R.id.sltContrato);
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
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(R.string.alert_cerrar_sesion);
                alert.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        try {
                            censoTipoArmadoDB.eliminarDatos();
                            censoArchivoDB.eliminarDatos();
                            censoDB.eliminarDatos();
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
                alert.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFrm()){
                    Log.d("GidDefaultContrato",""+contratoList.get(sltContrato.getSelectedItemPosition()).getId());
                    SharedPreferences.Editor editar = config.edit();
                    editar.putBoolean("config",true);
                    editar.putInt("id_municipio", municipioList.get(sltMunicipio.getSelectedItemPosition()).getId());
                    editar.putInt("id_proceso", procesoList.get(sltProceso.getSelectedItemPosition()).getId());
                    editar.putInt("id_contrato",contratoList.get(sltContrato.getSelectedItemPosition()).getId());
                    editar.putString("nombreMunicipio",municipioList.get(sltMunicipio.getSelectedItemPosition()).getDescripcion());
                    editar.putString("nombreProceso",procesoList.get(sltProceso.getSelectedItemPosition()).getDescripcion());
                    editar.putString("nombreContrato",contratoList.get(sltContrato.getSelectedItemPosition()).getDescripcion());
                    editar.commit();
                    database.close();
                    Intent i = new Intent(ConfigurarArea.this, Menu.class);
                    startActivity(i);
                    finish();
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

        cargarMunicipio(database);
        cargarProceso(database);
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
}
