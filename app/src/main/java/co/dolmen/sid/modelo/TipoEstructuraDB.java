package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoEstructura;

public class TipoEstructuraDB extends TipoEstructura implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;

    public TipoEstructuraDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_TIPO_ESTRUCTURA+"( _id INTEGER PRIMARY KEY,id_tipo_tension INTEGER, descripcion VARCHAR(80));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_ESTRUCTURA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoEstructura) {
            TipoEstructura tipoEstructura = (TipoEstructura) o;
            Cursor result = consultarId(tipoEstructura.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoEstructura.getId());
                contentValues.put("id_tipo_tension", tipoEstructura.getTipoTension().getId());
                contentValues.put("descripcion", tipoEstructura.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_ESTRUCTURA, null, contentValues);
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
        return null;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_ESTRUCTURA+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idTipoTension){
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_ESTRUCTURA+" WHERE id_tipo_tension="+idTipoTension;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
