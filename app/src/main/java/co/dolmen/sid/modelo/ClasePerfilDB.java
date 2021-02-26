package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ClasePerfil;

public class ClasePerfilDB extends ClasePerfil implements DatabaseDDL,DatabaseDLM {

    SQLiteDatabase db;
    String sql;
    ClasePerfil clasePerfil;

    public ClasePerfilDB(SQLiteDatabase sqLiteDatabase){
        clasePerfil = new ClasePerfil();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_CLASE_PERFIL +"(" +
                "_id INTEGER PRIMARY KEY," +
                "descripcion VARCHAR(45) NOT NULL" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CLASE_PERFIL);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ClasePerfil) {
            clasePerfil = (ClasePerfil) o;
            Cursor result = consultarId(clasePerfil.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", clasePerfil.getId());
                contentValues.put("descripcion", clasePerfil.getDescripcion());
                db.insert(Constantes.TABLA_CLASE_PERFIL, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CLASE_PERFIL);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_CLASE_PERFIL+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_CLASE_PERFIL+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
