package co.dolmen.sid.entidad;

public class CensoArchivo {
    private long id_censo_tecnico;
    private String archivo;

    public long getId_censo_tecnico() {
        return id_censo_tecnico;
    }

    public void setId_censo_tecnico(long id_censo_tecnico) {
        this.id_censo_tecnico = id_censo_tecnico;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
