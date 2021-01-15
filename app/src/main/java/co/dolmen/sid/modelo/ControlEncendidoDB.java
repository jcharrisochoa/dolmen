package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ControlEncendido;

public class ControlEncendidoDB extends ControlEncendido implements DatabaseDDL,DatabaseDLM {

    private SQLiteDatabase db;
    private String sql;

    public ControlEncendidoDB(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_CONTROL_ENCENDIDO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(80));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CONTROL_ENCENDIDO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ControlEncendido) {
            ControlEncendido controlEncendido = (ControlEncendido) o;
            Cursor result = consultarId(controlEncendido.getidControlEncendido());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", controlEncendido.getidControlEncendido());
                contentValues.put("descripcion", controlEncendido.getDescripcion());
                db.insert(Constantes.TABLA_CONTROL_ENCENDIDO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CONTROL_ENCENDIDO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_CONTROL_ENCENDIDO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CONTROL_ENCENDIDO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
