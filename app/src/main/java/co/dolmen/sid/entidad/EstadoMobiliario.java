package co.dolmen.sid.entidad;

public class EstadoMobiliario extends ProcesoSgc {
    int id;
    String descripcion;

    public EstadoMobiliario(){
        super();
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
