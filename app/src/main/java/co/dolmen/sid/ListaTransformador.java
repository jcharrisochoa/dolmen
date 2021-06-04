package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.utilidades.AdapterData;
import co.dolmen.sid.utilidades.AdapterElemento;

public class ListaTransformador extends AppCompatActivity {

    private SharedPreferences config;
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;


    private FloatingActionButton btnAgregar;
    private FloatingActionButton btnCancelar;

    private RecyclerView recyclerView;
    private AdapterElemento adapterElemento;
    private ElementoDB elementoDB;
    private ArrayList<Elemento> elementoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_transformador);

        setTitle(R.string.lista_transformador);

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);

        conn = new BaseDatos(ListaTransformador.this);
        database = conn.getReadableDatabase();

        btnCancelar = findViewById(R.id.fab_cancelar);
        btnAgregar  = findViewById(R.id.fab_agregar);

        elementoArrayList = new ArrayList<Elemento>();
        recyclerView = findViewById(R.id.rw_transformador);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        );

        elementoDB = new ElementoDB(database);
        try {
            consultarTransformador(database);
        } catch (ParseException e) {
            Log.d("Error",e.getMessage());
        }
        adapterElemento = new AdapterElemento(elementoArrayList);

        recyclerView.setAdapter(adapterElemento);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaTransformador.this, CrearElemento.class);
                startActivity(i);
                ListaTransformador.this.finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Menu.class);
                startActivity(i);
                ListaTransformador.this.finish();
            }
        });
    }

    private void consultarTransformador(SQLiteDatabase database) throws ParseException {
        elementoArrayList.clear();
        Elemento elemento;
        try {
            Cursor cursor = elementoDB.consultarElemento(idDefaultMunicipio,idDefaultProceso,0,14);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    elemento = new Elemento();
                    elemento.setElemento_no(cursor.getString(cursor.getColumnIndex("elemento_no")));
                    elemento.setDireccion(cursor.getString(cursor.getColumnIndex("direccion")));
                    elemento.setPlacaMT(cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));
                    elemento.setPlacaCT(cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                    elemento.setPotenciaTransformador(cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                    elementoArrayList.add(elemento);

                }
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}