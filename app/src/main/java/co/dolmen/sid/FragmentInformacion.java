package co.dolmen.sid;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.utilidades.DataSpinner;

public class FragmentInformacion extends Fragment {


    Spinner sltBarrio;
    Spinner sltTipoActividad;
    Spinner sltEstadoActividad;

    TextView txtMobiliario;
    TextView txtReferencia;

    EditText editMobiliarioNo;
    EditText editDireccion;
    //-
    View view;
    ActividadOperativa actividadOperativa;
    //--
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    //--
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> tipoActividadList;
    ArrayList<DataSpinner> estadoActividadList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

        Bundle bundle = this.getArguments();
        actividadOperativa = (ActividadOperativa) bundle.getSerializable("actividadOperativa");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_informacion, container, false);
        sltBarrio           = view.findViewById(R.id.slt_barrio);
        sltTipoActividad    = view.findViewById(R.id.slt_tipo_actividad);
        sltEstadoActividad  = view.findViewById(R.id.slt_estado_actividad);

        txtMobiliario       = view.findViewById(R.id.txt_mobiliario);
        txtReferencia       = view.findViewById(R.id.txt_referencia);

        editMobiliarioNo    = view.findViewById(R.id.txt_mobiliario_no);
        editDireccion       = view.findViewById(R.id.txt_direccion);
        //--
        txtMobiliario.setText(actividadOperativa.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(actividadOperativa.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());
        editMobiliarioNo.setText(String.valueOf(actividadOperativa.getElemento().getElemento_no()));
        editDireccion.setText(actividadOperativa.getDireccion());

        cargarBarrio(database);
        cargarTipoActividad(database);
        cargarEstadoActividad(database);

        return view;
    }

    private void cargarEstadoActividad(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        estadoActividadList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        EstadoActividadDB estadoActividadDB = new EstadoActividadDB(sqLiteDatabase);
        Cursor cursor = estadoActividadDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        estadoActividadList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    estadoActividadList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                    if(actividadOperativa.getEstadoActividad().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltEstadoActividad.setAdapter(dataAdapter);
        sltEstadoActividad.setSelection(pos);
    }
    //--
    private void cargarTipoActividad(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoActividadList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoActividadDB tipoActividadDB = new TipoActividadDB(sqLiteDatabase);
        Cursor cursor = tipoActividadDB.consultarTodo(actividadOperativa.getProcesoSgc().getId());
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoActividadList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipoActividadList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                    if(actividadOperativa.getTipoActividad().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoActividad.setAdapter(dataAdapter);
        sltTipoActividad.setSelection(pos);
    }
    //--
    private void cargarBarrio(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        barrioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        BarrioDB barrioDB = new BarrioDB(sqLiteDatabase);
        Cursor cursor = barrioDB.consultarTodo(actividadOperativa.getBarrio().getId());
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        barrioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    barrioList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                    if(actividadOperativa.getBarrio().getNombreBarrio().toUpperCase() == cursor.getString(2).toUpperCase()){
                        pos = i;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltBarrio.setAdapter(dataAdapter);
        sltBarrio.setSelection(pos);
    }
}