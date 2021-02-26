package co.dolmen.sid.entidad;

public class Censo {
    private long lastId;
    private int id_censo;
    private int interdistancia;
    private int ancho_via;
    private int mobiliario_no_anterior;
    private int mobiliario_no_posterior;

    private double potenciaTransformador;
    private double anden_a;
    private double anden_b;
    private double avance_anden;

    private char numeroMobiliarioVisible;
    private char mobiliarioEnSitio;
    private char puestaAtierra;
    private char posteExclusivoAP;
    private char estado;

    private float longitud;
    private float latitud;

    private String fchRegistro;
    private String observacion;
    private String posteNo;
    private String placaMtTransformador;
    private String placaCtTransformador;

    private Elemento elemento;
    private NormaConstruccionPoste normaConstruccionPoste;
    private RetenidaPoste retenidaPoste;
    private ClaseVia claseVia;
    private TipoRed tipoRed;
    private EstadoMobiliario estadoMobiliario;
    private TipoEscenario tipoEscenario;
    private Calibre calibre;
    private ClasePerfil clasePerfil;
    private FabricantePoste fabricantePoste;
    private FabricanteElemento fabricanteElemento;
    private TipoBrazo tipoBrazo;

    private String chkSwLuminariaVisible;
    private String chkSwPoseeLuminaria;
    private String chkSwPuestaTierra;
    private String chkSwPosteExclusivoAp;
    private String chkSwPosteBuenEstado;
    private String sector;
    private String zona;
    private String tipoPropietarioTransformador;
    private String chkSwMobiliarioBuenEstado;
    private String brazoMalEstado;
    private String visorMalEstado;
    private String sinBombillo;
    private String mobiliarioObsoleto;

    public int getAncho_via() {
        return ancho_via;
    }

    public void setAncho_via(int ancho_via) {
        this.ancho_via = ancho_via;
    }

    public String getChkSwTransformadorExclusivoAP() {
        return chkSwTransformadorExclusivoAP;
    }

    public void setChkSwTransformadorExclusivoAP(String chkSwTransformadorExclusivoAP) {
        this.chkSwTransformadorExclusivoAP = chkSwTransformadorExclusivoAP;
    }

    private String mobiliarioMalPosicionado;
    private String chkSwTransformadorExclusivoAP;



    public Censo(){

    }

    public Censo(int id_censo, int interdistancia, double potenciaTransformador, char numeroMobiliarioVisible, char mobiliarioEnSitio, char puestaAtierra,
                 char posteExclusivoAP, char estado, float longitud, float latitud, String fchRegistro, String observacion, String posteNo,
                 String placaMtTransformador, String placaCtTransformador, Elemento elemento, NormaConstruccionPoste normaConstruccionPoste,
                 RetenidaPoste retenidaPoste, ClaseVia claseVia, TipoRed tipoRed) {
        this.id_censo = id_censo;
        this.interdistancia = interdistancia;
        this.potenciaTransformador = potenciaTransformador;
        this.numeroMobiliarioVisible = numeroMobiliarioVisible;
        this.mobiliarioEnSitio = mobiliarioEnSitio;
        this.puestaAtierra = puestaAtierra;
        this.posteExclusivoAP = posteExclusivoAP;
        this.estado = estado;
        this.longitud = longitud;
        this.latitud = latitud;
        this.fchRegistro = fchRegistro;
        this.observacion = observacion;
        this.posteNo = posteNo;
        this.placaMtTransformador = placaMtTransformador;
        this.placaCtTransformador = placaCtTransformador;
        this.elemento = elemento;
        this.normaConstruccionPoste = normaConstruccionPoste;
        this.retenidaPoste = retenidaPoste;
        this.claseVia = claseVia;
        this.tipoRed = tipoRed;
    }

    public int getId_censo() {
        return id_censo;
    }

