package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoStock implements Serializable {
    int id;
    String descripcion;

    public TipoStock(){}

    public TipoStock(int id, String descripcion) {
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
