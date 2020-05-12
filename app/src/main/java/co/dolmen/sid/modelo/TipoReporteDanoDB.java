package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.TipoReporteDano;

public class TipoReporteDanoDB extends TipoReporteDano implements  DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;

    public TipoReporteDanoDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_TIPO_REPORTE_DANO +"(_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,descripcion VARCHAR(80));";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_TIPO_REPORTE_DANO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof TipoReporteDano) {
            TipoReporteDano tipoReporteDano = (TipoReporteDano) o;
            Cursor result = consultarId(tipoReporteDano.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", tipoReporteDano.getId());
                contentValues.put("id_proceso_sgc", tipoReporteDano.getProcesoSgc().getId());
                contentValues.put("descripcion", tipoReporteDano.getDescripcion());
                db.insert(Constantes.TABLA_TIPO_REPORTE_DANO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_TIPO_REPORTE_DANO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_REPORTE_DANO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idProceso){
        this.sql = "SELECT * FROM "+Constantes.TABLA_TIPO_REPORTE_DANO+" WHERE id_proceso_sgc="+idProceso+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_TIPO_REPORTE_DANO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
