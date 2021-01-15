package co.dolmen.sid.modelo;

import android.database.Cursor;

import co.dolmen.sid.entidad.Stock;

public class TipoBrazoDB extends Stock implements DatabaseDDL,DatabaseDLM{
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
