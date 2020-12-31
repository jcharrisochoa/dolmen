package co.dolmen.sid.entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Programa implements Serializable {
    int id;
    String descripcion;
    Date fechaPrograma;
    Municipio municipio;
    ProcesoSgc procesoSgc;
    List<ActividadOperativa> misActividadOperativa = new ArrayList<ActividadOperativa>();

    public Programa(){

    }

    public Programa(int id,String descripcion,Date fechaPrograma,Municipio municipio, ProcesoSgc procesoSgc){
        this.id = id;
        this.descripcion = descripcion;
        this.fechaPrograma = fechaPrograma;
        this.municipio = municipio;
        this.procesoSgc = procesoSgc;
    }

    public int getId() {
        return id;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaPrograma() {
        return fechaPrograma;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaPrograma(Date fechaPrograma) {
        this.fechaPrograma = fechaPrograma;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc) {
        this.procesoSgc = procesoSgc;
    }

    public void agregarActividad(ActividadOperativa actividadOperativa){
        misActividadOperativa.add(actividadOperativa);
    }
    public List<ActividadOperativa> getMisActividadOperativa(){
        return misActividadOperativa;
    }
}
