
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Datos que se devuelven al consultar una tarea
public class TareaResumen {

    private Integer id;
    private Integer idTablero;
    private Integer idResponsable;
    private String titulo;
    private String descripcion;
    private String estado;
    private LocalDate fechaLimite;
    private String observacion;
    private LocalDateTime fechaCompletada;

    public TareaResumen() {

    }

    public TareaResumen(Tarea tarea) {
        this.id = tarea.getId();
        this.idTablero = tarea.getTablero().getId();
        this.idResponsable = tarea.getResponsable().getId();
        this.titulo = tarea.getTitulo();
        this.descripcion = tarea.getDescripcion();
        this.estado = tarea.getEstado();
        this.fechaLimite = tarea.getFechaLimite();
        this.observacion = tarea.getObservacion();
        this.fechaCompletada = tarea.getFechaCompletada();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTablero() {
        return idTablero;
    }

    public void setIdTablero(Integer idTablero) {
        this.idTablero = idTablero;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }
}
