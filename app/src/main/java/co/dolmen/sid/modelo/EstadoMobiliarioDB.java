package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.EstadoMobiliario;

public class EstadoMobiliarioDB extends EstadoMobiliario implements DatabaseDLM,DatabaseDDL {
    SQLiteDatabase db;
    String sql;
    EstadoMobiliario estadoMobiliario;

    public EstadoMobiliarioDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        estadoMobiliario = new EstadoMobiliario();
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+Constantes.TABLA_ESTADO_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER, descripcion VARCHAR(45));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ESTADO_MOBILIARIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof EstadoMobiliario) {
            estadoMobiliario = (EstadoMobiliario) o;
            Cursor result = consultarId(estadoMobiliario.getIdEstadoMobiliario());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", estadoMobiliario.getIdEstadoMobiliario());
                contentValues.put("id_proceso_sgc", estadoMobiliario.getId());
                contentValues.put("descripcion", estadoMobiliario.getDescripcionEstadoMobiliario());
                db.insert(Constantes.TABLA_ESTADO_MOBILIARIO, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_ESTADO_MOBILIARIO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_ESTADO_MOBILIARIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
