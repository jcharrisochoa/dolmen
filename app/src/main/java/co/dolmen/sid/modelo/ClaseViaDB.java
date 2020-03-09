package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ClaseVia;

public class ClaseViaDB extends ClaseVia implements DatabaseDDL,DatabaseDLM{
    private SQLiteDatabase db;
    private String sql;
    private ClaseVia claseVia;

    public ClaseViaDB (SQLiteDatabase sqLiteDatabase){
        this.claseVia = new ClaseVia();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla(){
        this.sql = "create table "+ Constantes.TABLA_CLASE_VIA +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45),abreviatura VARCHAR(2));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla(){
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CLASE_VIA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ClaseVia) {
            claseVia = (ClaseVia) o;
            Cursor result = consultarId(claseVia.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", claseVia.getId());
                contentValues.put("descripcion", claseVia.getDescripcion());
                contentValues.put("abreviatura", claseVia.getAbreviatura());
                db.insert(Constantes.TABLA_CLASE_VIA, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_CLASE_VIA+" ORDER BY abreviatura";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CLASE_VIA+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
