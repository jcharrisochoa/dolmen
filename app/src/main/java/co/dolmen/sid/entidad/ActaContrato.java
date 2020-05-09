package co.dolmen.sid.entidad;

public class ActaContrato {
    int idActa;
    String descripcionActa;
    Contrato contrato;

    public int getIdActa() {
        return idActa;
    }

    public void setIdActa(int idActa) {
        this.idActa = idActa;
    }

    public String getDescripcionActa() {
        return descripcionActa;
    }

    public void setDescripcionActa(String descripcion) {
        this.descripcionActa = descripcion;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}
