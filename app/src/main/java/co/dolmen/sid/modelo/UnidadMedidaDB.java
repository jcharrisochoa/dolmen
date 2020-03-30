package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.UnidadMedida;

public class UnidadMedidaDB extends UnidadMedida implements DatabaseDLM,DatabaseDDL {
    private SQLiteDatabase db;
    private String sql;
    private UnidadMedida unidadMedida;

    public UnidadMedidaDB (SQLiteDatabase sqLiteDatabase){
        this.unidadMedida = new UnidadMedida();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_UNIDAD_MEDIDA +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_UNIDAD_MEDIDA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof UnidadMedida) {
            unidadMedida = (UnidadMedida) o;
            Cursor result = consultarId(unidadMedida.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", unidadMedida.getId());
                contentValues.put("descripcion", unidadMedida.getDescripcion());
                db.insert(Constantes.TABLA_UNIDAD_MEDIDA, null, contentValues);
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

    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_UNIDAD_MEDIDA+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_UNIDAD_MEDIDA+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
