package co.dolmen.sid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.location.LocationProvider;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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
import android.widget.CheckBox;
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
import com.loopj.android.http.ResponseHandlerInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Calibre;
import co.dolmen.sid.entidad.Censo;
import co.dolmen.sid.entidad.CensoTipoArmado;
import co.dolmen.sid.entidad.ClasePerfil;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.ComponenteNormaConstruccionRed;
import co.dolmen.sid.entidad.Contrato;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.FabricanteElemento;
import co.dolmen.sid.entidad.FabricantePoste;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.NormaConstruccionRed;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.RetenidaPoste;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.entidad.TipoEscenario;
import co.dolmen.sid.entidad.TipoEstructura;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoTension;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ClasePerfilDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ComercializadorDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.FabricanteElementoDB;
import co.dolmen.sid.modelo.FabricantePosteDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static java.lang.Integer.parseInt;

public class CensoTecnico extends AppCompatActivity {

    JSONObject json;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String nombreMunicipio;
    AlertDialog.Builder alert;
    AlertDialog.Builder alertDireccion;
    AlertDialog.Builder alertBuscarElemento;
    ProgressDialog dialog;
    //--
    Spinner sltTipologia;
    Spinner sltMobiliario;
    Spinner sltReferencia;
    Spinner sltEstadoMobiliario;
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
    Spinner sltTipoEscenario;
    Spinner sltCalibreConexionElemento;
    Spinner sltCalibreTipoArmado;
    Spinner sltFabricanteElemento;
    Spinner sltFabricantePoste;
    Spinner sltClasePerfil;
    Spinner sltTipoBrazo;
    Spinner sltComercializador;

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
    EditText txtAnchoVia;
    EditText txtAnden_a;
    EditText txtAnden_b;
    EditText txtAvanceAnden;
    EditText txtInterdistanciaAnterior;
    EditText txtMobiliarioAnterior;
    EditText txtMobiliarioSiguiente;

    //--
    Switch swLuminariaVisible;
    Switch swPoseeLuminaria;
    Switch swPuestaTierra;
    Switch swPosteExclusivoAp;
    Switch swPosteBuenEstado;
    Switch swMobiliarioBuenEstado;
    Switch swTranformadorExclusivoAP;
    //--
    FloatingActionButton btnGuardar;
    FloatingActionButton btnCancelar;
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
    ImageButton btnLimpiar;
    //--
    ArrayList<DataSpinner> tipologiaList;
    ArrayList<Mobiliario> mobiliarioList;
    ArrayList<ReferenciaMobiliario> referenciaMobiliarioList;
    ArrayList<DataSpinner> estadoMobiliarioList;
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
    ArrayList<DataSpinner> tipoEscenarioList;
    ArrayList<DataSpinner> calibreList;
    ArrayList<DataSpinner> fabricanteElementoList;
    ArrayList<DataSpinner> fabricantePosteList;
    ArrayList<DataSpinner> clasePerfilList;
    ArrayList<DataSpinner> tipoBrazoList;
    ArrayList<DataSpinner> comercializadorList;

    ComponenteNormaConstruccionRed componenteNormaConstruccionRed;
    //--
    ImageView imgFoto1;
    ImageView imgFoto2;
    //--
    TextView txt_norma_armado_red_1;
    TextView txt_norma_armado_red_2;
    TextView txt_norma_armado_red_3;
    TextView txtMensajeDireccion;

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
    RadioButton rdTransformadorPrivado;
    RadioButton rdTransformadorPublico;
    RadioButton rdTransformadorNoAplica;

    CheckBox chkBrazoMalEstado;
    CheckBox chkVisorMalEstado;
    CheckBox chkMobiliarioMalPosicionado;
    CheckBox chkMobiliarioObsoleto;
    CheckBox chkSinBombillo;

    private boolean accionarFoto1;
    private boolean accionarFoto2;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;
    private int idCenso;
    private int idTipoEscenario;
    private  int contenLenght =  0;
    private String encodeStringFoto_1;
    private String encodeStringFoto_2;
    private String path;

    private ArrayList<ComponenteNormaConstruccionRed> tipoArmadoList;
    private boolean gpsListener;
    private ProgressBar progressBarGuardarCenso;

    Elemento elemento;


    private String chkSwLuminariaVisible = "S";
    private String chkSwPoseeLuminaria = "S";
    private String chkSwPuestaTierra = "N";
    private String chkSwPosteExclusivoAp = "N";
    private String chkSwPosteBuenEstado = "S";
    private String chkSwMobiliarioBuenEstado = "S";
    private String zona ="U";
    private String sector = "N";
    private String tipoPropietarioTranformador = "NA";
    private String brazoMalEstado = "N";
    private String visorMalEstado = "N";
    private String sinBombillo = "N";
    private String mobiliarioObsoleto = "N";
    private String mobiliarioMalPosicionado = "N";
    private String chkSwTransformadorExclusivoAp = "N";

    //private final String CARPETA_RAIZ="ImagenesCenso/";
    //private final String RUTA_IMAGEN= CARPETA_RAIZ+"Img";

