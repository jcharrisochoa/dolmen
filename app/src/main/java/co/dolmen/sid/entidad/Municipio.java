package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Municipio implements Serializable {
    int id;
    String descripcion;

    public Municipio(){}

    public Municipio(int id, String descripcion) {
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
