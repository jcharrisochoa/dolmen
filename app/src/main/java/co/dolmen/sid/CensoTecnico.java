package co.dolmen.sid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.Layout;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Censo;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.ComponenteNormaConstruccionRed;
import co.dolmen.sid.entidad.Contrato;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.NormaConstruccionRed;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.RetenidaPoste;
import co.dolmen.sid.entidad.TipoEstructura;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoTension;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

import static java.lang.Integer.parseInt;

public class CensoTecnico extends AppCompatActivity{

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String nombreMunicipio;
    AlertDialog.Builder alert;
    AlertDialog.Builder alertDireccion;
    AlertDialog.Builder alertBuscarElemento;
    //--
    Spinner sltTipologia;
    Spinner sltMobiliario;
    Spinner sltReferencia;
    Spinner sltBarrio;
    Spinner sltTipoInterseccionA;
    Spinner sltTipoInterseccionB;
    Spinner sltClaseVia;
    Spinner sltTipoPoste;
    Spinner sltNormaConstruccionPoste;
    Spinner sltTipoRetenida;
    Spinner sltTipoRed;
    Spinner sltTipoTension;
    Spinner sltTipoEstructura;
    Spinner sltNormaConstruccionRed;
    Spinner sltCensoAsignado;
    //--
    EditText txtElementoNo;
    EditText txtLatitud;
    EditText txtLongitud;
    EditText txtBuscarElemento;
    EditText txtDireccion;
    EditText txtNumeroInterseccion;
    EditText txtNumeracionA;
    EditText txtNumeracionB;
    EditText txtInterdistancia;
    EditText txtPotenciaTransformador;
    EditText txtMtTransformador;
    EditText txtCtTransformador;
    EditText txtPosteNo;
    EditText txtObservacion;

    //--
    Switch swLuminariaVisible;
    Switch swPoseeLuminaria;
    Switch swPuestaTierra;
    Switch swPosteExclusivoAp;
    //--
    Button btnGuardar;
    Button btnCancelar;
    Button btnTomarFoto1;
    Button btnTomarFoto2;
    Button btnBorrarFoto1;
    Button btnBorrarFoto2;


    ImageButton btnAgregarArmadoRed;
    ImageButton btnEliminarArmado_1;
    ImageButton btnEliminarArmado_2;
    ImageButton btnEliminarArmado_3;
    ImageButton btnCapturarGPS;
    ImageButton btnBuscarElemento;
    ImageButton btnEditarDireccion;
    //--
    ArrayList<DataSpinner> tipologiaList;
    ArrayList<Mobiliario> mobiliarioList;
    ArrayList<ReferenciaMobiliario> referenciaMobiliarioList;
    ArrayList<DataSpinner> barrioList;
    ArrayList<DataSpinner> tipoInterseccionA;
    ArrayList<DataSpinner> tipoInterseccionB;
    ArrayList<DataSpinner> claseViaList;
    ArrayList<DataSpinner> tipoPosteList;
    ArrayList<DataSpinner> normaConstruccionPosteList;
    ArrayList<DataSpinner> tipoRetenidaList;
    ArrayList<DataSpinner> tipoRedList;
    ArrayList<DataSpinner> tipoTensionList;
    ArrayList<DataSpinner> tipoEstructuraList;
    ArrayList<DataSpinner> normaConstruccionRedList;
    ArrayList<DataSpinner> censoAsignadoList;
    ComponenteNormaConstruccionRed componenteNormaConstruccionRed;
    //--
    ImageView imgFoto1;
    ImageView imgFoto2;
    //--
    TextView txt_norma_armado_red_1;
    TextView txt_norma_armado_red_2;
    TextView txt_norma_armado_red_3;
    TextView txtMensajeDireccion;

    private boolean accionarFoto1;
    private boolean accionarFoto2;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;
    private int idCenso;
    private String encodeString;
    private String path;
    private ArrayList<ComponenteNormaConstruccionRed> tipoArmadoList;