    public LocationManager ubicacion;  //


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo_tecnico);


        conn = new BaseDatos(CensoTecnico.this);
        database = conn.getReadableDatabase();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constantes.MIN_UPDATE_TIME, Constantes.MIN_UPDATE_DISTANCE, new miLocalizacion());
            gpsListener  = true;
        }

        //--
        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setIcon(android.R.drawable.ic_dialog_alert);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.titulo_alerta);
        dialog.setIcon(R.drawable.icon_info);

        //--Datos de la tabla Armado Red--
        tipoArmadoList = new ArrayList<ComponenteNormaConstruccionRed>();
        //--
        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        idCenso = config.getInt("id_censo", 0);
        //--
        setTitle(getText(R.string.titulo_censo_tecnico) + " (" + nombreMunicipio + ")");
        //--
        sltTipologia = findViewById(R.id.slt_tipologia);
        sltMobiliario = findViewById(R.id.slt_mobiliario);
        sltReferencia = findViewById(R.id.slt_referencia);
        sltEstadoMobiliario = findViewById(R.id.slt_estado_mobiliario);
        sltBarrio = findViewById(R.id.slt_barrio);
        //sltTipoInterseccionA        = findViewById(R.id.slt_tipo_interseccion_a);
        //sltTipoInterseccionB        = findViewById(R.id.slt_tipo_interseccion_b);
        sltClaseVia = findViewById(R.id.slt_clase_via);
        sltTipoPoste = findViewById(R.id.slt_tipo_poste);
        sltNormaConstruccionPoste = findViewById(R.id.slt_norma_construccion_poste);
        sltTipoRetenida = findViewById(R.id.slt_tipo_retenida);
        sltTipoRed = findViewById(R.id.slt_tipo_red);
        sltTipoTension = findViewById(R.id.slt_tipo_tension);
        sltTipoEstructura = findViewById(R.id.slt_tipo_estructura);
        sltNormaConstruccionRed = findViewById(R.id.slt_norma_construccion_red);
        sltCensoAsignado = findViewById(R.id.slt_censo_asignado);
        sltTipoEscenario = findViewById(R.id.slt_tipo_escenario);
        sltCalibreConexionElemento = findViewById(R.id.slt_calibre_conexion_elemento);
        sltCalibreTipoArmado        = findViewById(R.id.slt_calibre_conductor_armado);
        sltFabricanteElemento = findViewById(R.id.slt_fabricante_elemento);
        sltFabricantePoste = findViewById(R.id.slt_fabricante_poste);
        sltClasePerfil = findViewById(R.id.slt_clase_perfil);
        sltTipoBrazo    = findViewById(R.id.slt_tipo_brazo);
        sltComercializador  = findViewById(R.id.slt_comercializador);
        //--
        txtElementoNo = findViewById(R.id.txt_elemento_no);
        txtLatitud = findViewById(R.id.txt_latitud);
        txtLongitud = findViewById(R.id.txt_longitud);
        txtBuscarElemento = findViewById(R.id.txt_buscar_elemento);
        txtDireccion = findViewById(R.id.txt_direccion);
        txtInterdistancia = findViewById(R.id.txt_interdistancia);
        txtPotenciaTransformador = findViewById(R.id.txt_potencia_transformador);
        txtMtTransformador = findViewById(R.id.txt_mt_transformador);
        txtCtTransformador = findViewById(R.id.txt_ct_transformador);
        txtPosteNo = findViewById(R.id.txt_poste_no);
        txtObservacion = findViewById(R.id.txt_observacion);
        txtAnchoVia     = findViewById(R.id.txt_ancho_via);
        txtInterdistanciaAnterior   = findViewById(R.id.txt_interdistancia_anterior);
        txtAnden_a = findViewById(R.id.txt_anden_a);
        txtAnden_b = findViewById(R.id.txt_anden_b);
        txtAvanceAnden = findViewById(R.id.txt_avance_anden);
        txtMobiliarioAnterior   = findViewById(R.id.txt_mobiliario_no_anterior);
        txtMobiliarioSiguiente  = findViewById(R.id.txt_mobiliario_no_siguiente);
        //--
        swLuminariaVisible = findViewById(R.id.sw_numero_luminaria_visible);
        swPoseeLuminaria = findViewById(R.id.sw_tiene_luminaria);
        swPosteExclusivoAp = findViewById(R.id.sw_poste_exclulsivo_alumbrado_publico);
        swPuestaTierra = findViewById(R.id.sw_puesta_tierra);
        swPosteBuenEstado = findViewById(R.id.sw_poste_en_buen_estado);
        swMobiliarioBuenEstado = findViewById(R.id.sw_mobiliario_en_buenas_condiciones);
        swTranformadorExclusivoAP = findViewById(R.id.sw_transformador_exclusivo_ap);
        //--
        rdZonaUrbano        = findViewById(R.id.rd_urbano);
        rdZonaRural         = findViewById(R.id.rd_rutal);
        rdSectorNormal      = findViewById(R.id.rd_normal);
        rdSectorSubNormal   = findViewById(R.id.rd_subnormal);
        rdTransformadorPrivado = findViewById(R.id.rd_transformador_privado);
        rdTransformadorPublico = findViewById(R.id.rd_transformador_publico);
        rdTransformadorNoAplica = findViewById(R.id.rd_transformador_no_aplica);
        //--
        chkBrazoMalEstado           = findViewById(R.id.chk_brazo_mal_estado);
        chkVisorMalEstado           = findViewById(R.id.chk_visor_mal_estado);
        chkMobiliarioMalPosicionado = findViewById(R.id.chk_mobiliario_mal_posicionado);
        chkMobiliarioObsoleto       = findViewById(R.id.chk_mobiliario_obsoleto);
        chkSinBombillo              = findViewById(R.id.chk_sin_bombillo);
        //-
        btnCapturarGPS = findViewById(R.id.btn_capturar_gps);
        btnGuardar = findViewById(R.id.fab_guardar);
        btnCancelar = findViewById(R.id.fab_cancelar);
        btnTomarFoto1 = findViewById(R.id.btn_tomar_foto_1);
        btnTomarFoto2 = findViewById(R.id.btn_tomar_foto_2);
        btnBorrarFoto1 = findViewById(R.id.btn_borrar_foto_1);
        btnBorrarFoto2 = findViewById(R.id.btn_borrar_foto_2);
        btnAgregarArmadoRed = findViewById(R.id.btn_agregar_armado);
        btnBuscarElemento = findViewById(R.id.btn_buscar_elemento);
        btnEliminarArmado_1 = findViewById(R.id.btn_eliminar_armado_1);
        btnEliminarArmado_2 = findViewById(R.id.btn_eliminar_armado_2);
        btnEliminarArmado_3 = findViewById(R.id.btn_eliminar_armado_3);
        btnEditarDireccion = findViewById(R.id.btn_editar_direccion);
        btnLimpiar = findViewById(R.id.btn_limpiar);
        //--
        imgFoto1 = findViewById(R.id.foto_1);
        imgFoto2 = findViewById(R.id.foto_2);
        //--
        txt_norma_armado_red_1 = findViewById(R.id.txt_norma_armado_red_1);
        txt_norma_armado_red_2 = findViewById(R.id.txt_norma_armado_red_2);
        txt_norma_armado_red_3 = findViewById(R.id.txt_norma_armado_red_3);
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
        swPosteBuenEstado.setChecked(true);

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
        swPuestaTierra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPuestaTierra = (isChecked) ? "S" : "N";
            }
        });
        swPosteExclusivoAp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPosteExclusivoAp = (isChecked) ? "S" : "N";
            }
        });
        swPosteBuenEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwPosteBuenEstado = (isChecked)?"S":"N";
            }
        });
        swMobiliarioBuenEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwMobiliarioBuenEstado = (isChecked)?"S":"N";
            }
        });

        swTranformadorExclusivoAP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                chkSwTransformadorExclusivoAp = (isChecked) ? "S" : "N";
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
        //--
        chkBrazoMalEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                brazoMalEstado = (isChecked) ? "S":"N";
            }
        });
        chkVisorMalEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                visorMalEstado = (isChecked) ? "S":"N";
            }
        });
        chkMobiliarioMalPosicionado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mobiliarioMalPosicionado = (isChecked) ? "S":"N";
            }
        });
        chkMobiliarioObsoleto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mobiliarioObsoleto = (isChecked) ? "S":"N";
            }
        });
        chkSinBombillo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                sinBombillo = (isChecked) ? "S":"N";
            }
        });

        //--
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
                if (validarFormulario()) {
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        //Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.close();
                //Intent i = new Intent(CensoTecnico.this, SubMenuCensoTecnico.class);
                Intent i = new Intent(CensoTecnico.this, Menu.class);
                startActivity(i);
                CensoTecnico.this.finish();
            }
        });
        btnAgregarArmadoRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sw = true;
                if (validarArmadoRed()) {
                    if (tipoArmadoList.size() < 3) {
                        //--Consultar si el tipo de armado ya existe en el array
                        int index = 0;
                        while (index < tipoArmadoList.size()) {
                            if (tipoArmadoList.get(index).getTipoRed().getId() == tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId()
                                    &&
                                    tipoArmadoList.get(index).getNormaConstruccionRed().getId() == normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getId()
                            ) {
                                sw = false;
                                break;
                            }
                            index++;
                        }
                        if (sw){
                            TipoRed tipoRedArmamdo = new TipoRed();
                            tipoRedArmamdo.setId(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
                            tipoRedArmamdo.setDescripcion(tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getDescripcion());

                            TipoTension tipoTensionArmado = new TipoTension();
                            tipoTensionArmado.setId(tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId());
                            tipoTensionArmado.setDescripcion(tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getDescripcion());

                            Calibre calibre = new Calibre();
                            calibre.setId_calibre(calibreList.get(sltCalibreTipoArmado.getSelectedItemPosition()).getId());
                            calibre.setDescripcion(calibreList.get(sltCalibreTipoArmado.getSelectedItemPosition()).getDescripcion());

                            NormaConstruccionRed normaConstruccionRed = new NormaConstruccionRed();

                            TipoEstructura tipoEstructuraArmado = normaConstruccionRed.getTipoEstructura();
                            tipoEstructuraArmado.setId(tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId());
                            tipoEstructuraArmado.setDescripcion(tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getDescripcion());

                            normaConstruccionRed.setId(normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getId());
                            normaConstruccionRed.setDescripcion(normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getDescripcion());

                            componenteNormaConstruccionRed = new ComponenteNormaConstruccionRed(
                                    tipoRedArmamdo,
                                    tipoTensionArmado,
                                    calibre,
                                    normaConstruccionRed
                            );
                            tipoArmadoList.add(componenteNormaConstruccionRed);
                        }
                    }
                    mostrarTablaArmado();

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
                Log.d("Mobiliario", "selected");
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

        sltComercializador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarNormaConstruccionRed(database);
                //Log.d(Constantes.TAG,"hola");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cargarTipologia(database);
        cargarBarrio(database);
        cargarEstadoMobiliario(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTension(database);
        cargarRetenidaPoste(database);
        cargarCensoAsignado(database);
        cargarTipoEscenario(database);
        cargarCalibre(database);
        cargarFabricanteElemento(database);
        cargarFabricantePoste(database);
        cargarClasePerfil(database);
        cargarTipoBrazo(database);
        cargarComercializador(database);
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
                        bitmap = BitmapFactory.decodeStream(s, null, options);
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        array = stream.toByteArray();
                        encode = Base64.encodeToString(array, 0);
                        //Log.d("Path",""+encode);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (accionarFoto1) {
                        imgFoto1.setImageURI(selectionPath);
                        encodeStringFoto_1 = encode;
                        accionarFoto1 = false;
                    }
                    if (accionarFoto2) {
                        imgFoto2.setImageURI(selectionPath);
                        encodeStringFoto_2 = encode;
                        accionarFoto2 = false;
                    }
                    break;
                case Constantes.CONS_TOMAR_FOTO:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path, options);

                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    array = stream.toByteArray();

                    if (accionarFoto1) {
                        imgFoto1.setImageBitmap(bitmap);
                        encodeStringFoto_1 = Base64.encodeToString(array, 0);
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        imgFoto2.setImageBitmap(bitmap);
                        encodeStringFoto_2 = Base64.encodeToString(array, 0);
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
    private boolean validarArmadoRed() {
        if (comercializadorList.get(sltComercializador.getSelectedItemPosition()).getId() == 0) {
            alert.setMessage(R.string.alert_comercializador);
            return false;
        } else {
            if (tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId() == 0) {
                alert.setMessage(R.string.alert_tipo_red);
                return false;
            } else {
                if (tipoTensionList.get(sltTipoTension.getSelectedItemPosition()).getId() == 0) {
                    alert.setMessage(R.string.alert_tipo_tension);
                    return false;
                } else {
                    if (calibreList.get(sltCalibreTipoArmado.getSelectedItemPosition()).getId() == 0) {
                        alert.setMessage(R.string.alert_tipo_calibre);
                        return false;
                    } else {
                        if (tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId() == 0) {
                            alert.setMessage(R.string.alert_tipo_estructura);
                            return false;
                        } else {
                            if (normaConstruccionRedList.get(sltNormaConstruccionRed.getSelectedItemPosition()).getId() == 0) {
                                alert.setMessage(R.string.alert_norma_construccion_red);
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

    //--
    private void mostrarTablaArmado() {
        txt_norma_armado_red_1.setText("-");
        txt_norma_armado_red_2.setText("-");
        txt_norma_armado_red_3.setText("-");
        int index = 0;
        while (index < tipoArmadoList.size()) {
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
    private void borrarItemTablaArmado(int index) {
        if (tipoArmadoList.size() > index)
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
    private void cargarFabricanteElemento(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        fabricanteElementoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        FabricanteElementoDB fabricanteElementoDB = new FabricanteElementoDB(sqLiteDatabase);
        Cursor cursor = fabricanteElementoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        fabricanteElementoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    fabricanteElementoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltFabricanteElemento.setAdapter(dataAdapter);
    }

    //--
    private void cargarFabricantePoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        fabricantePosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        FabricantePosteDB fabricantePosteDB = new FabricantePosteDB(sqLiteDatabase);
        Cursor cursor = fabricantePosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        fabricantePosteList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    fabricantePosteList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltFabricantePoste.setAdapter(dataAdapter);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoEscenario.setAdapter(dataAdapter);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltClaseVia.setAdapter(dataAdapter);
    }

    //--
    private void cargarClasePerfil(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        clasePerfilList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ClasePerfilDB clasePerfilDB = new ClasePerfilDB(sqLiteDatabase);
        Cursor cursor = clasePerfilDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        clasePerfilList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    clasePerfilList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltClasePerfil.setAdapter(dataAdapter);
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
    private void cargarComercializador(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        comercializadorList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ComercializadorDB comercializadorDB = new ComercializadorDB(sqLiteDatabase);
        Cursor cursor = comercializadorDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        comercializadorList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    comercializadorList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltComercializador.setAdapter(dataAdapter);
    }

    //--
    private void cargarNormaConstruccionRed(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int idTipoEstructura = (tipoEstructuraList == null)?0:tipoEstructuraList.get(sltTipoEstructura.getSelectedItemPosition()).getId();
        int idComercializador = (comercializadorList == null)?0:comercializadorList.get(sltComercializador.getSelectedItemPosition()).getId();
        normaConstruccionRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionRedDB normaConstruccionRedDB = new NormaConstruccionRedDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionRedDB.consultarTodo(idTipoEstructura,idComercializador);
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
        Cursor cursor = censoAsignadoDB.consultarTodo(idDefaultMunicipio, idDefaultProceso,"T");
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltCalibreConexionElemento.setAdapter(dataAdapter);
        sltCalibreTipoArmado.setAdapter(dataAdapter);
    }

    //--
    private void cargarTipoBrazo(SQLiteDatabase sqLiteDatabase){
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoBrazo.setAdapter(dataAdapter);
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
            try {
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
                    if (cursorElemento.getCount() > 1) {
                        alertBuscarElemento.setMessage("Existe mas de un elemento con el mismo número,Seleccione");
                        alertBuscarElemento.setNeutralButton("Aceptar", null);
                        alertBuscarElemento.create().show();
                    } else {
                        alertBuscarElemento.setMessage("Elemento "+txtBuscarElemento.getText() +" Encontrado!!");
                        alertBuscarElemento.setNeutralButton("Aceptar", null);
                        alertBuscarElemento.create().show();
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
            }catch (SQLException e){
               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    //--
    //--Administrar Direccion
    private void armarDireccion() {
        alertDireccion = new AlertDialog.Builder(this);
        alertDireccion.setTitle(R.string.titulo_direccion);
        alertDireccion.setCancelable(false);

        View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.direccion, null);
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

    //--Validar Formulario para enviar--
    private boolean validarFormulario() {
        boolean swTmp = true;
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
                            if (fabricanteElementoList.get(sltFabricanteElemento.getSelectedItemPosition()).getId()==0 && swPoseeLuminaria.isChecked()) {
                                alert.setMessage(R.string.alert_fabricante_elemento);
                                return false;
                            }
                            else {
                                if (tipoBrazoList.get(sltTipoBrazo.getSelectedItemPosition()).getId()==0 && swPoseeLuminaria.isChecked()) {
                                    alert.setMessage(R.string.alert_tipo_brazo);
                                    return false;
                                }
                                else {
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
                                                if (claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId() == 0) {
                                                    alert.setMessage(R.string.alert_censo_clase_via);
                                                    return false;
                                                } else {
                                                    if (clasePerfilList.get(sltClasePerfil.getSelectedItemPosition()).getId() == 0) {
                                                        alert.setMessage(R.string.alert_clase_perfil);
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
        sltClaseVia.setSelection(0);
        sltTipoPoste.setSelection(0);
        sltTipoRetenida.setSelection(0);
        sltTipoRed.setSelection(0);
        sltTipoTension.setSelection(0);
        sltCalibreConexionElemento.setSelection(0);
        sltCalibreTipoArmado.setSelection(0);
        sltClasePerfil.setSelection(0);
        sltFabricantePoste.setSelection(0);
        sltFabricanteElemento.setSelection(0);
        sltTipoEscenario.setSelection(0);
        sltTipoBrazo.setSelection(0);

        txtDireccion.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtInterdistancia.setText("");
        txtInterdistanciaAnterior.setText("");
        //txtPotenciaTransformador.setText("");
        //txtMtTransformador.setText("");
        //txtCtTransformador.setText("");
        txtPosteNo.setText("");
        txtObservacion.setText("");
        txtAnchoVia.setText("");
        txtAnden_a.setText("");
        txtAnden_b.setText("");
        txtAvanceAnden.setText("");
        txtInterdistancia.setText("");
        txtMobiliarioAnterior.setText("");
        txtMobiliarioSiguiente.setText("");

        swLuminariaVisible.setChecked(true);
        swPoseeLuminaria.setChecked(true);
        swPosteExclusivoAp.setChecked(false);
        swPuestaTierra.setChecked(false);
        swPosteBuenEstado.setChecked(true);
        swMobiliarioBuenEstado.setChecked(true);
        swPosteBuenEstado.setChecked(true);
        swTranformadorExclusivoAP.setChecked(false);

        chkBrazoMalEstado.setChecked(false);
        chkMobiliarioMalPosicionado.setChecked(false);
        chkMobiliarioObsoleto.setChecked(false);
        chkSinBombillo.setChecked(false);
        chkVisorMalEstado.setChecked(false);

        rdTransformadorPrivado.setChecked(false);
        rdTransformadorPublico.setChecked(false);
        rdTransformadorNoAplica.setChecked(true);

        borrarItemTablaArmado(0);
        borrarItemTablaArmado(1);
        borrarItemTablaArmado(2);

        imgFoto1.setImageResource(R.drawable.imagen_no_disponible);
        imgFoto2.setImageResource(R.drawable.imagen_no_disponible);
        txtBuscarElemento.setFocusable(true);
        txtBuscarElemento.setText("");
        tipoArmadoList.clear();
        mostrarTablaArmado();

        brazoMalEstado = "N";
        visorMalEstado = "N";
        sinBombillo = "N";
        mobiliarioObsoleto = "N";
        mobiliarioMalPosicionado = "N";
        chkSwLuminariaVisible = "S";
        chkSwPoseeLuminaria = "S";
        chkSwPuestaTierra = "N";
        chkSwPosteExclusivoAp = "N";
        chkSwPosteBuenEstado = "S";
        chkSwMobiliarioBuenEstado = "S";
        chkSwTransformadorExclusivoAp = "N";
        tipoPropietarioTranformador = "NA";
    }

    //--
    private void estadoPropiedadMobiliario(boolean estado){

        sltTipologia.setSelection(0);
        sltEstadoMobiliario.setSelection(0);

        sltTipologia.setEnabled(estado);
        sltMobiliario.setEnabled(estado);
        sltReferencia.setEnabled(estado);
        sltTipoBrazo.setEnabled(estado);
        sltFabricanteElemento.setEnabled(estado);
        sltCalibreConexionElemento.setEnabled(estado);
        sltEstadoMobiliario.setEnabled(estado);
        swLuminariaVisible.setChecked(estado);
        swLuminariaVisible.setEnabled(estado);


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
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
        btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
        //progressBarGuardarCenso.setVisibility(View.VISIBLE);

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

        Contrato contrato = new Contrato();
        contrato.setId(idDefaultContrato);

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

        TipoEscenario tipoEscenario = new TipoEscenario();
        tipoEscenario.setId(tipoEscenarioList.get(sltTipoEscenario.getSelectedItemPosition()).getId());

        Calibre calibre = new Calibre();
        calibre.setId_calibre(calibreList.get(sltCalibreConexionElemento.getSelectedItemPosition()).getId());

        Censo censo = new Censo();
        censo.setId_censo(censoAsignadoList.get(sltCensoAsignado.getSelectedItemPosition()).getId());
        censo.setElemento(elemento);
        censo.setClaseVia(claseVia);
        censo.setTipoRed(tipoRed);
        censo.setCalibre(calibre);
        censo.setEstadoMobiliario(estadoMobiliario);
        //Poste
        censo.setRetenidaPoste(retenidaPoste);
        censo.setInterdistancia(Integer.parseInt(txtInterdistancia.getText().toString()));

        int interdistancia_anterior = (txtInterdistanciaAnterior.getText().toString().isEmpty()) ? 0:Integer.parseInt(txtInterdistanciaAnterior.getText().toString());
        censo.setInterdistancia_anterior(interdistancia_anterior);

        int anchovia = (txtAnchoVia.getText().toString().isEmpty()) ? 0:Integer.parseInt(txtAnchoVia.getText().toString());
        censo.setAncho_via(anchovia);

        censo.setPosteNo(txtPosteNo.getText().toString());
        censo.setNormaConstruccionPoste(normaConstruccionPoste);
        //Transformador
        Double potencia = (txtPotenciaTransformador.getText().toString().isEmpty()) ? 0.0 : Double.parseDouble(txtPotenciaTransformador.getText().toString());
        //--
        censo.setPotenciaTransformador(potencia);
        censo.setPlacaCtTransformador(txtCtTransformador.getText().toString());
        censo.setPlacaMtTransformador(txtMtTransformador.getText().toString());
        censo.setChkSwTransformadorExclusivoAP(chkSwTransformadorExclusivoAp);
        //--

        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        censo.setFchRegistro(fechaHora);

        Float latitud = (txtLatitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(txtLatitud.getText().toString());
        Float longitud = (txtLongitud.getText().toString().isEmpty()) ? 0 : Float.parseFloat(txtLongitud.getText().toString());
        censo.setLatitud(latitud);
        censo.setLongitud(longitud);
        censo.setChkSwLuminariaVisible(chkSwLuminariaVisible);
        censo.setChkSwPoseeLuminaria(chkSwPoseeLuminaria);
        censo.setChkSwPuestaTierra(chkSwPuestaTierra);
        censo.setChkSwPosteExclusivoAp(chkSwPosteExclusivoAp);
        censo.setChkSwPosteBuenEstado(chkSwPosteBuenEstado);
        censo.setSector(sector);
        censo.setZona(zona);
        censo.setChkSwMobiliarioBuenEstado(chkSwMobiliarioBuenEstado);
        censo.setTipoPropietarioTransformador(tipoPropietarioTranformador);
        censo.setTipoEscenario(tipoEscenario);

        censo.setBrazoMalEstado(brazoMalEstado);
        censo.setVisorMalEstado(visorMalEstado);
        censo.setSinBombillo(sinBombillo);
        censo.setMobiliarioMalPosicionado(mobiliarioMalPosicionado);
        censo.setMobiliarioObsoleto(mobiliarioObsoleto);

        censo.setObservacion(txtObservacion.getText().toString());

        double andena       = (txtAnden_a.getText().toString().isEmpty()) ? 0 : Double.parseDouble(txtAnden_a.getText().toString());
        double andenb       = (txtAnden_b.getText().toString().isEmpty()) ? 0 : Double.parseDouble(txtAnden_b.getText().toString());
        double avanceAnden  = (txtAvanceAnden.getText().toString().isEmpty()) ? 0 : Double.parseDouble(txtAvanceAnden.getText().toString());

        censo.setAnden_a(andena);
        censo.setAnden_b(andenb);
        censo.setAvance_anden(avanceAnden);

        censo.setFabricanteElemento(
                new FabricanteElemento(
                        fabricanteElementoList.get(sltFabricanteElemento.getSelectedItemPosition()).getId(),
                        fabricanteElementoList.get(sltFabricanteElemento.getSelectedItemPosition()).getDescripcion()
                )
        );
        censo.setFabricantePoste(
                new FabricantePoste(
                        fabricantePosteList.get(sltFabricantePoste.getSelectedItemPosition()).getId(),
                        fabricantePosteList.get(sltFabricantePoste.getSelectedItemPosition()).getDescripcion()
                )
        );
        censo.setClasePerfil(
                new ClasePerfil(
                    clasePerfilList.get(sltClasePerfil.getSelectedItemPosition()).getId(),
                    clasePerfilList.get(sltClasePerfil.getSelectedItemPosition()).getDescripcion()
                )
        );

        censo.setTipoBrazo(
                new TipoBrazo(
                        tipoBrazoList.get(sltTipoBrazo.getSelectedItemPosition()).getId(),
                        tipoBrazoList.get(sltTipoBrazo.getSelectedItemPosition()).getDescripcion()
                )
        );
        int mAnterior       = (txtMobiliarioAnterior.getText().toString().isEmpty()) ? 0 : Integer.parseInt(txtMobiliarioAnterior.getText().toString());
        int mPosterior       = (txtMobiliarioSiguiente.getText().toString().isEmpty()) ? 0 : Integer.parseInt(txtMobiliarioSiguiente.getText().toString());
        censo.setMobiliario_no_anterior(mAnterior);
        censo.setMobiliario_no_posterior(mPosterior);

        CensoDB censoDB = new CensoDB(sqLiteDatabase);
        if (censoDB.agregarDatos(censo)) {
            //Log.d("LastId",""+censo.getLastId());

            //--Guardar el Tipo de Armado;
            for (int n = 0; n < tipoArmadoList.size(); n++) {
                TipoRed tipoRedArmado = new TipoRed();
                tipoRedArmado.setId(tipoArmadoList.get(n).getTipoRed().getId());

                Calibre calibreArmado = new Calibre();
                calibreArmado.setId_calibre(tipoArmadoList.get(n).getCalibre().getId_calibre());

                NormaConstruccionRed normaConstruccionRedTipoArmado = new NormaConstruccionRed();
                normaConstruccionRedTipoArmado.setId(tipoArmadoList.get(n).getNormaConstruccionRed().getId());

                CensoTipoArmado censoTipoArmado = new CensoTipoArmado();
                censoTipoArmado.setId_censo_tecnico(censo.getLastId());
                censoTipoArmado.setTipoRed(tipoRedArmado);
                censoTipoArmado.setNormaConstruccionRed(normaConstruccionRedTipoArmado);
                censoTipoArmado.setCalibre(calibreArmado);

                CensoTipoArmadoDB censoTipoArmadoDB = new CensoTipoArmadoDB(sqLiteDatabase);
                censoTipoArmadoDB.agregarDatos(censoTipoArmado);
            }

            //--Guardar Imagen
            //CensoArchivo censoArchivo = new CensoArchivo();
            CensoArchivoDB censoArchivoDB = new CensoArchivoDB(sqLiteDatabase);
            censoArchivoDB.setId_censo_tecnico(censo.getLastId());
            censoArchivoDB.setArchivo(encodeStringFoto_1);
            censoArchivoDB.agregarDatos(censoArchivoDB);
            censoArchivoDB.setArchivo(encodeStringFoto_2);
            censoArchivoDB.agregarDatos(censoArchivoDB);

            resetFrm(false);
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_almacenamiento_local);
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
        } else {
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
    }

    //--
    private void almacenarDatosEnRemoto() {
        dialog.show();
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
        requestParams.put("id_tipo_poste", tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId());
        requestParams.put("id_norma_construccion_poste", normaConstruccionPosteList.get(sltNormaConstruccionPoste.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_red", tipoRedList.get(sltTipoRed.getSelectedItemPosition()).getId());
        requestParams.put("poste_no", txtPosteNo.getText());
        requestParams.put("interdistancia", txtInterdistancia.getText());
        requestParams.put("interdistancia_anterior",txtInterdistanciaAnterior.getText());
        requestParams.put("puesta_a_tierra", chkSwPuestaTierra);
        requestParams.put("poste_exclusivo_ap", chkSwPosteExclusivoAp);
        requestParams.put("id_tipo_retenida", tipoRetenidaList.get(sltTipoRetenida.getSelectedItemPosition()).getId());
        requestParams.put("id_clase_via", claseViaList.get(sltClaseVia.getSelectedItemPosition()).getId());
        requestParams.put("potencia_transformador", txtPotenciaTransformador.getText());
        requestParams.put("placa_mt_transformador", txtMtTransformador.getText());
        requestParams.put("placa_ct_transformador", txtCtTransformador.getText());
        requestParams.put("poste_buen_estado", chkSwPosteBuenEstado);
        requestParams.put("sector",sector);
        requestParams.put("zona", zona);
        requestParams.put("id_tipo_escenario", tipoEscenarioList.get(sltTipoEscenario.getSelectedItemPosition()).getId());
        requestParams.put("mobiliario_buen_estado", chkSwMobiliarioBuenEstado);
        requestParams.put("tipo_propietario_transformador", tipoPropietarioTranformador);
        requestParams.put("brazo_mal_estado",brazoMalEstado);
        requestParams.put("visor_mal_estado",visorMalEstado);
        requestParams.put("mobiliario_mal_posicionado",mobiliarioMalPosicionado);
        requestParams.put("mobiliario_obsoleto",mobiliarioObsoleto);
        requestParams.put("mobiliario_sin_bombillo",sinBombillo);
        requestParams.put("id_calibre",calibreList.get(sltCalibreConexionElemento.getSelectedItemPosition()).getId());
        requestParams.put("ancho_via",txtAnchoVia.getText());
        requestParams.put("transformador_exclusivo_ap",chkSwTransformadorExclusivoAp);
        requestParams.put("id_fabricante_elemento",fabricanteElementoList.get(sltFabricanteElemento.getSelectedItemPosition()).getId());
        requestParams.put("id_fabricante_poste",fabricantePosteList.get(sltFabricantePoste.getSelectedItemPosition()).getId());
        requestParams.put("id_clase_perfil",clasePerfilList.get(sltClasePerfil.getSelectedItemPosition()).getId());
        requestParams.put("id_tipo_brazo",tipoBrazoList.get(sltTipoBrazo.getSelectedItemPosition()).getId());
        requestParams.put("anden_a",txtAnden_a.getText());
        requestParams.put("anden_b",txtAnden_b.getText());
        requestParams.put("avance_anden",txtAvanceAnden.getText());
        requestParams.put("mobiliario_no_anterior",txtMobiliarioAnterior.getText());
        requestParams.put("mobiliario_no_posterior",txtMobiliarioSiguiente.getText());
        requestParams.put("foto_1", encodeStringFoto_1);
        requestParams.put("foto_2", encodeStringFoto_2);
        int index = 0;
        List list = new ArrayList();
        while (index < tipoArmadoList.size()) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("id_tipo_red", tipoArmadoList.get(index).getTipoRed().getId());
            map.put("id_tipo_tension", tipoArmadoList.get(index).getTipoTension().getId());
            map.put("id_calibre", tipoArmadoList.get(index).getCalibre().getId_calibre());
            map.put("id_tipo_estructura", tipoArmadoList.get(index).getNormaConstruccionRed().getTipoEstructura().getId());
            map.put("id_norma_construccion", tipoArmadoList.get(index).getNormaConstruccionRed().getId());
            list.add(map);
            index++;
        }
        requestParams.put("tipo_armado", list);
        client.setTimeout(Constantes.TIMEOUT);

        RequestHandle post = client.post(ServicioWeb.urlGuardarCensoTecnico, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                btnGuardar.setEnabled(false);
                btnCancelar.setEnabled(false);
                //progressBarGuardarCenso.setVisibility(View.VISIBLE);
                dialog.setMessage(getText(R.string.dialogo_procesando));

                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                JSONObject jsonObject;
                String mensaje;
                dialog.dismiss();
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

                    //Log.d("resultado", "statusCode:" + statusCode + ", mensaje:" + mensaje+" ,respuesta:"+respuesta);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("resultado","Error: onsuccess"+e.getMessage()+"respuesta:"+respuesta);
                    Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Servicio Web, Código:" + statusCode, Toast.LENGTH_SHORT).show();
                }
                resetFrm(true);
                btnGuardar.setEnabled(true);
                btnCancelar.setEnabled(true);
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));
                //progressBarGuardarCenso.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
                btnGuardar.setEnabled(true);
                btnCancelar.setEnabled(true);
                dialog.dismiss();
                //progressBarGuardarCenso.setVisibility(View.INVISIBLE);
                btnCancelar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentCancel)));
                btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentOk)));

                Log.d("resultado","error "+respuesta);
                Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Código: " + statusCode, Toast.LENGTH_SHORT).show();

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
                int PERMISSIONS_REQUEST_CAMERA = 0;
                int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                switch (i) { //Tomar Foto
                    case 0:

                        if (ContextCompat.checkSelfPermission(CensoTecnico.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoTecnico.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        }
                        else {
                            if (ContextCompat.checkSelfPermission(CensoTecnico.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CensoTecnico.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                            } else {
                                tomarFoto();
                            }
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen
                       // int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                        if (ContextCompat.checkSelfPermission(CensoTecnico.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CensoTecnico.this,
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
        }

        nombreImagen = "IMG_" + (System.currentTimeMillis() / 100) + ".jpg";

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

    private void activarCoordenadas() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        int PERMISSIONS_REQUEST_LOCATION = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CensoTecnico.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

        }
        else {
            if(!gpsListener) {
                //Log.d("respuesta","activo");
               // ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new miLocalizacion());
            }
        }
    }

    private void listaProvider() {
        ubicacion = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        List<String> listProvider = ubicacion.getAllProviders();

        LocationProvider locationProvider = ubicacion.getProvider(listProvider.get(1));

        /*Log.d("Resultado", "Lista de Proveedores:" + listProvider.toString() + "\n" +
                "Exactitud:" + locationProvider.getAccuracy() + " mts\n" +
                "Nombre Proveedor:" + locationProvider.getName() + "\n" +
                "Requerimiento de Batteria:" + locationProvider.getPowerRequirement() + "\n" +
                "Soporte Altitud:" + locationProvider.supportsAltitude() + "\n" +
                "Soporte Velocidad:" + locationProvider.supportsSpeed()
        );*/
    }

    private class miLocalizacion implements LocationListener{
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

    private void sincronizarPendientes(){
        ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            CensoTecnico.Sincronizar sincronizar = new CensoTecnico.Sincronizar(this);
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

    private class Sincronizar extends AsyncTask<Void,Integer,Boolean>{

        private JSONArray jsonArray;
        private ProgressDialog dialog;
        private int progress;
        private TextView msgLogs;
        private View view;
        private AlertDialog.Builder alertDialog;
        private LayoutInflater inflater;
        CensoDB  censoDB;
        CensoTipoArmadoDB censoTipoArmadoDB;
        CensoArchivoDB censoArchivoDB;
        Cursor cursor;
        Cursor cursorTipoArmado;
        Cursor cursorCensoArchivo;

        public Sincronizar(Activity activity){
            dialog = new ProgressDialog(activity);
            alertDialog = new AlertDialog.Builder(activity);

            dialog.setCancelable(false);
            dialog.setTitle(R.string.titulo_alerta);
            dialog.setIcon(R.drawable.icon_info);

            inflater = LayoutInflater.from(activity);

            censoDB             = new CensoDB(database);
            censoTipoArmadoDB   = new CensoTipoArmadoDB(database);
            censoArchivoDB      = new CensoArchivoDB(database);
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
            //Log.d(Constantes.TAG,"->"+jsonArray.toString());
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage(getText(R.string.recopilando_datos)+" "+values[0].intValue()+"%");
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
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
                    try {
                        AsyncHttpClient client = new AsyncHttpClient();
                        StringEntity jsonParams = new StringEntity(jsonArray.toString(), "UTF-8");
                        jsonParams.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        client.setTimeout(Constantes.TIMEOUT);
                        RequestHandle post = client.post(getApplicationContext(), ServicioWeb.urlSincronizarCensoTecnico, jsonParams, "application/json", new AsyncHttpResponseHandler() {
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
                                        Log.d(Constantes.TAG, "contenLenght:" + contenLenght);
                                    }
                                }
                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                progress = (int) Math.round(((double) bytesWritten / (double) contenLenght) * 100);
                                //Log.d(Constantes.TAG,progress+"%");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String respuesta = new String(responseBody);
                                Log.d(Constantes.TAG, respuesta);
                                String log = "";
                                try {
                                    JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                    Log.d(Constantes.TAG, "JSON-RESPONSE:" + respuesta);
                                    JSONArray jArrayLog = jsonResponse.getJSONArray("log");
                                    for (int i = 0; i < jArrayLog.length(); i++) {
                                        JSONObject jLog = jArrayLog.getJSONObject(i);
                                        jLog.getInt("id");
                                        jLog.getInt("id_censo");
                                        jLog.getInt("mobiliario");
                                        jLog.getString("mensaje");
                                        jLog.getBoolean("procesar");
                                        log = log + "Mobiliario No: " + jLog.getInt("mobiliario") + "," + jLog.getString("mensaje") + "\n";
                                        if (jLog.getBoolean("procesar")) {
                                            censoArchivoDB.eliminarDatos(jLog.getInt("id"));
                                            censoTipoArmadoDB.eliminarDatos(jLog.getInt("id"));
                                            censoDB.eliminarDatos(jLog.getInt("id"));
                                        }
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

                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    setButton(true);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d(Constantes.TAG, "JSON-RESPONSE-ERROR:" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                String respuesta = new String(responseBody);
                                dialog.dismiss();
                                setButton(true);
                                Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " Código: " + statusCode + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(Constantes.TAG, "JSON-RESPONSE-ERROR:" + respuesta);
                            }

                            @Override
                            public void onUserException(Throwable error) {
                                super.onUserException(error);
                                dialog.dismiss();
                                setButton(true);
                                Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(Constantes.TAG, "JSON-RESPONSE-ERROR:" + error.getMessage());
                            }
                        });
                    }catch (RuntimeException error){
                        Toast.makeText(getApplicationContext(), getText(R.string.alert_error_ejecucion) + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        private JSONArray armarJSON() throws JSONException{
            int p = 0;
            JSONArray datos = new JSONArray();

            cursor = censoDB.consultarTodo();
            int size = cursor.getCount();
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
                    jsonObject.put("id_tipo_poste", cursor.getInt(cursor.getColumnIndex("id_tipo_poste")));
                    jsonObject.put("id_norma_construccion_poste", cursor.getInt(cursor.getColumnIndex("id_norma_construccion_poste")));
                    jsonObject.put("id_tipo_red", cursor.getInt(cursor.getColumnIndex("id_tipo_red")));
                    jsonObject.put("poste_no", cursor.getString(cursor.getColumnIndex("poste_no")));
                    jsonObject.put("interdistancia", cursor.getInt(cursor.getColumnIndex("interdistancia")));
                    jsonObject.put("interdistancia_anterior", cursor.getInt(cursor.getColumnIndex("interdistancia_anterior")));
                    jsonObject.put("puesta_a_tierra", cursor.getString(cursor.getColumnIndex("puesta_a_tierra")));
                    jsonObject.put("poste_exclusivo_ap", cursor.getString(cursor.getColumnIndex("poste_exclusivo_ap")));
                    jsonObject.put("id_tipo_retenida", cursor.getInt(cursor.getColumnIndex("id_tipo_retenida")));
                    jsonObject.put("id_clase_via", cursor.getInt(cursor.getColumnIndex("id_clase_via")));
                    jsonObject.put("serial_medidor", 0);
                    jsonObject.put("lectura_medidor", 0);
                    jsonObject.put("potencia_transformador", cursor.getDouble(cursor.getColumnIndex("potencia_transformador")));
                    jsonObject.put("placa_mt_transformador", cursor.getString(cursor.getColumnIndex("placa_mt_transformador")));
                    jsonObject.put("placa_ct_transformador", cursor.getString(cursor.getColumnIndex("placa_ct_transformador")));
                    jsonObject.put("poste_buen_estado", cursor.getString(cursor.getColumnIndex("poste_buen_estado")));
                    jsonObject.put("sector", cursor.getString(cursor.getColumnIndex("sector")));
                    jsonObject.put("zona", cursor.getString(cursor.getColumnIndex("zona")));
                    jsonObject.put("id_tipo_escenario", cursor.getString(cursor.getColumnIndex("id_tipo_escenario")));
                    jsonObject.put("mobiliario_buen_estado", cursor.getString(cursor.getColumnIndex("mobiliario_buen_estado")));
                    jsonObject.put("tipo_propietario_transformador", cursor.getString(cursor.getColumnIndex("tipo_propietario_transformador")));
                    jsonObject.put("brazo_mal_estado", cursor.getString(cursor.getColumnIndex("brazo_mal_estado")));
                    jsonObject.put("visor_mal_estado", cursor.getString(cursor.getColumnIndex("visor_mal_estado")));
                    jsonObject.put("mobiliario_mal_posicionado", cursor.getString(cursor.getColumnIndex("mobiliario_mal_posicionado")));
                    jsonObject.put("mobiliario_obsoleto", cursor.getString(cursor.getColumnIndex("mobiliario_obsoleto")));
                    jsonObject.put("mobiliario_sin_bombillo", cursor.getString(cursor.getColumnIndex("mobiliario_sin_bombillo")));
                    jsonObject.put("ancho_via", cursor.getString(cursor.getColumnIndex("ancho_via")));
                    jsonObject.put("transformador_exclusivo_ap", cursor.getString(cursor.getColumnIndex("transformador_exclusivo_ap")));
                    jsonObject.put("id_fabricante_elemento", cursor.getInt(cursor.getColumnIndex("id_fabricante_luminaria")));
                    jsonObject.put("id_fabricante_poste", cursor.getInt(cursor.getColumnIndex("id_fabricante_poste")));
                    jsonObject.put("id_clase_perfil", cursor.getInt(cursor.getColumnIndex("id_clasificacion_perfil")));
                    jsonObject.put("id_tipo_brazo", cursor.getInt(cursor.getColumnIndex("id_tipo_brazo")));
                    jsonObject.put("anden_a", cursor.getDouble(cursor.getColumnIndex("anden_a")));
                    jsonObject.put("anden_b", cursor.getDouble(cursor.getColumnIndex("anden_b")));
                    jsonObject.put("avance_anden", cursor.getInt(cursor.getColumnIndex("avance_anden")));
                    jsonObject.put("mobiliario_no_anterior",cursor.getInt(cursor.getColumnIndex("mobiliario_no_anterior")));
                    jsonObject.put("mobiliario_no_posterior",cursor.getInt(cursor.getColumnIndex("mobiliario_no_posterior")));

                    //--Tipo Armado
                    JSONArray jsonArrayTipoArmado = new JSONArray();
                    cursorTipoArmado = censoTipoArmadoDB.consultarTodo(cursor.getInt(cursor.getColumnIndex("id")));
                    if (cursorTipoArmado.moveToFirst()) {
                        do {
                            JSONObject jsonTipoArmado = new JSONObject();
                            jsonTipoArmado.put("id_tipo_red", cursorTipoArmado.getInt(cursorTipoArmado.getColumnIndex("id_tipo_red")));
                            jsonTipoArmado.put("id_norma_construccion", cursorTipoArmado.getInt(cursorTipoArmado.getColumnIndex("id_norma_construccion_red")));
                            jsonTipoArmado.put("id_calibre", cursorTipoArmado.getInt(cursorTipoArmado.getColumnIndex("id_calibre")));
                            jsonArrayTipoArmado.put(jsonTipoArmado);
                        } while (cursorTipoArmado.moveToNext());
                    }
                    cursorTipoArmado.close();
                    jsonObject.put("tipo_armado", jsonArrayTipoArmado);

                    //--Imagenes
                    JSONArray jsonArrayFoto = new JSONArray();
                    cursorCensoArchivo = censoArchivoDB.consultarTodo(cursor.getInt(cursor.getColumnIndex("id")));
                    if (cursorCensoArchivo.moveToFirst()) {
                        do {
                            jsonArrayFoto.put(cursorCensoArchivo.getString(cursorCensoArchivo.getColumnIndex("archivo")));
                        } while (cursorCensoArchivo.moveToNext());
                    }
                    jsonObject.put("foto", jsonArrayFoto);

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