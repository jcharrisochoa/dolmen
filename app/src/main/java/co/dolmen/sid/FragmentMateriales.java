package co.dolmen.sid;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Articulo;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.StockDB;
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
    private int idDefaultBodega;
    //--
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;
    //--
    AlertDialog.Builder alert;
    SharedPreferences config;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

        config = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
        idDefaultBodega = config.getInt("id_bodega", 0);

        Bundle bundle = this.getArguments();
        actividadOperativa = (ActividadOperativa) bundle.getSerializable("actividadOperativa");

        movimientoArticuloArrayList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_materiales, container, false);

        alert = new AlertDialog.Builder(view.getContext());
        alert.setCancelable(false);
        alert.setIcon(R.drawable.icon_problem);


        sltTipoStock        = view.findViewById(R.id.slt_tipo_stock);
        sltArticulo         = view.findViewById(R.id.slt_articulo);
        sltTipoMovimiento   = view.findViewById(R.id.slt_movimiento);
        txtCodigoArticulo   = view.findViewById(R.id.txt_codigo_articulo);
        txtCantidad         = view.findViewById(R.id.txt_cantidad);
        btnAgregarArticulo  = view.findViewById(R.id.btn_agregar_articulo);
        recyclerView =       view.findViewById(R.id.rw_materiales);


        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false)
        );
        adapterMovimientoArticulo = new AdapterMovimientoArticulo(movimientoArticuloArrayList);
        recyclerView.setAdapter(adapterMovimientoArticulo);

        adapterMovimientoArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("programacion","hola");
            }
        });

        sltTipoStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (tipoStockList.get(position).getId()) {
                    case 1:
                    case 5:
                        sltTipoMovimiento.setSelection(1);
                        sltTipoMovimiento.setEnabled(false);
                        break;
                    case 2:
                    case 4:
                        sltTipoMovimiento.setSelection(2);
                        sltTipoMovimiento.setEnabled(false);
                        break;
                    default:
                        sltTipoMovimiento.setSelection(0);
                        sltTipoMovimiento.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtCodigoArticulo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean sw = false;
                if(keyCode==13 || keyCode == 66){
                    int pos = 0;
                    for(DataSpinner articulo:articuloList) {
                        if(String.valueOf(articulo.getId()).contentEquals(txtCodigoArticulo.getText().toString())){
                            sltArticulo.setSelection(pos);
                            sw = true;
                            break;
                        }
                        pos++;
                    }
                    if(!sw){
                        Toast.makeText(view.getContext(),"Articulo No "+txtCodigoArticulo.getText()+" no existe",Toast.LENGTH_SHORT).show();
                        sltArticulo.setSelection(0);
                    }
                }
                return false;
            }
        });

        txtCodigoArticulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos = 0;
                boolean sw = false;
                for(DataSpinner articulo:articuloList) {
                    if(String.valueOf(articulo.getId()).contentEquals(txtCodigoArticulo.getText().toString())){
                        sltArticulo.setSelection(pos);
                        sw = true;
                        break;
                    }
                    pos++;
                }
                if(!sw){
                    sltArticulo.setSelection(0);
                }
            }
        });

        btnAgregarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarArticulo();
            }
        });

        cargarTipoStock(database);
        cargarArticulo(database);
        cargarTipoMovimiento();
        return  view;
    }

    private void agregarArticulo() {
        if(validarAgregarMaterial()){
            if(tipoMovimientoList.get(sltTipoMovimiento.getSelectedItemPosition()).getId()==1){ //valida solo cuando es movimiento de salida
                double stock = consultarStock();

                adapterMovimientoArticulo.addItem(
                        new MovimientoArticulo(
                                articuloList.get(sltArticulo.getSelectedItemPosition()).getId(),
                                tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getId(),
                                Float.parseFloat(txtCantidad.getText().toString()),
                                articuloList.get(sltArticulo.getSelectedItemPosition()).getDescripcion(),
                                tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getDescripcion(),
                                tipoMovimientoList.get(sltTipoMovimiento.getSelectedItemPosition()).getDescripcion()
                        )
                );
                //--Consultar cantidad agregada en el listado
                //stock - cantidad > la cantidad digitada ?
            }
            else{
                //for(MovimientoArticulo movimientoArticulo : )
                adapterMovimientoArticulo.addItem(
                        new MovimientoArticulo(
                                articuloList.get(sltArticulo.getSelectedItemPosition()).getId(),
                                tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getId(),
                                Float.parseFloat(txtCantidad.getText().toString()),
                                articuloList.get(sltArticulo.getSelectedItemPosition()).getDescripcion(),
                                tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getDescripcion(),
                                tipoMovimientoList.get(sltTipoMovimiento.getSelectedItemPosition()).getDescripcion()
                        )
                );
            }
            resetFrmArticulo();
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

    private double consultarStock() {  /// de donde saco el parametro de bodega?
        double cantidad = 0;
        StockDB stockDB = new StockDB(database);
        Cursor cursor = stockDB.consultarTodo(idDefaultBodega,
                articuloList.get(sltArticulo.getSelectedItemPosition()).getId(),
                tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getId(),
                actividadOperativa.getCentroCosto().getIdCentroCosto()
                );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cantidad = cursor.getDouble(cursor.getColumnIndex("cantidad"));
        }
        return  cantidad;
    }

    private boolean validarAgregarMaterial(){
        if(tipoStockList.get(sltTipoStock.getSelectedItemPosition()).getId() == 0){
            alert.setMessage(R.string.alert_tipo_stock);
            return false;
        }
        else{
            if(tipoMovimientoList.get(sltTipoMovimiento.getSelectedItemPosition()).getId() == 0){
                alert.setMessage(R.string.alert_tipo_movimiento);
                return false;
            }
            else {
                if(articuloList.get(sltArticulo.getSelectedItemPosition()).getId() == 0){
                    alert.setMessage(R.string.alert_articulo);
                    return false;
                }
                else {
                    if(txtCantidad.getText().toString().isEmpty()){
                        alert.setMessage(R.string.alert_cantidad_articulo);
                        return false;
                    }
                    else {
                        if(Double.parseDouble(txtCantidad.getText().toString()) == 0){
                            alert.setMessage(R.string.alert_cantidad);
                            return false;
                        }
                        else
                            return true;
                    }
                }
            }
        }
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
        tipoMovimientoList.add(dataSpinner);
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

    private void resetFrmArticulo(){
        sltTipoStock.setSelection(0);
        sltTipoMovimiento.setSelection(0);
        sltArticulo.setSelection(0);
        txtCantidad.setText("");
        txtCodigoArticulo.setText("");
    }

}