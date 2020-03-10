package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.RetenidaPoste;

public class RetenidaPosteDB extends RetenidaPoste implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;
    RetenidaPoste retenidaPoste;

    public RetenidaPosteDB(SQLiteDatabase sqLiteDatabase){
        this.retenidaPoste = new RetenidaPoste();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_RETENIDA_POSTE+"( _id INTEGER PRIMARY KEY,descripcion VARCHAR(80),norma VARCHAR(12));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_RETENIDA_POSTE);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof RetenidaPoste) {
            retenidaPoste = (RetenidaPoste) o;
            Cursor result = consultarId(retenidaPoste.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", retenidaPoste.getId());
                contentValues.put("descripcion", retenidaPoste.getDescripcion());
                contentValues.put("norma", retenidaPoste.getNorma());
                db.insert(Constantes.TABLA_RETENIDA_POSTE, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_RETENIDA_POSTE+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_RETENIDA_POSTE+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
