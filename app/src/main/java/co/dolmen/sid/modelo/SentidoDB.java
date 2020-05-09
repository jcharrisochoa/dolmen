package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Sentido;


public class SentidoDB extends Sentido implements DatabaseDLM,DatabaseDDL {
    private SQLiteDatabase db;
    private String sql;
    private Sentido sentido;

    public SentidoDB (SQLiteDatabase sqLiteDatabase){
        this.sentido = new Sentido();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_SENTIDO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_SENTIDO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Sentido) {
            sentido = (Sentido) o;
            Cursor result = consultarId(sentido.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", sentido.getId());
                contentValues.put("descripcion", sentido.getDescripcion());
                db.insert(Constantes.TABLA_SENTIDO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_SENTIDO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_SENTIDO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_SENTIDO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
