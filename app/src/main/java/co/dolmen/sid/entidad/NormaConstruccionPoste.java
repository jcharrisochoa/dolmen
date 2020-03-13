package co.dolmen.sid.entidad;

public class NormaConstruccionPoste {
    int id;
    String descripcion;
    TipoPoste tipoPoste;

    public NormaConstruccionPoste() {
        this.tipoPoste = new TipoPoste();
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

    public TipoPoste getTipoPoste() {
        return tipoPoste;
    }

    public void setTipoPoste(TipoPoste tipoPoste) {
        this.tipoPoste = tipoPoste;
    }
}
