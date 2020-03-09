package co.dolmen.sid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;

public class BaseDatos extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private ClaseViaDB claseViaDB;
    private EstadoMobiliarioDB estadoMobiliarioDB;
    private EstadoActividadDB estadoActividadDB;


    public BaseDatos(Context context){
        super(context, Constantes.NOMBRE_BASEDATOS,null,Constantes.VERSION_BASEDATOS);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        claseViaDB = new ClaseViaDB(sqLiteDatabase);
        claseViaDB.crearTabla();

        estadoMobiliarioDB = new EstadoMobiliarioDB(sqLiteDatabase);
        estadoMobiliarioDB.crearTabla();

        estadoActividadDB = new EstadoActividadDB(sqLiteDatabase);
        estadoActividadDB.crearTabla();

        /*db.execSQL(ConsultasSQL.CREAR_TABLA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_TIPOLOGIA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_REFERENCIA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_BARRIO);
        db.execSQL(ConsultasSQL.CREATE_SENTIDO);
        db.execSQL(ConsultasSQL.CREATE_UNIDAD_MEDIDA);
        db.execSQL(ConsultasSQL.CREATE_TIPO_RED);
        db.execSQL(ConsultasSQL.CREATE_TIPO_POSTE);
        db.execSQL(ConsultasSQL.CREATE_CLASE_VIA);
        db.execSQL(ConsultasSQL.CREATE_TIPO_VIA);
        db.execSQL(ConsultasSQL.CREATE_PROCESO);
        db.execSQL(ConsultasSQL.CREATE_MUNICIPIO);
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
        claseViaDB.borrarTabla();
        estadoMobiliarioDB.borrarTabla();
        estadoActividadDB.borrarTabla();
        onCreate(sqLiteDatabase);
        /*db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_ESTADO_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_TIPOLOGIA_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_REFERNCIA_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_BARRIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_SENTIDO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_UNIDAD_MEDIDA));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_TIPOLOGIA_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_ESTADO_MOBILIARIO));
        db.execSQL(ConsultasSQL.dropTable(ConsultasSQL.TABLA_ESTADO_MOBILIARIO));*/
        Log.d("DataBase","update");

    }
}