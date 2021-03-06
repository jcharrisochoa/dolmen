package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.CensoAsignado;

public class CensoAsignadoDB extends CensoAsignado implements DatabaseDDL,DatabaseDLM {

    CensoAsignado censoAsignado;
    SQLiteDatabase db;
    String sql;

    public CensoAsignadoDB(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        this.censoAsignado = new CensoAsignado();
    }

    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_CENSO_ASIGNADO + "(id_censo INTEGER NOT NULL ,id_municipio INTEGER NOT NULL,id_proceso_sgc INTEGER NOT NULL,tipo VARCHAR(1))");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_ASIGNADO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof CensoAsignado) {
            censoAsignado = (CensoAsignado) o;
            Cursor result = consultarTodo(censoAsignado.getId_municipio(),censoAsignado.getId_proceso_sgc(),censoAsignado.getTipo());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id_municipio",censoAsignado.getId_municipio());
                contentValues.put("id_proceso_sgc",censoAsignado.getId_proceso_sgc());
                contentValues.put("id_censo",censoAsignado.getId());
                contentValues.put("tipo",censoAsignado.getTipo());
                try {
                    db.insertWithOnConflict(Constantes.TABLA_CENSO_ASIGNADO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
                }catch (SQLiteException e){
                    Log.d("ErrorI",""+e.getMessage());
                    return false;
                }
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENSO_ASIGNADO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_ASIGNADO+" ORDER BY id_censo";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int idMunicipio,int idProcesoSGC,String tipo) {
        this.sql = "SELECT * FROM "+
                Constantes.TABLA_CENSO_ASIGNADO+
                " where id_municipio="+idMunicipio+
                " and id_proceso_sgc="+idProcesoSGC+
                " and tipo='"+tipo+"'"+
                " ORDER BY id_censo";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
