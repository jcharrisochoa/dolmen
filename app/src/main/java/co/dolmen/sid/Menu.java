package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import co.dolmen.sid.entidad.TipoBaseFotocelda;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoBalastoDB;
import co.dolmen.sid.modelo.TipoBaseFotoceldaDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInstalacionRedDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idCenso;

    ImageButton btnPerfil;
    ImageButton btnSalir;
    ImageButton btnCensoTecnico;
    ImageButton btnReporteDano;
    ImageButton btnCrearActividad;
    ImageButton btnActualizarElemento;
    ImageButton btnCrearElemento;
    ImageButton btnCensoCarga;
    ImageButton btnMisActividades;
    ImageButton btnMiStock;

    SharedPreferences config;
    TextView txtNombreMunicipio;
    TextView txtNombreProceso;
    TextView txtNombreContrato;

    SQLiteOpenHelper conn;
    private CensoDB censoDB;
    private CensoTipoArmadoDB censoTipoArmadoDB;
    private CensoArchivoDB censoArchivoDB;
    private ProgramaDB programaDB;
    private CensoAsignadoDB censoAsignadoDB;
    private ElementoDB elementoDB;

    private ProcesoSgcDB procesoSgcDB;
    private ContratoDB contratoDB;
    private ActaContratoDB actaContratoDB;
    private BarrioDB  barrioDB;
    private MunicipioDB municipioDB;
    private TipoReporteDanoDB tipoReporteDanoDB;
    private TipoActividadDB tipoActividadDB;
    private EstadoMobiliarioDB estadoMobiliarioDB;
    private EstadoActividadDB estadoActividadDB;
    private TipologiaDB tipologiaDB;
    private MobiliarioDB mobiliarioDB;
    private ReferenciaMobiliarioDB referenciaMobiliarioDB;
    private ActividadOperativaDB actividadOperativaDB;
    private StockDB stockDB;
    private BodegaDB bodegaDB;
    private TipoInstalacionRedDB tipoInstalacionRedDB;
    private TipoBaseFotoceldaDB tipoBaseFotoceldaDB;
    private TipoBrazoDB tipoBrazoDB;
    private TipoBalastoDB tipoBalastoDB;
    private ControlEncendidoDB controlEncendidoDB;
    private TipoPosteDB tipoPosteDB;
    private TipoRedDB tipoRedDB;
    private NormaConstruccionPosteDB normaConstruccionPosteDB;
    private CalibreDB calibreDB;
    private TipoStockDB tipoStockDB;
    private ClaseViaDB claseViaDB;
    private RetenidaPosteDB retenidaPosteDB;
    private TipoTensionDB tipoTensionDB;
    private TipoEstructuraDB tipoEstructuraDB;
    private NormaConstruccionRedDB normaConstruccionRedDB;


    SQLiteDatabase database;
    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle(getText(R.string.titulo_menu));

        conn = new BaseDatos(Menu.this);
        database = conn.getReadableDatabase();

        censoDB                 = new CensoDB(database);
        censoTipoArmadoDB       = new CensoTipoArmadoDB(database);
        censoArchivoDB          = new CensoArchivoDB(database);
        programaDB              = new ProgramaDB(database);
        censoAsignadoDB         = new CensoAsignadoDB(database);
        elementoDB              = new ElementoDB(database);
        procesoSgcDB            = new ProcesoSgcDB(database);
        contratoDB              = new ContratoDB(database);
        actaContratoDB          = new ActaContratoDB(database);
        barrioDB                = new BarrioDB(database);
        municipioDB             = new MunicipioDB(database);
        tipoReporteDanoDB       = new TipoReporteDanoDB(database);
        tipoActividadDB         = new TipoActividadDB(database);
        estadoMobiliarioDB      = new EstadoMobiliarioDB(database);
        estadoActividadDB       = new EstadoActividadDB(database);
        tipologiaDB             = new TipologiaDB(database);
        mobiliarioDB            = new MobiliarioDB(database);
        referenciaMobiliarioDB  = new ReferenciaMobiliarioDB(database);
        actividadOperativaDB    = new ActividadOperativaDB(database);
        stockDB                 = new StockDB(database);
        bodegaDB                = new BodegaDB(database);
        tipoInstalacionRedDB    = new TipoInstalacionRedDB(database);
        tipoBaseFotoceldaDB     = new TipoBaseFotoceldaDB(database);
        tipoBrazoDB             = new TipoBrazoDB(database);
        tipoBalastoDB           = new TipoBalastoDB(database);
        controlEncendidoDB      = new ControlEncendidoDB(database);
        tipoPosteDB             = new TipoPosteDB(database);
        tipoRedDB               = new TipoRedDB(database);
        normaConstruccionPosteDB = new NormaConstruccionPosteDB(database);
        calibreDB               = new CalibreDB(database);
        tipoStockDB             = new TipoStockDB(database);
        claseViaDB              = new ClaseViaDB(database);
        retenidaPosteDB         = new RetenidaPosteDB(database);
        tipoTensionDB           = new TipoTensionDB(database);
        tipoEstructuraDB        = new TipoEstructuraDB(database);
        normaConstruccionRedDB  = new NormaConstruccionRedDB(database);

        alert = new AlertDialog.Builder(this);

        config = getSharedPreferences("config", MODE_PRIVATE);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso   = config.getString("nombreProceso", "");
        nombreContrato  = config.getString("nombreContrato", "");


        btnPerfil           = findViewById(R.id.btn_perfil);
        btnSalir            = findViewById(R.id.btn_salir);
        btnReporteDano      = findViewById(R.id.btn_reportar_dano);
        btnCrearActividad   = findViewById(R.id.btn_crear_actividad);
        btnActualizarElemento = findViewById(R.id.btn_actualizar_elemento);
        btnCrearElemento      = findViewById(R.id.btn_crear_elemento);
        btnCensoCarga       = findViewById(R.id.btn_censo_carga);
        btnCensoTecnico     = findViewById(R.id.btn_censo_tecnico);
        btnMisActividades   = findViewById(R.id.btn_mis_actividades);
        btnMiStock          = findViewById(R.id.btn_mi_stock);

        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ConfigurarArea.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.setTitle(R.string.titulo_alerta);
                alert.setMessage(R.string.alert_cerrar_sesion);
                alert.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        try {
                            censoTipoArmadoDB.eliminarDatos();
                            censoArchivoDB.eliminarDatos();
                            censoDB.eliminarDatos();
                            programaDB.eliminarDatos();
                            censoAsignadoDB.eliminarDatos();
                            elementoDB.eliminarDatos();
                            procesoSgcDB.eliminarDatos();
                            contratoDB.eliminarDatos();
                            actaContratoDB.eliminarDatos();
                            barrioDB.eliminarDatos();
                            municipioDB.eliminarDatos();
                            tipoReporteDanoDB.eliminarDatos();
                            tipoActividadDB.eliminarDatos();
                            estadoMobiliarioDB.eliminarDatos();
                            estadoActividadDB.eliminarDatos();
                            tipologiaDB.eliminarDatos();
                            mobiliarioDB.eliminarDatos();
                            referenciaMobiliarioDB.eliminarDatos();
                            actividadOperativaDB.eliminarDatos();
                            stockDB.eliminarDatos();
                            bodegaDB.eliminarDatos();
                            tipoInstalacionRedDB.eliminarDatos();
                            tipoBaseFotoceldaDB.eliminarDatos();
                            tipoBrazoDB.eliminarDatos();
                            tipoBalastoDB.eliminarDatos();
                            controlEncendidoDB.eliminarDatos();
                            tipoPosteDB.eliminarDatos();
                            tipoRedDB.eliminarDatos();
                            normaConstruccionPosteDB.eliminarDatos();
                            calibreDB.eliminarDatos();
                            tipoStockDB.eliminarDatos();
                            claseViaDB.eliminarDatos();
                            retenidaPosteDB.eliminarDatos();
                            tipoTensionDB.eliminarDatos();
                            tipoEstructuraDB.eliminarDatos();
                            normaConstruccionRedDB.eliminarDatos();

                            config.edit().clear().commit();
                            Intent intent = new Intent(Menu.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            Menu.this.finish();
                        }catch (SQLException e){
                            Toast.makeText(getApplicationContext(),"ERROR"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
                alert.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                    }
                });
                alert.create().show();

            }
        });

        btnCensoTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,SubMenuCensoTecnico.class);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnCensoCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,CensoCarga.class);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnReporteDano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ReporteDano.class);
                startActivity(i);
                Menu.this.finish();
            }
        });

        btnCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,GenerarActividadOperativa.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnActualizarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,ActualizarElemento.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnCrearElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this,CrearElemento.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnMisActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, ListaActividad.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
        btnMiStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, ListaStock.class);
                startActivity(i);
                Menu.this.finish();
            }
        });
    }
}
