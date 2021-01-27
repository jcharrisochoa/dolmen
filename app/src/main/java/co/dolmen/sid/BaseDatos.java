package co.dolmen.sid;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EquipoDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ProveedorDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.SentidoDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoBalastoDB;
import co.dolmen.sid.modelo.TipoBaseFotoceldaDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoConductorElectricoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInstalacionRedDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.modelo.UnidadMedidaDB;
import co.dolmen.sid.modelo.VatiajeDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;

public class BaseDatos extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private ClaseViaDB claseViaDB;
    private EstadoMobiliarioDB estadoMobiliarioDB;
    private EstadoActividadDB estadoActividadDB;
    private VatiajeDB vatiajeDB;
    private UnidadMedidaDB unidadMedidaDB;
    private TipoRedDB tipoRedDB;
    private TipoPosteDB tipoPosteDB;
    private TipoInterseccionDB tipoInterseccionDB;
    private TipoEspacioDB tipoEspacioDB;
    private MunicipioDB municipioDB;
    private ProcesoSgcDB procesoSgcDB;
    private BarrioDB barrioDB;
    private TipoTensionDB tipoTensionDB;
    private RetenidaPosteDB retenidaPosteDB;
    private TipologiaDB tipologiaDB;
    private MobiliarioDB mobiliarioDB;
    private ReferenciaMobiliarioDB referenciaMobiliarioDB;
    private ContratoDB contratoDB;
    private NormaConstruccionPosteDB normaConstruccionPosteDB;
    private TipoEstructuraDB tipoEstructuraDB;
    private NormaConstruccionRedDB normaConstruccionRedDB;
    private ElementoDB elementoDB;
    private CensoDB censoDB;
    private CensoTipoArmadoDB censoTipoArmadoDB;
    private CensoArchivoDB censoArchivoDB;
    private CensoAsignadoDB censoAsignadoDB;
    private TipoReporteDanoDB tipoReporteDanoDB;
    private ProgramaDB programaDB;
    private TipoActividadDB tipoActividadDB;
    private SentidoDB sentidoDB;
    private ActaContratoDB actaContratoDB;
    private ProveedorDB proveedorDB;
    private TipoEscenarioDB tipoEscenarioDB;
    private TipoConductorElectricoDB tipoConductorElectricoDB;
    private CalibreDB calibreDB;
    private TipoStockDB tipoStockDB;
    private ArticuloDB articuloDB;
    private StockDB stockDB;
    private ActividadOperativaDB actividadOperativaDB;
    private TipoBrazoDB tipoBrazoDB;
    private TipoBalastoDB tipoBalastoDB;
    private TipoBaseFotoceldaDB tipoBaseFotoceldaDB;
    private ControlEncendidoDB controlEncendidoDB;
    private TipoInstalacionRedDB tipoInstalacionRedDB;
    private BodegaDB bodegaDB;
    private EquipoDB equipoDB;


    public BaseDatos(Context context){
        super(context, Constantes.NOMBRE_BASEDATOS,null,Constantes.VERSION_BASEDATOS);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //onUpgrade(sqLiteDatabase,Constantes.VERSION_BASEDATOS,Constantes.VERSION_BASEDATOS+1);
        tipologiaDB = new TipologiaDB(sqLiteDatabase);
        tipologiaDB.crearTabla();

        mobiliarioDB = new MobiliarioDB(sqLiteDatabase);
        mobiliarioDB.crearTabla();

        referenciaMobiliarioDB = new ReferenciaMobiliarioDB(sqLiteDatabase);
        referenciaMobiliarioDB.crearTabla();

        claseViaDB = new ClaseViaDB(sqLiteDatabase);
        claseViaDB.crearTabla();

        estadoMobiliarioDB = new EstadoMobiliarioDB(sqLiteDatabase);
        estadoMobiliarioDB.crearTabla();

        estadoActividadDB = new EstadoActividadDB(sqLiteDatabase);
        estadoActividadDB.crearTabla();

        vatiajeDB = new VatiajeDB(sqLiteDatabase);
        vatiajeDB.crearTabla();

        unidadMedidaDB = new UnidadMedidaDB(sqLiteDatabase);
        unidadMedidaDB.crearTabla();

        tipoRedDB = new TipoRedDB(sqLiteDatabase);
        tipoRedDB.crearTabla();

        tipoPosteDB = new TipoPosteDB(sqLiteDatabase);
        tipoPosteDB.crearTabla();

        tipoInterseccionDB = new TipoInterseccionDB(sqLiteDatabase);
        tipoInterseccionDB.crearTabla();

        tipoEspacioDB = new TipoEspacioDB(sqLiteDatabase);
        tipoEspacioDB.crearTabla();

        municipioDB = new MunicipioDB(sqLiteDatabase);
        municipioDB.crearTabla();

        procesoSgcDB = new ProcesoSgcDB(sqLiteDatabase);
        procesoSgcDB.crearTabla();

        barrioDB = new BarrioDB(sqLiteDatabase);
        barrioDB.crearTabla();

        tipoTensionDB = new TipoTensionDB(sqLiteDatabase);
        tipoTensionDB.crearTabla();

        retenidaPosteDB = new RetenidaPosteDB(sqLiteDatabase);
        retenidaPosteDB.crearTabla();

        contratoDB = new ContratoDB(sqLiteDatabase);
        contratoDB.crearTabla();

        normaConstruccionPosteDB = new NormaConstruccionPosteDB(sqLiteDatabase);
        normaConstruccionPosteDB.crearTabla();

        tipoEstructuraDB = new TipoEstructuraDB(sqLiteDatabase);
        tipoEstructuraDB.crearTabla();

        normaConstruccionRedDB = new NormaConstruccionRedDB(sqLiteDatabase);
        normaConstruccionRedDB.crearTabla();

        elementoDB = new ElementoDB(sqLiteDatabase);
        elementoDB.crearTabla();

        censoDB = new CensoDB(sqLiteDatabase);
        censoDB.crearTabla();

        censoArchivoDB = new CensoArchivoDB(sqLiteDatabase);
        censoArchivoDB.crearTabla();

        censoTipoArmadoDB = new CensoTipoArmadoDB(sqLiteDatabase);
        censoTipoArmadoDB.crearTabla();

        censoAsignadoDB = new CensoAsignadoDB(sqLiteDatabase);
        censoAsignadoDB.crearTabla();

        tipoReporteDanoDB = new TipoReporteDanoDB(sqLiteDatabase);
        tipoReporteDanoDB.crearTabla();

        programaDB = new ProgramaDB(sqLiteDatabase);
        programaDB.crearTabla();

        tipoActividadDB = new TipoActividadDB(sqLiteDatabase);
        tipoActividadDB.crearTabla();

        sentidoDB = new SentidoDB(sqLiteDatabase);
        sentidoDB.crearTabla();

        actaContratoDB = new ActaContratoDB(sqLiteDatabase);
        actaContratoDB.crearTabla();

        proveedorDB = new ProveedorDB(sqLiteDatabase);
        proveedorDB.crearTabla();

        tipoEscenarioDB = new TipoEscenarioDB(sqLiteDatabase);
        tipoEscenarioDB.crearTabla();

        tipoConductorElectricoDB = new TipoConductorElectricoDB(sqLiteDatabase);
        tipoConductorElectricoDB.crearTabla();

        calibreDB  = new CalibreDB(sqLiteDatabase);
        calibreDB.crearTabla();

        tipoStockDB  = new TipoStockDB(sqLiteDatabase);
        tipoStockDB.crearTabla();

        articuloDB = new ArticuloDB(sqLiteDatabase);
        articuloDB.crearTabla();

        stockDB = new StockDB(sqLiteDatabase);
        stockDB.crearTabla();

        actividadOperativaDB = new ActividadOperativaDB(sqLiteDatabase);
        actividadOperativaDB.crearTabla();

        tipoBrazoDB = new TipoBrazoDB(sqLiteDatabase);
        tipoBrazoDB.crearTabla();

        tipoBalastoDB = new TipoBalastoDB(sqLiteDatabase);
        tipoBalastoDB.crearTabla();

        tipoBaseFotoceldaDB = new TipoBaseFotoceldaDB(sqLiteDatabase);
        tipoBaseFotoceldaDB.crearTabla();

        controlEncendidoDB = new ControlEncendidoDB(sqLiteDatabase);
        controlEncendidoDB.crearTabla();

        tipoInstalacionRedDB = new TipoInstalacionRedDB(sqLiteDatabase);
        tipoInstalacionRedDB.crearTabla();

        bodegaDB    = new BodegaDB(sqLiteDatabase);
        bodegaDB.crearTabla();

        equipoDB = new EquipoDB(sqLiteDatabase);
        equipoDB.crearTabla();
        //Log.d("DataBase","create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            tipologiaDB = new TipologiaDB(sqLiteDatabase);
            mobiliarioDB = new MobiliarioDB(sqLiteDatabase);
            referenciaMobiliarioDB = new ReferenciaMobiliarioDB(sqLiteDatabase);
            claseViaDB = new ClaseViaDB(sqLiteDatabase);
            estadoMobiliarioDB = new EstadoMobiliarioDB(sqLiteDatabase);
            estadoActividadDB = new EstadoActividadDB(sqLiteDatabase);
            vatiajeDB = new VatiajeDB(sqLiteDatabase);
            unidadMedidaDB = new UnidadMedidaDB(sqLiteDatabase);
            tipoRedDB = new TipoRedDB(sqLiteDatabase);
            tipoPosteDB = new TipoPosteDB(sqLiteDatabase);
            tipoInterseccionDB = new TipoInterseccionDB(sqLiteDatabase);
            tipoEspacioDB = new TipoEspacioDB(sqLiteDatabase);
            municipioDB = new MunicipioDB(sqLiteDatabase);
            procesoSgcDB = new ProcesoSgcDB(sqLiteDatabase);
            barrioDB = new BarrioDB(sqLiteDatabase);
            tipoTensionDB = new TipoTensionDB(sqLiteDatabase);
            retenidaPosteDB = new RetenidaPosteDB(sqLiteDatabase);
            contratoDB = new ContratoDB(sqLiteDatabase);
            normaConstruccionPosteDB = new NormaConstruccionPosteDB(sqLiteDatabase);
            tipoEstructuraDB = new TipoEstructuraDB(sqLiteDatabase);
            normaConstruccionRedDB = new NormaConstruccionRedDB(sqLiteDatabase);
            elementoDB = new ElementoDB(sqLiteDatabase);
            censoDB = new CensoDB(sqLiteDatabase);
            censoArchivoDB = new CensoArchivoDB(sqLiteDatabase);
            censoTipoArmadoDB = new CensoTipoArmadoDB(sqLiteDatabase);
            censoAsignadoDB = new CensoAsignadoDB(sqLiteDatabase);
            tipoReporteDanoDB = new TipoReporteDanoDB(sqLiteDatabase);
            programaDB = new ProgramaDB(sqLiteDatabase);
            tipoActividadDB = new TipoActividadDB(sqLiteDatabase);
            sentidoDB = new SentidoDB(sqLiteDatabase);
            actaContratoDB = new ActaContratoDB(sqLiteDatabase);
            proveedorDB = new ProveedorDB(sqLiteDatabase);
            tipoEscenarioDB = new TipoEscenarioDB(sqLiteDatabase);
            tipoConductorElectricoDB = new TipoConductorElectricoDB(sqLiteDatabase);
            calibreDB  = new CalibreDB(sqLiteDatabase);
            tipoStockDB  = new TipoStockDB(sqLiteDatabase);
            articuloDB  = new ArticuloDB(sqLiteDatabase);
            stockDB = new StockDB(sqLiteDatabase);
            actividadOperativaDB = new ActividadOperativaDB(sqLiteDatabase);
            tipoBrazoDB = new TipoBrazoDB(sqLiteDatabase);
            tipoBalastoDB = new TipoBalastoDB(sqLiteDatabase);
            tipoBaseFotoceldaDB = new TipoBaseFotoceldaDB(sqLiteDatabase);
            controlEncendidoDB = new ControlEncendidoDB(sqLiteDatabase);
            tipoInstalacionRedDB = new TipoInstalacionRedDB(sqLiteDatabase);
            bodegaDB    = new BodegaDB(sqLiteDatabase);
            equipoDB    = new EquipoDB(sqLiteDatabase);

            tipologiaDB.borrarTabla();
            mobiliarioDB.borrarTabla();
            referenciaMobiliarioDB.borrarTabla();
            claseViaDB.borrarTabla();
            estadoMobiliarioDB.borrarTabla();
            estadoActividadDB.borrarTabla();
            vatiajeDB.borrarTabla();
            unidadMedidaDB.borrarTabla();
            tipoRedDB.borrarTabla();
            tipoPosteDB.borrarTabla();
            tipoInterseccionDB.borrarTabla();
            tipoEspacioDB.borrarTabla();
            municipioDB.borrarTabla();
            procesoSgcDB.borrarTabla();
            barrioDB.borrarTabla();
            tipoTensionDB.borrarTabla();
            retenidaPosteDB.borrarTabla();
            contratoDB.borrarTabla();
            normaConstruccionPosteDB.borrarTabla();
            tipoEstructuraDB.borrarTabla();
            normaConstruccionRedDB.borrarTabla();
            elementoDB.borrarTabla();
            censoDB.borrarTabla();
            censoArchivoDB.borrarTabla();
            censoTipoArmadoDB.borrarTabla();
            censoAsignadoDB.borrarTabla();
            tipoReporteDanoDB.borrarTabla();
            programaDB.borrarTabla();
            tipoActividadDB.borrarTabla();
            sentidoDB.borrarTabla();
            actaContratoDB.borrarTabla();
            proveedorDB.borrarTabla();
            tipoEscenarioDB.borrarTabla();
            tipoConductorElectricoDB.borrarTabla();
            calibreDB.borrarTabla();
            tipoStockDB.borrarTabla();
            articuloDB.borrarTabla();
            stockDB.borrarTabla();
            actividadOperativaDB.borrarTabla();
            tipoBrazoDB.borrarTabla();
            tipoBalastoDB.borrarTabla();
            tipoBaseFotoceldaDB.borrarTabla();
            controlEncendidoDB.borrarTabla();
            tipoInstalacionRedDB.borrarTabla();
            bodegaDB.borrarTabla();
            equipoDB.borrarTabla();

            onCreate(sqLiteDatabase);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}