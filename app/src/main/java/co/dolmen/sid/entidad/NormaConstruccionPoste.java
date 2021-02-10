package co.dolmen.sid.entidad;

import java.io.Serializable;

public class NormaConstruccionPoste implements Serializable {
    int id;
    String descripcion;
    TipoPoste tipoPoste;

    public NormaConstruccionPoste() {
        this.tipoPoste = new TipoPoste();
    }

    public NormaConstruccionPoste(int id, String descripcion, TipoPoste tipoPoste) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipoPoste = tipoPoste;
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
