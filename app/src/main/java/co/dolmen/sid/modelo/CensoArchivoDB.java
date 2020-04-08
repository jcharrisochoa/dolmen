package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.CensoArchivo;
import co.dolmen.sid.entidad.Elemento;

public class CensoArchivoDB extends CensoArchivo implements DatabaseDLM,DatabaseDDL   {
    SQLiteDatabase db;
    String sql;
    CensoArchivo censoArchivo;

    public CensoArchivoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL(
                "create table "+ Constantes.TABLA_CENSO_TECNICO_ARCHIVO+
                        "("+
                        "id INTEGER PRIMARY KEY ASC NOT NULL,"+
                        "id_censo_tecnico INTEGER NOT NULL,"+
                        "archivo TEXT NOT NULL)"
        );
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_TECNICO_ARCHIVO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof CensoArchivo) {
            censoArchivo = (CensoArchivo) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_censo_tecnico", censoArchivo.getId_censo_tecnico());
            contentValues.put("archivo", censoArchivo.getArchivo());
            try {
                db.insertWithOnConflict(Constantes.TABLA_CENSO_TECNICO_ARCHIVO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
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
        return null;
    }
}
