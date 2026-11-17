
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDate;

// Datos permitidos para modificar una tarea
public class TareaModificacion {

    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;

    public TareaModificacion() {

    }

    public TareaModificacion(
        String titulo,
        String descripcion,
        LocalDate fechaLimite
    ) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
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
