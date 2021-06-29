package co.dolmen.sid.entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActividadOperativa implements Serializable {

    private int idActividad;
    private int idEspacioPublicitario;
    private int idUsuarioDescarga;

    private int idReporteDano;
    private int idClienteExterno;
    private String nombreCliente;
    private String direccionCliente;
    private String telefonoCliente;

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
    private double longitud;
    private double latitud;
    private String et;
    private String usuarioProgramaActividad;
    private String nroElementoDesmontado;
    private String usuarioGeneraActividad;
    private String observacion;
    private String pendienteSincronizar;
    private String afectadoPorVandalismo;
    private String elementoNoEncontrado;
    private ArrayList<ArchivoActividad> archivoActividadList;
    private List<Elemento> elementosDesmontadosList;
    private List<Integer> vatiajeDesmontadoList;


    public ActividadOperativa(){
        archivoActividadList = new ArrayList<ArchivoActividad>();
    }

    public void setAfectadoPorVandalismo(String afectadoPorVandalismo) {
        this.afectadoPorVandalismo = afectadoPorVandalismo;
    }

    public void setElementoNoEncontrado(String elementoNoEncontrado) {
        this.elementoNoEncontrado = elementoNoEncontrado;
    }

    public ArrayList<ArchivoActividad> getArchivoActividad() {
        return archivoActividadList;
    }

    public void setArchivoActividads(ArrayList<ArchivoActividad> archivoActividad) {
        this.archivoActividadList = archivoActividad;
    }

    public void agregarArchivoActividad(ArchivoActividad archivoActividad){
        archivoActividadList.add(archivoActividad);
    }

    public int getCantidadArchivoActividad(){
        return archivoActividadList.size();
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
        archivoActividadList = new ArrayList<ArchivoActividad>();
    }

    public String isAfectadoPorVandalismo() {
        return afectadoPorVandalismo;
    }

    public String isElementoNoEncontrado() {
         return elementoNoEncontrado;
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

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
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

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setElemento(Elemento elemento){
        this.elemento = elemento;
    }

    public void setEstadoActividad(EstadoActividad estadoActividad){
        this.estadoActividad = estadoActividad;
    }

    public void setFechaActividad(Date fechaActividad){
        this.fechaActividad = fechaActividad;
    }

    public void setProcesoSgc(ProcesoSgc procesoSgc){
        this.procesoSgc = procesoSgc;
    }

    public void setTipoActividad(TipoActividad tipoActividad){
        this.tipoActividad = tipoActividad;
    }

    public void setIdEspacioPublicitario(int idEspacioPublicitario) {
        this.idEspacioPublicitario = idEspacioPublicitario;
    }

    public void setIdUsuarioDescarga(int idUsuarioDescarga) {
        this.idUsuarioDescarga = idUsuarioDescarga;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public void setCentroCosto(CentroCosto centroCosto) {
        this.centroCosto = centroCosto;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public void setTipoReporteDano(TipoReporteDano tipoReporteDano) {
        this.tipoReporteDano = tipoReporteDano;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void setFechaPrograma(Date fechaPrograma) {
        this.fechaPrograma = fechaPrograma;
    }

    public void setFechaEnSitio(Date fechaEnSitio) {
        this.fechaEnSitio = fechaEnSitio;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public void setUsuarioProgramaActividad(String usuarioProgramaActividad) {
        this.usuarioProgramaActividad = usuarioProgramaActividad;
    }

    public void setNroElementoDesmontado(String nroElementoDesmontado) {
        this.nroElementoDesmontado = nroElementoDesmontado;
    }

    public void setUsuarioGeneraActividad(String usuarioGeneraActividad) {
        this.usuarioGeneraActividad = usuarioGeneraActividad;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setPendienteSincronizar(String pendienteSincronizar) {
        this.pendienteSincronizar = pendienteSincronizar;
    }

    public List<Elemento> getElementosDesmontadosList() {
        return elementosDesmontadosList;
    }

    public void setElementosDesmontadosList(List<Elemento> elementosDesmontadosList) {
        this.elementosDesmontadosList = elementosDesmontadosList;
    }

    public ArrayList<ArchivoActividad> getArchivoActividadList() {
        return archivoActividadList;
    }

    public void setArchivoActividadList(ArrayList<ArchivoActividad> archivoActividadList) {
        this.archivoActividadList = archivoActividadList;
    }

    public List<Integer> getVatiajeDesmontadoList() {
        return vatiajeDesmontadoList;
    }

    public void setVatiajeDesmontadoList(List<Integer> vatiajeDesmontadoList) {
        this.vatiajeDesmontadoList = vatiajeDesmontadoList;
    }

    public int getIdReporteDano() {
        return idReporteDano;
    }

    public void setIdReporteDano(int idReporteDano) {
        this.idReporteDano = idReporteDano;
    }

    public int getIdClienteExterno() {
        return idClienteExterno;
    }

    public void setIdClienteExterno(int idClienteExterno) {
        this.idClienteExterno = idClienteExterno;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    @Override
    public String toString() {
        return "ActividadOperativa{" +
                "idActividad=" + idActividad +
                ", idEspacioPublicitario=" + idEspacioPublicitario +
                ", idUsuarioDescarga=" + idUsuarioDescarga +
                ", idReporteDano=" + idReporteDano +
                ", idClienteExterno=" + idClienteExterno +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", direccionCliente='" + direccionCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", programa=" + programa.getId() +
                ", procesoSgc=" + procesoSgc.getDescripcion() +
                ", elemento={" +
                "  id="+elemento.getId() +
                ", mobiliario_no="+elemento.getElemento_no()+
                ", mobiliario_no="+elemento.getDireccion()+
                ", latitud="+elemento.getLatitud()+
                ", longitud="+elemento.getLongitud()+
                "}"+
                ", centroCosto=" + centroCosto.getDescripcionCentroCosto() +
                ", barrio=" + barrio.getNombreBarrio() +
                ", estadoActividad=" + estadoActividad.getDescripcion() +
                ", tipoReporteDano=" + tipoReporteDano.getDescripcion() +
                ", tipoActividad=" + tipoActividad.getDescripcion() +
                ", equipo=" + equipo.getCodigo() +
                ", fechaPrograma=" + fechaPrograma +
                ", fechaActividad=" + fechaActividad +
                ", fechaEnSitio=" + fechaEnSitio +
                ", fechaEjecucion=" + fechaEjecucion +
                ", direccion='" + direccion + '\'' +
                ", longitud=" + longitud +
                ", latitud=" + latitud +
                ", et='" + et + '\'' +
                ", usuarioProgramaActividad='" + usuarioProgramaActividad + '\'' +
                ", nroElementoDesmontado='" + nroElementoDesmontado + '\'' +
                ", usuarioGeneraActividad='" + usuarioGeneraActividad + '\'' +
                ", observacion='" + observacion + '\'' +
                ", pendienteSincronizar='" + pendienteSincronizar + '\'' +
                ", afectadoPorVandalismo='" + afectadoPorVandalismo + '\'' +
                ", elementoNoEncontrado='" + elementoNoEncontrado + '\'' +
                ", archivoActividadList=" + archivoActividadList +
                ", elementosDesmontadosList=" + elementosDesmontadosList +
                ", vatiajeDesmontadoList=" + vatiajeDesmontadoList +
                '}';
    }
}
