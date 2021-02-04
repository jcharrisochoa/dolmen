package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ArchivoActividad;

public class ArchivoActividadDB extends ArchivoActividad implements DatabaseDDL,DatabaseDLM {

    SQLiteDatabase db;
    String sql;
    ArchivoActividad archivoActividad;

    public ArchivoActividadDB(SQLiteDatabase sqLiteDatabase) {
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA +"("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "id_actividad INTEGER NOT NULL," +
                "tipo VARCHAR(1) NOT NULL DEFAULT 'A'," +
                "archivo TEXT NOT NULL" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        long lastId;
        if(o instanceof ArchivoActividad) {
            archivoActividad = (ArchivoActividad) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_actividad", archivoActividad.getId_actividad());
            contentValues.put("tipo", archivoActividad.getTipo());
            contentValues.put("archivo", archivoActividad.getArchivo());
            try {
                lastId = db.insert(Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA, null, contentValues);
                Log.d(Constantes.TAG,"lastid="+lastId);
                return true;
            }
            catch (SQLiteException  e){
                Log.d(Constantes.TAG,"Error"+e.getMessage());
                e.printStackTrace();
                return false;
            }

        }
        else {
            Log.d(Constantes.TAG,"no es del tipo");
            return false;
        }
    }

    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int id_actividad) {
        this.sql = "SELECT * FROM "+Constantes.TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA+" WHERE id_actividad="+id_actividad;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
