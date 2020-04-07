package co.dolmen.sid.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Elemento;

public class CensoTipoArmadoDB implements DatabaseDLM,DatabaseDDL   {
    SQLiteDatabase db;
    String sql;
    Elemento elemento;

    public CensoTipoArmadoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        db.execSQL(
                "create table "+ Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO+
                        "("+
                        "id INTEGER PRIMARY KEY ASC NOT NULL,"+
                        "id_censo_tecnico INTEGER NOT NULL,"+
                        "id_tipo_red INTEGER NOT NULL,"+
                        "id_norma_construccion_red INTEGER NOT NULL)"
        );
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_TECNICO_TIPO_ARMADO);
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
