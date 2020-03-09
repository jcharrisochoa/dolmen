package co.dolmen.sid.entidad;

public class Barrio extends Municipio{
    int idBarrio;
    String nombreBarrio;
    public Barrio (){
        super();
    }

    public int getIdBarrio() {
        return idBarrio;
    }

    public void setIdBarrio(int idBarrio) {
        this.idBarrio = idBarrio;
    }

    public String getNombreBarrio() {
        return nombreBarrio;
    }

    public void setNombreBarrio(String nombreBarrio) {
        this.nombreBarrio = nombreBarrio;
    }
}
