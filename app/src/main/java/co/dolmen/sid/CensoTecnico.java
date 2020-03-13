package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.NormaConstruccionRed;
import co.dolmen.sid.entidad.RetenidaPoste;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;

public class CensoTecnico extends AppCompatActivity {

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String nombreMunicipio;
    //--
    Spinner sltTipologia;
    Spinner sltBarrio;
    Spinner sltTipoInterseccionA;
    Spinner sltTipoInterseccionB;
    Spinner sltClaseVia;
    Spinner sltTipoPoste;
    Spinner sltNormaConstruccionPoste;
    Spinner sltTipoRetenida;
    Spinner sltTipoRed;
    Spinner sltTipoTension;
    Spinner sltTipoEstructura;
    Spinner getSltNormaConstruccionRed;
    //--
    Button btnCancelar;
    //--
    ArrayList<DataSpinner> tipologiaList;
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> tipoInterseccionA;
    ArrayList<DataSpinner> tipoInterseccionB;
    ArrayList<DataSpinner> claseViaList;
    ArrayList<DataSpinner> tipoPosteList;
    ArrayList<DataSpinner> normaConstruccionPosteList;
    ArrayList<DataSpinner> tipoRetenidaList;
    ArrayList<DataSpinner> tipoRedList;
    ArrayList<DataSpinner> tipoTensionList;
    ArrayList<DataSpinner> tipoEstructuraList;
    ArrayList<DataSpinner> normaConstruccionRedList;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo_tecnico);
        conn = new BaseDatos(CensoTecnico.this);
        database = conn.getReadableDatabase();

        //--
        config = getSharedPreferences("config",MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultMunicipio  = config.getInt("id_municipio",0);
        idDefaultProceso    = config.getInt("id_proceso",0);
        idDefaultContrato   = config.getInt("id_contrato",0);
        nombreMunicipio = config.getString("nombreMunicipio", "");

        setTitle(getText(R.string.titulo_censo_tecnico)+" ("+nombreMunicipio+")");
        //--
        sltTipologia                = findViewById(R.id.slt_tipologia);
        sltBarrio                   = findViewById(R.id.slt_barrio);
        sltTipoInterseccionA        = findViewById(R.id.slt_tipo_interseccion_a);
        sltTipoInterseccionB        = findViewById(R.id.slt_tipo_interseccion_b);
        sltClaseVia                 = findViewById(R.id.slt_clase_via);
        sltTipoPoste                = findViewById(R.id.slt_tipo_poste);
        sltNormaConstruccionPoste   = findViewById(R.id.slt_norma_construccion_poste);
        sltTipoRetenida             = findViewById(R.id.slt_tipo_retenida);
        sltTipoRed                  = findViewById(R.id.slt_tipo_red);
        sltTipoTension              = findViewById(R.id.slt_tipo_tension);
        sltTipoEstructura           = findViewById(R.id.slt_tipo_estructura);
        getSltNormaConstruccionRed  = findViewById(R.id.slt_norma_construccion_red);
        //--
        btnCancelar                 = findViewById(R.id.btn_cancelar);
        //--
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CensoTecnico.this,Menu.class);
                startActivity(i);
                CensoTecnico.this.finish();
            }
        });

        sltTipoPoste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionPoste(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sltTipoTension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarTipoEstructura(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sltTipoEstructura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionRed(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cargarTipologia(database);
        cargarBarrio(database);
        cargarTipoInterseccion(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTension(database);
        cargarRetenidaPoste(database);
    }
    private void cargarTipologia(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipologiaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipologiaDB tipologiaDB = new TipologiaDB(sqLiteDatabase);
        Cursor cursor = tipologiaDB.consultarTodo(idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipologiaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipologiaList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipologia.setAdapter(dataAdapter);
    }
    //--
    private void cargarBarrio(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        barrioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        BarrioDB barrioDB = new BarrioDB(sqLiteDatabase);
        Cursor cursor = barrioDB.consultarTodo(idDefaultMunicipio);
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        barrioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    barrioList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltBarrio.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoInterseccion(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoInterseccionA = new ArrayList<DataSpinner>();
        tipoInterseccionB = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoInterseccionDB tipoInterseccionDB = new TipoInterseccionDB(sqLiteDatabase);
        Cursor cursor = tipoInterseccionDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoInterseccionA.add(dataSpinner);
        tipoInterseccionB.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipoInterseccionA.add(dataSpinner);
                    tipoInterseccionB.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoInterseccionA.setAdapter(dataAdapter);
        sltTipoInterseccionB.setAdapter(dataAdapter);
    }
    //--
    private void cargarClaseVia(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        claseViaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ClaseViaDB claseViaDB = new ClaseViaDB(sqLiteDatabase);
        Cursor cursor = claseViaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        claseViaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    claseViaList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltClaseVia.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoPoste(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        tipoPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoPosteDB tipoPosteDB = new TipoPosteDB(sqLiteDatabase);
        Cursor cursor = tipoPosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoPosteList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoPosteList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoPoste.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoRed(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        tipoRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoRedDB tipoRedDB = new TipoRedDB(sqLiteDatabase);
        Cursor cursor = tipoRedDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoRedList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoRedList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRed.setAdapter(dataAdapter);
    }
    //--
    private void cargarTension(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        tipoTensionList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoTensionDB tipoTensionDB = new TipoTensionDB(sqLiteDatabase);
        Cursor cursor = tipoTensionDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoTensionList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoTensionList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoTension.setAdapter(dataAdapter);
    }
    //--
    private void cargarRetenidaPoste(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        tipoRetenidaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        RetenidaPosteDB retenidaPosteDB = new RetenidaPosteDB(sqLiteDatabase);
        Cursor cursor = retenidaPosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoRetenidaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoRetenidaList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRetenida.setAdapter(dataAdapter);
    }

    private void cargarNormaConstruccionPoste(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        int idTipoPoste = tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId();
        normaConstruccionPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionPosteDB normaConstruccionPosteDB = new NormaConstruccionPosteDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionPosteDB.consultarTodo(idTipoPoste);
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        normaConstruccionPosteList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    normaConstruccionPosteList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltNormaConstruccionPoste.setAdapter(dataAdapter);
    }

    private void cargarTipoEstructura(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        int idTipoTension = tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId();
        tipoEstructuraList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoEstructuraDB tipoEstructuraDB = new TipoEstructuraDB(sqLiteDatabase);
        Cursor cursor = tipoEstructuraDB.consultarTodo(idTipoTension);
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        tipoEstructuraList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipoEstructuraList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoEstructura.setAdapter(dataAdapter);

    }

    private void cargarNormaConstruccionRed(SQLiteDatabase sqLiteDatabase){
        int i = 0;
        int idTipoEstructura = tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId();
        normaConstruccionRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionRedDB normaConstruccionRedDB = new NormaConstruccionRedDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionRedDB.consultarTodo(idTipoEstructura);
        DataSpinner dataSpinner = new DataSpinner(i,getText(R.string.seleccione).toString());
        normaConstruccionRedList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    normaConstruccionRedList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSltNormaConstruccionRed.setAdapter(dataAdapter);
    }

}
