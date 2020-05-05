package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoActividad;

public class TipoActividadDB extends TipoActividad implements  DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public TipoActividadDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_ACTIVIDAD +"(_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,descripcion VARCHAR(80));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_ACTIVIDAD);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoActividad) {
            TipoActividad tipoActividad = (TipoActividad) o;
            Cursor result = consultarId(tipoActividad.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoActividad.getId());
                contentValues.put("id_proceso_sgc", tipoActividad.getProcesoSgc().getId());
                contentValues.put("descripcion", tipoActividad.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_ACTIVIDAD, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_ACTIVIDAD+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idProceso){
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_ACTIVIDAD+" WHERE id_proceso_sgc="+idProceso+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPO_ACTIVIDAD+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
