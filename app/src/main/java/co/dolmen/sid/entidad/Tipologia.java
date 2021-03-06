package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Tipologia extends ProcesoSgc implements Serializable {
    int idTipologia;
    String descripcionTipologia;
    ProcesoSgc procesoSgc;

    public Tipologia(){
        super();
    }

    public Tipologia(int idTipologia,String descripcionTipologia){
        super();
        this.idTipologia = idTipologia;
        this.descripcionTipologia = descripcionTipologia;
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
