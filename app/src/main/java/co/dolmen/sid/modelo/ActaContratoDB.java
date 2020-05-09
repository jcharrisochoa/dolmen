package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ActaContrato;

public class ActaContratoDB extends ActaContrato implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    private ActaContrato actaContrato;

    public ActaContratoDB(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        this.actaContrato = new ActaContrato();
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_ACTA_CONTRATO +"(_id INTEGER PRIMARY KEY,id_contrato INTEGER,descripcion VARCHAR(45));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ACTA_CONTRATO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ActaContrato) {
            actaContrato = (ActaContrato) o;
            Cursor result = consultarId(actaContrato.getIdActa());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", actaContrato.getIdActa());
                contentValues.put("id_contrato", actaContrato.getContrato().getId());
                contentValues.put("descripcion", actaContrato.getDescripcionActa());
                db.insert(Constantes.TABLA_ACTA_CONTRATO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ACTA_CONTRATO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_ACTA_CONTRATO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_ACTA_CONTRATO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarActaContrato(int idContrato){
        this.sql = "SELECT * FROM "+Constantes.TABLA_ACTA_CONTRATO+" WHERE id_contrato="+idContrato;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