    public void setId_censo(int id_censo) {
        this.id_censo = id_censo;
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

    public char getNumeroMobiliarioVisible() {
        return numeroMobiliarioVisible;
    }

    public void setNumeroMobiliarioVisible(char numeroMobiliarioVisible) {
        this.numeroMobiliarioVisible = numeroMobiliarioVisible;
    }

    public char getMobiliarioEnSitio() {
        return mobiliarioEnSitio;
    }

    public void setMobiliarioEnSitio(char mobiliarioEnSitio) {
        this.mobiliarioEnSitio = mobiliarioEnSitio;
    }

    public char getPuestaAtierra() {
        return puestaAtierra;
    }

    public void setPuestaAtierra(char puestaAtierra) {
        this.puestaAtierra = puestaAtierra;
    }

    public char getPosteExclusivoAP() {
        return posteExclusivoAP;
    }

    public void setPosteExclusivoAP(char posteExclusivoAP) {
        this.posteExclusivoAP = posteExclusivoAP;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getPosteNo() {
        return posteNo;
    }

    public void setPosteNo(String posteNo) {
        this.posteNo = posteNo;
    }

    public String getPlacaMtTransformador() {
        return placaMtTransformador;
    }

    public void setPlacaMtTransformador(String placaMtTransformador) {
        this.placaMtTransformador = placaMtTransformador;
    }

    public String getPlacaCtTransformador() {
        return placaCtTransformador;
    }

    public void setPlacaCtTransformador(String placaCtTransformador) {
        this.placaCtTransformador = placaCtTransformador;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public NormaConstruccionPoste getNormaConstruccionPoste() {
        return normaConstruccionPoste;
    }

    public void setNormaConstruccionPoste(NormaConstruccionPoste normaConstruccionPoste) {
        this.normaConstruccionPoste = normaConstruccionPoste;
    }

    public RetenidaPoste getRetenidaPoste() {
        return retenidaPoste;
    }

    public void setRetenidaPoste(RetenidaPoste retenidaPoste) {
        this.retenidaPoste = retenidaPoste;
    }

    public ClaseVia getClaseVia() {
        return claseVia;
    }

    public void setClaseVia(ClaseVia claseVia) {
        this.claseVia = claseVia;
    }

    public TipoRed getTipoRed() {
        return tipoRed;
    }

    public void setTipoRed(TipoRed tipoRed) {
        this.tipoRed = tipoRed;
    }

    public String getChkSwLuminariaVisible() {
        return chkSwLuminariaVisible;
    }

    public void setChkSwLuminariaVisible(String chkSwLuminariaVisible) {
        this.chkSwLuminariaVisible = chkSwLuminariaVisible;
    }

    public String getChkSwPoseeLuminaria() {
        return chkSwPoseeLuminaria;
    }

    public void setChkSwPoseeLuminaria(String chkSwPoseeLuminaria) {
        this.chkSwPoseeLuminaria = chkSwPoseeLuminaria;
    }

    public String getChkSwPuestaTierra() {
        return chkSwPuestaTierra;
    }

    public void setChkSwPuestaTierra(String chkSwPuestaTierra) {
        this.chkSwPuestaTierra = chkSwPuestaTierra;
    }

    public String getChkSwPosteExclusivoAp() {
        return chkSwPosteExclusivoAp;
    }

    public void setChkSwPosteExclusivoAp(String chkSwPosteExclusivoAp) {
        this.chkSwPosteExclusivoAp = chkSwPosteExclusivoAp;
    }
    public EstadoMobiliario getEstadoMobiliario() {
        return estadoMobiliario;
    }

    public void setEstadoMobiliario(EstadoMobiliario estadoMobiliario) {
        this.estadoMobiliario = estadoMobiliario;
    }
    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public String getChkSwPosteBuenEstado() {
        return chkSwPosteBuenEstado;
    }

    public void setChkSwPosteBuenEstado(String chkSwPosteBuenEstado) {
        this.chkSwPosteBuenEstado = chkSwPosteBuenEstado;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public TipoEscenario getTipoEscenario() {
        return tipoEscenario;
    }

    public void setTipoEscenario(TipoEscenario tipoEscenario) {
        this.tipoEscenario = tipoEscenario;
    }

    public String getChkSwMobiliarioBuenEstado() {
        return chkSwMobiliarioBuenEstado;
    }

    public void setChkSwMobiliarioBuenEstado(String chkSwMobiliarioBuenEstado) {
        this.chkSwMobiliarioBuenEstado = chkSwMobiliarioBuenEstado;
    }

    public String getTipoPropietarioTransformador() {
        return tipoPropietarioTransformador;
    }

    public void setTipoPropietarioTransformador(String tipoPropietarioTransformador) {
        this.tipoPropietarioTransformador = tipoPropietarioTransformador;
    }

    public String getBrazoMalEstado() {
        return brazoMalEstado;
    }

    public void setBrazoMalEstado(String brazoMalEstado) {
        this.brazoMalEstado = brazoMalEstado;
    }

    public String getVisorMalEstado() {
        return visorMalEstado;
    }

    public void setVisorMalEstado(String visorMalEstado) {
        this.visorMalEstado = visorMalEstado;
    }

    public String getSinBombillo() {
        return sinBombillo;
    }

    public void setSinBombillo(String sinBombillo) {
        this.sinBombillo = sinBombillo;
    }

    public String getMobiliarioObsoleto() {
        return mobiliarioObsoleto;
    }

    public void setMobiliarioObsoleto(String mobiliarioObsoleto) {
        this.mobiliarioObsoleto = mobiliarioObsoleto;
    }

    public String getMobiliarioMalPosicionado() {
        return mobiliarioMalPosicionado;
    }

    public void setMobiliarioMalPosicionado(String mobiliarioMalPosicionado) {
        this.mobiliarioMalPosicionado = mobiliarioMalPosicionado;
    }

    public Calibre getCalibre() {
        return calibre;
    }

    public void setCalibre(Calibre calibre) {
        this.calibre = calibre;
    }

    public ClasePerfil getClasePerfil() {
        return clasePerfil;
    }

    public void setClasePerfil(ClasePerfil clasePerfil) {
        this.clasePerfil = clasePerfil;
    }

    public FabricantePoste getFabricantePoste() {
        return fabricantePoste;
    }

    public void setFabricantePoste(FabricantePoste fabricantePoste) {
        this.fabricantePoste = fabricantePoste;
    }

    public FabricanteElemento getFabricanteElemento() {
        return fabricanteElemento;
    }

    public void setFabricanteElemento(FabricanteElemento fabricanteElemento) {
        this.fabricanteElemento = fabricanteElemento;
    }

    public TipoBrazo getTipoBrazo() {
        return tipoBrazo;
    }

    public void setTipoBrazo(TipoBrazo tipoBrazo) {
        this.tipoBrazo = tipoBrazo;
    }

    public int getMobiliario_no_anterior() {
        return mobiliario_no_anterior;
    }

    public void setMobiliario_no_anterior(int mobiliario_no_anterior) {
        this.mobiliario_no_anterior = mobiliario_no_anterior;
    }

    public int getMobiliario_no_posterior() {
        return mobiliario_no_posterior;
    }

    public void setMobiliario_no_posterior(int mobiliario_no_posterior) {
        this.mobiliario_no_posterior = mobiliario_no_posterior;
    }

    public double getAnden_a() {
        return anden_a;
    }

    public void setAnden_a(double anden_a) {
        this.anden_a = anden_a;
    }

    public double getAnden_b() {
        return anden_b;
    }

    public void setAnden_b(double anden_b) {
        this.anden_b = anden_b;
    }

    public double getAvance_anden() {
        return avance_anden;
    }

    public void setAvance_anden(double avance_anden) {
        this.avance_anden = avance_anden;
    }
}
