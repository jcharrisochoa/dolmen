package co.dolmen.sid.entidad;

public class Contrato {
    int id;
    String descripcion;
    Municipio municipio;
    ProcesoSgc procesoSgc;

    public Contrato(){
        municipio = new Municipio();
        procesoSgc = new ProcesoSgc();
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
