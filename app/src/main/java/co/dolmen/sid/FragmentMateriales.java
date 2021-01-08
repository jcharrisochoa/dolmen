package co.dolmen.sid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.utilidades.AdapterData;
import co.dolmen.sid.utilidades.AdapterMovimientoArticulo;
import co.dolmen.sid.utilidades.DataSpinner;

public class FragmentMateriales extends Fragment {

    private Spinner sltTipoStock;
    private Spinner sltArticulo;
    private Spinner sltTipoMovimiento;
    private EditText txtCodigoArticulo;
    private EditText txtCantidad;
    private ImageButton btnAgregarArticulo;
    //--
    private View view;
    private ActividadOperativa actividadOperativa;
    private ArrayList<DataSpinner> tipoStockList;
    private ArrayList<DataSpinner> articuloList;
    private ArrayList<DataSpinner> tipoMovimientoList;
    private ArrayList<MovimientoArticulo> movimientoArticuloArrayList;
    private RecyclerView recyclerView;
    private AdapterMovimientoArticulo adapterMovimientoArticulo;
    //--
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;
    //--


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

        Bundle bundle = this.getArguments();
        actividadOperativa = (ActividadOperativa) bundle.getSerializable("actividadOperativa");

        movimientoArticuloArrayList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_materiales, container, false);
        sltTipoStock        = view.findViewById(R.id.slt_tipo_stock);
        sltArticulo         = view.findViewById(R.id.slt_articulo);
        sltTipoMovimiento   = view.findViewById(R.id.slt_movimiento);
        txtCodigoArticulo   = view.findViewById(R.id.txt_codigo_articulo);
        txtCantidad         = view.findViewById(R.id.txt_cantidad);
        btnAgregarArticulo  = view.findViewById(R.id.btn_agregar_articulo);
        recyclerView =       view.findViewById(R.id.rw_materiales);

        movimientoArticuloArrayList.add(
                new MovimientoArticulo(1, 1, 10,"Abrazadera","Stock","Salida")
        );
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false)
        );
        adapterMovimientoArticulo = new AdapterMovimientoArticulo(movimientoArticuloArrayList);
        recyclerView.setAdapter(adapterMovimientoArticulo);

        cargarTipoStock(database);
        cargarArticulo(database);
        cargarTipoMovimiento();
        return  view;
    }

    private void cargarArticulo(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        articuloList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ArticuloDB articuloDB = new ArticuloDB(sqLiteDatabase);
        Cursor cursor = articuloDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        articuloList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    articuloList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltArticulo.setAdapter(dataAdapter);
    }

    private void cargarTipoStock(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoStockList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoStockDB tipoStockDB = new TipoStockDB(sqLiteDatabase);
        Cursor cursor = tipoStockDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoStockList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoStockList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoStock.setAdapter(dataAdapter);
    }

    private  void cargarTipoMovimiento(){
        tipoMovimientoList = new ArrayList<DataSpinner>();
        DataSpinner dataSpinner;
        List<String> labels = new ArrayList<>();
        dataSpinner = new DataSpinner(0, getText(R.string.seleccione).toString());
        tipoStockList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());

        dataSpinner = new DataSpinner(1, getString(R.string.movimiento_salida));
        tipoMovimientoList.add(dataSpinner);
        labels.add(getString(R.string.movimiento_salida));

        dataSpinner = new DataSpinner(2, getString(R.string.movimiento_entrada));
        tipoMovimientoList.add(dataSpinner);
        labels.add(getString(R.string.movimiento_entrada));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoMovimiento.setAdapter(dataAdapter);
    }

}