package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.FabricanteElemento;

public class FabricanteElementoDB extends FabricanteElemento implements DatabaseDLM,DatabaseDDL {
    SQLiteDatabase db;
    String sql;
    FabricanteElemento fabricanteElemento;

    public FabricanteElementoDB(SQLiteDatabase sqLiteDatabase){
        fabricanteElemento = new FabricanteElemento();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_FABRICANTE_ELEMENTO +"(" +
                "_id INTEGER PRIMARY KEY," +
                "descripcion VARCHAR(45) NOT NULL" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_FABRICANTE_ELEMENTO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof FabricanteElemento) {
            fabricanteElemento = (FabricanteElemento) o;
            Cursor result = consultarId(fabricanteElemento.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", fabricanteElemento.getId());
                contentValues.put("descripcion", fabricanteElemento.getDescripcion());
                db.insert(Constantes.TABLA_FABRICANTE_ELEMENTO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_FABRICANTE_ELEMENTO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_FABRICANTE_ELEMENTO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_FABRICANTE_ELEMENTO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
