package co.dolmen.sid;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.utilidades.DataSpinner;

import static java.lang.Integer.parseInt;

public class FragmentElemento extends Fragment {

    ImageButton btnBuscarElemento;
    ImageButton btnLimpiarBusquedaElemento;

    TextView txtMobiliario;
    TextView txtReferencia;
    TextView txtMobiliarioNo;

    EditText editMobiliarioNo;

    Spinner sltEstadoMobiliario;
    Spinner sltClaseVia;
    Spinner sltTipoPoste;
    Spinner sltNormaConstruccionPoste;
    Spinner sltTipoRed;
    Spinner sltTipoEscenario;
    Spinner sltCalibreConexionElemento;
    Spinner sltTipoBrazo;

    View view;
    ActividadOperativa actividadOperativa;
    AlertDialog.Builder alertBuscarElemento;
    AlertDialog.Builder alert;
    //--
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;

    ArrayList<DataSpinner> estadoMobiliarioList;
    ArrayList<DataSpinner> claseViaList;
    ArrayList<DataSpinner> tipoPosteList;
    ArrayList<DataSpinner> tipoRedList;
    ArrayList<DataSpinner> normaConstruccionPosteList;
    ArrayList<DataSpinner> tipoEscenarioList;
    ArrayList<DataSpinner> calibreList;
    ArrayList<DataSpinner> tipoBrazoList;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;


