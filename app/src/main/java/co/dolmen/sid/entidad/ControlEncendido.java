package co.dolmen.sid.entidad;

public class ControlEncendido {
    int id_control_encendido;
    String descripcion;

    public ControlEncendido(){}

    public ControlEncendido(int id_control_encendido, String descripcion) {
        this.id_control_encendido = id_control_encendido;
        this.descripcion = descripcion;
    }

    public int getId_control_encendido() {
        return id_control_encendido;
    }

    public void setId_control_encendido(int id_control_encendido) {
        this.id_control_encendido = id_control_encendido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
