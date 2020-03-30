package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoPoste;

public class TipoPosteDB extends TipoPoste implements DatabaseDLM,DatabaseDDL {
    private SQLiteDatabase db;
    private String sql;
    private TipoPoste tipoPoste;

    public TipoPosteDB(SQLiteDatabase sqLiteDatabase) {
        this.tipoPoste = new TipoPoste();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_POSTE +"(_id INTEGER PRIMARY KEY,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLA_TIPO_POSTE);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoPoste) {
            tipoPoste = (TipoPoste) o;
            Cursor result = consultarId(tipoPoste.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoPoste.getId());
                contentValues.put("descripcion", tipoPoste.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_POSTE, null, contentValues);
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_TIPO_POSTE+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_TIPO_POSTE+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
