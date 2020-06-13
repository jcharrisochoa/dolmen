package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Calibre;

public class CalibreDB extends Calibre implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;
    Calibre calibre;

    public CalibreDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        this.calibre = new Calibre();
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_CALIBRE +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CALIBRE);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Calibre) {
            calibre = (Calibre) o;
            Cursor result = consultarId(calibre.getId_calibre());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", calibre.getId_calibre());
                contentValues.put("descripcion", calibre.getDescripcion());
                db.insert(Constantes.TABLA_CALIBRE, null, contentValues);
            }
            result.close();
            return true;
        }
        else
            return false;
    }

    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {

    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_CALIBRE+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CALIBRE+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
