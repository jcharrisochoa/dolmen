package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoBrazo;

public class TipoBrazoDB extends TipoBrazo implements DatabaseDDL,DatabaseDLM{
    private SQLiteDatabase db;
    private String sql;

    public TipoBrazoDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_BRAZO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(80));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_BRAZO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoBrazo) {
            TipoBrazo tipoBrazo = (TipoBrazo) o;
            Cursor result = consultarId(tipoBrazo.getidTipoBrazo());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoBrazo.getidTipoBrazo());
                contentValues.put("descripcion", tipoBrazo.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_BRAZO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_TIPO_BRAZO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_BRAZO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPO_BRAZO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
