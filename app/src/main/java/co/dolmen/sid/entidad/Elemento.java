package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Elemento implements Serializable {
    private int id;
    private String elemento_no;
    private String direccion;
    private float latitud;
    private float longitud;

    private Municipio municipio;
    private Barrio barrio;
    private ProcesoSgc procesoSgc;
    private Contrato contrato;
    private Tipologia tipologia;
    private Mobiliario mobiliario;
    private ReferenciaMobiliario referenciaMobiliario;
    private EstadoMobiliario estadoMobiliario;
    private TipoBalasto tipoBalasto;
    private TipoBaseFotocelda tipoBaseFotocelda;
    private TipoBrazo tipoBrazo;
    private ControlEncendido controlEncendido;
    private String zona;
    private String sector;
    private TipoEscenario tipoEscenario;
    private ClaseVia claseVia;
    private int anchoVia;
    private NormaConstruccionPoste normaConstruccionPoste;
    private boolean posteExclusivo;
    private String posteNo;
    private int interdistancia;
    private double potenciaTransformador;
    private String placaMT;
    private String placaCT;
    private boolean transformadorExclusivo;
    private Calibre calibre;
    private TipoInstalacionRed tipoInstalacionRed;
    private TipoRed tipoRed;
    private String encodeStringFoto;
    private int cantidad;

    private Sentido sentido;
    private Proveedor proveedor;
    private UnidadMedida unidadMedida;
    private String tercero;
    private String serialMedidor;
    private int lecturaMedidor;
    private ActaContrato actaContrato;
    private String observacion;
    private Temporal temporal;

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

    public TipoBalasto getTipoBalasto() {
        return tipoBalasto;
    }

    public void setTipoBalasto(TipoBalasto tipoBalasto) {
        this.tipoBalasto = tipoBalasto;
    }

    public TipoBaseFotocelda getTipoBaseFotocelda() {
        return tipoBaseFotocelda;
    }

    public void setTipoBaseFotocelda(TipoBaseFotocelda tipoBaseFotocelda) {
        this.tipoBaseFotocelda = tipoBaseFotocelda;
    }

    public TipoBrazo getTipoBrazo() {
        return tipoBrazo;
    }

    public void setTipoBrazo(TipoBrazo tipoBrazo) {
        this.tipoBrazo = tipoBrazo;
    }

    public ControlEncendido getControlEncendido() {
        return controlEncendido;
    }

    public void setControlEncendido(ControlEncendido controlEncendido) {
        this.controlEncendido = controlEncendido;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public TipoEscenario getTipoEscenario() {
        return tipoEscenario;
    }

    public void setTipoEscenario(TipoEscenario tipoEscenario) {
        this.tipoEscenario = tipoEscenario;
    }

    public ClaseVia getClaseVia() {
        return claseVia;
    }

    public void setClaseVia(ClaseVia claseVia) {
        this.claseVia = claseVia;
    }

    public int getAnchoVia() {
        return anchoVia;
    }

    public void setAnchoVia(int anchoVia) {
        this.anchoVia = anchoVia;
    }

    public NormaConstruccionPoste getNormaConstruccionPoste() {
        return normaConstruccionPoste;
    }

    public void setNormaConstruccionPoste(NormaConstruccionPoste normaConstruccionPoste) {
        this.normaConstruccionPoste = normaConstruccionPoste;
    }

    public boolean isPosteExclusivo() {
        return posteExclusivo;
    }

    public void setPosteExclusivo(boolean posteExclusivo) {
        this.posteExclusivo = posteExclusivo;
    }

    public String getPosteNo() {
        return posteNo;
    }

    public void setPosteNo(String posteNo) {
        this.posteNo = posteNo;
    }

    public int getInterdistancia() {
        return interdistancia;
    }

    public void setInterdistancia(int interdistancia) {
        this.interdistancia = interdistancia;
    }

    public double getPotenciaTransformador() {
        return potenciaTransformador;
    }

    public void setPotenciaTransformador(double potenciaTransformador) {
        this.potenciaTransformador = potenciaTransformador;
    }

    public String getPlacaMT() {
        return placaMT;
    }

    public void setPlacaMT(String placaMT) {
        this.placaMT = placaMT;
    }

    public String getPlacaCT() {
        return placaCT;
    }

    public void setPlacaCT(String placaCT) {
        this.placaCT = placaCT;
    }

    public boolean isTransformadorExclusivo() {
        return transformadorExclusivo;
    }

    public void setTransformadorExclusivo(boolean transformadorExclusivo) {
        this.transformadorExclusivo = transformadorExclusivo;
    }

    public Calibre getCalibre() {
        return calibre;
    }

    public void setCalibre(Calibre calibre) {
        this.calibre = calibre;
    }

    public TipoInstalacionRed getTipoInstalacionRed() {
        return tipoInstalacionRed;
    }

    public void setTipoInstalacionRed(TipoInstalacionRed tipoInstalacionRed) {
        this.tipoInstalacionRed = tipoInstalacionRed;
    }

    public TipoRed getTipoRed() {
        return tipoRed;
    }

    public void setTipoRed(TipoRed tipoRed) {
        this.tipoRed = tipoRed;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getEncodeStringFoto() {
        return encodeStringFoto;
    }

    public void setEncodeStringFoto(String encodeStringFoto) {
        this.encodeStringFoto = encodeStringFoto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Sentido getSentido() {
        return sentido;
    }

    public void setSentido(Sentido sentido) {
        this.sentido = sentido;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getTercero() {
        return tercero;
    }

    public void setTercero(String tercero) {
        this.tercero = tercero;
    }

    public String getSerialMedidor() {
        return serialMedidor;
    }

    public void setSerialMedidor(String serialMedidor) {
        this.serialMedidor = serialMedidor;
    }

    public int getLecturaMedidor() {
        return lecturaMedidor;
    }

    public void setLecturaMedidor(int lecturaMedidor) {
        this.lecturaMedidor = lecturaMedidor;
    }

    public ActaContrato getActaContrato() {
        return actaContrato;
    }

    public void setActaContrato(ActaContrato actaContrato) {
        this.actaContrato = actaContrato;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Temporal getTemporal() {
        return temporal;
    }

    public void setTemporal(Temporal temporal) {
        this.temporal = temporal;
    }
}

enum Temporal{
    S,N;
}