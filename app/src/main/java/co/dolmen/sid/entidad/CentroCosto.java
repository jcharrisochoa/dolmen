package co.dolmen.sid.entidad;

import java.io.Serializable;

public class CentroCosto implements Serializable {
    int idCentroCosto;
    String descripcionCentroCosto;

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
}
