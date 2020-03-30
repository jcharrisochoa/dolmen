package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.EstadoMobiliario;

public class EstadoActividadDB extends EstadoActividad implements DatabaseDLM,DatabaseDDL {
    SQLiteDatabase db;
    String sql;
    EstadoActividad estadoActividad;

    public EstadoActividadDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        estadoActividad = new EstadoActividad();
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_ESTADO_ACTIVIDAD+"( _id INTEGER PRIMARY KEY, descripcion VARCHAR(80));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ESTADO_ACTIVIDAD);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof EstadoActividad) {
            estadoActividad = (EstadoActividad) o;
            Cursor result = consultarId(estadoActividad.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", estadoActividad.getId());
                contentValues.put("descripcion", estadoActividad.getDescripcion());
                db.insert(Constantes.TABLA_ESTADO_ACTIVIDAD, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_ESTADO_ACTIVIDAD+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_ESTADO_ACTIVIDAD+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
