package co.dolmen.sid.entidad;

import java.io.Serializable;

public class EstadoMobiliario extends ProcesoSgc implements Serializable {
    int id;
    String descripcion;

    public EstadoMobiliario(){
        super();
    }

    public EstadoMobiliario(int id,String descripcion){
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getIdEstadoMobiliario() {
        return id;
    }

    public void setIdEstadoMobiliario(int id) {
        this.id = id;
    }

    public String getDescripcionEstadoMobiliario() {
        return descripcion;
    }

    public void setDescripcionEstadoMobiliario(String descripcion) {
        this.descripcion = descripcion;
    }
}
