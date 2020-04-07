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
        db.execSQL("create table "+ Constantes.TABLA_CENSO_ASIGNADO + "(id_censo INTEGER NOT NULL ,id_municipio INTEGER NOT NULL,id_proceso_sgc INTEGER NOT NULL)");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_ASIGNADO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof CensoAsignado) {
            censoAsignado = (CensoAsignado) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_municipio",censoAsignado.getId_municipio());
            contentValues.put("id_proceso_sgc",censoAsignado.getId_proceso_sgc());
            contentValues.put("id_censo",censoAsignado.getId());
            try {
                db.insertWithOnConflict(Constantes.TABLA_CENSO_ASIGNADO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            }catch (SQLiteException e){
                Log.d("ErrorI",""+e.getMessage());
                return false;
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
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_ASIGNADO+" ORDER BY id_censo";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int idMunicipio,int idProcesoSGC) {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_ASIGNADO+" where id_municipio="+idMunicipio+" and id_proceso_sgc="+idProcesoSGC
                +" ORDER BY id_censo";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
