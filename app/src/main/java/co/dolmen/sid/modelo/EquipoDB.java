package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Bodega;
import co.dolmen.sid.entidad.Equipo;

public class EquipoDB extends Equipo implements DatabaseDDL,DatabaseDLM {

    SQLiteDatabase db;
    String sql;

    public  EquipoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;

    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_EQUIPO +"(_id INTEGER PRIMARY KEY,codigo VARCHAR(8),serial VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_EQUIPO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Equipo) {
            Equipo equipo = (Equipo) o;
            Cursor result = consultarId(equipo.getIdEquipo());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", equipo.getIdEquipo());
                contentValues.put("codigo", equipo.getCodigo());
                contentValues.put("serial", equipo.getSerial());
                db.insert(Constantes.TABLA_EQUIPO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_EQUIPO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_EQUIPO+" ORDER BY serial";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_EQUIPO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
