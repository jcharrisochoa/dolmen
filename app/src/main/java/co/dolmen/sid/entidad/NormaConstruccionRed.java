package co.dolmen.sid.entidad;

public class NormaConstruccionRed {
    int id;
    String descripcion;
    TipoEstructura tipoEstructura;

    public NormaConstruccionRed(){
        tipoEstructura = new TipoEstructura();
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

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }
}
