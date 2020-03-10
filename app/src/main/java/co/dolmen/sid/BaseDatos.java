package co.dolmen.sid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.security.PrivateKey;

import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.TipoEspacio;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
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


    public BaseDatos(Context context){
        super(context, Constantes.NOMBRE_BASEDATOS,null,Constantes.VERSION_BASEDATOS);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //onUpgrade(sqLiteDatabase,0,0);
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

        /*db.execSQL(ConsultasSQL.CREAR_TABLA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_TIPOLOGIA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_REFERENCIA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREATE_SENTIDO);
        db.execSQL(ConsultasSQL.CREATE_TIPO_ACTIVIDAD);
        db.execSQL(ConsultasSQL.CREATE_TIPO_REPORTE);
        db.execSQL(ConsultasSQL.CREATE_CONTRATO);
        db.execSQL(ConsultasSQL.CREATE_ACTA_CONTRATO);
        db.execSQL(ConsultasSQL.CREATE_PROVEEDOR);
        db.execSQL(ConsultasSQL.CREATE_PROGRAMA);*/
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
        Log.d("DataBase","update");

    }
}