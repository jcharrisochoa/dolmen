package co.dolmen.sid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Censo;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;

import static java.lang.Integer.parseInt;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CensoCargaDistribucion extends AppCompatActivity {
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

    FloatingActionButton btnGuardar;
    FloatingActionButton btnCancelar;

    ImageButton btnCapturarGPS;
    ImageButton btnBuscarElemento;
    ImageButton btnEditarDireccion;
    ImageButton btnLimpiar;
    ImageButton btnSincronizar;

    Button btnDelFoto1;
    Button btnDelFoto2;

    ImageView foto1;
    ImageView foto2;

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

    TextView viewTransformadorNo;
    TextView viewTipoTransformador;
    TextView viewPotenciaTransformador;
    TextView viewPlacaMT;
    TextView viewPlacaCT;
    TextView viewTransformadorExclusivoAp;
    TextView viewDireccionTransformador;
    //TextView viewPropiedadTransformador;
    //TextView viewCodigoSAI;

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
    private boolean accionarFoto1;
    private boolean accionarFoto2;

    Elemento elemento;
    private CensoDB censoDB;
    private Elemento transformador;

    private String chkSwLuminariaVisible = "S";
    private String chkSwPoseeLuminaria = "S";
    private String zona ="U";
    private String sector = "N";
    private String chkSwMobiliarioBuenEstado = "S";
    private String path;
    private String encodeStringFoto_1 = "";
    private String encodeStringFoto_2 = "";
    private String sincronizado = "S";
    public LocationManager ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo_carga_distribucion);

        conn = new BaseDatos(getApplicationContext());
        database = conn.getReadableDatabase();

        try {
            censoDB = new CensoDB(database);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new CensoCargaDistribucion.miLocalizacion());
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

        Intent i = getIntent();
        transformador = (Elemento)i.getSerializableExtra("transformador");

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
        btnGuardar = findViewById(R.id.fab_guardar);
        btnCancelar = findViewById(R.id.fab_cancelar);
        btnEditarDireccion = findViewById(R.id.btn_editar_direccion);
        btnLimpiar = findViewById(R.id.btn_limpiar);
        btnBuscarElemento = findViewById(R.id.btn_buscar_elemento);
        btnDelFoto1 = findViewById(R.id.btn_borrar_foto_1);
        btnDelFoto2 = findViewById(R.id.btn_borrar_foto_2);


        foto1 = findViewById(R.id.foto_1);
        foto2 = findViewById(R.id.foto_2);

        //--
        viewLatitud = findViewById(R.id.gps_latitud);
        viewLongitud = findViewById(R.id.gps_longitud);
        viewAltitud  = findViewById(R.id.gps_altitud);
        viewPrecision = findViewById(R.id.gps_precision);
        viewDireccion = findViewById(R.id.gps_direccion);
        viewVelocidad  = findViewById(R.id.gps_velocidad);
        //--
        viewTransformadorNo             = findViewById(R.id.txt_transformador_no);
        viewTipoTransformador           = findViewById(R.id.txt_transformador_tipo);
        viewPotenciaTransformador       = findViewById(R.id.txt_potencia_transformador);
        viewPlacaCT                     = findViewById(R.id.txt_ct_transformador);
        viewPlacaMT                     = findViewById(R.id.txt_mt_transformador);
        viewTransformadorExclusivoAp    = findViewById(R.id.txt_transformador_exclusivo_ap);
        viewDireccionTransformador      = findViewById(R.id.txt_direccion_transformador);
        //viewCodigoSAI                   = findViewById(R.id.txt_codigo_sai);

        viewTransformadorNo.setText(transformador.getElemento_no());
        viewTipoTransformador.setText(transformador.getMobiliario().getDescripcionMobiliario());
        viewPotenciaTransformador.setText(transformador.getReferenciaMobiliario().getDescripcionReferenciaMobiliario());
        viewDireccionTransformador.setText(transformador.getDireccion());
        viewPlacaMT.setText(transformador.getPlacaMT());
        viewPlacaCT.setText(transformador.getPlacaCT());
        viewTransformadorExclusivoAp.setText((transformador.isTransformadorExclusivo())?"SI":"NO");

        //--
        txtElementoNo.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resetFrm(true);
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
                Intent i = new Intent(getApplicationContext(), ListaTransformador.class);
                startActivity(i);
                CensoCargaDistribucion.this.finish();
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
                        alert.setMessage(getString(R.string.alert_conexion) +" los datos se guardarán en el dispositivo");
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

        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto1 = true;
                cargarImagen();
            }
        });

        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto2 = true;
                cargarImagen();
            }
        });

        btnDelFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(foto1);
            }
        });
        btnDelFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(foto2);
            }
        });

        cargarTipologia(database);
        cargarBarrio(database);
        cargarEstadoMobiliario(database);
        cargarCensoAsignado(database);
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
               // sincronizar();
                break;
        }
        return true;//super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        InputStream s = CensoCargaDistribucion.this.getContentResolver().openInputStream(selectionPath);
                        bitmap = BitmapFactory.decodeStream(s,null,options);
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        array = stream.toByteArray();
                        encode = Base64.encodeToString(array,0);
                        //Log.d("Path",""+encode);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (accionarFoto1) {
                        foto1.setImageURI(selectionPath);
                        //imgAntfoto1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        //imgAntfoto1.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        encodeStringFoto_1 = encode;
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        foto2.setImageURI(selectionPath);
                        encodeStringFoto_2 = encode;
                        accionarFoto2 = false;
                    }

                    break;
                case Constantes.CONS_TOMAR_FOTO:

                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path, options);

                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    array = stream.toByteArray();

                    if (accionarFoto1) {
                        foto1.setImageBitmap(bitmap);
                        encodeStringFoto_1 = Base64.encodeToString(array, 0);
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        foto2.setImageBitmap(bitmap);
                        encodeStringFoto_2 = Base64.encodeToString(array, 0);
                        accionarFoto2 = false;
                    }

                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            accionarFoto1 = false;
            accionarFoto2 = false;
            Toast.makeText(getApplicationContext(), getText(R.string.alert_cancelar_camara), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_camara), Toast.LENGTH_SHORT).show();
        }

    }

    private void guardarFormulario(char tipoAlmacenamiento, SQLiteDatabase sqLiteDatabase) {
        switch (tipoAlmacenamiento) {
            case 'L':
                sincronizado = "N";
                ProgressDialog progress = new ProgressDialog(CensoCargaDistribucion.this);
                progress.setCancelable(false);
                progress.setTitle(R.string.titulo_alerta);
                progress.setIcon(R.drawable.icon_info);
                progress.setMessage(getString(R.string.almacenando));
                setButton(false);
                progress.show();

                if (almacenarDatosLocal(sqLiteDatabase)) {
                    progress.dismiss();
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
                    progress.dismiss();
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
                break;
            case 'R':
                almacenarDatosEnRemoto();
                break;
        }
    }

    //--
    private void almacenarDatosEnRemoto() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();

        ProgressDialog progress = new ProgressDialog(CensoCargaDistribucion.this);
        progress.setCancelable(false);
        progress.setTitle(R.string.titulo_alerta);
        progress.setIcon(R.drawable.icon_info);
        progress.setMessage(getString(R.string.almacenando));

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
        requestParams.put("id_elemento_transformador", transformador.getId());
        requestParams.put("foto_1", encodeStringFoto_1);
        requestParams.put("foto_2", encodeStringFoto_2);

        Log.d(Constantes.TAG,"=>"+requestParams.toString());
        client.setTimeout(Constantes.TIMEOUT);

        RequestHandle post = client.post(ServicioWeb.urlGuardarCensoCarga, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setButton(false);
                progress.show();
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

                    if(jsonObject.getInt("estado") == 1) {
                        sincronizado = "S";
                        if(almacenarDatosLocal(database)){
                            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                        }
                        resetFrm(true);
                    }

                    Log.d(Constantes.TAG, "statusCode:" + statusCode + ", mensaje:" + mensaje+" ,respuesta:"+respuesta);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(Constantes.TAG,"Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                    Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Servicio Web, Código:" + statusCode, Toast.LENGTH_SHORT).show();
                }
                setButton(true);
                progress.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
                Log.d(Constantes.TAG,"error "+respuesta);
                Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Código: " + statusCode, Toast.LENGTH_LONG).show();
                setButton(true);
                progress.dismiss();
            }
        });
    }

    //--
    private boolean almacenarDatosLocal(SQLiteDatabase sqLiteDatabase) {
        boolean response = true;
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
        censo.setSincronizado((sincronizado.contentEquals("S"))?true:false);
        censo.setElementoTransformador(transformador.getId());

        CensoDB censoDB = new CensoDB(sqLiteDatabase);
        if(censoDB.agregarDatosCensoCarga(censo)){
            CensoArchivoDB censoArchivoDB = new CensoArchivoDB(sqLiteDatabase);
            censoArchivoDB.setId_censo_tecnico(censo.getLastId());
            censoArchivoDB.setArchivo(encodeStringFoto_1);
            censoArchivoDB.agregarDatos(censoArchivoDB);
            censoArchivoDB.setArchivo(encodeStringFoto_2);
            censoArchivoDB.agregarDatos(censoArchivoDB);
            response = true;
        }
        else{
            response = false;
        }

        return response;
        /*if (censoDB.agregarDatosCensoCarga(censo)) {

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
        }*/
    }

    //--
    public void quitarFoto(ImageView imageView){
        imageView.setImageResource(R.drawable.icon_no_photography);
        switch (imageView.getId()){
            case R.id.foto_antes_1:
                encodeStringFoto_1 = "";
                break;
            case R.id.foto_antes_2:
                encodeStringFoto_2 = "";
                break;
        }
    }

    //--
    private void cargarImagen(){
        final CharSequence[] opciones = {getText(R.string.tomar_foto).toString(), getText(R.string.cargar_imagen).toString(), getText(R.string.btn_cancelar)};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(CensoCargaDistribucion.this);
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                int PERMISSIONS_REQUEST_CAMERA = 0;
                switch (i) { //Tomar Foto
                    case 0:
                        if (ContextCompat.checkSelfPermission(CensoCargaDistribucion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoCargaDistribucion.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        }
                        else {
                            if (ContextCompat.checkSelfPermission(CensoCargaDistribucion.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CensoCargaDistribucion.this,
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

                        if (ContextCompat.checkSelfPermission(CensoCargaDistribucion.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoCargaDistribucion.this,
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

        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
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
                                        if(encodeStringFoto_1.isEmpty()) {
                                            alert.setMessage(R.string.alert_censo_foto_1);
                                            return false;
                                        }
                                        else{
                                            if(encodeStringFoto_2.isEmpty()){
                                                alert.setMessage(R.string.alert_censo_foto_2);
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
        encodeStringFoto_1 = "";
        encodeStringFoto_2 = "";
        foto1.setImageResource(R.drawable.imagen_no_disponible);
        foto2.setImageResource(R.drawable.imagen_no_disponible);
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

    //--
    private void setButton(boolean estado){
        //btnSincronizar.setEnabled(estado);
        btnGuardar.setEnabled(estado);
        btnCancelar.setEnabled(estado);
        btnBuscarElemento.setEnabled(estado);
        btnLimpiar.setEnabled(estado);
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

            ActivityCompat.requestPermissions(CensoCargaDistribucion.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

        }
        else {
            if(!gpsListener) {
                //Log.d("respuesta","activo");
                ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new CensoCargaDistribucion.miLocalizacion());
            }
        }
    }

    private class miLocalizacion implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //coordenadaActiva = false;
            float mts =  Math.round(location.getAccuracy()*100)/100;
            float alt =  Math.round(location.getAltitude()*100)/100;
            viewLatitud.setText(String.valueOf(location.getLatitude()));
            viewLongitud.setText(String.valueOf(location.getLongitude()));
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