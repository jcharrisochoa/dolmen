package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Vatiaje;

public class VatiajeDB extends Vatiaje implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    private Vatiaje vatiaje;

    public VatiajeDB (SQLiteDatabase sqLiteDatabase){
        this.vatiaje = new Vatiaje();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_POTENCIA_TIPO_LUZ +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_POTENCIA_TIPO_LUZ);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Vatiaje) {
            vatiaje = (Vatiaje) o;
            Cursor result = consultarId(vatiaje.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", vatiaje.getId());
                contentValues.put("descripcion", vatiaje.getDescripcion());
                db.insert(Constantes.TABLA_POTENCIA_TIPO_LUZ, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_POTENCIA_TIPO_LUZ+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_POTENCIA_TIPO_LUZ+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
