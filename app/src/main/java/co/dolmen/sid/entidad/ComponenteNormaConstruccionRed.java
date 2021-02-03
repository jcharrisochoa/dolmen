package co.dolmen.sid.entidad;

import java.io.Serializable;

public class ComponenteNormaConstruccionRed implements Serializable {

    NormaConstruccionRed normaConstruccionRed;
    TipoRed tipoRed;
    TipoTension tipoTension;
    Calibre calibre;

    public ComponenteNormaConstruccionRed(TipoRed tipoRed, TipoTension tipoTension,Calibre calibre,NormaConstruccionRed normaConstruccionRed) {
        this.normaConstruccionRed = normaConstruccionRed;
        this.tipoRed = tipoRed;
        this.tipoTension = tipoTension;
        this.calibre = calibre;
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

    public Calibre getCalibre() {
        return calibre;
    }

    public void setCalibre(Calibre calibre) {
        this.calibre = calibre;
    }
}
