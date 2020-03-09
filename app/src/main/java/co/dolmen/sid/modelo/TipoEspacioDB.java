package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoEspacio;

public class TipoEspacioDB extends TipoEspacio implements DatabaseDLM,DatabaseDDL {
    String sql;
    SQLiteDatabase db;
    TipoEspacio tipoEspacio;

    public TipoEspacioDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        this.tipoEspacio = new TipoEspacio();
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_ESPACIO +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLA_TIPO_ESPACIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoEspacio) {
            tipoEspacio = (TipoEspacio) o;
            Cursor result = consultarId(tipoEspacio.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoEspacio.getId());
                contentValues.put("descripcion", tipoEspacio.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_ESPACIO, null, contentValues);
            }
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_ESPACIO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_ESPACIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
