package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Articulo;
import co.dolmen.sid.entidad.TipoStock;

public class ArticuloDB extends Articulo implements DatabaseDDL,DatabaseDLM {

    private SQLiteDatabase db;
    private String sql;
    private Articulo articulo;

    public ArticuloDB (SQLiteDatabase sqLiteDatabase){
        this.articulo = new Articulo();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_ARTICULO +"(_id INTEGER PRIMARY KEY,descripcion TEXT);";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ARTICULO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Articulo) {
            articulo = (Articulo) o;
            Cursor result = consultarId(articulo.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", articulo.getId());
                contentValues.put("descripcion", articulo.getDescripcion());
                db.insert(Constantes.TABLA_ARTICULO, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_ARTICULO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_ARTICULO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
