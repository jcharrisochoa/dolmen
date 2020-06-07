package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Censo;
import co.dolmen.sid.entidad.CensoTipoArmado;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.Contrato;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.NormaConstruccionRed;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.RetenidaPoste;
import co.dolmen.sid.entidad.TipoEscenario;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CensoCarga extends AppCompatActivity {
    JSONObject json;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String nombreMunicipio;
    AlertDialog.Builder alert;
    AlertDialog.Builder alertDireccion;
    AlertDialog.Builder alertBuscarElemento;
    AlertDialog.Builder alertSincronizar;
    AlertDialog.Builder alertLogs;

    Spinner sltTipologia;
    Spinner sltMobiliario;
    Spinner sltReferencia;
    Spinner sltEstadoMobiliario;
    Spinner sltBarrio;
    Spinner sltTipoInterseccionA;
    Spinner sltTipoInterseccionB;
    Spinner sltCensoAsignado;

    EditText txtElementoNo;
    EditText txtLatitud;
    EditText txtLongitud;
    EditText txtBuscarElemento;
    EditText txtDireccion;
    EditText txtNumeroInterseccion;
    EditText txtNumeracionA;
    EditText txtNumeracionB;
    EditText txtObservacion;
    EditText txtLog;

    Switch swLuminariaVisible;
    Switch swPoseeLuminaria;
    Switch swMobiliarioBuenEstado;

    Button btnGuardar;
    Button btnCancelar;

    ImageButton btnCapturarGPS;
    ImageButton btnBuscarElemento;
    ImageButton btnEditarDireccion;
    ImageButton btnLimpiar;
    ImageButton btnSincronizar;

    ArrayList<DataSpinner> tipologiaList;
    ArrayList<Mobiliario> mobiliarioList;
    ArrayList<ReferenciaMobiliario> referenciaMobiliarioList;
    ArrayList<DataSpinner> estadoMobiliarioList;
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> tipoInterseccionA;
    ArrayList<DataSpinner> tipoInterseccionB;
    ArrayList<DataSpinner> censoAsignadoList;

    TextView viewLatitud;
    TextView viewLongitud;
    TextView viewAltitud;
    TextView viewPrecision;
    TextView viewDireccion;
    TextView viewVelocidad;


    RadioButton rdZonaUrbano;
    RadioButton rdZonaRural;
    RadioButton rdSectorNormal;
    RadioButton rdSectorSubNormal;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;
    private int idCenso;

    private boolean gpsListener;
    private ProgressBar progressBarGuardarCenso;

    Elemento elemento;
    private CensoDB censoDB;

    private String chkSwLuminariaVisible = "S";
    private String chkSwPoseeLuminaria = "S";
    private String zona ="U";
    private String sector = "N";
    private String chkSwMobiliarioBuenEstado = "S";

    public LocationManager ubicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo_carga);

        conn = new BaseDatos(getApplicationContext());
        database = conn.getReadableDatabase();

        try {
            censoDB = new CensoDB(database);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new CensoCarga.miLocalizacion());
            gpsListener  = true;
        }

        //--
        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setIcon(android.R.drawable.ic_dialog_alert);

        alertSincronizar = new AlertDialog.Builder(this);
        alertSincronizar.setCancelable(false);
        alertSincronizar.setIcon(android.R.drawable.ic_dialog_info);



        //--
        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        idCenso = config.getInt("id_censo", 0);
        //--
        setTitle(getText(R.string.titulo_censo_carga) + " (" + nombreMunicipio + ")");
        //--
        sltTipologia = findViewById(R.id.slt_tipologia);
        sltMobiliario = findViewById(R.id.slt_mobiliario);
        sltReferencia = findViewById(R.id.slt_referencia);
        sltEstadoMobiliario = findViewById(R.id.slt_estado_mobiliario);
        sltBarrio = findViewById(R.id.slt_barrio);
        sltCensoAsignado = findViewById(R.id.slt_censo_asignado);
        //--
        txtElementoNo = findViewById(R.id.txt_elemento_no);
        txtLatitud = findViewById(R.id.txt_latitud);
        txtLongitud = findViewById(R.id.txt_longitud);
        txtBuscarElemento = findViewById(R.id.txt_buscar_elemento);
        txtDireccion = findViewById(R.id.txt_direccion);
        txtObservacion = findViewById(R.id.txt_observacion);
        //--
        swLuminariaVisible = findViewById(R.id.sw_numero_luminaria_visible);
        swPoseeLuminaria = findViewById(R.id.sw_tiene_luminaria);
        swMobiliarioBuenEstado = findViewById(R.id.sw_mobiliario_en_buenas_condiciones);
        //--
        rdZonaUrbano        = findViewById(R.id.rd_urbano);
        rdZonaRural         = findViewById(R.id.rd_rutal);
        rdSectorNormal      = findViewById(R.id.rd_normal);
        rdSectorSubNormal   = findViewById(R.id.rd_subnormal);
        //--
        btnCapturarGPS = findViewById(R.id.btn_capturar_gps);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnEditarDireccion = findViewById(R.id.btn_editar_direccion);
        btnLimpiar = findViewById(R.id.btn_limpiar);
        btnBuscarElemento = findViewById(R.id.btn_buscar_elemento);
        btnSincronizar = findViewById(R.id.btn_sincronizar_carga);

        //--
        viewLatitud = findViewById(R.id.gps_latitud);
        viewLongitud = findViewById(R.id.gps_longitud);
        viewAltitud  = findViewById(R.id.gps_altitud);
        viewPrecision = findViewById(R.id.gps_precision);
        viewDireccion = findViewById(R.id.gps_direccion);
        viewVelocidad  = findViewById(R.id.gps_velocidad);
        //--
        progressBarGuardarCenso = findViewById(R.id.progressBarGuardarCenso);
        //--
        txtElementoNo.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);
        progressBarGuardarCenso.setVisibility(View.INVISIBLE);

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFrm(true);
            }
        });

        swLuminariaVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwLuminariaVisible = (isChecked) ? "S" : "N";
            }
        });
        swPoseeLuminaria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPoseeLuminaria = (isChecked) ? "S" : "N";
                estadoPropiedadMobiliario(isChecked);
            }
        });
        swMobiliarioBuenEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwMobiliarioBuenEstado = (isChecked)?"S":"N";
            }
        });

        rdZonaUrbano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zona = "U";
            }
        });
        rdZonaRural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zona = "R";
            }
        });
        rdSectorNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sector = "N";
            }
        });
        rdSectorSubNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sector = "S";
            }
        });
        btnCapturarGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activarCoordenadas();
                if(!estadoGPS()){
                    alert.setTitle(getString(R.string.titulo_alerta));
                    alert.setMessage(getString(R.string.alert_gps_deshabilitado));
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();
                }
                else {
                    txtLatitud.setText(viewLatitud.getText().toString().trim());
                    txtLongitud.setText(viewLongitud.getText().toString().trim());
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.close();
                Intent i = new Intent(getApplicationContext(), Menu.class);
                startActivity(i);
                CensoCarga.this.finish();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarFormulario()) {
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                        guardarFormulario('R', database);
                    } else {
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                guardarFormulario('L', database);
                            }
                        });
                        alert.create().show();
                    }
                } else {
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
        btnBuscarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarElemento(database);
            }
        });

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sincronizar();
            }
        });



        btnEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                armarDireccion();
            }
        });
        //--
        sltTipologia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarMobiliario(database);
                //Log.d("Mobiliario", "selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
        sltMobiliario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarReferencia(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cargarTipologia(database);
        cargarBarrio(database);
        cargarEstadoMobiliario(database);
        cargarCensoAsignado(database);
    }
    //--Cargar Parametrizacion--
    private void cargarTipologia(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipologiaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipologiaDB tipologiaDB = new TipologiaDB(sqLiteDatabase);
        Cursor cursor = tipologiaDB.consultarTodo(idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipologia.setAdapter(dataAdapter);
    }

    //--
    private void cargarMobiliario(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        //Localizo el id seleccionado
        int idTipologia = tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId();

        //---Crear la lista
        mobiliarioList = new ArrayList<Mobiliario>(); //La lista de consulta de tipo Mobiliario
        List<String> labels = new ArrayList<>(); //La lista de label a mostar en el spinner

        //Realizo la consulta a la base datos
        MobiliarioDB mobiliarioDB = new MobiliarioDB(sqLiteDatabase);
        Cursor cursor = mobiliarioDB.consultarTodo(idTipologia);
        //--Agregar a la lista y label la opcion de -Seleccione--
        Mobiliario mobiliario = new Mobiliario(i, getText(R.string.seleccione).toString());
        mobiliarioList.add(mobiliario);

        labels.add(getText(R.string.seleccione).toString());
        //--Recorrer el cursor de la consulta e ir creando objeto de tipo mobiliario para agregar
        //a la lista mobiliarioList y a la Lista label que es la que se muestra en el spinner
        //Log.d("Mobiliario","idTipologia:"+idTipologia);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    mobiliario = new Mobiliario(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    mobiliarioList.add(mobiliario);
                    labels.add(cursor.getString(2).toUpperCase());
                    if (idMobiliarioBusqueda == cursor.getInt(0)) {
                        pos = i;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltMobiliario.setAdapter(dataAdapter);
        sltMobiliario.setSelection(pos);
    }

    //--
    private void cargarReferencia(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        referenciaMobiliarioList = new ArrayList<ReferenciaMobiliario>();
        List<String> labels = new ArrayList<String>();
        int idMobiliario = mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario();

        ReferenciaMobiliario referenciaMobiliario = new ReferenciaMobiliario(i, getText(R.string.seleccione).toString());
        referenciaMobiliarioList.add(referenciaMobiliario);
        labels.add(getText(R.string.seleccione).toString());

        ReferenciaMobiliarioDB referenciaMobiliarioDB = new ReferenciaMobiliarioDB(sqLiteDatabase);
        Cursor cursor = referenciaMobiliarioDB.consultarTodo(idMobiliario);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    referenciaMobiliario = new ReferenciaMobiliario(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    referenciaMobiliarioList.add(referenciaMobiliario);
                    labels.add(cursor.getString(2).toUpperCase());
                    if (idReferenciaBusqueda == cursor.getInt(0)) {
                        pos = i;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltReferencia.setAdapter(dataAdapter);
        sltReferencia.setSelection(pos);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltEstadoMobiliario.setAdapter(dataAdapter);
    }

    //--
    private void cargarBarrio(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        barrioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        BarrioDB barrioDB = new BarrioDB(sqLiteDatabase);
        Cursor cursor = barrioDB.consultarTodo(idDefaultMunicipio);
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
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoInterseccionA.setAdapter(dataAdapter);
        sltTipoInterseccionB.setAdapter(dataAdapter);
    }

    //--
    private void cargarCensoAsignado(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        censoAsignadoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        CensoAsignadoDB censoAsignadoDB = new CensoAsignadoDB(sqLiteDatabase);
        Cursor cursor = censoAsignadoDB.consultarTodo(idDefaultMunicipio, idDefaultProceso,"C");
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        censoAsignadoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), String.valueOf(cursor.getInt(0)));
                    censoAsignadoList.add(dataSpinner);
                    labels.add(cursor.getString(0).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltCensoAsignado.setAdapter(dataAdapter);
    }

    //--
    private void estadoPropiedadMobiliario(boolean estado){

        sltTipologia.setSelection(0);
        sltEstadoMobiliario.setSelection(0);

        sltTipologia.setEnabled(estado);
        sltMobiliario.setEnabled(estado);
        sltReferencia.setEnabled(estado);
        sltEstadoMobiliario.setEnabled(estado);
        swLuminariaVisible.setChecked(estado);
        swLuminariaVisible.setEnabled(estado);

    }

    //--
    private void buscarElemento(SQLiteDatabase sqLiteDatabase) {
        if (txtBuscarElemento.getText().toString().trim().length() == 0) {
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
            alertBuscarElemento = new AlertDialog.Builder(this);
            alertBuscarElemento.setCancelable(false);
            alertBuscarElemento.setIcon(android.R.drawable.ic_dialog_alert);
            ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
            Cursor cursorElemento = elementoDB.consultarElemento(idDefaultMunicipio, idDefaultProceso, parseInt(txtBuscarElemento.getText().toString()));
            if (cursorElemento.getCount() == 0) {
                alertBuscarElemento.setTitle(R.string.titulo_alerta);
                alertBuscarElemento.setMessage(getText(R.string.alert_elemento_no_encontrado) + " sobre el Elemento: " + txtBuscarElemento.getText() + ". ¿Desea registrar el Elemento?");
                alertBuscarElemento.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtElementoNo.setText(txtBuscarElemento.getText());
                        elemento = new Elemento();
                        elemento.setId(0);
                        elemento.setElemento_no(txtBuscarElemento.getText().toString());
                        resetFrm(false);
                    }
                });
                alertBuscarElemento.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetFrm(true);
                    }
                });
                alertBuscarElemento.create().show();

            } else {
                if(cursorElemento.getCount()>1){
                    alertBuscarElemento.setMessage("Existe mas de un elemento con el mismo número,Seleccione");
                    alertBuscarElemento.setNeutralButton("Aceptar",null);
                    alertBuscarElemento.create().show();
                }else {
                    cursorElemento.moveToFirst();

                    elemento = new Elemento();
                    elemento.setId(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("_id"))));
                    elemento.setElemento_no(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));

                    EstadoMobiliario estadoMobiliario = new EstadoMobiliario();
                    estadoMobiliario.setIdEstadoMobiliario(cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario")));

                    elemento.setEstadoMobiliario(estadoMobiliario);

                    swLuminariaVisible.setEnabled(false);
                    swLuminariaVisible.setChecked(true);
                    swPoseeLuminaria.setEnabled(false);
                    swPoseeLuminaria.setChecked(true);

                    txtElementoNo.setText(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));
                    txtDireccion.setText(cursorElemento.getString(cursorElemento.getColumnIndex("direccion")));

                    idMobiliarioBusqueda = cursorElemento.getInt(cursorElemento.getColumnIndex("id_mobiliario"));
                    idReferenciaBusqueda = cursorElemento.getInt(cursorElemento.getColumnIndex("id_referencia"));

                    //tipologiaList
                    for (int i = 0; i < tipologiaList.size(); i++) {
                        if (tipologiaList.get(i).getId() == cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipologia"))) {
                            sltTipologia.setAdapter(sltTipologia.getAdapter());
                            sltTipologia.setSelection(i);
                        }
                    }
                    // this.cargarMobiliario(database);
                    // this.cargarReferencia(database);
                    //EstadoList
                    for (int i = 0; i < estadoMobiliarioList.size(); i++) {
                        if (estadoMobiliarioList.get(i).getId() == cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario"))) {
                            sltEstadoMobiliario.setSelection(i);
                        }
                    }
                    //barrioList
                    for (int i = 0; i < barrioList.size(); i++) {
                        if (barrioList.get(i).getId() == cursorElemento.getInt(cursorElemento.getColumnIndex("id_barrio"))) {
                            sltBarrio.setSelection(i);
                        }
                    }
                }

            }
            cursorElemento.close();
        }
    }

    //--Validar Formulario para enviar--
    private boolean validarFormulario() {
        if (censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId() == 0) {
            alert.setMessage(R.string.alert_censo_asignado);
            return false;
        } else {
            if (txtElementoNo.getText().toString().isEmpty() && swLuminariaVisible.isChecked()) {
                alert.setMessage(R.string.alert_censo_tecnico_elemento_no);
                return false;
            } else {
                if (tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId() == 0 && swPoseeLuminaria.isChecked()) {
                    alert.setMessage(R.string.alert_censo_tipologia);
                    return false;
                } else {
                    if (mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario() == 0 && swPoseeLuminaria.isChecked()) {
                        alert.setMessage(R.string.alert_censo_mobiliario);
                        return false;
                    } else {
                        if (referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario() == 0 && swPoseeLuminaria.isChecked()) {
                            alert.setMessage(R.string.alert_censo_referencia);
                            return false;
                        } else {
                            if (estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId() == 0 && swPoseeLuminaria.isChecked()) {
                                alert.setMessage(R.string.alert_censo_estado_mobiliario);
                                return false;
                            } else {
                                if (barrioList.get(sltBarrio.getSelectedItemPosition()).getId() == 0) {
                                    alert.setMessage(R.string.alert_censo_barrio);
                                    return false;
                                } else {
                                    if (txtDireccion.getText().toString().isEmpty()) {
                                        alert.setMessage(R.string.alert_censo_direccion);
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //--
    private void resetFrm(boolean enabled) {
        idMobiliarioBusqueda = 0;
        idReferenciaBusqueda = 0;
        swLuminariaVisible.setEnabled(enabled);
        swPoseeLuminaria.setEnabled(enabled);

        if (enabled) {
            txtElementoNo.setText("");
        }

        sltTipologia.setSelection(0);
        sltEstadoMobiliario.setSelection(0);
        sltBarrio.setSelection(0);

        txtDireccion.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtObservacion.setText("");

        swLuminariaVisible.setChecked(true);
        swPoseeLuminaria.setChecked(true);
        swMobiliarioBuenEstado.setChecked(true);

        txtBuscarElemento.setFocusable(true);
        txtBuscarElemento.setText("");
        rdSectorNormal.setChecked(true);
        rdSectorSubNormal.setChecked(false);
        sector="N";
        rdZonaRural.setChecked(false);
        rdZonaUrbano.setChecked(true);
        zona="U";

        chkSwLuminariaVisible = "S";
        chkSwPoseeLuminaria = "S";
        chkSwMobiliarioBuenEstado = "S";
    }

    //--
    private void guardarFormulario(char tipoAlmacenamiento, SQLiteDatabase sqLiteDatabase) {
        switch (tipoAlmacenamiento) {
            case 'L':
                almacenarDatosLocal(sqLiteDatabase);
                break;
            case 'R':
                almacenarDatosEnRemoto();
                break;
        }
    }

    //--
    private void almacenarDatosLocal(SQLiteDatabase sqLiteDatabase) {
        setButton(false);
        Barrio barrio = new Barrio();
        barrio.setIdBarrio(barrioList.get(sltBarrio.getSelectedItemPosition()).getId());
        barrio.setNombreBarrio(barrioList.get(sltBarrio.getSelectedItemPosition()).getDescripcion());

        //--Datos del Municipio
        barrio.setId(idDefaultMunicipio);
        barrio.setDescripcion(nombreMunicipio);

        Tipologia tipologia = new ReferenciaMobiliario();
        tipologia.setIdTipologia(tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId());
        tipologia.setDescripcionTipologia(tipologiaList.get(sltTipologia.getSelectedItemPosition()).getDescripcion());
        //--Datos del Proceso
        tipologia.setId(idDefaultProceso);

        Mobiliario mobiliario = new ReferenciaMobiliario();
        mobiliario.setIdMobiliario(mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario());
        mobiliario.setDescripcionMobiliario(mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getDescripcionMobiliario());

        ReferenciaMobiliario referenciaMobiliario = new ReferenciaMobiliario();
        referenciaMobiliario.setIdReferenciaMobiliario(referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario());
        referenciaMobiliario.setDescripcionReferenciaMobiliario(referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getDescripcionReferenciaMobiliario());

        EstadoMobiliario estadoMobiliario = new EstadoMobiliario();
        estadoMobiliario.setIdEstadoMobiliario(estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId());
        estadoMobiliario.setDescripcionEstadoMobiliario(estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getDescripcion());


        if (txtElementoNo.getText().toString().isEmpty() && !swLuminariaVisible.isChecked()) {
            elemento = new Elemento();
            elemento.setId(0);
            elemento.setElemento_no("");
        }
        elemento.setDireccion(txtDireccion.getText().toString());
        elemento.setBarrio(barrio);
        elemento.setTipologia(tipologia);
        elemento.setMobiliario(mobiliario);
        elemento.setReferenciaMobiliario(referenciaMobiliario);

        Censo censo = new Censo();
        censo.setId_censo(censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId());
        censo.setElemento(elemento);
        censo.setEstadoMobiliario(estadoMobiliario);

        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        censo.setFchRegistro(fechaHora);

        Float latitud = (txtLatitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(txtLatitud.getText().toString());
        Float longitud = (txtLongitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(txtLongitud.getText().toString());
        censo.setLatitud(latitud);
        censo.setLongitud(longitud);
        censo.setChkSwLuminariaVisible(chkSwLuminariaVisible);
        censo.setChkSwPoseeLuminaria(chkSwPoseeLuminaria);
        censo.setSector(sector);
        censo.setZona(zona);
        censo.setChkSwMobiliarioBuenEstado(chkSwMobiliarioBuenEstado);
        censo.setObservacion(txtObservacion.getText().toString());

        CensoDB censoDB = new CensoDB(sqLiteDatabase);
        if (censoDB.agregarDatosCensoCarga(censo)) {

            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_almacenamiento_local);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    resetFrm(false);
                    setButton(true);
                }
            });
            alert.create().show();
        } else {
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_error_almacenando_datos);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    setButton(true);
                }
            });
            alert.create().show();
        }
        //setButton(true);
    }

    //--
    private void sincronizar(){
        final String tag = "Log:\n";
        if (censoDB.consultarTodo().getCount() > 0) {
            alertSincronizar.setTitle(R.string.titulo_alerta);
            alertSincronizar.setMessage("Se va(n) a sincronizar  "+censoDB.consultarTodo().getCount()+" elemento(s).");

            alertSincronizar.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setButton(false);
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(), "Conectando con " + networkInfo.getTypeName() + " / " + networkInfo.getExtraInfo(), Toast.LENGTH_LONG).show();
                        JSONObject principal = new JSONObject();
                        try{
                            principal.put("json",armarJson(censoDB.consultarTodo()));
                            // Log.d("JSON",principal.toString());
                            final AsyncHttpClient client = new AsyncHttpClient();
                            StringEntity jsonParams = new StringEntity(principal.toString(), "UTF-8");
                            jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            client.setTimeout(Constantes.TIMEOUT);
                            RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlSincronizarCensoCarga, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String respuesta = new String(responseBody);
                                    String logs = tag;
                                    //Log.d("JSON-RESPONSE:",respuesta);
                                    try {
                                        JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                        JSONArray jArrayLog = jsonResponse.getJSONArray("log");
                                        for (int i=0;i<jArrayLog.length();i++){
                                            JSONObject jLog = jArrayLog.getJSONObject(i);
                                            jLog.getInt("id");
                                            jLog.getInt("id_censo");
                                            jLog.getInt("mobiliario");
                                            jLog.getString("mensaje");
                                            jLog.getBoolean("procesar");
                                            logs = logs + "Mobiliario No: "+jLog.getInt("mobiliario")+","+jLog.getString("mensaje")+"\n";
                                            if (jLog.getBoolean("procesar")){
                                                censoDB.eliminarDatos(jLog.getInt("id"));
                                            }
                                        }
                                        visualizarLogs(logs,jsonResponse.getString("mensaje"));
                                        setButton(true);

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    setButton(true);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    String respuesta = new String(responseBody);
                                    Log.d("JSON-RESPONSE-ERROR:",respuesta);
                                    Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                                    setButton(true);
                                }
                            });
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"Error generando empaquetando de datos",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            setButton(true);
                        }

                    } else {
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setButton(true);
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                    }
                }
            });
            alertSincronizar.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setButton(true);
                    Toast.makeText(getApplicationContext(),"Accion Cancelada",Toast.LENGTH_LONG).show();
                    dialogInterface.cancel();
                }
            });
            alertSincronizar.create().show();

        }
        else{
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_sin_datos_por_sincronizar);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setButton(true);
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
    }
    //--
    private JSONArray armarJson(Cursor cursor) throws JSONException{
        JSONArray datos = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                jsonObject.put("id_usuario", idUsuario);
                jsonObject.put("id_municipio", cursor.getInt(cursor.getColumnIndex("id_municipio")));
                jsonObject.put("id_barrio", cursor.getInt(cursor.getColumnIndex("id_barrio")));
                jsonObject.put("id_tipologia", cursor.getInt(cursor.getColumnIndex("id_tipologia")));
                jsonObject.put("id_mobiliario", cursor.getInt(cursor.getColumnIndex("id_mobiliario")));
                jsonObject.put("id_referencia", cursor.getInt(cursor.getColumnIndex("id_referencia")));
                jsonObject.put("id_estado_mobiliario", cursor.getInt(cursor.getColumnIndex("id_estado_mobiliario")));
                jsonObject.put("longitud", cursor.getFloat(cursor.getColumnIndex("longitud")));
                jsonObject.put("latitud", cursor.getFloat(cursor.getColumnIndex("latitud")));
                jsonObject.put("direccion", cursor.getString(cursor.getColumnIndex("direccion")));
                jsonObject.put("fch_registro", cursor.getString(cursor.getColumnIndex("fch_registro")));
                jsonObject.put("observacion", cursor.getString(cursor.getColumnIndex("observacion")));
                jsonObject.put("id_censo", cursor.getInt(cursor.getColumnIndex("id_censo")));
                jsonObject.put("id_elemento", cursor.getInt(cursor.getColumnIndex("id_elemento")));
                jsonObject.put("mobiliario_no", cursor.getInt(cursor.getColumnIndex("mobiliario_no")));
                jsonObject.put("numero_mobiliario_visible", cursor.getString(cursor.getColumnIndex("numero_mobiliario_visible")));
                jsonObject.put("mobiliario_en_sitio", cursor.getString(cursor.getColumnIndex("mobiliario_en_sitio")));
                jsonObject.put("id_sentido", 0);
                jsonObject.put("cantidad", 1);
                jsonObject.put("serial_medidor", 0);
                jsonObject.put("lectura_medidor", 0);
                jsonObject.put("sector", cursor.getString(cursor.getColumnIndex("sector")));
                jsonObject.put("zona", cursor.getString(cursor.getColumnIndex("zona")));
                jsonObject.put("mobiliario_buen_estado", cursor.getString(cursor.getColumnIndex("mobiliario_buen_estado")));

                datos.put(jsonObject);
            } while (cursor.moveToNext());
        }

        return datos;
    }

    //--
    private void visualizarLogs(String logs,String msg){
        alertLogs = new AlertDialog.Builder(this);
        View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.visualizar_logs,null);
        txtLog         = content.findViewById(R.id.txt_log);
        txtLog.setEnabled(false);
        txtLog.setText(msg+"\n\n"+logs);
        alertLogs.setTitle(R.string.titulo_alerta);
        alert.setMessage(msg);
        alertLogs.setView(content);
        alertLogs.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setButton(true);
                dialogInterface.cancel();
            }
        });

        alertLogs.create().setCancelable(false);
        alertLogs.create().show();
    }

    //--
    private void setButton(boolean estado){
        btnSincronizar.setEnabled(estado);
        btnGuardar.setEnabled(estado);
        btnCancelar.setEnabled(estado);
        btnBuscarElemento.setEnabled(estado);
        btnLimpiar.setEnabled(estado);

        if(estado) {
            progressBarGuardarCenso.setVisibility(View.INVISIBLE);
        }
        else{
            progressBarGuardarCenso.setVisibility(View.VISIBLE);
        }
    }

    //--
    private void almacenarDatosEnRemoto() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();

        Integer id_elemento_t = (txtElementoNo.getText().toString().isEmpty() && !swLuminariaVisible.isChecked()) ? null : elemento.getId();

        requestParams.put("id_usuario", idUsuario);
        requestParams.put("id_municipio", idDefaultMunicipio);
        requestParams.put("id_barrio", barrioList.get(sltBarrio.getSelectedItemPosition()).getId());
        requestParams.put("direccion", txtDireccion.getText());
        requestParams.put("id_tipologia", tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId());
        requestParams.put("id_mobiliario", mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario());
        requestParams.put("id_referencia", referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario());
        requestParams.put("id_estado_mobiliario", estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId());
        requestParams.put("longitud", txtLongitud.getText());
        requestParams.put("latitud", txtLatitud.getText());
        requestParams.put("observacion", txtObservacion.getText());
        requestParams.put("id_censo", censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId());
        requestParams.put("id_elemento", id_elemento_t);
        requestParams.put("mobiliario_no", txtElementoNo.getText());
        requestParams.put("numero_mobiliario_visible", chkSwLuminariaVisible);
        requestParams.put("mobiliario_en_sitio", chkSwPoseeLuminaria);
        requestParams.put("cantidad", 1);
        requestParams.put("sector",sector);
        requestParams.put("zona", zona);
        requestParams.put("mobiliario_buen_estado", chkSwMobiliarioBuenEstado);

        client.setTimeout(Constantes.TIMEOUT);

        RequestHandle post = client.post(ServicioWeb.urlGuardarCensoCarga, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setButton(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                JSONObject jsonObject;
                String mensaje;
                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    mensaje = jsonObject.getString("mensaje");

                    alert.setTitle(R.string.titulo_alerta);
                    alert.setMessage(mensaje);
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();

                    Log.d("resultado", "statusCode:" + statusCode + ", mensaje:" + mensaje+" ,respuesta:"+respuesta);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("resultado","Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                    Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Servicio Web, Código:" + statusCode, Toast.LENGTH_SHORT).show();
                }
                resetFrm(true);
                setButton(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
                Log.d("resultado","error "+respuesta);
                Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Código: " + statusCode, Toast.LENGTH_SHORT).show();
                setButton(true);
            }
        });
    }

    //--Administrar Direccion
    private void armarDireccion() {
        alertDireccion = new AlertDialog.Builder(this);
        alertDireccion.setTitle(R.string.titulo_direccion);
        alertDireccion.setCancelable(false);

        View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.direccion, null);

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
                    Toast.makeText(getApplicationContext(),"No selecciono un Tipo de Interseccion",Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(txtNumeroInterseccion.getText().toString())) {
                        //txtMensajeDireccion.setText("Digite el Número de la Intersección");
                        Toast.makeText(getApplicationContext(),"No digitó el número de la intersección",Toast.LENGTH_LONG).show();
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
                            txtDireccion.setText(miDireccion);
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

        /*alertDireccion.setView(content)
                // Add action buttons
                .setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (tipoInterseccionA.get(sltTipoInterseccionA.getSelectedItemPosition()).getId() == 0) {
                            //txtMensajeDireccion.setText("Seleccione Tipo Intersección");
                            //Log.d("Busqueda", "Seleccione Tipo Intersección");
                            Toast.makeText(getApplicationContext(),"No selecciono un Tipo de Interseccion",Toast.LENGTH_LONG).show();
                        } else {
                            if (TextUtils.isEmpty(txtNumeroInterseccion.getText().toString())) {
                                //txtMensajeDireccion.setText("Digite el Número de la Intersección");
                                Toast.makeText(getApplicationContext(),"No digitó el número de la intersección",Toast.LENGTH_LONG).show();
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
                                    txtDireccion.setText(miDireccion);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDireccion.create().setCancelable(false);
        alertDireccion.create().show();*/

    }

    //--Administracion del GPS
    public boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return (ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    private void activarCoordenadas() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        int PERMISSIONS_REQUEST_LOCATION = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CensoCarga.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

        }
        else {
            if(!gpsListener) {
                //Log.d("respuesta","activo");
                ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new miLocalizacion());
            }
        }
    }

    private class miLocalizacion implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //coordenadaActiva = false;
            float mts =  Math.round(location.getAccuracy()*100)/100;
            float alt =  Math.round(location.getAltitude()*100)/100;
            viewLatitud.setText(""+location.getLatitude());
            viewLongitud.setText(""+location.getLongitude());
            viewAltitud.setText(alt+ "Mts");
            viewPrecision.setText(mts+ " Mts");
            viewVelocidad.setText(location.getSpeed()+" Km/h");
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> listDireccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                String miDireccion = listDireccion.get(0).getThoroughfare()+" "+
                        listDireccion.get(0).getFeatureName()+", "+
                        listDireccion.get(0).getLocality()+"/"+
                        listDireccion.get(0).getAdminArea();

                viewDireccion.setText(miDireccion);
                //--direccion.get(0).getAddressLine(0)
            }catch (IOException e){
                viewDireccion.setText("Sin Internet para convertir coordenadas en dirección");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getApplicationContext(),s+" Activo",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(),s+" Inactivo",Toast.LENGTH_LONG).show();
        }
    }
}
