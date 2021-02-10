package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.CentroCosto;

public class CentroCostoDB extends CentroCosto implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;
    CentroCosto centroCosto;
    public CentroCostoDB(SQLiteDatabase sqLiteDatabase){
        centroCosto = new CentroCosto();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_CENTRO_COSTO +"(" +
                "_id INTEGER PRIMARY KEY," +
                "codigo INTEGER NOT NULL ," +
                "descripcion VARCHAR(45) NOT NULL" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENTRO_COSTO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof CentroCosto) {
            centroCosto = (CentroCosto) o;
            Cursor result = consultarId(centroCosto.getIdCentroCosto());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", centroCosto.getIdCentroCosto());
                contentValues.put("codigo", centroCosto.getCodigo());
                contentValues.put("descripcion", centroCosto.getDescripcionCentroCosto());
                db.insert(Constantes.TABLA_CENTRO_COSTO, null, contentValues);
            }
            result.close();
            return true;
        }
        else
            return false;
    }

    @Override
    public void actualizarDatos(Object o) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENTRO_COSTO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_CENTRO_COSTO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CENTRO_COSTO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
