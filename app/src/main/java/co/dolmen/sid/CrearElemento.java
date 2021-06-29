package co.dolmen.sid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ElementoDB;
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
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CrearElemento extends AppCompatActivity {

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    AlertDialog.Builder alert;
    SharedPreferences config;
    AlertDialog.Builder alertDireccion;


    private ProgressBar progressBar;

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
    private Integer conexionElectrica = 0;
    private  int contenLenght =  0;

    private boolean gpsListener;
    public LocationManager ubicacion;

   /* private TextView txtNombreMunicipio;
    private TextView txtNombreProceso;
    private TextView txtNombreContrato;*/
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
    private EditText txtCantidad;
    private EditText txtObservacion;
    private EditText txtPosteno;
    private EditText txtTransformador;
    private EditText txtPotenciaTransformador;
    private EditText txtMtTransformador;
    private EditText txtCtTransformador;
    private EditText txtSerialMedidor;
    private EditText txtLecturaMedidor;

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
    private Spinner sltProveedor;


    private FloatingActionButton btnGuardar;
    private FloatingActionButton btnCancelar;
    private Button btnTomarFoto;
    private Button btnBorrarFoto;
    private ImageButton btnEditarDireccion;
    private ImageButton btnCapturarGPS;

    private Switch swConexionElectrica;
    private Switch swTranformadorExclusivoAP;

    RadioButton rdTransformadorPrivado;
    RadioButton rdTransformadorPublico;
    RadioButton rdTransformadorNoAplica;

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

    ToggleButton chkTercero;
    String tercero = "N";
    private String chkSwTransformadorExclusivoAp = "N";
    private String tipoPropietarioTranformador = "NA";

    @RequiresApi(api = Build.VERSION_CODES.O)
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

       /* txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);*/
        txtDireccion        =  findViewById(R.id.txt_direccion);
        txtLatitud          =  findViewById(R.id.txt_latitud);
        txtLongitud         =  findViewById(R.id.txt_longitud);
        txtCantidad                 = findViewById(R.id.txtCantidad);
        txtObservacion              = findViewById(R.id.txtObservacion);
        txtPosteno                  = findViewById(R.id.txtPosteno);
        txtTransformador            = findViewById(R.id.txtTranformador);
        txtPotenciaTransformador    = findViewById(R.id.txtPotenciaTranformador);
        txtMtTransformador = findViewById(R.id.txt_mt_transformador);
        txtCtTransformador = findViewById(R.id.txt_ct_transformador);
        txtSerialMedidor            = findViewById(R.id.txtSerialMedidor);
        txtLecturaMedidor           = findViewById(R.id.txtLecturaMedidor);
        //--
        viewLatitud             = findViewById(R.id.gps_latitud);
        viewLongitud            = findViewById(R.id.gps_longitud);
        viewAltitud             = findViewById(R.id.gps_altitud);
        viewPrecision           = findViewById(R.id.gps_precision);
        viewDireccion           = findViewById(R.id.gps_direccion);
        viewVelocidad           = findViewById(R.id.gps_velocidad);
        //-
        chkTercero  = findViewById(R.id.chkTercero);
        swConexionElectrica = findViewById(R.id.swConexionElectrica);
        swTranformadorExclusivoAP = findViewById(R.id.sw_transformador_exclusivo_ap);
        //--
        rdTransformadorPrivado = findViewById(R.id.rd_transformador_privado);
        rdTransformadorPublico = findViewById(R.id.rd_transformador_publico);
        rdTransformadorNoAplica = findViewById(R.id.rd_transformador_no_aplica);
        //-
        btnGuardar              = findViewById(R.id.fab_guardar);
        btnCancelar             = findViewById(R.id.fab_cancelar);
        btnEditarDireccion      = findViewById(R.id.btn_editar_direccion);
        btnTomarFoto            = findViewById(R.id.btn_tomar_foto);
        btnBorrarFoto           = findViewById(R.id.btn_borrar_foto);
        btnCapturarGPS          = findViewById(R.id.btn_capturar_gps);
        //--
        imgFoto = findViewById(R.id.foto);
        //--
        progressBar            = findViewById(R.id.progressBarGuardarNuevoElemento);
        //--
        /*txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);*/

        swTranformadorExclusivoAP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwTransformadorExclusivoAp = (isChecked) ? "S" : "N";
            }
        });

        rdTransformadorPrivado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipoPropietarioTranformador = "PV";
            }
        });

        rdTransformadorPublico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipoPropietarioTranformador = "PB";
            }
        });

        rdTransformadorNoAplica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipoPropietarioTranformador = "NA";
            }
        });

        txtDireccion.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFrm()){
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);

                    btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
                    btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));

                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                        GuardarMobiliario();
                    }
                    else{
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BigInteger bi;
                                int _hascode = 0;
                                try {
                                    LocalDateTime localDateTime;
                                    localDateTime = LocalDateTime.now();
                                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                                    messageDigest.update(localDateTime.toString().getBytes());
                                    byte[] hash = messageDigest.digest();
                                    bi = new BigInteger( hash );
                                    _hascode = Math.abs(bi.hashCode());
                                    Log.d(Constantes.TAG,"hascode="+Math.abs(_hascode));
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.d(Constantes.TAG,e.getMessage());
                                }

                                if(!guardarLocal(_hascode,"","S")){
                                    Log.d(Constantes.TAG,"Error Guardando Localmente el elemento");
                                    alert.setTitle(R.string.titulo_alerta);
                                    alert.setMessage(R.string.alert_error_almacenando_datos);
                                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            btnGuardar.setEnabled(true);
                                            btnCancelar.setEnabled(true);
                                            btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                                            btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                                        }
                                    });
                                    alert.create().show();
                                }
                                else{
                                    alert.setTitle(R.string.titulo_alerta);
                                    alert.setMessage(R.string.alert_almacenamiento_local);
                                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            resetFrm();
                                            btnGuardar.setEnabled(true);
                                            btnCancelar.setEnabled(true);
                                            btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                                            btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                                        }
                                    });
                                    alert.create().show();
                                }
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

        chkTercero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tercero = (compoundButton.isChecked())?"S":"N";

            }
        });

        swConexionElectrica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    conexionElectrica = 1;
                }
                else {
                    conexionElectrica = 0;
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_censo_tecnico,menu);
        return true; //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sincronizar:
                sincronizarPendientes();
                break;
        }
        return true;//super.onOptionsItemSelected(item);
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
                    actaList.add(dataSpinner);
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
        alertDireccion.setCancelable(false);
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
                            //txtMensajeDireccion.setText("Seleccione Tipo Intersección");
                            //Log.d("Busqueda","Seleccione Tipo Intersección");
                            Toast.makeText(getApplicationContext(),"No selecciono un Tipo de Interseccion",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(TextUtils.isEmpty(txtNumeroInterseccion.getText().toString())){
                                //txtMensajeDireccion.setText("Digite el Número de la Intersección");
                                Toast.makeText(getApplicationContext(),"No digitó el número de la intersección",Toast.LENGTH_LONG).show();
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
                int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                int PERMISSIONS_REQUEST_CAMERA = 0;
                switch (i) { //Tomar Foto
                    case 0:
                        if (ContextCompat.checkSelfPermission(CrearElemento.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CrearElemento.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        }
                        else {
                            if (ContextCompat.checkSelfPermission(CrearElemento.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CrearElemento.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                            }
                            else {
                                tomarFoto();
                            }
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen

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

    private void resetFrm(){
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        sltSentido.setSelection(0);
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtTransformador.setText("");
        txtMtTransformador.setText("");
        txtCtTransformador.setText("");
        txtSerialMedidor.setText("");
        txtLecturaMedidor.setText("");
        txtObservacion.setText("");
        txtCantidad.setText("1");
        chkTercero.setChecked(false);
        sltProveedor.setSelection(0);
        imgFoto.setImageResource(R.drawable.imagen_no_disponible);
        encodeString="";
    }
    //-
    private boolean validarFrm(){
        if(unidadList.get(sltUnidad.getSelectedItemPosition()).getId()==0){
            alert.setMessage(R.string.alert_unidad_medida);
            return false;
        }
        else{
            if(txtCantidad.getText().toString().isEmpty()){
                alert.setMessage(R.string.alert_cantidad);
                return false;
            }
            else{
                if (tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId() == 0) {
                    alert.setMessage(R.string.alert_censo_tipologia);
                    return false;
                }
                else {
                    if (mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario() == 0) {
                        alert.setMessage(R.string.alert_censo_mobiliario);
                        return false;
                    }
                    else {
                        if(barrioList.get(sltBarrio.getSelectedItemPosition()).getId()==0){
                            alert.setMessage(R.string.alert_censo_barrio);
                            return false;
                        }
                        else {
                            if (txtDireccion.getText().toString().isEmpty()) {
                                alert.setMessage(R.string.alert_censo_direccion);
                                return false;
                            }
                            else {
                                if (idDefaultProceso == 19) {
                                    if (referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario() == 0) {
                                        alert.setMessage(R.string.alert_censo_referencia);
                                        return false;
                                    }
                                    else {
                                        if (tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId() == 0) {
                                            alert.setMessage(R.string.alert_censo_tipo_poste);
                                            return false;
                                        }
                                        else {
                                            if (tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId() == 0) {
                                                alert.setMessage(R.string.alert_tipo_red);
                                                return false;
                                            }
                                            else {
                                                if (claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId() == 0) {
                                                    alert.setMessage(R.string.alert_censo_clase_via);
                                                    return false;
                                                }
                                                else {
                                                    if (estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId() == 0) {
                                                        alert.setMessage(R.string.alert_censo_estado_mobiliario);
                                                        return false;
                                                    }
                                                    else{
                                                        if(encodeString == ""){
                                                            alert.setMessage(R.string.alert_fotografia);
                                                            return false;
                                                        }
                                                        else
                                                            return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (sentidoList.get(sltSentido.getSelectedItemPosition()).getId() == 0) {
                                        alert.setMessage(R.string.alert_sentido);
                                        return false;
                                    }
                                    else {
                                        if (unidadList.get(sltUnidad.getSelectedItemPosition()).getId() == 0) {
                                            alert.setMessage(R.string.alert_unidad_medida);
                                            return false;
                                        }
                                        else {
                                            if (estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId() == 0) {
                                                alert.setMessage(R.string.alert_censo_estado_mobiliario);
                                                return false;
                                            }
                                            else
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
    }
    //--
    private void GuardarMobiliario(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id_usuario",idUsuario);
        requestParams.put("id_municipio",idDefaultMunicipio);
        requestParams.put("id_contrato",idDefaultContrato);
        requestParams.put("id_proceso",idDefaultProceso);
        requestParams.put("id_acta",actaList.get(sltActaContrato.getSelectedItemPosition()).getId());
        requestParams.put("id_barrio",barrioList.get(sltBarrio.getSelectedItemPosition()).getId());
        requestParams.put("direccion",txtDireccion.getText());
        requestParams.put("latitud",txtLatitud.getText());
        requestParams.put("longitud",txtLongitud.getText());
        requestParams.put("id_tipologia",tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId());
        requestParams.put("id_mobiliario",mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario());
        requestParams.put("id_referencia",referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario());
        requestParams.put("id_sentido",sentidoList.get(sltSentido.getSelectedItemPosition()).getId());
        requestParams.put("tercero",tercero);
        requestParams.put("id_proveedor",proveedorList.get(sltProveedor.getSelectedItemPosition()).getId());
        requestParams.put("cantidad",txtCantidad.getText());
        requestParams.put("id_unidad",unidadList.get(sltUnidad.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_poste",tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId());
        requestParams.put("poste_no",txtPosteno.getText());
        requestParams.put("id_tipo_red",tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());

        requestParams.put("transformador",txtTransformador.getText()); //codigo sai
        requestParams.put("potencia_transformador",txtPotenciaTransformador.getText());
        requestParams.put("placa_MT",txtMtTransformador.getText());
        requestParams.put("placa_CT",txtCtTransformador.getText());

        requestParams.put("transformador_compartido",(chkSwTransformadorExclusivoAp.contentEquals("S"))?"N":"S");

        requestParams.put("serial_medidor",txtSerialMedidor.getText());
        requestParams.put("lectura_medidor",txtLecturaMedidor.getText());
        requestParams.put("id_clase_via",claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId());
        requestParams.put("id_estado",estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId());
        requestParams.put("observacion",txtObservacion.getText());
        requestParams.put("conexion_electrica",conexionElectrica);
        requestParams.put("encode_string",encodeString);
        client.setTimeout(Constantes.TIMEOUT);

        RequestHandle post = client.post(ServicioWeb.urlGuardarNuevoElemento, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                //Log.d("resultado",""+respuesta);
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
                    if(jsonObject.getInt("idelemento")!=0){
                        if(!guardarLocal(
                                jsonObject.getInt("idelemento"),
                                jsonObject.getString("mobiliario_no"),"N"
                        )){
                            Log.d(Constantes.TAG,"Error Guardando Localmente el elemento");
                            Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Error Guardando localmente el elemento", Toast.LENGTH_SHORT).show();
                        }
                    }
                    resetFrm();
                    btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                    btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                    progressBar.setVisibility(View.INVISIBLE);


                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d(Constantes.TAG,"Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                    Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Servicio Web, Código:"+statusCode, Toast.LENGTH_SHORT).show();
                    btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                    btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
                Log.d(Constantes.TAG,"Error "+respuesta);
                Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode, Toast.LENGTH_SHORT).show();
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public boolean guardarLocal(int id_elemento,String mobiliario_no,String temporal){

        ElementoDB elementoDB = new ElementoDB(database);
        float potenciaTransformador = (txtPotenciaTransformador.getText().toString().isEmpty())?0:Float.parseFloat(txtPotenciaTransformador.getText().toString());
        int lecturaMedidor = (txtLecturaMedidor.getText().toString().isEmpty())?0:Integer.parseInt(txtLecturaMedidor.getText().toString());
        float lat = (txtLatitud.getText().toString().isEmpty())?0:Float.parseFloat(txtLatitud.getText().toString());
        float lon = (txtLongitud.getText().toString().isEmpty())?0:Float.parseFloat(txtLongitud.getText().toString());

        return elementoDB.agregarDatos(
                id_elemento,mobiliario_no,
                txtDireccion.getText().toString(),
                idDefaultMunicipio,
                barrioList.get(sltBarrio.getSelectedItemPosition()).getId(),
                idDefaultProceso,
                tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId(),
                mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario(),
                referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario(),
                estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId(),
                0,0,0,"U","N",
                lat,
                lon,
                claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId(),
                0,
                tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId(),
                0,txtPosteno.getText().toString(),0,0,
                tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId(),
                0,0,0,
                (chkSwTransformadorExclusivoAp.contentEquals("S"))?"N":"S",
                "",
                potenciaTransformador,
                txtMtTransformador.getText().toString(),
                txtCtTransformador.getText().toString(),
                sentidoList.get(sltSentido.getSelectedItemPosition()).getId(),
                proveedorList.get(sltProveedor.getSelectedItemPosition()).getId(),
                unidadList.get(sltUnidad.getSelectedItemPosition()).getId(),
                Integer.parseInt(txtCantidad.getText().toString()),
                tercero,
                txtSerialMedidor.getText().toString(),
                lecturaMedidor,
                actaList.get(sltActaContrato.getSelectedItemPosition()).getId(),
                txtObservacion.getText().toString(),temporal,
                encodeString
        );
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

    //--Sincronizar Elementos pendientes
    private void sincronizarPendientes(){
        ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            CrearElemento.Sincronizar sincronizar = new CrearElemento.Sincronizar(this);
            sincronizar.execute();
        } else {
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

    private class Sincronizar extends AsyncTask<Void,Integer,Boolean> {

        private JSONArray jsonArray;
        private ProgressDialog dialog;
        private int progress;
        private TextView msgLogs;
        private View view;
        private AlertDialog.Builder alertDialog;
        private LayoutInflater inflater;
        ElementoDB elementoDB;
        Cursor cursor;

        public Sincronizar(Activity activity) {
            dialog = new ProgressDialog(activity);
            alertDialog = new AlertDialog.Builder(activity);

            dialog.setCancelable(false);
            dialog.setTitle(R.string.titulo_alerta);
            dialog.setIcon(R.drawable.icon_info);

            inflater = LayoutInflater.from(activity);

            elementoDB = new ElementoDB(database);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setButton(false);

            dialog.setMessage(getText(R.string.recopilando_datos)+" 0%");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                jsonArray = armarJSON();
            } catch (JSONException e) {
                setButton(true);

                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage(getText(R.string.recopilando_datos)+" "+values[0].intValue()+"%");
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.d(Constantes.TAG,"json->"+jsonArray);
                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    alert.setMessage(getText(R.string.alert_sin_datos_por_sincronizar));
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            setButton(true);
                        }
                    });
                    alert.create().show();
                }
                else{
                    AsyncHttpClient client = new AsyncHttpClient();
                    StringEntity jsonParams = new StringEntity(jsonArray.toString(), "UTF-8");
                    jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.setTimeout(Constantes.TIMEOUT);
                    RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlSincronizarElemento, jsonParams, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            dialog.setMessage(getText(R.string.dialogo_procesando));
                        }

                        @Override
                        public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                            super.onPreProcessResponse(instance, response);
                            Header[] headers = response.getAllHeaders();
                            for (Header header : headers) {
                                if (header.getName().equalsIgnoreCase("content-length")) {
                                    String value = header.getValue();
                                    contenLenght = Integer.valueOf(value);
                                    Log.d(Constantes.TAG,"contenLenght:"+contenLenght);
                                }
                            }
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            progress = (int)Math.round(((double)bytesWritten/(double)contenLenght)*100);
                            //Log.d(Constantes.TAG,progress+"%");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String respuesta = new String(responseBody);
                            Log.d(Constantes.TAG, respuesta);
                            String log = "";
                            try {
                                JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                Log.d(Constantes.TAG,"JSON-RESPONSE:"+respuesta);
                                JSONArray jsonArrayLog = jsonResponse.getJSONArray("log");
                                ElementoDB elementoDBTemp = new ElementoDB(database);

                                for (int i = 0; i < jsonArrayLog.length(); i++) {

                                    //opcion actualizar registro bd local
                                    if (jsonArrayLog.getJSONObject(i).getBoolean("procesado")) {
                                        elementoDBTemp.actualizarElementoTemporal(
                                                jsonArrayLog.getJSONObject(i).getInt("id_temporal"),
                                                jsonArrayLog.getJSONObject(i).getInt("id_elemento"),
                                                jsonArrayLog.getJSONObject(i).getString("mobiliario_no")
                                                );
                                    }
                                    log = log +  jsonArrayLog.getJSONObject(i).getString("mensaje")+",TempID : "+jsonArrayLog.getJSONObject(i).getInt("id_temporal")+"\n";
                                }


                                view = inflater.inflate(R.layout.dialogo_log, null);
                                msgLogs = view.findViewById(R.id.msg_logs);
                                msgLogs.setText(log);

                                alertDialog.setView(view);
                                alertDialog.setCancelable(false);
                                alertDialog.setTitle(R.string.titulo_alerta);
                                alertDialog.setIcon(R.drawable.icon_problem);
                                alertDialog.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        setButton(true);
                                    }
                                });
                                alertDialog.create().show();

                                dialog.dismiss();

                            }catch (JSONException e){
                                dialog.dismiss();
                                setButton(true);
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                Log.d(Constantes.TAG,"JSON-RESPONSE-ERROR:"+e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            String respuesta = new String(responseBody);
                            dialog.dismiss();
                            setButton(true);
                            Toast.makeText(getApplicationContext(),getText(R.string.alert_error_ejecucion)+ " Código: "+statusCode+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(Constantes.TAG,"JSON-RESPONSE-ERROR:"+respuesta);
                        }

                        @Override
                        public void onUserException(Throwable error) {
                            super.onUserException(error);
                            dialog.dismiss();
                            setButton(true);
                            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(Constantes.TAG,"JSON-RESPONSE-ERROR:"+error.getMessage());
                        }
                    });
                }
            }
        }

        private JSONArray armarJSON() throws JSONException {
            int p = 0;
            JSONArray datos = new JSONArray();

            cursor = elementoDB.consultarTemporales();
            int size = cursor.getCount();
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
                    jsonObject.put("id_usuario", idUsuario);
                    jsonObject.put("id_tipologia", cursor.getInt(cursor.getColumnIndex("id_tipologia")));
                    jsonObject.put("id_mobiliario", cursor.getInt(cursor.getColumnIndex("id_mobiliario")));
                    jsonObject.put("id_referencia", cursor.getInt(cursor.getColumnIndex("id_referencia")));
                    jsonObject.put("id_estado_mobiliario", cursor.getInt(cursor.getColumnIndex("id_estado_mobiliario")));
                    jsonObject.put("id_proceso",idDefaultProceso);
                    jsonObject.put("id_municipio", cursor.getInt(cursor.getColumnIndex("id_municipio")));
                    jsonObject.put("id_barrio", cursor.getInt(cursor.getColumnIndex("id_barrio")));
                    jsonObject.put("longitud", cursor.getFloat(cursor.getColumnIndex("longitud")));
                    jsonObject.put("latitud", cursor.getFloat(cursor.getColumnIndex("latitud")));
                    jsonObject.put("direccion", cursor.getString(cursor.getColumnIndex("direccion")));
                    jsonObject.put("sector", cursor.getString(cursor.getColumnIndex("sector")));
                    jsonObject.put("zona", cursor.getString(cursor.getColumnIndex("zona")));
                    jsonObject.put("id_sentido", cursor.getInt(cursor.getColumnIndex("id_sentido")));
                    jsonObject.put("cantidad", cursor.getInt(cursor.getColumnIndex("cantidad")));
                    jsonObject.put("id_tipo_poste", cursor.getInt(cursor.getColumnIndex("id_tipo_poste")));
                    jsonObject.put("id_tipo_red", cursor.getInt(cursor.getColumnIndex("id_tipo_red")));
                    jsonObject.put("poste_no", cursor.getString(cursor.getColumnIndex("poste_no")));
                    jsonObject.put("id_clase_via", cursor.getInt(cursor.getColumnIndex("id_clase_via")));
                    jsonObject.put("serial_medidor", cursor.getString(cursor.getColumnIndex("serial_medidor")));
                    jsonObject.put("lectura_medidor", cursor.getInt(cursor.getColumnIndex("lectura_medidor")));
                    jsonObject.put("potencia_transformador", cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                    jsonObject.put("placa_mt_transformador", cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                    jsonObject.put("placa_ct_transformador", cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));
                    jsonObject.put("transformador_compartido", cursor.getString(cursor.getColumnIndex("transformador_compartido")));
                    jsonObject.put("id_unidad_medida", cursor.getInt(cursor.getColumnIndex("id_unidad_medida")));
                    jsonObject.put("id_proveedor", cursor.getInt(cursor.getColumnIndex("id_proveedor")));
                    jsonObject.put("id_acta", cursor.getInt(cursor.getColumnIndex("id_acta")));
                    jsonObject.put("tercero", cursor.getString(cursor.getColumnIndex("tercero")));
                    //jsonObject.put("fch_registro", cursor.getString(cursor.getColumnIndex("fch_registro")));
                    jsonObject.put("observacion", cursor.getString(cursor.getColumnIndex("observacion")));
                    jsonObject.put("foto", cursor.getString(cursor.getColumnIndex("foto")));
                    datos.put(jsonObject);
                    progress = (int)Math.round((double)(p+1)/size*100);
                    publishProgress(progress);
                    p++;
                } while (cursor.moveToNext());
            }
            return datos;
        }

        private void setButton(boolean estado){
            btnCancelar.setEnabled(estado);
            btnCancelar.setEnabled(estado);
            if(estado){
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
            }
            else{
                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            }
        }

    }
}
