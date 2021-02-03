package co.dolmen.sid.entidad;

public class ArchivoActividad {
    private int id_actividad;
    private String archivo;
    private String tipo;

    public ArchivoActividad(){}

    public ArchivoActividad(int id_actividad, String archivo, String tipo) {
        this.id_actividad = id_actividad;
        this.archivo = archivo;
        this.tipo = tipo;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
