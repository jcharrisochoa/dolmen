package co.dolmen.sid.entidad;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.modelo.DatabaseDDL;
import co.dolmen.sid.modelo.DatabaseDLM;

public class ElementoDesmontadoDB implements DatabaseDDL, DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public ElementoDesmontadoDB (SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_ELEMENTO_DESMONTADO +"(_id INTEGER PRIMARY KEY,id_actividad INTEGER NOT NULL,id_elemento INTEGER NOT NULL);";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ELEMENTO_DESMONTADO);
    }

    @Override
    public boolean agregarDatos(Object E) {
        return false;
    }

    public boolean agregarDatos(int id_actividad,int id_elemento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_elemento", id_elemento);
        contentValues.put("id_actividad", id_actividad);
        db.insert(Constantes.TABLA_ELEMENTO, null, contentValues);
        return true;
    }

    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ELEMENTO_DESMONTADO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "select * from "+Constantes.TABLA_ELEMENTO_DESMONTADO;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
