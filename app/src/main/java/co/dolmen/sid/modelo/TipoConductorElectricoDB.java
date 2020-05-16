package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoConductorElectrico;

public class TipoConductorElectricoDB extends TipoConductorElectrico implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public TipoConductorElectricoDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_CONDUCTOR_ELECTRICO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CONDUCTOR_ELECTRICO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoConductorElectrico) {
            TipoConductorElectrico tipoConductorElectrico = (TipoConductorElectrico) o;
            Cursor result = consultarId(tipoConductorElectrico.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoConductorElectrico.getId());
                contentValues.put("descripcion", tipoConductorElectrico.getDescripcion());
                db.insert(Constantes.TABLA_CONDUCTOR_ELECTRICO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CONDUCTOR_ELECTRICO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_CONDUCTOR_ELECTRICO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CONDUCTOR_ELECTRICO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
