package co.dolmen.sid.utilidades;

public class DataSpinner {

    private int id;
    private String descripcion;

    public DataSpinner(){

    }

    public DataSpinner(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

