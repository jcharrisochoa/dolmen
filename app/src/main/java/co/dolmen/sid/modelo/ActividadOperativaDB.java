package co.dolmen.sid.modelo;

import android.database.Cursor;

import co.dolmen.sid.BaseDatos;
import co.dolmen.sid.entidad.ActividadOperativa;

public class ActividadOperativaDB extends ActividadOperativa implements DatabaseDDL,DatabaseDLM {
    @Override
    public void crearTabla() {

    }

    @Override
    public void borrarTabla() {

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
