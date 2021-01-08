package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.TipoStock;

public class TipoStockDB extends TipoStock implements DatabaseDDL, DatabaseDLM {

    private SQLiteDatabase db;
    private String sql;
    private TipoStock tipoStock;

    public TipoStockDB (SQLiteDatabase sqLiteDatabase){
        this.tipoStock = new TipoStock();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_STOCK +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_STOCK);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoStock) {
            tipoStock = (TipoStock) o;
            Cursor result = consultarId(tipoStock.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoStock.getId());
                contentValues.put("descripcion", tipoStock.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_STOCK, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_TIPO_STOCK);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_STOCK+" ORDER BY descripcion desc";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_STOCK+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

}
