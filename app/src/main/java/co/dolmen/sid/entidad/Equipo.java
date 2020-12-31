package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Equipo implements Serializable {
    private int idEquipo;
    private String codigo;
    private String serial;
    private String descripcion;

    public Equipo(){};

    public Equipo (int idEquipo,String codigo,String serial,String descripcion){
        this.idEquipo = idEquipo;
        this.codigo = codigo;
        this.serial = serial;
        this.descripcion = descripcion;
    }

    public Equipo (int idEquipo,String serial){
        this.idEquipo = idEquipo;
        this.serial = serial;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
