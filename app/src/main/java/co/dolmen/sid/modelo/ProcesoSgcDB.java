package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.ProcesoSgc;

public class ProcesoSgcDB extends ProcesoSgc implements DatabaseDLM,DatabaseDDL {
    String sql;
    SQLiteDatabase db;
    ProcesoSgc procesoSgc;

    public ProcesoSgcDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_PROCESO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLA_PROCESO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ProcesoSgc) {
            procesoSgc = (ProcesoSgc) o;
            Cursor result = consultarId(procesoSgc.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", procesoSgc.getId());
                contentValues.put("descripcion", procesoSgc.getDescripcion());
                db.insert(Constantes.TABLA_PROCESO, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_PROCESO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_PROCESO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
