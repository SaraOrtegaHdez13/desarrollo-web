package mx.uv.luzioteamtime.modelo;

// Datos recibidos para agregar un integrante a un equipo
public class MiembroRegistro {

    private String correo;
    private String rol;

    public MiembroRegistro() {

    }

    public MiembroRegistro(String correo, String rol) {
        this.correo = correo;
        this.rol = rol;
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