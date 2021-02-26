package co.dolmen.sid.utilidades;

import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.entidad.ClasePerfil;
import co.dolmen.sid.modelo.ClasePerfilDB;
import co.dolmen.sid.modelo.ElementoDesmontadoDB;
import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArchivoActividadDB;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.CentroCostoDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EquipoDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.FabricanteElementoDB;
import co.dolmen.sid.modelo.FabricantePosteDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MovimientoArticuloDB;
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
import co.dolmen.sid.modelo.VatiajeDesmontadoDB;

public class MiBaseDatos {
    private SQLiteDatabase db;
    public ClaseViaDB claseViaDB;
    public EstadoMobiliarioDB estadoMobiliarioDB;
    public EstadoActividadDB estadoActividadDB;
    public VatiajeDB vatiajeDB;
    public UnidadMedidaDB unidadMedidaDB;
    public TipoRedDB tipoRedDB;
    public TipoPosteDB tipoPosteDB;
    public TipoInterseccionDB tipoInterseccionDB;
    public TipoEspacioDB tipoEspacioDB;
    public MunicipioDB municipioDB;
    public ProcesoSgcDB procesoSgcDB;
    public BarrioDB barrioDB;
    public TipoTensionDB tipoTensionDB;
    public RetenidaPosteDB retenidaPosteDB;
    public TipologiaDB tipologiaDB;
    public MobiliarioDB mobiliarioDB;
    public ReferenciaMobiliarioDB referenciaMobiliarioDB;
    public ContratoDB contratoDB;
    public NormaConstruccionPosteDB normaConstruccionPosteDB;
    public TipoEstructuraDB tipoEstructuraDB;
    public NormaConstruccionRedDB normaConstruccionRedDB;
    public ElementoDB elementoDB;
    public CensoDB censoDB;
    public CensoTipoArmadoDB censoTipoArmadoDB;
    public CensoArchivoDB censoArchivoDB;
    public CensoAsignadoDB censoAsignadoDB;
    public TipoReporteDanoDB tipoReporteDanoDB;
    public ProgramaDB programaDB;
    public TipoActividadDB tipoActividadDB;
    public SentidoDB sentidoDB;
    public ActaContratoDB actaContratoDB;
    public ProveedorDB proveedorDB;
    public TipoEscenarioDB tipoEscenarioDB;
    public TipoConductorElectricoDB tipoConductorElectricoDB;
    public CalibreDB calibreDB;
    public TipoStockDB tipoStockDB;
    public ArticuloDB articuloDB;
    public StockDB stockDB;
    public ActividadOperativaDB actividadOperativaDB;
    public TipoBrazoDB tipoBrazoDB;
    public TipoBalastoDB tipoBalastoDB;
    public TipoBaseFotoceldaDB tipoBaseFotoceldaDB;
    public ControlEncendidoDB controlEncendidoDB;
    public TipoInstalacionRedDB tipoInstalacionRedDB;
    public BodegaDB bodegaDB;
    public EquipoDB equipoDB;
    public ArchivoActividadDB archivoActividadDB;
    public MovimientoArticuloDB movimientoArticuloDB;
    public CentroCostoDB centroCostoDB;
    public ElementoDesmontadoDB elementoDesmontadoDB;
    public VatiajeDesmontadoDB vatiajeDesmontadoDB;
    public ClasePerfilDB clasePerfilDB;
    public FabricantePosteDB fabricantePosteDB;
    public FabricanteElementoDB fabricanteElementoDB;

    public MiBaseDatos(SQLiteDatabase sqLiteDatabase){
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
        archivoActividadDB = new ArchivoActividadDB(sqLiteDatabase);
        movimientoArticuloDB    = new MovimientoArticuloDB(sqLiteDatabase);
        centroCostoDB   = new CentroCostoDB(sqLiteDatabase);
        elementoDesmontadoDB = new ElementoDesmontadoDB(sqLiteDatabase);
        vatiajeDesmontadoDB = new VatiajeDesmontadoDB(sqLiteDatabase);
        clasePerfilDB   = new ClasePerfilDB(sqLiteDatabase);
        fabricantePosteDB   = new FabricantePosteDB(sqLiteDatabase);
        fabricanteElementoDB = new FabricanteElementoDB(sqLiteDatabase);

    }

    public void crearTabla(){
        tipologiaDB.crearTabla();
        mobiliarioDB.crearTabla();
        referenciaMobiliarioDB.crearTabla();
        claseViaDB.crearTabla();
        estadoMobiliarioDB.crearTabla();
        estadoActividadDB.crearTabla();
        vatiajeDB.crearTabla();
        unidadMedidaDB.crearTabla();
        tipoRedDB.crearTabla();
        tipoPosteDB.crearTabla();
        tipoInterseccionDB.crearTabla();
        tipoEspacioDB.crearTabla();
        municipioDB.crearTabla();
        procesoSgcDB.crearTabla();
        barrioDB.crearTabla();
        tipoTensionDB.crearTabla();
        retenidaPosteDB.crearTabla();
        contratoDB.crearTabla();
        normaConstruccionPosteDB.crearTabla();
        tipoEstructuraDB.crearTabla();
        normaConstruccionRedDB.crearTabla();
        elementoDB.crearTabla();
        censoDB.crearTabla();
        censoArchivoDB.crearTabla();
        censoTipoArmadoDB.crearTabla();
        censoAsignadoDB.crearTabla();
        tipoReporteDanoDB.crearTabla();
        programaDB.crearTabla();
        tipoActividadDB.crearTabla();
        sentidoDB.crearTabla();
        actaContratoDB.crearTabla();
        proveedorDB.crearTabla();
        tipoEscenarioDB.crearTabla();
        tipoConductorElectricoDB.crearTabla();
        calibreDB.crearTabla();
        tipoStockDB.crearTabla();
        articuloDB.crearTabla();
        stockDB.crearTabla();
        actividadOperativaDB.crearTabla();
        tipoBrazoDB.crearTabla();
        tipoBalastoDB.crearTabla();
        tipoBaseFotoceldaDB.crearTabla();
        controlEncendidoDB.crearTabla();
        tipoInstalacionRedDB.crearTabla();
        bodegaDB.crearTabla();
        equipoDB.crearTabla();
        archivoActividadDB.crearTabla();
        movimientoArticuloDB.crearTabla();
        centroCostoDB.crearTabla();
        elementoDesmontadoDB.crearTabla();
        vatiajeDesmontadoDB.crearTabla();
        clasePerfilDB.crearTabla();
        fabricantePosteDB.crearTabla();
        fabricanteElementoDB.crearTabla();
        //Log.d("DataBase","create");
    }

    public void borrarTabla(){
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
        archivoActividadDB.borrarTabla();
        movimientoArticuloDB.borrarTabla();
        centroCostoDB.borrarTabla();
        elementoDesmontadoDB.borrarTabla();
        vatiajeDesmontadoDB.borrarTabla();
        clasePerfilDB.borrarTabla();
        fabricantePosteDB.borrarTabla();
        fabricanteElementoDB.borrarTabla();
    }

    public void eliminarDatos(){
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
        equipoDB.eliminarDatos();
        archivoActividadDB.eliminarDatos();
        movimientoArticuloDB.eliminarDatos();
        centroCostoDB.eliminarDatos();
        elementoDesmontadoDB.eliminarDatos();
        vatiajeDesmontadoDB.eliminarDatos();
        clasePerfilDB.eliminarDatos();
        fabricantePosteDB.eliminarDatos();
        fabricanteElementoDB.eliminarDatos();
    }
}
