package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Municipio;

public class MunicipioDB extends Municipio implements DatabaseDDL,DatabaseDLM {
    String sql;
    SQLiteDatabase db;
    Municipio municipio;

    public MunicipioDB(SQLiteDatabase sqLiteDatabase){
        this.municipio = new Municipio();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_MUNICIPIO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLA_MUNICIPIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Municipio) {
            municipio = (Municipio) o;
            Cursor result = consultarId(municipio.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", municipio.getId());
                contentValues.put("descripcion", municipio.getDescripcion());
                db.insert(Constantes.TABLA_MUNICIPIO, null, contentValues);
            }
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_MUNICIPIO+" ORDER BY  descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_MUNICIPIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