    private double latitud;
    private double longitud;
    Elemento elemento;

    String chkSwLuminariaVisible = "S";
    String chkSwPoseeLuminaria = "S";
    String chkSwPuestaTierra = "N";
    String chkSwPosteExclusivoAp = "N";

    //private final String CARPETA_RAIZ="ImagenesCenso/";
    //private final String RUTA_IMAGEN= CARPETA_RAIZ+"Img";

    private FusedLocationProviderClient fusedLocationClient;
    public LocationManager ubicacion;  //


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo_tecnico);

        conn = new BaseDatos(CensoTecnico.this);
        database = conn.getReadableDatabase();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            txtLatitud.setText(String.valueOf(location.getLatitude()));
                            txtLongitud.setText(String.valueOf(location.getLongitude()));
                        } else {
                            txtLatitud.setText("0");
                            txtLongitud.setText("0");
                        }
                    }
                });

        //--
        alert = new AlertDialog.Builder(this);

        //--Datos de la tabla Armado Red--
        tipoArmadoList = new ArrayList<ComponenteNormaConstruccionRed>();
        //--
        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        nombreMunicipio     = config.getString("nombreMunicipio", "");
        idCenso             = config.getInt("id_censo",0);
        //--
        setTitle(getText(R.string.titulo_censo_tecnico) + " (" + nombreMunicipio + ")");
        //--
        sltTipologia                = findViewById(R.id.slt_tipologia);
        sltMobiliario               = findViewById(R.id.slt_mobiliario);
        sltReferencia               = findViewById(R.id.slt_referencia);
        sltBarrio                   = findViewById(R.id.slt_barrio);
        //sltTipoInterseccionA        = findViewById(R.id.slt_tipo_interseccion_a);
        //sltTipoInterseccionB        = findViewById(R.id.slt_tipo_interseccion_b);
        sltClaseVia                 = findViewById(R.id.slt_clase_via);
        sltTipoPoste                = findViewById(R.id.slt_tipo_poste);
        sltNormaConstruccionPoste   = findViewById(R.id.slt_norma_construccion_poste);
        sltTipoRetenida             = findViewById(R.id.slt_tipo_retenida);
        sltTipoRed                  = findViewById(R.id.slt_tipo_red);
        sltTipoTension              = findViewById(R.id.slt_tipo_tension);
        sltTipoEstructura           = findViewById(R.id.slt_tipo_estructura);
        sltNormaConstruccionRed     = findViewById(R.id.slt_norma_construccion_red);
        sltCensoAsignado            = findViewById(R.id.slt_censo_asignado);
        //--
        txtElementoNo               = findViewById(R.id.txt_elemento_no);
        txtLatitud                  = findViewById(R.id.txt_latitud);
        txtLongitud                 = findViewById(R.id.txt_longitud);
        txtBuscarElemento           = findViewById(R.id.txt_buscar_elemento);
        txtDireccion                = findViewById(R.id.txt_direccion);
        txtInterdistancia           = findViewById(R.id.txt_interdistancia);
        txtPotenciaTransformador    = findViewById(R.id.txt_potencia_transformador);
        txtMtTransformador          = findViewById(R.id.txt_mt_transformador);
        txtCtTransformador          = findViewById(R.id.txt_ct_transformador);
        txtPosteNo                  = findViewById(R.id.txt_poste_no);
        txtObservacion              = findViewById(R.id.txt_observacion);
        //--
        swLuminariaVisible          = findViewById(R.id.sw_numero_luminaria_visible);
        swPoseeLuminaria            = findViewById(R.id.sw_tiene_luminaria);
        swPosteExclusivoAp          = findViewById(R.id.sw_poste_exclulsivo_alumbrado_publico);
        swPuestaTierra              = findViewById(R.id.sw_puesta_tierra);
        //--
        btnCapturarGPS              = findViewById(R.id.btn_capturar_gps);
        btnGuardar                  = findViewById(R.id.btn_guardar);
        btnCancelar                 = findViewById(R.id.btn_cancelar);
        btnTomarFoto1               = findViewById(R.id.btn_tomar_foto_1);
        btnTomarFoto2               = findViewById(R.id.btn_tomar_foto_2);
        btnBorrarFoto1              = findViewById(R.id.btn_borrar_foto_1);
        btnBorrarFoto2              = findViewById(R.id.btn_borrar_foto_2);
        btnAgregarArmadoRed         = findViewById(R.id.btn_agregar_armado);
        btnBuscarElemento           = findViewById(R.id.btn_buscar_elemento);
        btnEliminarArmado_1         = findViewById(R.id.btn_eliminar_armado_1);
        btnEliminarArmado_2         = findViewById(R.id.btn_eliminar_armado_2);
        btnEliminarArmado_3         = findViewById(R.id.btn_eliminar_armado_3);
        btnEditarDireccion          = findViewById(R.id.btn_editar_direccion);
        //--
        imgFoto1 = findViewById(R.id.foto_1);
        imgFoto2 = findViewById(R.id.foto_2);
        //--
        txt_norma_armado_red_1      = findViewById(R.id.txt_norma_armado_red_1);
        txt_norma_armado_red_2      = findViewById(R.id.txt_norma_armado_red_2);
        txt_norma_armado_red_3      = findViewById(R.id.txt_norma_armado_red_3);
        //--
        txtElementoNo.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);

        swLuminariaVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwLuminariaVisible = (isChecked)?"S":"N";
            }
        });
        swPoseeLuminaria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPoseeLuminaria = (isChecked)?"S":"N";
            }
        });
        swPuestaTierra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPuestaTierra = (isChecked)?"S":"N";
            }
        });
        swPosteExclusivoAp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPosteExclusivoAp = (isChecked)?"S":"N";
            }
        });
        //swLuminariaVisible.setEnabled(false);
        //swPoseeLuminaria.setEnabled(false);

        btnCapturarGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarCoordenadas();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFormulario()){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                        guardarFormulario('R',database);
                    }
                    else{
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                guardarFormulario('L',database);
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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CensoTecnico.this, Menu.class);
                startActivity(i);
                CensoTecnico.this.finish();
            }
        });
        btnAgregarArmadoRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarArmadoRed()){
                    if(tipoArmadoList.size()< 3){
                        TipoRed tipoRedArmamdo = new TipoRed();
                        tipoRedArmamdo.setId(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
                        tipoRedArmamdo.setDescripcion(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getDescripcion());

                        TipoTension tipoTensionArmado = new TipoTension();
                        tipoTensionArmado.setId(tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId());
                        tipoTensionArmado.setDescripcion(tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getDescripcion());

                        NormaConstruccionRed normaConstruccionRed = new NormaConstruccionRed();

                        TipoEstructura tipoEstructuraArmado =  normaConstruccionRed.getTipoEstructura();
                        tipoEstructuraArmado.setId(tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId());
                        tipoEstructuraArmado.setDescripcion(tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getDescripcion());

                        normaConstruccionRed.setId(normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getId());
                        normaConstruccionRed.setDescripcion(normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getDescripcion());

                        componenteNormaConstruccionRed = new ComponenteNormaConstruccionRed(
                                tipoRedArmamdo,
                                tipoTensionArmado,
                                normaConstruccionRed
                                );
                        tipoArmadoList.add(componenteNormaConstruccionRed);
                    }
                    mostrarTablaArmado();

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
        btnBuscarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarElemento(database);
            }
        });
        btnEliminarArmado_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarItemTablaArmado(0);
            }
        });
        btnEliminarArmado_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarItemTablaArmado(1);
            }
        });
        btnEliminarArmado_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarItemTablaArmado(2);
            }
        });
        btnTomarFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionarFoto1 = true;
                cargarImagen();
            }
        });
        btnTomarFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionarFoto2 = true;
                cargarImagen();
            }
        });
        btnBorrarFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFoto1.setImageResource(R.drawable.imagen_no_disponible);
            }
        });
        btnBorrarFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFoto2.setImageResource(R.drawable.imagen_no_disponible);
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
        sltTipoPoste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionPoste(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sltTipoTension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarTipoEstructura(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sltTipoEstructura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionRed(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cargarTipologia(database);
        cargarBarrio(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTension(database);
        cargarRetenidaPoste(database);
        cargarCensoAsignado(database);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constantes.CONS_SELECCIONAR_IMAGEN:
                    Uri selectionPath = data.getData();
                    if (accionarFoto1) {
                        imgFoto1.setImageURI(selectionPath);
                        accionarFoto1 = false;
                    }
                    if (accionarFoto2) {
                        imgFoto2.setImageURI(selectionPath);
                        accionarFoto2 = false;
                    }
                    break;
                case Constantes.CONS_TOMAR_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (accionarFoto1) {
                        imgFoto1.setImageBitmap(bitmap);
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        imgFoto2.setImageBitmap(bitmap);
                        accionarFoto2 = false;
                    }
                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_cancelar_camara), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_camara), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    //--Administrar Tabla Armado Red----
    private boolean validarArmadoRed(){
        if(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId() == 0){
            alert.setMessage(R.string.alert_tipo_red);
            return false;
        }
        else{
            if(tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId() == 0){
                alert.setMessage(R.string.alert_tipo_tension);
                return false;
            }
            else{
                if(tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId()==0){
                    alert.setMessage(R.string.alert_tipo_estructura);
                    return false;
                }
                else{
                    if(normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getId()==0){
                        alert.setMessage(R.string.alert_norma_construccion_red);
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            }
        }
    }
    //--
    private void mostrarTablaArmado(){
        txt_norma_armado_red_1.setText("-");
        txt_norma_armado_red_2.setText("-");
        txt_norma_armado_red_3.setText("-");
        int index = 0;
        while(index < tipoArmadoList.size()){
            switch (index) {
                case 0:
                    txt_norma_armado_red_1.setText(tipoArmadoList.get(index).getNormaConstruccionRed().getDescripcion());
                    break;
                case 1:
                    txt_norma_armado_red_2.setText(tipoArmadoList.get(index).getNormaConstruccionRed().getDescripcion());
                    break;
                case 2:
                    txt_norma_armado_red_3.setText(tipoArmadoList.get(index).getNormaConstruccionRed().getDescripcion());
                    break;
            }
            index++;
        }
    }
    //--
    private void borrarItemTablaArmado(int index){
        if(tipoArmadoList.size()>index)
            tipoArmadoList.remove(index);
        mostrarTablaArmado();
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
    private void cargarTension(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoTensionList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoTensionDB tipoTensionDB = new TipoTensionDB(sqLiteDatabase);
        Cursor cursor = tipoTensionDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoTensionList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoTensionList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoTension.setAdapter(dataAdapter);
    }
    //--
    private void cargarRetenidaPoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoRetenidaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        RetenidaPosteDB retenidaPosteDB = new RetenidaPosteDB(sqLiteDatabase);
        Cursor cursor = retenidaPosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoRetenidaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoRetenidaList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRetenida.setAdapter(dataAdapter);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltNormaConstruccionPoste.setAdapter(dataAdapter);
    }
    //--
    private void cargarTipoEstructura(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int idTipoTension = tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId();
        tipoEstructuraList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoEstructuraDB tipoEstructuraDB = new TipoEstructuraDB(sqLiteDatabase);
        Cursor cursor = tipoEstructuraDB.consultarTodo(idTipoTension);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoEstructuraList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipoEstructuraList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoEstructura.setAdapter(dataAdapter);

    }
    //--
    private void cargarNormaConstruccionRed(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int idTipoEstructura = tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId();
        normaConstruccionRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionRedDB normaConstruccionRedDB = new NormaConstruccionRedDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionRedDB.consultarTodo(idTipoEstructura);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        normaConstruccionRedList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(3).toUpperCase());
                    normaConstruccionRedList.add(dataSpinner);
                    labels.add(cursor.getString(3).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltNormaConstruccionRed.setAdapter(dataAdapter);
    }
    //--
    private void cargarCensoAsignado(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        censoAsignadoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        CensoAsignadoDB censoAsignadoDB = new CensoAsignadoDB(sqLiteDatabase);
        Cursor cursor = censoAsignadoDB.consultarTodo(idDefaultMunicipio,idDefaultProceso);
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


            alertBuscarElemento = new AlertDialog.Builder(this);
            ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
            Cursor cursor = elementoDB.consultarElemento(idDefaultMunicipio,idDefaultProceso,parseInt(txtBuscarElemento.getText().toString()));
            if(cursor.getCount() == 0) {
                alertBuscarElemento.setTitle(R.string.titulo_alerta);
                alertBuscarElemento.setMessage(getText(R.string.alert_elemento_no_encontrado)+" sobre el Elemento: "+txtBuscarElemento.getText()+". ¿Desea registrar el Elemento?" );
                alertBuscarElemento.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtElementoNo.setText(txtBuscarElemento.getText());
                        elemento = new Elemento();
                        elemento.setId(0);
                        elemento.setElemento_no(txtBuscarElemento.getText().toString());
                        resetFrm(true);
                    }
                });
                alertBuscarElemento.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetFrm(false);
                    }
                });
                alertBuscarElemento.create().show();

            }
            else {

                cursor.moveToFirst();

                elemento = new Elemento();
                elemento.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                elemento.setElemento_no(cursor.getString(cursor.getColumnIndex("elemento_no")));

                txtElementoNo.setEnabled(false);
                txtElementoNo.setText(cursor.getString(cursor.getColumnIndex("elemento_no")));
                txtDireccion.setText(cursor.getString(cursor.getColumnIndex("direccion")));
                //mobiliarioList
                idMobiliarioBusqueda =  cursor.getInt(cursor.getColumnIndex("id_mobiliario"));
                idReferenciaBusqueda =  cursor.getInt(cursor.getColumnIndex("id_referencia"));

                //tipologiaList
                for(int i=0;i<tipologiaList.size();i++){
                    if(tipologiaList.get(i).getId() == cursor.getInt(cursor.getColumnIndex("id_tipologia"))){
                        sltTipologia.setSelection(i);
                    }
                }
                //barrioList
                for(int i=0;i<barrioList.size();i++){
                    if(barrioList.get(i).getId() == cursor.getInt(cursor.getColumnIndex("id_barrio"))){
                        sltBarrio.setSelection(i);
                    }
                }
            }
        }
    }
    //--
    //--Administrar Direccion
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

    //--Validar Formulario para enviar--
    private boolean validarFormulario(){
        boolean swTmp = true;
        if(censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId()==0){
            alert.setMessage(R.string.alert_censo_asignado);
            return false;
        }
        else {
            if (txtElementoNo.getText().toString().isEmpty()) {
                alert.setMessage(R.string.alert_censo_tecnico_elemento_no);
                return false;
            } else {
                if (tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId() == 0) {
                    alert.setMessage(R.string.alert_censo_tipologia);
                    return false;
                } else {
                    if (mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getIdMobiliario() == 0) {
                        alert.setMessage(R.string.alert_censo_mobiliario);
                        return false;
                    } else {
                        if (referenciaMobiliarioList.get(sltReferencia.getSelectedItemPosition()).getIdReferenciaMobiliario() == 0) {
                            alert.setMessage(R.string.alert_censo_mobiliario);
                            return false;
                        } else {
                            if (barrioList.get(sltBarrio.getSelectedItemPosition()).getId() == 0) {
                                alert.setMessage(R.string.alert_censo_referencia);
                                return false;
                            } else {
                                if (txtDireccion.getText().toString().isEmpty()) {
                                    alert.setMessage(R.string.alert_censo_direccion);
                                    return false;
                                } else {
                                    if (claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId() == 0) {
                                        alert.setMessage(R.string.alert_censo_clase_via);
                                        return false;
                                    } else {
                                        if (tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId() == 0) {
                                            alert.setMessage(R.string.alert_censo_tipo_poste);
                                            return false;
                                        } else {
                                            if (txtInterdistancia.getText().toString().isEmpty()) {
                                                alert.setMessage(R.string.alert_censo_interdistancia);
                                                return false;
                                            } else {
                                                //return true;
                                                if (!txtPotenciaTransformador.getText().toString().isEmpty()) {
                                                    if (txtMtTransformador.getText().toString().isEmpty()) {
                                                        alert.setMessage(R.string.alert_censo_mt_transformador);
                                                        swTmp = false;
                                                        return false;
                                                    } else {
                                                        if (txtCtTransformador.getText().toString().isEmpty()) {
                                                            alert.setMessage(R.string.alert_censo_ct_transformador);
                                                            swTmp = false;
                                                            return false;
                                                        }
                                                    }
                                                }

                                                if (swTmp) {
                                                    if (tipoArmadoList.size() == 0) {
                                                        alert.setMessage(R.string.alert_censo_tipo_armado_red);
                                                        return false;
                                                    } else {
                                                        if (imgFoto1.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.imagen_no_disponible).getConstantState())) {
                                                            alert.setMessage(R.string.alert_censo_foto_1);
                                                            return false;
                                                        } else {
                                                            if (imgFoto2.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.imagen_no_disponible).getConstantState())) {
                                                                alert.setMessage(R.string.alert_censo_foto_2);
                                                                return false;
                                                            } else {
                                                                return true;
                                                            }
                                                        }
                                                    }
                                                } else
                                                    return false;

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
    }
    //--
    private void resetFrm(boolean enabled){
        idMobiliarioBusqueda = 0;
        idReferenciaBusqueda = 0;

        txtElementoNo.setEnabled(enabled);

        if(!enabled)
            txtElementoNo.setText("");

        sltTipologia.setSelection(0);
        sltBarrio.setSelection(0);
        sltClaseVia.setSelection(0);
        sltTipoPoste.setSelection(0);
        sltTipoRetenida.setSelection(0);
        sltTipoRed.setSelection(0);
        sltTipoTension.setSelection(0);

        txtDireccion.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtInterdistancia.setText("");
        txtPotenciaTransformador.setText("");
        txtMtTransformador.setText("");
        txtCtTransformador.setText("");
        txtPosteNo.setText("");
        txtObservacion.setText("");

        swLuminariaVisible.setChecked(true);
        swPoseeLuminaria.setChecked(true);
        swPosteExclusivoAp.setChecked(false);
        swPuestaTierra.setChecked(false);

        borrarItemTablaArmado(0);
        borrarItemTablaArmado(1);
        borrarItemTablaArmado(2);

        imgFoto1.setImageResource(R.drawable.imagen_no_disponible);
        imgFoto2.setImageResource(R.drawable.imagen_no_disponible);
        txtBuscarElemento.setFocusable(true);
    }
    //--
    private void guardarFormulario(char tipoAlmacenamiento,SQLiteDatabase sqLiteDatabase){
        switch (tipoAlmacenamiento){
            case 'L':
                almacenarDatosLocal(sqLiteDatabase);
                break;
            case 'R':
                almacenarDatosEnRemoto(sqLiteDatabase);
                break;
        }
    }
    //--
    private void almacenarDatosLocal(SQLiteDatabase sqLiteDatabase){
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

        Contrato contrato = new Contrato();
        contrato.setId(idDefaultContrato);

        elemento.setDireccion(txtDireccion.getText().toString());
        elemento.setBarrio(barrio);
        elemento.setTipologia(tipologia);
        elemento.setMobiliario(mobiliario);
        elemento.setReferenciaMobiliario(referenciaMobiliario);
        elemento.setContrato(contrato);


        TipoPoste tipoPoste = new TipoPoste();
        tipoPoste.setId(tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId());
        tipoPoste.setDescripcion(tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getDescripcion());

        NormaConstruccionPoste normaConstruccionPoste = new NormaConstruccionPoste();
        normaConstruccionPoste.setId(normaConstruccionPosteList.get(sltNormaConstruccionPoste.getSelectedItemPosition()).getId());
        normaConstruccionPoste.setDescripcion(normaConstruccionPosteList.get(sltNormaConstruccionPoste.getSelectedItemPosition()).getDescripcion());
        normaConstruccionPoste.setTipoPoste(tipoPoste);

        RetenidaPoste retenidaPoste = new RetenidaPoste();
        retenidaPoste.setId(tipoRetenidaList.get(sltTipoRetenida.getSelectedItemPosition()).getId());
        retenidaPoste.setDescripcion(tipoRetenidaList.get(sltTipoRetenida.getSelectedItemPosition()).getDescripcion());

        ClaseVia claseVia = new ClaseVia();
        claseVia.setId(claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId());
        claseVia.setAbreviatura(claseViaList.get(sltClaseVia.getSelectedItemPosition()).getDescripcion());

        TipoRed tipoRed = new TipoRed();
        tipoRed.setId(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
        tipoRed.setDescripcion(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getDescripcion());

        Censo censo = new Censo();
        censo.setId_censo(censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId());
        censo.setElemento(elemento);
        censo.setClaseVia(claseVia);
        censo.setTipoRed(tipoRed);
        //Poste
        censo.setRetenidaPoste(retenidaPoste);
        censo.setInterdistancia(Integer.parseInt(txtInterdistancia.getText().toString()));
        censo.setPosteNo(txtPosteNo.getText().toString());
        censo.setNormaConstruccionPoste(normaConstruccionPoste);
        //Transformador
        Double potencia = (txtPotenciaTransformador.getText().toString().isEmpty())?0.0:Double.parseDouble(txtPotenciaTransformador.getText().toString());
        //--
        censo.setPotenciaTransformador(potencia);
        censo.setPlacaCtTransformador(txtCtTransformador.getText().toString());
        censo.setPlacaMtTransformador(txtMtTransformador.getText().toString());
        //--

        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
        censo.setFchRegistro(fechaHora);

        Float latitud = (txtLatitud.getText().toString().isEmpty())?0:Float.parseFloat(txtLatitud.getText().toString());
        Float longitud = (txtLongitud.getText().toString().isEmpty())?0:Float.parseFloat(txtLongitud.getText().toString());
        censo.setLatitud(latitud);
        censo.setLongitud(longitud);
        censo.setChkSwLuminariaVisible(chkSwLuminariaVisible);
        censo.setChkSwPoseeLuminaria(chkSwPoseeLuminaria);
        censo.setChkSwPuestaTierra(chkSwPuestaTierra);
        censo.setChkSwPosteExclusivoAp(chkSwPosteExclusivoAp);
        censo.setObservacion(txtObservacion.getText().toString());

        CensoDB censoDB = new CensoDB(sqLiteDatabase);
        if(censoDB.agregarDatos(censo)){
            resetFrm(false);
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_almacenamiento_local);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
        else{
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_error_almacenando_datos);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
    }
    //--
    private void almacenarDatosEnRemoto(SQLiteDatabase sqLiteDatabase){
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();

        requestParams.put("id_usuario",idUsuario);
       /* requestParams.put("id_municipio",idMunicipio);
        requestParams.put("id_contrato",idContrato);
        requestParams.put("id_proceso",idProceso);
        requestParams.put("id_acta",actaList.get(sltActaContrato.getSelectedItemPosition()).getId());
        requestParams.put("id_barrio",barrioList.get(sltBarrio.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_via",tipoViaList.get(sltCruce.getSelectedItemPosition()).getId());
        requestParams.put("cruce_no",txtCruceno.getText());
        requestParams.put("placa_a",txtPlaca_a_no.getText());
        requestParams.put("placa_b",txtPlaca_b_no.getText());
        requestParams.put("latitud",txtLatitud.getText());
        requestParams.put("longitud",txtLongitud.getText());
        requestParams.put("id_tipologia",tipologiaList.get(sltTipologia.getSelectedItemPosition()).getId());
        requestParams.put("id_mobiliario",mobiliarioList.get(sltMobiliario.getSelectedItemPosition()).getId());
        requestParams.put("id_referencia",referenciaList.get(sltReferencia.getSelectedItemPosition()).getId());
        requestParams.put("id_sentido",sentidoList.get(sltSentido.getSelectedItemPosition()).getId());
        requestParams.put("tercero",tercero);
        requestParams.put("id_proveedor",proveedorList.get(sltProveedor.getSelectedItemPosition()).getId());
        requestParams.put("cantidad",txtCantidad.getText());
        requestParams.put("id_unidad",unidadList.get(sltUnidad.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_poste",tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId());
        requestParams.put("poste_no",txtPosteno.getText());
        requestParams.put("id_tipo_red",tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
        requestParams.put("transformador",txtTransformador.getText());
        requestParams.put("potencia_transformador",txtPotenciaTransformador.getText());
        requestParams.put("serial_medidor",txtSerialMedidor.getText());
        requestParams.put("lectura_medidor",txtLecturaMedidor.getText());
        requestParams.put("id_clase_via",claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_via",tipoViaList.get(sltCruce.getSelectedItemPosition()).getId());
        requestParams.put("id_estado",estadoMobiliarioList.get(sltEstadoMobiliario.getSelectedItemPosition()).getId());
        requestParams.put("observacion",txtObservacion.getText());
        requestParams.put("conexion_electrica",conexionElectrica);
        requestParams.put("encode_string",encodeString);*/
        client.setTimeout(Constantes.TIMEOUT);

        Log.d("params:",ServicioWeb.urlGuardarCensoTecnico+"?"+requestParams);
        RequestHandle post = client.post(ServicioWeb.urlGuardarCensoTecnico, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPreProcessResponse(instance, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    //--Administrar Cámara--
    public void cargarImagen() {
        final CharSequence[] opciones = {getText(R.string.tomar_foto).toString(), getText(R.string.cargar_imagen).toString(), getText(R.string.btn_cancelar)};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(CensoTecnico.this);
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) { //Tomar Foto
                    case 0:
                        int PERMISSIONS_REQUEST_CAMERA = 0;
                        if (ContextCompat.checkSelfPermission(CensoTecnico.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoTecnico.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            tomarFoto();
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen
                        int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                        if (ContextCompat.checkSelfPermission(CensoTecnico.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoTecnico.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
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

        Uri photoURI = FileProvider.getUriForFile(CensoTecnico.this, getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
    }

    //--Administracion del GPS
    public boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return (ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }
    private void tomarCoordenadas() {
        int PERMISSIONS_REQUEST_LOCATION = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CensoTecnico.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            if (!estadoGPS()) {
                alert.setTitle(getString(R.string.titulo_alerta));
                alert.setMessage(getString(R.string.alert_gps_deshabilitado));
                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            } else {
               /*fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    txtLatitud.setText(String.valueOf(location.getLatitude()));
                                    txtLongitud.setText(String.valueOf(location.getLongitude()));
                                } else {
                                    txtLatitud.setText("0");
                                    txtLongitud.setText("0");
                                }
                            }
                        });*/
            }
        }
    }
}