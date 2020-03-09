package co.dolmen.sid.modelo;

import android.database.Cursor;

public interface DatabaseDLM <Entidad> {
    boolean agregarDatos(Entidad E);
    void actualizarDatos(Entidad E);
    void eliminarDatos();
    Cursor consultarTodo();
}
