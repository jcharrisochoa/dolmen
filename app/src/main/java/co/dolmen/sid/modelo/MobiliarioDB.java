package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Mobiliario;

public class MobiliarioDB extends Mobiliario implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;

    public MobiliarioDB(SQLiteDatabase sqLiteDatabase) {
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_tipologia INTEGER, descripcion VARCHAR(45));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_MOBILIARIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Mobiliario) {
            Mobiliario mobiliario = (Mobiliario) o;
            Cursor result = consultarId(mobiliario.getIdMobiliario());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", mobiliario.getIdMobiliario());
                contentValues.put("id_tipologia", mobiliario.getId());
                contentValues.put("descripcion", mobiliario.getDescripcionMobiliario());
                db.insert(Constantes.TABLA_MOBILIARIO, null, contentValues);
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
        this.sql = "SELECT _id FROM "+Constantes.TABLA_MOBILIARIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idTipologia){
        this.sql = "SELECT * FROM "+ Constantes.TABLA_MOBILIARIO+" WHERE id_tipologia="+idTipologia;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
