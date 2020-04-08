package co.dolmen.sid.entidad;

public class CensoTipoArmado {
    private  long id_censo_tecnico;
    private  TipoRed tipoRed;
    private  NormaConstruccionRed normaConstruccionRed;

    public CensoTipoArmado(){

    }

    public long getId_censo_tecnico() {
        return id_censo_tecnico;
    }

    public void setId_censo_tecnico(long id_censo_tecnico) {
        this.id_censo_tecnico = id_censo_tecnico;
    }

    public TipoRed getTipoRed() {
        return tipoRed;
    }

    public void setTipoRed(TipoRed tipoRed) {
        this.tipoRed = tipoRed;
    }

    public NormaConstruccionRed getNormaConstruccionRed() {
        return normaConstruccionRed;
    }

    public void setNormaConstruccionRed(NormaConstruccionRed normaConstruccionRed) {
        this.normaConstruccionRed = normaConstruccionRed;
    }
}
