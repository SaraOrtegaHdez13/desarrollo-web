
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tablero", nullable = false)
    private Tablero tablero;

    @ManyToOne
    @JoinColumn(name = "id_responsable", nullable = false)
    private Usuario responsable;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String estado;

    private LocalDate fechaLimite;

    @Column(length = 255)
    private String observacion;

    private LocalDateTime fechaCompletada;

    public Tarea() {

    }

    public Tarea(
        Tablero tablero,
        Usuario responsable,
        String titulo,
        String descripcion,
        String estado,
        LocalDate fechaLimite,
        String observacion,
        LocalDateTime fechaCompletada
    ) {
        this.tablero = tablero;
        this.responsable = responsable;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaLimite = fechaLimite;
        this.observacion = observacion;
        this.fechaCompletada = fechaCompletada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
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
