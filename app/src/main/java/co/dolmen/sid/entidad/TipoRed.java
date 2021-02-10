package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoRed  implements Serializable {
    int id;
    String descripcion;

    public TipoRed(){

    }

    public TipoRed(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
