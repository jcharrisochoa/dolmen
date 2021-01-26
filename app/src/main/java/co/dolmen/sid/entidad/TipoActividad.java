package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoActividad implements Serializable {
    int id;
    String descripcion;
    ProcesoSgc procesoSgc;

    public TipoActividad(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public TipoActividad(){}

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

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc) {
        this.procesoSgc = procesoSgc;
    }
}
