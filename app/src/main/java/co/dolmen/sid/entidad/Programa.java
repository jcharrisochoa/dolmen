package co.dolmen.sid.entidad;

public class Programa {
    int id;
    Municipio municipio;
    ProcesoSgc procesoSgc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc) {
        this.procesoSgc = procesoSgc;
    }
}
