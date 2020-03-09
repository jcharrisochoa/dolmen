package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoInterseccion;

public class TipoInterseccionDB extends TipoInterseccion implements DatabaseDDL,DatabaseDLM {
    String sql;
    SQLiteDatabase db;
    TipoInterseccion tipoInterseccion;

    public TipoInterseccionDB(SQLiteDatabase sqLiteDatabase){
        this.tipoInterseccion = new TipoInterseccion();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_INTERSECCION +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45),abreviatura VARCHAR(12));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLA_TIPO_INTERSECCION);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoInterseccion) {
            tipoInterseccion = (TipoInterseccion) o;
            Cursor result = consultarId(tipoInterseccion.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoInterseccion.getId());
                contentValues.put("descripcion", tipoInterseccion.getDescripcion());
                contentValues.put("abreviatura", tipoInterseccion.getAbreviatura());
                db.insert(Constantes.TABLA_TIPO_INTERSECCION, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_INTERSECCION+" order by abreviatura";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_INTERSECCION+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
