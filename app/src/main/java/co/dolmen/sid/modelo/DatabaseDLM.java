package co.dolmen.sid.modelo;

public interface DatabaseDLM <Entidad> {
    boolean agregarDatos(Entidad E);
    void actualizarDatos(Entidad E);
    void eliminarDatos();
    void consultarTodo();
}
