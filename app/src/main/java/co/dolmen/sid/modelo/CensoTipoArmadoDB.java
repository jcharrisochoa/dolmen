package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.CensoTipoArmado;
import co.dolmen.sid.entidad.Elemento;

public class CensoTipoArmadoDB extends CensoTipoArmado implements DatabaseDLM,DatabaseDDL   {
    SQLiteDatabase db;
    String sql;
    CensoTipoArmado censoTipoArmado;

    public CensoTipoArmadoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        this.censoTipoArmado = new CensoTipoArmado();
    }

    @Override
    public void crearTabla() {
        db.execSQL(
                "create table "+ Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO+
                        "("+
                        "id INTEGER PRIMARY KEY ASC NOT NULL,"+
                        "id_censo_tecnico INTEGER NOT NULL,"+
                        "id_tipo_red INTEGER NOT NULL,"+
                        "id_norma_construccion_red INTEGER NOT NULL,"+
                        "id_calibre INTEGER NOT NULL )"
        );
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof CensoTipoArmado) {
            censoTipoArmado = (CensoTipoArmado) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_censo_tecnico", censoTipoArmado.getId_censo_tecnico());
            contentValues.put("id_tipo_red", censoTipoArmado.getTipoRed().getId());
            contentValues.put("id_norma_construccion_red", censoTipoArmado.getNormaConstruccionRed().getId());
            contentValues.put("id_calibre", censoTipoArmado.getCalibre().getId_calibre());
            try {
                db.insertWithOnConflict(Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO);
    }

    public void eliminarDatos(int id_censo_tecnico) {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO+ " WHERE id_censo_tecnico="+id_censo_tecnico);
    }
    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int id_censo_tecnico) {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO+" WHERE id_censo_tecnico="+id_censo_tecnico;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}
