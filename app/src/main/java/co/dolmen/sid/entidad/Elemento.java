package co.dolmen.sid.entidad;

public class Elemento {
    private int id;
    private String elemento_no;
    private String direccion;

    private Municipio municipio;
    private Barrio barrio;
    private ProcesoSgc procesoSgc;
    private Contrato contrato;
    private Tipologia tipologia;
    private Mobiliario mobiliario;
    private ReferenciaMobiliario referenciaMobiliario;
    private EstadoMobiliario estadoMobiliario;

    public Elemento(){

    }

    public Elemento(int id, String elemento_no, String direccion, Municipio municipio, Barrio barrio, Tipologia tipologia,
                    Mobiliario mobiliario, ReferenciaMobiliario referenciaMobiliario, EstadoMobiliario estadoMobiliario) {
        this.id = id;
        this.elemento_no = elemento_no;
        this.direccion = direccion;
        this.municipio = municipio;
        this.barrio = barrio;
        this.tipologia = tipologia;
        this.mobiliario = mobiliario;
        this.referenciaMobiliario = referenciaMobiliario;
        this.estadoMobiliario = estadoMobiliario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElemento_no() {
        return elemento_no;
    }

    public void setElemento_no(String elemento_no) {
        this.elemento_no = elemento_no;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc) {
        this.procesoSgc = procesoSgc;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    public Mobiliario getMobiliario() {
        return mobiliario;
    }

    public void setMobiliario(Mobiliario mobiliario) {
        this.mobiliario = mobiliario;
    }

    public ReferenciaMobiliario getReferenciaMobiliario() {
        return referenciaMobiliario;
    }

    public void setReferenciaMobiliario(ReferenciaMobiliario referenciaMobiliario) {
        this.referenciaMobiliario = referenciaMobiliario;
    }

    public EstadoMobiliario getEstadoMobiliario() {
        return estadoMobiliario;
    }

    public void setEstadoMobiliario(EstadoMobiliario estadoMobiliario) {
        this.estadoMobiliario = estadoMobiliario;
    }
}
