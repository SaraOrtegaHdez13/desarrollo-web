
package mx.uv.luzioteamtime.modelo;

public class EquipoResumen {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean archivado;
    private String rol;

    public EquipoResumen() {

    }

    public EquipoResumen(
        Integer id,
        String nombre,
        String descripcion,
        Boolean archivado,
        String rol
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.archivado = archivado;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getArchivado() {
        return archivado;
    }

    public void setArchivado(Boolean archivado) {
        this.archivado = archivado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
