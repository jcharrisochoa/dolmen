package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.NormaConstruccionPoste;

public class NormaConstruccionPosteDB extends NormaConstruccionPoste implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;

    public NormaConstruccionPosteDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+"( _id INTEGER PRIMARY KEY,id_tipo_poste INTEGER, descripcion VARCHAR(80));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_NORMA_CONSTRUCCION_POSTE);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof NormaConstruccionPoste) {
            NormaConstruccionPoste normaConstruccionPoste = (NormaConstruccionPoste) o;
            Cursor result = consultarId(normaConstruccionPoste.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", normaConstruccionPoste.getId());
                contentValues.put("id_tipo_poste", normaConstruccionPoste.getTipoPoste().getId());
                contentValues.put("descripcion", normaConstruccionPoste.getDescripcion());
                db.insert(Constantes.TABLA_NORMA_CONSTRUCCION_POSTE, null, contentValues);
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
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idTipoPoste){
        this.sql = "SELECT * FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" WHERE id_tipo_poste="+idTipoPoste;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
