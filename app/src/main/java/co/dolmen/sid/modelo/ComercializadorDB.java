package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.Comercializador;

public class ComercializadorDB extends Comercializador implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    Comercializador comercializador;

    public ComercializadorDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        comercializador = new Comercializador();
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_COMERCIALIZADOR_ENERGIA +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_COMERCIALIZADOR_ENERGIA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Comercializador) {
            comercializador = (Comercializador) o;
            Cursor result = consultarId(comercializador.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", comercializador.getId());
                contentValues.put("descripcion", comercializador.getDescripcion());
                db.insert(Constantes.TABLA_COMERCIALIZADOR_ENERGIA, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_COMERCIALIZADOR_ENERGIA);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_COMERCIALIZADOR_ENERGIA+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_COMERCIALIZADOR_ENERGIA+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
