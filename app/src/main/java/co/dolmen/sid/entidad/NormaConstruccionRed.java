package co.dolmen.sid.entidad;

import java.io.Serializable;

public class NormaConstruccionRed  implements Serializable {
    int id;
    String descripcion;
    String norma;

    TipoEstructura tipoEstructura;
    Comercializador comercializador;

    public NormaConstruccionRed(){
        tipoEstructura = new TipoEstructura();
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

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    public Comercializador getComercializador() {
        return comercializador;
    }

    public void setComercializador(Comercializador comercializador) {
        this.comercializador = comercializador;
    }
}
