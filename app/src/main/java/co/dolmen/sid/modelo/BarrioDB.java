package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Barrio;

public class BarrioDB extends Barrio implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;
    Barrio barrio;

    public BarrioDB(SQLiteDatabase sqLiteDatabase){
        barrio = new Barrio();
        db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_BARRIO+"( _id INTEGER PRIMARY KEY,id_municipio INTEGER, descripcion VARCHAR(120));");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_BARRIO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Barrio) {
            barrio = (Barrio) o;
            //Log.d("barrio",""+barrio.getIdBarrio());
            Cursor result = consultarId(barrio.getIdBarrio());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", barrio.getIdBarrio());
                contentValues.put("id_municipio", barrio.getId());
                contentValues.put("descripcion", barrio.getNombreBarrio());
                db.insert(Constantes.TABLA_BARRIO, null, contentValues);
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
        this.sql = "SELECT * FROM "+Constantes.TABLA_BARRIO+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idMunicipio){
        this.sql = "SELECT * FROM "+Constantes.TABLA_BARRIO+" WHERE id_municipio="+idMunicipio+" order by descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_BARRIO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
