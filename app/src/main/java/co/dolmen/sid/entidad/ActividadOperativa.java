package co.dolmen.sid.entidad;

import java.util.Date;

public class ActividadOperativa {

    private int idActividad;
    private int idEspacioPublicitario;
    private int idUsuarioDescarga;
    private Programa programa;
    private ProcesoSgc procesoSgc;
    private Elemento elemento;
    private CentroCosto centroCosto;
    private Barrio barrio;
    private EstadoActividad estadoActividad;
    private TipoReporteDano tipoReporteDano;
    private TipoActividad tipoActividad;
    private Equipo equipo;
    private Date fechaPrograma;
    private Date fechaActividad;
    private Date fechaEnSitio;
    private Date fechaEjecucion;
    private String direccion;
    private float longitud;
    private float latitud;
    private String et;
    private String usuarioProgramaActividad;
    private String nroElementoDesmontado;
    private String usuarioGeneraActividad;
    private String observacion;
    private String pendienteSincronizar;

    public ActividadOperativa(){

    }

    public ActividadOperativa(int idActividad, int idEspacioPublicitario, Programa programa, ProcesoSgc procesoSgc, Elemento elemento, CentroCosto centroCosto, Barrio barrio, EstadoActividad estadoActividad,
                              TipoReporteDano tipoReporteDano, TipoActividad tipoActividad, Equipo equipo, Date fechaPrograma, Date fechaActividad, String direccion, String et, String usuarioProgramaActividad
                              ) {
        this.idActividad = idActividad;
        this.idEspacioPublicitario = idEspacioPublicitario;
        this.programa = programa;
        this.procesoSgc = procesoSgc;
        this.elemento = elemento;
        this.centroCosto = centroCosto;
        this.barrio = barrio;
        this.estadoActividad = estadoActividad;
        this.tipoReporteDano = tipoReporteDano;
        this.tipoActividad = tipoActividad;
        this.equipo = equipo;
        this.fechaPrograma = fechaPrograma;
        this.fechaActividad = fechaActividad;
        this.direccion = direccion;
        this.et = et;
        this.usuarioProgramaActividad = usuarioProgramaActividad;
        this.usuarioGeneraActividad = usuarioGeneraActividad;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public CentroCosto getCentroCosto() {
        return centroCosto;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public int getIdEspacioPublicitario() {
        return idEspacioPublicitario;
    }

    public int getIdUsuarioDescarga() {
        return idUsuarioDescarga;
    }

    public Date getFechaPrograma() {
        return fechaPrograma;
    }

    public Date getFechaActividad() {
        return fechaActividad;
    }

    public Date getFechaEnSitio() {
        return fechaEnSitio;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public String getDireccion() {
        return direccion;
    }

    public float getLongitud() {
        return longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public String getEt() {
        return et;
    }

    public String getNroElementoDesmontado() {
        return nroElementoDesmontado;
    }

    public String getUsuarioGeneraActividad() {
        return usuarioGeneraActividad;
    }

    public String getObservacion() {
        return observacion;
    }

    public String getPendienteSincronizar() {
        return pendienteSincronizar;
    }

    public Programa getPrograma() {
        return programa;
    }

    public ProcesoSgc getProcesoSgc() {
        return procesoSgc;
    }

    public EstadoActividad getEstadoActividad() {
        return estadoActividad;
    }

    public TipoReporteDano getTipoReporteDano() {
        return tipoReporteDano;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public String getUsuarioProgramaActividad() {
        return usuarioProgramaActividad;
    }
}
