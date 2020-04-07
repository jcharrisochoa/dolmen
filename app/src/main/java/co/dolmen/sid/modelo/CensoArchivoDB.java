package co.dolmen.sid.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Elemento;

public class CensoArchivoDB implements DatabaseDLM,DatabaseDDL   {
    SQLiteDatabase db;
    String sql;
    Elemento elemento;

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
    public boolean agregarDatos(Object E) {
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
