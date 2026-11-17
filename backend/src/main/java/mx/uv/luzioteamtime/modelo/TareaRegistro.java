
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDate;

// Datos que recibe el backend para crear una tarea
public class TareaRegistro {

    private Integer idResponsable;
    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;

    public TareaRegistro() {
    }

    public TareaRegistro(
        Integer idResponsable,
        String titulo,
        String descripcion,
        LocalDate fechaLimite
    ) {
        this.idResponsable = idResponsable;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
