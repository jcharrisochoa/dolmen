package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Tipologia;

public class TipologiaDB extends Tipologia implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;

    public TipologiaDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_TIPOLOGIA_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER, descripcion VARCHAR(45));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Tipologia) {
            Tipologia tipologia = (Tipologia) o;
            Cursor result = consultarId(tipologia.getIdTipologia());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipologia.getIdTipologia());
                contentValues.put("id_proceso_sgc", tipologia.getId());
                contentValues.put("descripcion", tipologia.getDescripcionTipologia());
                db.insert(Constantes.TABLA_TIPOLOGIA_MOBILIARIO, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idProceso){
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" WHERE id_proceso_sgc="+idProceso+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
