package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ReferenciaMobiliario;

public class ReferenciaMobiliarioDB extends ReferenciaMobiliario implements DatabaseDLM,DatabaseDDL {
    SQLiteDatabase db;
    String sql;
    public ReferenciaMobiliarioDB(SQLiteDatabase sqLiteDatabase) {
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_REFERNCIA_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_mobiliario INTEGER, descripcion VARCHAR(45));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_REFERNCIA_MOBILIARIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ReferenciaMobiliario) {
            ReferenciaMobiliario referenciaMobiliario = (ReferenciaMobiliario) o;
            Cursor result = consultarId(referenciaMobiliario.getIdReferenciaMobiliario());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", referenciaMobiliario.getIdReferenciaMobiliario());
                contentValues.put("id_mobiliario", referenciaMobiliario.getId());
                contentValues.put("descripcion", referenciaMobiliario.getDescripcionReferenciaMobiliario());
                db.insert(Constantes.TABLA_REFERNCIA_MOBILIARIO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_REFERNCIA_MOBILIARIO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idMobiliario) {
        this.sql = "SELECT * FROM "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" where id_mobiliario="+idMobiliario+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
