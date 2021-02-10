package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoPoste implements Serializable {
    int id;
    String descripcion;

    public TipoPoste(){}


    public TipoPoste(int id, String descripcion) {
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
