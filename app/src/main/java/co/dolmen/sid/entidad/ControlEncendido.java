package co.dolmen.sid.entidad;

import java.io.Serializable;

public class ControlEncendido implements Serializable {
    int idControlEncendido;
    String descripcion;

    public ControlEncendido(){}

    public ControlEncendido(int idControlEncendido, String descripcion) {
        this.idControlEncendido = idControlEncendido;
        this.descripcion = descripcion;
    }

    public int getidControlEncendido() {
        return idControlEncendido;
    }

    public void setidControlEncendido(int idControlEncendido) {
        this.idControlEncendido = idControlEncendido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
