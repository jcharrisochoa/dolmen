package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Contrato;

public class ContratoDB extends Contrato implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;
    public ContratoDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_CONTRATO+"( _id INTEGER PRIMARY KEY,id_municipio INTEGER, id_proceso_sgc INTEGER, descripcion VARCHAR(80));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CONTRATO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Contrato) {
            Contrato contrato = (Contrato) o;
            Cursor result = consultarId(contrato.getId());
            if(result.getCount() == 0) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", contrato.getId());
                contentValues.put("id_municipio", contrato.getMunicipio().getId());
                contentValues.put("id_proceso_sgc", contrato.getProcesoSgc().getId());
                contentValues.put("descripcion", contrato.getDescripcion());

               db.insert(Constantes.TABLA_CONTRATO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CONTRATO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT *  FROM "+ Constantes.TABLA_CONTRATO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_CONTRATO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idMunicipio,int idProceso){
        this.sql = "SELECT *  FROM "+ Constantes.TABLA_CONTRATO+
                " WHERE id_municipio="+idMunicipio+
                " and id_proceso_sgc="+idProceso+
                " ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
