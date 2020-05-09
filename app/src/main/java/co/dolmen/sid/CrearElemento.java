package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.ProveedorDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.SentidoDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.modelo.UnidadMedidaDB;
import co.dolmen.sid.utilidades.DataSpinner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CrearElemento extends AppCompatActivity {

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    AlertDialog.Builder alert;
    SharedPreferences config;
    AlertDialog.Builder alertDireccion;

    private String encodeString;
    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private String path;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;

    private boolean gpsListener;
    public LocationManager ubicacion;

    private TextView txtNombreMunicipio;
    private TextView txtNombreProceso;
    private TextView txtNombreContrato;
    private TextView txtMensajeDireccion;
    private EditText txtDireccion;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private TextView viewLatitud;
    private TextView viewLongitud;
    private TextView viewAltitud;
    private TextView viewPrecision;
    private TextView viewDireccion;
    private TextView viewVelocidad;

    private EditText txtNumeroInterseccion;
    private EditText txtNumeracionA;
    private EditText txtNumeracionB;

    private Spinner sltTipoInterseccionA;
    private Spinner sltTipoInterseccionB;
    private Spinner sltActaContrato;
    private Spinner sltSentido;
    private Spinner sltUnidad;
    private Spinner sltTipoRed;
    private Spinner sltTipoPoste;
    private Spinner sltClaseVia;
    private Spinner sltEstadoMobiliario;
    private Spinner sltTipologia;
    private Spinner sltBarrio;
    private Spinner sltMobiliario;
    private Spinner sltReferencia;
    private Spinner sltCruce;
    private Spinner sltProveedor;


    private Button btnGuardar;
    private Button btnCancelar;
    private Button btnTomarFoto;
    private Button btnBorrarFoto;
    private ImageButton btnEditarDireccion;
    private ImageButton btnCapturarGPS;

    private ImageView imgFoto;

    ArrayList<DataSpinner> tipologiaList;
    ArrayList<Mobiliario> mobiliarioList;
    ArrayList<ReferenciaMobiliario> referenciaMobiliarioList;
    ArrayList<DataSpinner> estadoMobiliarioList;
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> claseViaList;
    ArrayList<DataSpinner> tipoPosteList;
    ArrayList<DataSpinner> tipoRedList;
    ArrayList<DataSpinner> tipoInterseccionA;
    ArrayList<DataSpinner> tipoInterseccionB;
    ArrayList<DataSpinner> actaList;
    ArrayList<DataSpinner> unidadList;
    ArrayList<DataSpinner> sentidoList;
    ArrayList<DataSpinner> proveedorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_elemento);

        setTitle(getText(R.string.titulo_crear_elemento));

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");

        conn = new BaseDatos(CrearElemento.this);
        database = conn.getReadableDatabase();
        //--
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new miLocalizacion());
            gpsListener  = true;
        }

        //--
        alert = new AlertDialog.Builder(this);

        sltActaContrato     = findViewById(R.id.sltActaContrato);
        sltSentido          = findViewById(R.id.sltSentido);
        sltUnidad           = findViewById(R.id.sltUnidad);
        sltTipoRed          = findViewById(R.id.sltTipoRed);
        sltTipoPoste        = findViewById(R.id.sltTipoPoste);
        sltClaseVia          = findViewById(R.id.sltClaseVia);
        sltEstadoMobiliario = findViewById(R.id.sltEstado);
        sltTipologia        = findViewById(R.id.sltTipologia);
        sltBarrio           = findViewById(R.id.sltBarrio);
        sltMobiliario       = findViewById(R.id.sltMobiliario);
        sltReferencia       = findViewById(R.id.sltReferenciaMobiliario);
        sltProveedor        = findViewById(R.id.sltProveedor);

        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);
        txtDireccion        =  findViewById(R.id.txt_direccion);
        txtLatitud          =  findViewById(R.id.txt_latitud);
        txtLongitud         =  findViewById(R.id.txt_longitud);
        //--
        viewLatitud             = findViewById(R.id.gps_latitud);
        viewLongitud            = findViewById(R.id.gps_longitud);
        viewAltitud             = findViewById(R.id.gps_altitud);
        viewPrecision           = findViewById(R.id.gps_precision);
        viewDireccion           = findViewById(R.id.gps_direccion);
        viewVelocidad           = findViewById(R.id.gps_velocidad);

        //-
        btnGuardar              = findViewById(R.id.btnGuardar);
        btnCancelar             = findViewById(R.id.btnCancelar);
        btnEditarDireccion      = findViewById(R.id.btn_editar_direccion);
        btnTomarFoto            = findViewById(R.id.btn_tomar_foto);
        btnBorrarFoto           = findViewById(R.id.btn_borrar_foto);
        btnCapturarGPS          = findViewById(R.id.btn_capturar_gps);
        //--
        imgFoto = findViewById(R.id.foto);
        //--

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

        txtDireccion.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CrearElemento.this,Menu.class);
                startActivity(i);
                CrearElemento.this.finish();
            }
        });
        btnEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                armarDireccion();
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        btnBorrarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFoto.setImageResource(R.drawable.imagen_no_disponible);
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
        sltTipologia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarMobiliario(database);
                //Log.d("Mobiliario","selected");
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
        cargarEstadoMobiliario(database);
        cargarBarrio(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarUnidadMedida(database);
        cargarSentido(database);
        cargarActa(database);
        cargarProveedor(database);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap;
        ByteArrayOutputStream stream;
        byte[] array;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constantes.CONS_SELECCIONAR_IMAGEN:
                    String encode = "";
                    Uri selectionPath = data.getData();
                    //Log.d("Path",""+selectionPath.getPath());

                    try {
                        InputStream s = getContentResolver().openInputStream(selectionPath);
                        bitmap = BitmapFactory.decodeStream(s,null,options);
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        array = stream.toByteArray();
                        encode = Base64.encodeToString(array,0);
                        //Log.d("Path",""+encode);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    imgFoto.setImageURI(selectionPath);
                    encodeString = encode;

                    break;
                case Constantes.CONS_TOMAR_FOTO:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path, options);

                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    array = stream.toByteArray();

                    imgFoto.setImageBitmap(bitmap);
                    encodeString = Base64.encodeToString(array,0);

                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_cancelar_camara), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_camara), Toast.LENGTH_SHORT).show();
        }

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
        mobiliarioList = new ArrayList<Mobiliario>();
        List<String> labels = new ArrayList<>();

        //Realizo la consulta a la base datos
        MobiliarioDB mobiliarioDB = new MobiliarioDB(sqLiteDatabase);
        Cursor cursor = mobiliarioDB.consultarTodo(idTipologia);
        Mobiliario mobiliario = new Mobiliario(i, getText(R.string.seleccione).toString());
        mobiliarioList.add(mobiliario);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    mobiliario = new Mobiliario(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    mobiliarioList.add(mobiliario);
                    labels.add(cursor.getString(2).toUpperCase());
                    if(idMobiliarioBusqueda == cursor.getInt(0)){
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
                    if(idReferenciaBusqueda == cursor.getInt(0)){
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
    private void cargarEstadoMobiliario(SQLiteDatabase sqLiteDatabase){
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
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    estadoMobiliarioList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
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
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    claseViaList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRed.setAdapter(dataAdapter);
    }
    //--
    private void cargarUnidadMedida(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        unidadList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        UnidadMedidaDB unidadMedidaDB = new UnidadMedidaDB(sqLiteDatabase);
        Cursor cursor = unidadMedidaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        unidadList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    unidadList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltUnidad.setAdapter(dataAdapter);
    }
    //--
    private void cargarSentido(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        sentidoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        SentidoDB sentidoDB = new SentidoDB(sqLiteDatabase);
        Cursor cursor = sentidoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        sentidoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    sentidoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltSentido.setAdapter(dataAdapter);
    }
    //--
    private void cargarActa(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        actaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ActaContratoDB actaContratoDB = new ActaContratoDB(sqLiteDatabase);
        Cursor cursor = actaContratoDB.consultarActaContrato(idDefaultContrato);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        actaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    sentidoList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltActaContrato.setAdapter(dataAdapter);
    }
    //--
    private void cargarProveedor(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        proveedorList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ProveedorDB proveedorDB = new ProveedorDB(sqLiteDatabase);
        Cursor cursor = proveedorDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        proveedorList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    proveedorList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltProveedor.setAdapter(dataAdapter);
    }
    //--
    private void armarDireccion(){
        alertDireccion = new AlertDialog.Builder(this);
        View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.direccion,null);
        txtMensajeDireccion         = content.findViewById(R.id.txt_mensaje_direccion);
        sltTipoInterseccionA        = content.findViewById(R.id.slt_tipo_interseccion_a);
        sltTipoInterseccionB        = content.findViewById(R.id.slt_tipo_interseccion_b);
        txtNumeroInterseccion       = content.findViewById(R.id.numero_interseccion);
        txtNumeracionA              = content.findViewById(R.id.txt_numeracion_a);
        txtNumeracionB              = content.findViewById(R.id.txt_numeracion_b);
        cargarTipoInterseccion(database);
        alertDireccion.setTitle(R.string.titulo_direccion);
        alertDireccion.setView(content)
                // Add action buttons
                .setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(tipoInterseccionA.get(sltTipoInterseccionA.getSelectedItemPosition()).getId() == 0){
                            txtMensajeDireccion.setText("Seleccione Tipo Intersección");
                            Log.d("Busqueda","Seleccione Tipo Intersección");
                        }
                        else{
                            if(TextUtils.isEmpty(txtNumeroInterseccion.getText().toString())){
                                txtMensajeDireccion.setText("Digite el Número de la Intersección");
                            }
                            else{
                                String miDireccion = "";

                                miDireccion = miDireccion + tipoInterseccionA.get(sltTipoInterseccionA.getSelectedItemPosition()).getDescripcion();
                                miDireccion = miDireccion + " " + txtNumeroInterseccion.getText().toString();

                                if(tipoInterseccionA.get(sltTipoInterseccionB.getSelectedItemPosition()).getId() != 0 ){
                                    miDireccion = miDireccion + " " + tipoInterseccionB.get(sltTipoInterseccionB.getSelectedItemPosition()).getDescripcion();
                                }
                                if(!TextUtils.isEmpty(txtNumeracionA.getText().toString())){
                                    if(tipoInterseccionA.get(sltTipoInterseccionB.getSelectedItemPosition()).getId() != 0 ) {
                                        miDireccion = miDireccion + " " + txtNumeracionA.getText().toString();
                                    }
                                    else{
                                        miDireccion = miDireccion + " N " + txtNumeracionA.getText().toString();
                                    }
                                }
                                if(!TextUtils.isEmpty(txtNumeracionB.getText().toString())){
                                    miDireccion = miDireccion + " - " +txtNumeracionB.getText().toString();
                                }
                                if(!miDireccion.isEmpty())
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
        alertDireccion.create().show();
    }
    //--
    private void cargarImagen(){
        final CharSequence[] opciones = {getText(R.string.tomar_foto).toString(), getText(R.string.cargar_imagen).toString(), getText(R.string.btn_cancelar)};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(CrearElemento.this);
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) { //Tomar Foto
                    case 0:
                        int PERMISSIONS_REQUEST_CAMERA = 0;
                        if (ContextCompat.checkSelfPermission(CrearElemento.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CrearElemento.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            tomarFoto();
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen
                        int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                        if (ContextCompat.checkSelfPermission(CrearElemento.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CrearElemento.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/");
                            startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"), Constantes.CONS_SELECCIONAR_IMAGEN);
                        }
                        dialogInterface.dismiss();
                        break;
                    case 2: //Cancelar
                        dialogInterface.dismiss();
                        break;
                    default:
                        dialogInterface.dismiss();
                }

            }
        });
        alertOpciones.show();
    }
    //--
    public void tomarFoto() {
        String nombreImagen = "";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constantes.IMAGE_DIRECTORY_NAME);
        boolean iscreada = mediaStorageDir.exists();

        if (iscreada == false) {
            iscreada = mediaStorageDir.mkdir();
        } else {
            nombreImagen = "IMG_" + (System.currentTimeMillis() / 100) + ".jpg";
        }

        path = mediaStorageDir.getPath() + File.separator + nombreImagen;
        File imagen = new File(path);

        Uri photoURI = FileProvider.getUriForFile(CrearElemento.this, getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
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

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

        }
        else {
            if(!gpsListener) {
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
