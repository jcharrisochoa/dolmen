package co.dolmen.sid;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.text.Editable;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.ConsultasSQL;

public class BaseDatos extends SQLiteOpenHelper {
    private String sql;
    private SQLiteDatabase db;

    public BaseDatos(Context context){
        super(context, Constantes.NOMBRE_BASEDATOS,null,Constantes.VERSION_BASEDATOS);
        //db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL(ConsultasSQL.CREAR_TABLA_ESTADO_MOBILILARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_TIPOLOGIA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_MOBILIARIO);
        db.execSQL(ConsultasSQL.CREAR_TABLA_REFERENCIA);
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
        db.execSQL(ConsultasSQL.CREATE_PROGRAMA);
        Log.d("DataBase","create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("DataBase","update");
    }
}