package co.dolmen.sid.entidad;

public class TipoBaseFotocelda {
    int id_tipo_base_fotocelda;
    String descripcion;

    public TipoBaseFotocelda(){

    }

    public TipoBaseFotocelda(int id_tipo_base_fotocelda, String descripcion) {
        this.id_tipo_base_fotocelda = id_tipo_base_fotocelda;
        this.descripcion = descripcion;
    }

    public int getId_tipo_base_fotocelda() {
        return id_tipo_base_fotocelda;
    }

    public void setId_tipo_base_fotocelda(int id_tipo_base_fotocelda) {
        this.id_tipo_base_fotocelda = id_tipo_base_fotocelda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
