package co.dolmen.sid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
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
        Log.d("DataBase","create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
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
        //onCreate(sqLiteDatabase);
        Log.d("DataBase","update");

    }
}