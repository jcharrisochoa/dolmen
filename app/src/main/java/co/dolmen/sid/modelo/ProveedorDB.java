package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Proveedor;

public class ProveedorDB extends Proveedor implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    Proveedor proveedor;

    public ProveedorDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        this.proveedor = new Proveedor();
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_PROVEEDOR +"(_id INTEGER PRIMARY KEY,nombre VARCHAR(85));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_PROVEEDOR);
    }

    public void iniciarTransaccion(){
        db.beginTransaction();
    }

    public void finalizarTransaccion(){
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Proveedor) {
            proveedor = (Proveedor) o;
            Cursor result = consultarId(proveedor.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", proveedor.getId());
                contentValues.put("nombre", proveedor.getNombre());
                db.insert(Constantes.TABLA_PROVEEDOR, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_PROVEEDOR);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_PROVEEDOR+" ORDER BY nombre";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_PROVEEDOR+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
