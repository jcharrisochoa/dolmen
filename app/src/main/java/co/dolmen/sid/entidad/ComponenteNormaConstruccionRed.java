package co.dolmen.sid.entidad;

public class ComponenteNormaConstruccionRed {

    NormaConstruccionRed normaConstruccionRed;
    TipoRed tipoRed;
    TipoTension tipoTension;

    public ComponenteNormaConstruccionRed(TipoRed tipoRed, TipoTension tipoTension,NormaConstruccionRed normaConstruccionRed) {
        this.normaConstruccionRed = normaConstruccionRed;
        this.tipoRed = tipoRed;
        this.tipoTension = tipoTension;
    }

    public NormaConstruccionRed getNormaConstruccionRed() {
        return normaConstruccionRed;
    }

    public void setNormaConstruccionRed(NormaConstruccionRed normaConstruccionRed) {
        this.normaConstruccionRed = normaConstruccionRed;
    }

    public TipoRed getTipoRed() {
        return tipoRed;
    }

    public void setTipoRed(TipoRed tipoRed) {
        this.tipoRed = tipoRed;
    }

    public TipoTension getTipoTension() {
        return tipoTension;
    }

    public void setTipoTension(TipoTension tipoTension) {
        this.tipoTension = tipoTension;
    }

}
