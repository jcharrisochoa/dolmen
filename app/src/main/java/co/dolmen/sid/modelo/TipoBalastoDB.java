package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoBalasto;

public class TipoBalastoDB extends TipoBalasto implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public TipoBalastoDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_BALASTO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(80));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_BALASTO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoBalasto) {
            TipoBalasto tipoBalasto = (TipoBalasto) o;
            Cursor result = consultarId(tipoBalasto.getIdTipoBalasto());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoBalasto.getIdTipoBalasto());
                contentValues.put("descripcion", tipoBalasto.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_BALASTO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_TIPO_BALASTO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_BALASTO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPO_BALASTO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
