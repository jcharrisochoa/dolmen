package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import co.dolmen.sid.Constantes;

public class VatiajeDesmontadoDB implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public VatiajeDesmontadoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_VATIAJE_DESMONTADO +"(_id INTEGER PRIMARY KEY,id_actividad INTEGER NOT NULL,id_vatiaje INTEGER NOT NULL);";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_VATIAJE_DESMONTADO);
    }

    @Override
    public boolean agregarDatos(Object E) {
        return false;
    }

    public boolean agregarDatos(int id_actividad,int id_vatiaje){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_vatiaje", id_vatiaje);
        contentValues.put("id_actividad", id_actividad);
        try {
            long lastId = db.insert(Constantes.TABLA_VATIAJE_DESMONTADO, null, contentValues);
            return true;
        }
        catch (SQLiteException e){
            Log.d(Constantes.TAG,"Error"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_VATIAJE_DESMONTADO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "select * from "+Constantes.TABLA_VATIAJE_DESMONTADO;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int id_actividad) {
        this.sql = "select vd._id,vd.id_vatiaje ,pl.descripcion as vatiaje " +
                "from "+Constantes.TABLA_VATIAJE_DESMONTADO +" vd " +
                "join "+Constantes.TABLA_POTENCIA_TIPO_LUZ+" pl on(vd.id_vatiaje = pl._id) " +
                "where " +
                "vd.id_actividad="+id_actividad;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
