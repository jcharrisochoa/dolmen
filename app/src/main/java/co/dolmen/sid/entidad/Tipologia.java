package co.dolmen.sid.entidad;

public class Tipologia extends ProcesoSgc {
    int idTipologia;
    String descripcionTipologia;
    ProcesoSgc procesoSgc;

    public Tipologia(){
        super();
    }

    public int getIdTipologia() {
        return idTipologia;
    }

    public void setIdTipologia(int idTipologia) {
        this.idTipologia = idTipologia;
    }

    public String getDescripcionTipologia() {
        return descripcionTipologia;
    }

    public void setDescripcionTipologia(String descripcionTipologia) {
        this.descripcionTipologia = descripcionTipologia;
    }

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc) {
        this.procesoSgc = procesoSgc;
    }
}
