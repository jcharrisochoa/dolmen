package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;

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
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ActualizarElemento extends AppCompatActivity {

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
    private String actualizarCoordenadas;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;

    Elemento elemento;

    private TextView txtNombreMunicipio;
    private TextView txtNombreProceso;
    private TextView txtNombreContrato;
    private TextView txtMensajeDireccion;

    private Button btnCancelar;
    private Button btnGuardar;
    private Button btnTomarFoto;
    private Button btnBorrarFoto;

    private ImageButton btnEditarDireccion;
    private ImageButton btnCapturarGPS;
    private ImageButton btnBuscarElemento;
    private ImageView imgFoto;

    private EditText txtIdElemento;
    private EditText txtDireccion;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText txtPosteno;
    private EditText txtAlturaPoste;
    private EditText txtInterdistancia;
    private EditText txtTransformadorno;
    private EditText txtPotenciaTransformador;
    private EditText txtNumeroInterseccion;
    private EditText txtNumeracionA;
    private EditText txtNumeracionB;
    private EditText txtBuscarElemento;

    private Spinner sltTipologia;
    private Spinner sltMobiliario;
    private Spinner sltReferencia;
    private Spinner sltBarrio;
    private Spinner sltTipoPoste;
    private Spinner sltTipoRed;
    private Spinner sltClaseVia;
    private Spinner sltEstadoMobiliario;
    private Spinner sltTipoInterseccionA;
    private Spinner sltTipoInterseccionB;

    private ProgressBar progressBar;

    private Switch swActualizarPosicion;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_elemento);

        setTitle(getText(R.string.titulo_actualizar_elemento));

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");

        conn = new BaseDatos(ActualizarElemento.this);
        database = conn.getReadableDatabase();

        //--
        alert = new AlertDialog.Builder(this);

        progressBar = findViewById(R.id.progressBarConsultarElemento);
        btnCancelar     = findViewById(R.id.btnCancelar);
        btnGuardar      = findViewById(R.id.btnGuardar);
        btnEditarDireccion          = findViewById(R.id.btn_editar_direccion);
        btnTomarFoto    = findViewById(R.id.btn_tomar_foto);
        btnBorrarFoto   = findViewById(R.id.btn_borrar_foto);
        btnCapturarGPS  = findViewById(R.id.btn_capturar_gps);
        btnBuscarElemento           = findViewById(R.id.btn_buscar_elemento);

        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);

        txtPosteno          =  findViewById(R.id.txtPosteNo);
        txtAlturaPoste      =  findViewById(R.id.txtAlturaPoste);
        txtInterdistancia   =  findViewById(R.id.txtInterdistancia);
        txtTransformadorno  =  findViewById(R.id.txtTransformadorNo);
        txtPotenciaTransformador=  findViewById(R.id.txtPotenciaTransformador);

        txtBuscarElemento   =  findViewById(R.id.txt_buscar_elemento);
        txtIdElemento       =  findViewById(R.id.txtIdElmento);
        txtDireccion        =  findViewById(R.id.txt_direccion);
        txtLatitud          =  findViewById(R.id.txt_latitud);
        txtLongitud         =  findViewById(R.id.txt_longitud);
        txtIdElemento       =  findViewById(R.id.txtIdElmento);

        sltTipologia        =  findViewById(R.id.sltTipologia);
        sltMobiliario       =  findViewById(R.id.sltMobiliario);
        sltReferencia       =  findViewById(R.id.sltReferencia);
        sltTipoPoste        =  findViewById(R.id.sltTipoPoste);
        sltTipoRed          =  findViewById(R.id.sltTipoRed);
        sltClaseVia         =  findViewById(R.id.sltClaseVia);
        sltEstadoMobiliario =  findViewById(R.id.sltEstadoMobiliario);
        sltBarrio           =  findViewById(R.id.sltBarrio);

        imgFoto = findViewById(R.id.foto);

        swActualizarPosicion=  findViewById(R.id.swActualizarPosicion);


        swActualizarPosicion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                btnCapturarGPS.setEnabled(isChecked);
                actualizarCoordenadas = (isChecked)?"S":"N";
            }
        });

        txtDireccion.setEnabled(false);
        btnCapturarGPS.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);

        txtIdElemento.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActualizarElemento.this,Menu.class);
                startActivity(i);
                ActualizarElemento.this.finish();
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

        btnBuscarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarElemento(database);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFrm()){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        ActualizarMobiliario();
                    }
                    else{
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                    }
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
        cargarBarrio(database);
        cargarEstadoMobiliario(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
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
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ActualizarElemento.this);
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) { //Tomar Foto
                    case 0:
                        int PERMISSIONS_REQUEST_CAMERA = 0;
                        if (ContextCompat.checkSelfPermission(ActualizarElemento.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ActualizarElemento.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            tomarFoto();
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen
                        int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                        if (ContextCompat.checkSelfPermission(ActualizarElemento.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ActualizarElemento.this,
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

        Uri photoURI = FileProvider.getUriForFile(ActualizarElemento.this, getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
    }
    //--
    private void buscarElemento(SQLiteDatabase sqLiteDatabase){
        if(txtBuscarElemento.getText().toString().trim().length() == 0){
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_elemento_buscar);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
        else{
            ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()) {
                Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                final AsyncHttpClient client = new AsyncHttpClient();
                RequestParams requestParams = new RequestParams();

                requestParams.add("idmunicipio", String.valueOf(idDefaultMunicipio));
                requestParams.add("idproceso", String.valueOf(idDefaultProceso));
                requestParams.add("idcontrato", String.valueOf(idDefaultContrato));
                requestParams.add("elemento", txtBuscarElemento.getText().toString());
                client.setTimeout(Constantes.TIMEOUT);

                //Log.d("resultado","parametros:"+requestParams.toString());

                RequestHandle post = client.post(ServicioWeb.urlConsultarElemento, requestParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String respuesta = new String(responseBody);
                        //Log.d("resultado",""+respuesta);
                        try{
                            JSONObject jsonObject = new JSONObject(new String(responseBody));
                            String mensaje = jsonObject.getString("mensaje");
                            alert.setTitle(R.string.titulo_alerta);
                            alert.setMessage(mensaje);

                            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            alert.create().show();

                            txtIdElemento.setText(jsonObject.getString("id"));
                            txtPosteno.setText(jsonObject.getString("posteno"));
                            txtAlturaPoste.setText(jsonObject.getString("alturaposte"));
                            txtInterdistancia.setText(jsonObject.getString("interdistancia"));
                            txtTransformadorno.setText(jsonObject.getString("transformador"));
                            txtPotenciaTransformador.setText(jsonObject.getString("potenciatransformador"));
                            txtDireccion.setText(jsonObject.getString("direccion"));
                            txtLatitud.setText(jsonObject.getString("latitud"));
                            txtLongitud.setText(jsonObject.getString("longitud"));
                            idMobiliarioBusqueda = parseInt(jsonObject.getString("idmobiliario"));
                            idReferenciaBusqueda = parseInt(jsonObject.getString("idreferencia"));

                            //tipologiaList
                            for(int i=0;i<tipologiaList.size();i++){
                                if(tipologiaList.get(i).getId() == parseInt(jsonObject.getString("idtipologia"))){
                                    sltTipologia.setAdapter(sltTipologia.getAdapter());
                                    sltTipologia.setSelection(i);
                                }
                            }
                            // this.cargarMobiliario(database);
                            // this.cargarReferencia(database);
                            //EstadoList
                            for(int i=0;i<estadoMobiliarioList.size();i++){
                                if(estadoMobiliarioList.get(i).getId() == parseInt(jsonObject.getString("idestadomobiliario"))){
                                    sltEstadoMobiliario.setSelection(i);
                                }
                            }
                            //barrioList
                            for(int i=0;i<barrioList.size();i++){
                                if(barrioList.get(i).getId() == parseInt(jsonObject.getString("idbarrio"))){
                                    sltBarrio.setSelection(i);
                                }
                            }
                            //TipoPosteList
                            for(int i=0;i<tipoPosteList.size();i++){
                                if(tipoPosteList.get(i).getId() == parseInt(jsonObject.getString("idtipoposte"))){
                                    sltTipoPoste.setSelection(i);
                                }
                            }
                            //TipoRedList
                            for(int i=0;i<tipoRedList.size();i++){
                                if(tipoRedList.get(i).getId() == parseInt(jsonObject.getString("idtipored"))){
                                    sltTipoRed.setSelection(i);
                                }
                            }
                            //ClaseVia
                            for(int i=0;i<claseViaList.size();i++){
                                if(claseViaList.get(i).getId() == parseInt(jsonObject.getString("idclasevia"))){
                                    sltClaseVia.setSelection(i);
                                }
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }catch (JSONException e){
                            e.printStackTrace();
                            //Log.d("resultado","Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                            Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Servicio Web, Código:"+statusCode, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String respuesta = new String(responseBody);
                        Log.d("resultado","Error "+respuesta);
                        Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            else{
                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(R.string.alert_conexion);
                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        }
    }
    //--
    private boolean validarFrm(){
        if(txtIdElemento.getText().toString().isEmpty()){
            alert.setMessage(R.string.alert_censo_tecnico_elemento_no);
            return false;
        }
        else{
            if (tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId() == 0) {
                alert.setMessage(R.string.alert_censo_tipologia);
                return false;
            }
            else{
                if (mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario() == 0) {
                    alert.setMessage(R.string.alert_censo_mobiliario);
                    return false;
                }
                else {
                    if (barrioList.get(sltBarrio.getSelectedItemPosition()).getId() == 0) {
                        alert.setMessage(R.string.alert_censo_barrio);
                        return false;
                    }
                    else {
                        if(txtDireccion.getText().toString().isEmpty()){
                            alert.setMessage(R.string.alert_censo_direccion);
                            return false;
                        }
                        else {
                            if(swActualizarPosicion.isChecked() && txtLatitud.getText().toString().isEmpty() && txtLongitud.getText().toString().isEmpty()){
                                alert.setMessage(R.string.alert_coordenadas);
                                return false;
                            }
                            else {
                                if(idDefaultProceso == 19){
                                    if (referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario() == 0) {
                                        alert.setMessage(R.string.alert_censo_referencia);
                                        return false;
                                    }
                                    else{
                                        if (tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId() == 0) {
                                            alert.setMessage(R.string.alert_censo_tipo_poste);
                                            return false;
                                        } else {
                                            if (tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId() == 0) {
                                                alert.setMessage(R.string.alert_tipo_red);
                                                return false;
                                            } else {
                                                if (claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId() == 0) {
                                                    alert.setMessage(R.string.alert_censo_clase_via);
                                                    return false;
                                                } else {
                                                    if (estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId() == 0) {
                                                        alert.setMessage(R.string.alert_censo_estado_mobiliario);
                                                        return false;
                                                    }
                                                    else{
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else{
                                    if (estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId() == 0) {
                                        alert.setMessage(R.string.alert_censo_estado_mobiliario);
                                        return false;
                                    }
                                    else{
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
    private void ActualizarMobiliario(){

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("mobiliario_no",txtBuscarElemento.getText());
        requestParams.put("id_elemento",txtIdElemento.getText());
        requestParams.put("id_usuario",idUsuario);
        requestParams.put("id_municipio",idDefaultMunicipio);
        requestParams.put("id_proceso",idDefaultProceso);
        requestParams.put("id_contrato",idDefaultContrato);
        requestParams.put("id_barrio",barrioList.get(sltBarrio.getSelectedItemPosition()).getId());
        requestParams.put("latitud",txtLatitud.getText());
        requestParams.put("longitud",txtLongitud.getText());
        requestParams.put("id_tipologia",tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId());
        requestParams.put("id_mobiliario",mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario());
        requestParams.put("id_referencia",referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario());
        requestParams.put("id_tipo_poste",tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_red",tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
        requestParams.put("id_clase_via",claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId());;
        requestParams.put("id_estado",estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId());
        requestParams.put("direccion",txtDireccion.getText());
        requestParams.put("actualiza_posicion",actualizarCoordenadas);
        requestParams.put("poste_no",txtPosteno.getText());
        requestParams.put("altura_poste",txtAlturaPoste.getText());
        requestParams.put("interdistancia",txtInterdistancia.getText());
        requestParams.put("transformador_no",txtTransformadorno.getText());
        requestParams.put("potencia_transformador",txtPotenciaTransformador.getText());
        requestParams.put("encode_string",encodeString);
        client.setTimeout(Constantes.TIMEOUT);
        RequestHandle post = client.post(ServicioWeb.urlActualizarElemento, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Log.d("resultado",""+respuesta);
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    String mensaje = jsonObject.getString("mensaje");
                    alert.setTitle(R.string.titulo_alerta);
                    alert.setMessage(mensaje);

                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();
                    progressBar.setVisibility(View.INVISIBLE);
                    resetFrm();

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("resultado","Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                    Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Servicio Web, Código:"+statusCode, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
                Log.d("resultado","Error "+respuesta);
                Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    //-
    private void resetFrm(){
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtPosteno.setText("");
        txtAlturaPoste.setText("");
        txtInterdistancia.setText("");
        txtTransformadorno.setText("");
        txtPotenciaTransformador.setText("");
        txtIdElemento.setText("");
        txtDireccion.setText("");
        sltTipologia.setSelection(0);
        sltMobiliario.setSelection(0);
        sltReferencia.setSelection(0);
        sltBarrio.setSelection(0);
        sltTipoPoste.setSelection(0);
        sltTipoRed.setSelection(0);
        sltClaseVia.setSelection(0);
        sltEstadoMobiliario.setSelection(0);
        swActualizarPosicion.setChecked(false);
        imgFoto.setImageResource(R.drawable.imagen_no_disponible);
        encodeString=null;
    }
}
