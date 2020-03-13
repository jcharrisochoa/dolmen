package co.dolmen.sid.entidad;

public class TipoEstructura {
    int id;
    String descripcion;
    TipoTension tipoTension;
    public TipoEstructura(){
        tipoTension = new TipoTension();
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

    public TipoTension getTipoTension() {
        return tipoTension;
    }

    public void setTipoTension(TipoTension tipoTension) {
        this.tipoTension = tipoTension;
    }
}
