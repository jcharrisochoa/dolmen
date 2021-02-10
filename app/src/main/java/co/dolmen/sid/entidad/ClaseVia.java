package co.dolmen.sid.entidad;

import java.io.Serializable;

public class ClaseVia implements Serializable {
    int id;
    String descripcion;
    String abreviatura;

    public ClaseVia(){}

    public ClaseVia(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }
}
