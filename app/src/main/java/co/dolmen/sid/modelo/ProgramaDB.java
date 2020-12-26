package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Programa;

public class ProgramaDB extends Programa implements  DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;


    public ProgramaDB(SQLiteDatabase db) {
        this.db = db;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_PROGRAMA +"("+
                "_id INTEGER PRIMARY KEY NOT NULL,"+
                "descripcion TEXT NOT NULL,"+
                "fecha_programa date not null,"+
                "id_proceso_sgc INTEGER NOT NULL," +
                "id_municipio INTEGER NOT NULL);";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_PROGRAMA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Programa) {
            Programa programa = (Programa) o;
            Cursor result = consultarId(programa.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", programa.getId());
                contentValues.put("descripcion",programa.getDescripcion());
                contentValues.put("fecha_programa",new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format( programa.getFechaPrograma()));
                contentValues.put("id_proceso_sgc", programa.getProcesoSgc().getId());
                contentValues.put("id_municipio", programa.getMunicipio().getId());
                db.insert(Constantes.TABLA_PROGRAMA, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_PROGRAMA);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_PROGRAMA;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idMunicipio, int idProcesoSGC) {
        this.sql = "SELECT * FROM "+Constantes.TABLA_PROGRAMA+" WHERE id_municipio= "+idMunicipio +" AND id_proceso_sgc="+idProcesoSGC+" ORDER BY _id";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_PROGRAMA+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
