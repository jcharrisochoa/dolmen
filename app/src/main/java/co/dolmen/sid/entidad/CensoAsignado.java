package co.dolmen.sid.entidad;

public class CensoAsignado {
    private int id;
    private int id_municipio;
    private int id_proceso_sgc;

    public CensoAsignado(int id, int id_municipio, int id_proceso_sgc) {
        this.id = id;
        this.id_municipio = id_municipio;
        this.id_proceso_sgc = id_proceso_sgc;
    }

    public  CensoAsignado(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public int getId_proceso_sgc() {
        return id_proceso_sgc;
    }

    public void setId_proceso_sgc(int id_proceso_sgc) {
        this.id_proceso_sgc = id_proceso_sgc;
    }
}
