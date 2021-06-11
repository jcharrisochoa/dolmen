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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
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

    private EditText txtDatoBusqueda;

    private FloatingActionButton btnAgregar;
    private FloatingActionButton btnCancelar;

    private RecyclerView recyclerView;
    private AdapterElemento adapterElemento;
    private ElementoDB elementoDB;
    private ArrayList<Elemento> elementoArrayList;

    private Switch swBuscarPorElemento;
    private Switch swBuscarPorDireccion;
    private Switch swBuscarPorPlaca;

    private ImageButton btnBuscarTransformador;
    private ImageButton btnCancelarBusqueda;


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

        txtDatoBusqueda         = findViewById(R.id.txt_dato_busqueda);

        btnBuscarTransformador      = findViewById(R.id.btn_buscar_transformador);
        btnCancelarBusqueda     = findViewById(R.id.btn_cancelar_busqueda);

        swBuscarPorElemento     = findViewById(R.id.sw_buscar_elemento);
        swBuscarPorDireccion    = findViewById(R.id.sw_buscar_direccion);
        swBuscarPorPlaca        = findViewById(R.id.sw_buscar_placa);


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

        adapterElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaTransformador.this,CensoCargaDistribucion.class);
                Elemento elemento  =  adapterElemento.getElementoArrayList().get(recyclerView.getChildLayoutPosition(v));
                i.putExtra("transformador",elemento);
                startActivity(i);
                ListaTransformador.this.finish();
            }
        });
        recyclerView.setAdapter(adapterElemento);

        txtDatoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorPlaca.isChecked()
                );
            }
        });

        btnBuscarTransformador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorPlaca.isChecked()
                );
            }
        });

        btnCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDatoBusqueda.setText("");
                swBuscarPorElemento.setChecked(false);
                swBuscarPorDireccion.setChecked(false);
                swBuscarPorPlaca.setChecked(true);
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorPlaca.isChecked()
                );
            }
        });

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

    private void filter(String dato, boolean elemento, boolean direccion, boolean placa){
        ArrayList<Elemento> filterElemento = new ArrayList<Elemento>();

        for(Elemento e:elementoArrayList){
            if(elemento) {
                if (e.getElemento_no().contains(dato)) {
                    filterElemento.add(e);
                }
            }
            if(direccion) {
                if (e.getDireccion().toUpperCase().contains(dato.toUpperCase())) {
                    filterElemento.add(e);
                }
            }
            if(placa) {
                if (e.getPlacaCT().toUpperCase().contains(dato.toUpperCase()) || e.getPlacaMT().toUpperCase().contains(dato.toUpperCase())) {
                    filterElemento.add(e);
                }
            }
        }
        adapterElemento.filterList(filterElemento);
    }


    private void consultarTransformador(SQLiteDatabase database) throws ParseException {
        elementoArrayList.clear();
        Elemento elemento;
        try {
            Cursor cursor = elementoDB.consultarElemento(idDefaultMunicipio,idDefaultProceso,0,14);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {


                    Mobiliario mobiliario = new Mobiliario(
                            cursor.getInt(cursor.getColumnIndex("id_mobiliario")),
                            cursor.getString(cursor.getColumnIndex("mobiliario"))
                    );

                    ReferenciaMobiliario referenciaMobiliario = new ReferenciaMobiliario(
                            cursor.getInt(cursor.getColumnIndex("id_referencia")),
                            cursor.getString(cursor.getColumnIndex("referencia"))
                    );
                    elemento = new Elemento();
                    elemento.setMobiliario(mobiliario);
                    elemento.setReferenciaMobiliario(referenciaMobiliario);
                    elemento.setElemento_no(cursor.getString(cursor.getColumnIndex("elemento_no")));
                    elemento.setDireccion(cursor.getString(cursor.getColumnIndex("direccion")));
                    elemento.setPlacaMT(cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));
                    elemento.setPlacaCT(cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                    elemento.setPotenciaTransformador(cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                    //elemento.setTransformadorExclusivo((cursor.getString(cursor.getColumnIndex("transformador_compartido ")).contentEquals("S"))?false:true);
                    elementoArrayList.add(elemento);

                }
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}