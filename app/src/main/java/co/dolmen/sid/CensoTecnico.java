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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.ComponenteNormaConstruccionRed;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.NormaConstruccionRed;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoEstructura;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoTension;
import co.dolmen.sid.modelo.BarrioDB;
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

import static java.lang.Integer.parseInt;

public class CensoTecnico extends AppCompatActivity{

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String nombreMunicipio;
    AlertDialog.Builder alert;
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
    //--
    EditText txtElementoNo;
    EditText txtLatitud;
    EditText txtLongitud;
    EditText txtBuscarElemento;
    EditText txtDireccion;
    //--
    Switch swLuminariaVisible;
    Switch swPoseeLuminaria;
    //--
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
    ComponenteNormaConstruccionRed componenteNormaConstruccionRed;
    //--
    ImageView imgFoto1;
    ImageView imgFoto2;
    //--
    TextView txt_norma_armado_red_1;
    TextView txt_norma_armado_red_2;
    TextView txt_norma_armado_red_3;

    private boolean accionarFoto1;
    private boolean accionarFoto2;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;
    private String encodeString;
    private String path;
    private ArrayList<ComponenteNormaConstruccionRed> tipoArmadoList;

    private double latitud;
    private double longitud;

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
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
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
        //--
        txtElementoNo               = findViewById(R.id.txt_elemento_no);
        txtLatitud                  = findViewById(R.id.txt_latitud);
        txtLongitud                 = findViewById(R.id.txt_longitud);
        txtBuscarElemento           = findViewById(R.id.txt_buscar_elemento);
        txtDireccion                = findViewById(R.id.txt_direccion);
        //--
        swLuminariaVisible          = findViewById(R.id.sw_numero_luminaria_visible);
        swPoseeLuminaria            = findViewById(R.id.sw_tiene_luminaria);
        //--
        btnCapturarGPS              = findViewById(R.id.btn_capturar_gps);
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
        //swLuminariaVisible.setEnabled(false);
        //swPoseeLuminaria.setEnabled(false);
        //--
        btnCapturarGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarCoordenadas();
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
                View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.direccion,null);
                sltTipoInterseccionA        = content.findViewById(R.id.slt_tipo_interseccion_a);
                sltTipoInterseccionB        = content.findViewById(R.id.slt_tipo_interseccion_b);
                cargarTipoInterseccion(database);
                alert.setTitle(R.string.titulo_direccion);
                alert.setView(content)
                        // Add action buttons
                        .setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        })
                        .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });

                alert.create().show();
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
            ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
            Cursor cursor = elementoDB.consultarElemento(idDefaultMunicipio,idDefaultProceso,parseInt(txtBuscarElemento.getText().toString()));
            if(cursor.getCount() == 0) {
                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(getText(R.string.alert_elemento_no_encontrado)+" sobre el Elemento: "+txtBuscarElemento.getText() );
                alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
                sltTipologia.setSelection(0);
                sltBarrio.setSelection(0);
                txtElementoNo.setText("");
                txtDireccion.setText("");
                txtElementoNo.setEnabled(true);
            }
            else {

                cursor.moveToFirst();

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