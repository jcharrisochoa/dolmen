package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoRed;

public class TipoRedDB extends TipoRed implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    private TipoRed tipoRed;

    public TipoRedDB (SQLiteDatabase sqLiteDatabase){
        this.tipoRed = new TipoRed();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_RED +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_RED);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoRed) {
            tipoRed = (TipoRed) o;
            Cursor result = consultarId(tipoRed.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoRed.getId());
                contentValues.put("descripcion", tipoRed.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_RED, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_RED+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_RED+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
