package co.dolmen.sid.entidad;

public class ReferenciaMobiliario extends Mobiliario {

    int idReferenciaMobiliario;
    String descripcionReferenciaMobiliario;
    Mobiliario mobiliario;
    public ReferenciaMobiliario(){
        super();
    }
    public ReferenciaMobiliario(int idReferenciaMobiliario,String descripcionReferenciaMobiliario){
        super();
        this.idReferenciaMobiliario = idReferenciaMobiliario;
        this.descripcionReferenciaMobiliario = descripcionReferenciaMobiliario;
    }

    public int getIdReferenciaMobiliario() {
        return idReferenciaMobiliario;
    }

    public void setIdReferenciaMobiliario(int idReferenciaMobiliario) {
        this.idReferenciaMobiliario = idReferenciaMobiliario;
    }

    public String getDescripcionReferenciaMobiliario() {
        return descripcionReferenciaMobiliario;
    }

    public void setDescripcionReferenciaMobiliario(String descripcionReferenciaMobiliario) {
        this.descripcionReferenciaMobiliario = descripcionReferenciaMobiliario;
    }

    public Mobiliario getMobiliario() {
        return mobiliario;
    }

    public void setMobiliario(Mobiliario mobiliario) {
        this.mobiliario = mobiliario;
    }
}
