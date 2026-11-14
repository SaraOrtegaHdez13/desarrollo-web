package mx.uv.luzioteamtime.modelo;

// Datos de un integrante que se mostrarán al consultar un equipo
public class MiembroResumen {

    private Integer idMiembro;
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;

    public MiembroResumen() {

    }

    public MiembroResumen(
        Integer idMiembro,
        Integer idUsuario,
        String nombre,
        String correo,
        String rol
    ) {
        this.idMiembro = idMiembro;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Integer getIdMiembro() {
        return idMiembro;
    }

    public void setIdMiembro(Integer idMiembro) {
        this.idMiembro = idMiembro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}