    public FragmentElemento() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_elemento,container,false);

        alert = new AlertDialog.Builder(view.getContext());
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        btnBuscarElemento   = view.findViewById(R.id.btn_buscar_elemento);
        btnLimpiarBusquedaElemento  = view.findViewById(R.id.btn_limpiar_busqueda_elemento);

        txtMobiliario       = view.findViewById(R.id.txt_mobiliario);
        txtReferencia       = view.findViewById(R.id.txt_referencia);
        txtMobiliarioNo     = view.findViewById(R.id.txt_mobiliario_numero);

        editMobiliarioNo    = view.findViewById(R.id.txt_mobiliario_no);

        txtMobiliarioNo.setText(actividadOperativa.getElemento().getElemento_no());
        txtMobiliario.setText(actividadOperativa.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(actividadOperativa.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());

        editMobiliarioNo.setText(String.valueOf(actividadOperativa.getElemento().getElemento_no()));


        sltEstadoMobiliario         = view.findViewById(R.id.slt_estado_mobiliario);
        sltClaseVia                 = view.findViewById(R.id.slt_clase_via);
        sltTipoPoste                = view.findViewById(R.id.slt_tipo_poste);
        sltNormaConstruccionPoste   = view.findViewById(R.id.slt_norma_construccion_poste);
        sltTipoRed                  = view.findViewById(R.id.slt_tipo_red);
        sltTipoEscenario            = view.findViewById(R.id.slt_tipo_escenario);
        sltCalibreConexionElemento  = view.findViewById(R.id.slt_calibre_conexion_elemento);
        sltTipoBrazo                = view.findViewById(R.id.slt_tipo_brazo);

        sltTipoPoste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionPoste(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnBuscarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarElemento(database);
            }
        });

        btnLimpiarBusquedaElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFrmBuscarElemento();
            }
        });

        cargarEstadoMobiliario(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTipoEscenario(database);
        cargarCalibre(database);
        cargarNormaConstruccionPoste(database);
        cargarTipoBrazo(database);
        /*cargarTipoBalasto(database);
        cargarTipoBaseFotocelda(database);
        cargarTipoInstalacionRed(database);
        cargarControlEncendido(database);*/
        return view;
    }



    //--
    private void buscarElemento(SQLiteDatabase sqLiteDatabase) {
        if (editMobiliarioNo.getText().toString().trim().length() == 0) {
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_elemento_buscar);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        } else {
            alertBuscarElemento = new AlertDialog.Builder(view.getContext());
            alertBuscarElemento.setCancelable(false);
            alertBuscarElemento.setIcon(android.R.drawable.ic_dialog_alert);
            try {
                ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
                Cursor cursorElemento = elementoDB.consultarElemento(idDefaultMunicipio, idDefaultProceso, parseInt(editMobiliarioNo.getText().toString()));
                if (cursorElemento.getCount() == 0) {
                    alertBuscarElemento.setTitle(R.string.titulo_alerta);
                    alertBuscarElemento.setMessage(getText(R.string.alert_elemento_no_encontrado) + " sobre el Elemento: " + editMobiliarioNo.getText());
                    alertBuscarElemento.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetFrmBuscarElemento();
                        }
                    });
                    alertBuscarElemento.create().show();

                } else {
                    if (cursorElemento.getCount() > 1) {
                        alertBuscarElemento.setMessage("Existe mas de un elemento con el mismo número,Seleccione");
                        alertBuscarElemento.setNeutralButton("Aceptar", null);
                        alertBuscarElemento.create().show();
                    } else {
                        cursorElemento.moveToFirst();

                        Elemento elemento = new Elemento();
                        elemento.setId(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("_id"))));
                        elemento.setElemento_no(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));

                        Tipologia tipologia = new Tipologia();
                        tipologia.setIdTipologia(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("id_tipologia"))));
                        tipologia.setDescripcionTipologia(cursorElemento.getString(cursorElemento.getColumnIndex("tipologia")));
                        elemento.setTipologia(tipologia);

                        Mobiliario mobiliario = new Mobiliario();
                        mobiliario.setIdMobiliario(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("id_mobiliario"))));
                        mobiliario.setDescripcionMobiliario(cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario")));
                        elemento.setMobiliario(mobiliario);

                       /* EstadoMobiliario estadoMobiliario = new EstadoMobiliario();
                        estadoMobiliario.setIdEstadoMobiliario(cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario")));
                        estadoMobiliario.setDescripcionEstadoMobiliario(cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario")));
                        elemento.setEstadoMobiliario(estadoMobiliario);*/

                        actividadOperativa.setElemento(elemento);

                        txtMobiliarioNo.setText(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));
                        //editDireccion.setText(cursorElemento.getString(cursorElemento.getColumnIndex("direccion")));
                        txtMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario")));
                        txtReferencia.setText(cursorElemento.getString(cursorElemento.getColumnIndex("referencia")));
                        //txtEstadoMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario")));

                    }

                }
                cursorElemento.close();
            }catch (SQLException e){
                Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //--
    private void resetFrmBuscarElemento() {
        editMobiliarioNo.setText("");
        txtMobiliarioNo.setText("");
        txtMobiliario.setText("");
        txtReferencia.setText("");
        actividadOperativa.setElemento(new Elemento());
    }
    //--
    private void cargarEstadoMobiliario(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        estadoMobiliarioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        EstadoMobiliarioDB estadoMobiliarioDB = new EstadoMobiliarioDB(sqLiteDatabase);
        Cursor cursor = estadoMobiliarioDB.consultarTodo(idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        estadoMobiliarioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    if (    cursor.getInt(0) == 10 ||
                            cursor.getInt(0) == 11 ||
                            cursor.getInt(0) == 13 ||
                            cursor.getInt(0) == 15){
                        i++;
                        dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                        estadoMobiliarioList.add(dataSpinner);
                        labels.add(cursor.getString(2).toUpperCase());
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltEstadoMobiliario.setAdapter(dataAdapter);
    }
    //--
    private void cargarClaseVia(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        claseViaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ClaseViaDB claseViaDB = new ClaseViaDB(sqLiteDatabase);
        Cursor cursor = claseViaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        claseViaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    claseViaList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltClaseVia.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoPoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoPosteDB tipoPosteDB = new TipoPosteDB(sqLiteDatabase);
        Cursor cursor = tipoPosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoPoste.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoRed(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoRedDB tipoRedDB = new TipoRedDB(sqLiteDatabase);
        Cursor cursor = tipoRedDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRed.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoEscenario(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoEscenarioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoEscenarioDB tipoEscenarioDB = new TipoEscenarioDB(sqLiteDatabase);
        Cursor cursor = tipoEscenarioDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoEscenarioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoEscenarioList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoEscenario.setAdapter(dataAdapter);
    }
    //--
    private void cargarCalibre(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        calibreList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        CalibreDB calibreDB = new CalibreDB(sqLiteDatabase);
        Cursor cursor = calibreDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        calibreList.add(dataSpinner);

        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    calibreList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltCalibreConexionElemento.setAdapter(dataAdapter);
        sltCalibreConexionElemento.setAdapter(dataAdapter);
    }
    //--
    private void cargarNormaConstruccionPoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int idTipoPoste = tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId();
        normaConstruccionPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionPosteDB normaConstruccionPosteDB = new NormaConstruccionPosteDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionPosteDB.consultarTodo(idTipoPoste);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltNormaConstruccionPoste.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoBrazo(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoBrazoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoBrazoDB tipoBrazoDB = new TipoBrazoDB(sqLiteDatabase);
        Cursor cursor = tipoBrazoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoBrazoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoBrazoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoBrazo.setAdapter(dataAdapter);
    }
}