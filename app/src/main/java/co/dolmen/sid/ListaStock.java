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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import co.dolmen.sid.entidad.Articulo;
import co.dolmen.sid.entidad.Bodega;
import co.dolmen.sid.entidad.CentroCosto;
import co.dolmen.sid.entidad.Stock;
import co.dolmen.sid.entidad.TipoStock;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.utilidades.AdapterStock;

public class ListaStock extends AppCompatActivity {

    private SharedPreferences config;
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;

    private FloatingActionButton fabCancelar;
    private ImageButton btnBuscarStock;
    private ImageButton btnCancelarBusqueda;
    private RecyclerView recyclerView;
    private EditText txtDatoStock;
    private Switch swCodigo;
    private Switch swNombre;

    private ArrayList<Stock> stocksList;
    private AdapterStock adapterStock;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idDefaultBodega;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_stock);

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        idDefaultBodega     = config.getInt("id_bodega", 0);

        conn = new BaseDatos(this);
        database = conn.getReadableDatabase();

        BodegaDB bodegaDB = new BodegaDB(database);
        Cursor cursor = bodegaDB.consultarId(idDefaultBodega);
        cursor.moveToFirst();
        setTitle(cursor.getString(cursor.getColumnIndex("descripcion")).toLowerCase());
        cursor.close();

        stocksList = new ArrayList<Stock>();

        fabCancelar             = findViewById(R.id.fab_cancelar);
        btnBuscarStock          = findViewById(R.id.btn_buscar_stock);
        btnCancelarBusqueda     = findViewById(R.id.btn_cancelar_busqueda);
        txtDatoStock            = findViewById(R.id.txt_dato_busqueda);
        swCodigo                = findViewById(R.id.sw_buscar_codigo);
        swNombre                = findViewById(R.id.sw_buscar_descripcion);

        recyclerView            = findViewById(R.id.rw_stock);



        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        );
        consultarSock(database);
        adapterStock = new AdapterStock(stocksList);
        recyclerView.setAdapter(adapterStock);


        txtDatoStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(txtDatoStock.getText().toString(),swCodigo.isChecked(),swNombre.isChecked());
            }
        });

        btnBuscarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(txtDatoStock.getText().toString(),swCodigo.isChecked(),swNombre.isChecked());
            }
        });

        btnCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDatoStock.setText("");
                swCodigo.setChecked(true);
                swNombre.setChecked(false);
                filter(txtDatoStock.getText().toString(),swCodigo.isChecked(),swNombre.isChecked());
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListaStock.this, Menu.class);
                startActivity(i);
                ListaStock.this.finish();
            }
        });
    }

    private void filter(String dato,boolean codigo,boolean nombre){
        ArrayList<Stock> filterStock = new ArrayList<Stock>();

        for(Stock s:stocksList){
            if(codigo) {
                if (String.valueOf(s.getArticulo().getId()).contains(dato)) {
                    filterStock.add(s);
                }
            }
            if(nombre) {
                if (s.getArticulo().getDescripcion().toUpperCase().contains(dato.toUpperCase())) {
                    filterStock.add(s);
                }
            }
        }
        adapterStock.filterList(filterStock);
    }

    private void consultarSock(SQLiteDatabase sqLiteDatabase) throws  SQLException {
        stocksList.clear();
        Stock stock;
        float cantidad = 0;
        StockDB stockDB = new StockDB(sqLiteDatabase);
        Cursor cursor = stockDB.consultarTodo(idDefaultBodega,0,0,0);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Bodega bodega = new Bodega(
                        cursor.getInt(cursor.getColumnIndex("id_bodega")),
                        cursor.getString(cursor.getColumnIndex("bodega"))
                );

                CentroCosto centroCosto = new CentroCosto();
                centroCosto.setIdCentroCosto(cursor.getInt(cursor.getColumnIndex("id_centro_costo")));

                TipoStock tipoStock = new TipoStock();
                tipoStock.setId(cursor.getInt(cursor.getColumnIndex("id_tipo_stock")));
                tipoStock.setDescripcion(cursor.getString(cursor.getColumnIndex("tipo_stock")));

                Articulo articulo = new Articulo();
                articulo.setId(cursor.getInt(cursor.getColumnIndex("id_articulo")));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex("articulo")));

                cantidad = cursor.getFloat(cursor.getColumnIndex("cantidad"));

                stocksList.add(new Stock(bodega,centroCosto,articulo,tipoStock,cantidad));
            }
        }
    }
}