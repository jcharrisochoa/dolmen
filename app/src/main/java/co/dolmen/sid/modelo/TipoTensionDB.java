package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoTension;

public class TipoTensionDB extends TipoTension implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;
    TipoTension tipoTension;
    public TipoTensionDB(SQLiteDatabase sqLiteDatabase) {
        this.tipoTension = new TipoTension();
        this.db = sqLiteDatabase;

    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_TIPO_TENSION+"( _id INTEGER PRIMARY KEY,descripcion VARCHAR(45));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_TENSION);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoTension) {
            tipoTension = (TipoTension) o;
            Cursor result = consultarId(tipoTension.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoTension.getId());
                contentValues.put("descripcion", tipoTension.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_TENSION, null, contentValues);
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

    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_TENSION+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPO_TENSION+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
