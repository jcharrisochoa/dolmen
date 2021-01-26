package co.dolmen.sid;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.utilidades.DataSpinner;

import static java.lang.Integer.parseInt;

public class FragmentInformacion extends Fragment {


    Spinner sltBarrio;
    Spinner sltTipoActividad;
    Spinner sltEstadoActividad;
    Spinner sltTipoInterseccionA;
    Spinner sltTipoInterseccionB;


    EditText editDireccion;
    EditText editObservacion;
    TextView txtMensajeDireccion;
    EditText txtNumeroInterseccion;
    EditText txtNumeracionA;
    EditText txtNumeracionB;

    ImageButton btnEditarDireccion;

    //-
    View view;
    ActividadOperativa actividadOperativa;
    AlertDialog.Builder alertDireccion;
    AlertDialog.Builder alert;
    //--
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    //--
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> tipoActividadList;
    ArrayList<DataSpinner> estadoActividadList;
    ArrayList<DataSpinner> tipoInterseccionA;
    ArrayList<DataSpinner> tipoInterseccionB;
    //--
    Switch swVandalismo;
    Switch swElementoNoEncontrado;
    //--
    char vandalismo = 'N';
    char elementoNoEncontrato = 'N';
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

        config = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);

        Bundle bundle = this.getArguments();
        actividadOperativa = (ActividadOperativa) bundle.getSerializable("actividadOperativa");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_informacion, container, false);

        alert = new AlertDialog.Builder(view.getContext());
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        swVandalismo        = view.findViewById(R.id.sw_afectado_vandalismo);
        swElementoNoEncontrado  = view.findViewById(R.id.sw_elemento_no_encontrado);

        sltBarrio           = view.findViewById(R.id.slt_barrio);
        sltTipoActividad    = view.findViewById(R.id.slt_tipo_actividad);
        sltEstadoActividad  = view.findViewById(R.id.slt_estado_actividad);
        editDireccion       = view.findViewById(R.id.txt_direccion);
        editObservacion     = view.findViewById(R.id.txt_observacion);
        btnEditarDireccion  = view.findViewById(R.id.btn_editar_direccion);

        //--
        editDireccion.setText(actividadOperativa.getDireccion());
        editDireccion.setEnabled(false);


        swVandalismo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                vandalismo = (isChecked) ? 'S' : 'N';
            }
        });

        swElementoNoEncontrado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                elementoNoEncontrato = (isChecked) ? 'S' : 'N';
            }
        });


        btnEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                armarDireccion();
            }
        });

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
                    if(actividadOperativa.getBarrio().getNombreBarrio().toUpperCase().contentEquals(cursor.getString(2).toUpperCase())){
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
    //--
    private void cargarTipoInterseccion(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoInterseccionA = new ArrayList<DataSpinner>();
        tipoInterseccionB = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoInterseccionDB tipoInterseccionDB = new TipoInterseccionDB(sqLiteDatabase);
        Cursor cursor = tipoInterseccionDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoInterseccionA.setAdapter(dataAdapter);
        sltTipoInterseccionB.setAdapter(dataAdapter);
    }
    //--
    private void armarDireccion() {
        alertDireccion = new AlertDialog.Builder(view.getContext());
        alertDireccion.setTitle(R.string.titulo_direccion);
        alertDireccion.setCancelable(false);

        View content = LayoutInflater.from(view.getContext()).inflate(R.layout.direccion, null);
        txtMensajeDireccion = content.findViewById(R.id.txt_mensaje_direccion);
        sltTipoInterseccionA = content.findViewById(R.id.slt_tipo_interseccion_a);
        sltTipoInterseccionB = content.findViewById(R.id.slt_tipo_interseccion_b);

        txtNumeroInterseccion = content.findViewById(R.id.numero_interseccion);
        txtNumeracionA = content.findViewById(R.id.txt_numeracion_a);
        txtNumeracionB = content.findViewById(R.id.txt_numeracion_b);
        cargarTipoInterseccion(database);

        alertDireccion.setView(content);
        alertDireccion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (tipoInterseccionA.get(sltTipoInterseccionA.getSelectedItemPosition()).getId() == 0) {
                    //txtMensajeDireccion.setText("Seleccione Tipo Intersección");
                    //Log.d("Busqueda", "Seleccione Tipo Intersección");
                    Toast.makeText(view.getContext(),"No selecciono un Tipo de Interseccion",Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(txtNumeroInterseccion.getText().toString())) {
                        //txtMensajeDireccion.setText("Digite el Número de la Intersección");
                        Toast.makeText(view.getContext(),"No digitó el número de la intersección",Toast.LENGTH_LONG).show();
                    } else {
                        String miDireccion = "";

                        miDireccion = miDireccion + tipoInterseccionA.get(sltTipoInterseccionA.getSelectedItemPosition()).getDescripcion();
                        miDireccion = miDireccion + " " + txtNumeroInterseccion.getText().toString();

                        if (tipoInterseccionA.get(sltTipoInterseccionB.getSelectedItemPosition()).getId() != 0) {
                            miDireccion = miDireccion + " " + tipoInterseccionB.get(sltTipoInterseccionB.getSelectedItemPosition()).getDescripcion();
                        }
                        if (!TextUtils.isEmpty(txtNumeracionA.getText().toString())) {
                            if (tipoInterseccionA.get(sltTipoInterseccionB.getSelectedItemPosition()).getId() != 0) {
                                miDireccion = miDireccion + " " + txtNumeracionA.getText().toString();
                            } else {
                                miDireccion = miDireccion + " N " + txtNumeracionA.getText().toString();
                            }
                        }
                        if (!TextUtils.isEmpty(txtNumeracionB.getText().toString())) {
                            miDireccion = miDireccion + " - " + txtNumeracionB.getText().toString();
                        }
                        if (!miDireccion.isEmpty())
                            editDireccion.setText(miDireccion);
                    }
                }
            }
        });
        alertDireccion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDireccion.create().show();

    }

}