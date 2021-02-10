package co.dolmen.sid.entidad;

import java.io.Serializable;

public class CentroCosto implements Serializable {
    private int idCentroCosto;
    private int codigo;
    private String descripcionCentroCosto;

    public CentroCosto(){}

    public CentroCosto(int idCentroCosto, int codigo, String descripcionCentroCosto) {
        this.idCentroCosto = idCentroCosto;
        this.codigo = codigo;
        this.descripcionCentroCosto = descripcionCentroCosto;
    }

    public int getIdCentroCosto() {
        return idCentroCosto;
    }

    public void setIdCentroCosto(int idCentroCosto) {
        this.idCentroCosto = idCentroCosto;
    }

    public String getDescripcionCentroCosto() {
        return descripcionCentroCosto;
    }

    public void setDescripcionCentroCosto(String descripcionCentroCosto) {
        this.descripcionCentroCosto = descripcionCentroCosto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
