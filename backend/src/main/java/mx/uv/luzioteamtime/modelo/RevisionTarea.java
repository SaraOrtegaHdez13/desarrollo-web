
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;

// Registra cada entrega y su revisión dentro de una tarea
@Entity
@Table(name = "revisiones_tarea")
public class RevisionTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tarea", nullable = false)
    private Tarea tarea;

    @Column(name = "numero_entrega")
    private Integer numeroEntrega;

    @ManyToOne
    @JoinColumn(name = "id_entregado_por")
    private Usuario entregadoPor;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "id_revisor")
    private Usuario revisor;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(length = 20)
    private String resultado;

    @Column(length = 500)
    private String observacion;

    public RevisionTarea() {

    }

    public RevisionTarea(
        Tarea tarea,
        Integer numeroEntrega,
        Usuario entregadoPor,
        LocalDateTime fechaEntrega,
        Usuario revisor,
        LocalDateTime fechaRevision,
        String resultado,
        String observacion
    ) {
        this.tarea = tarea;
        this.numeroEntrega = numeroEntrega;
        this.entregadoPor = entregadoPor;
        this.fechaEntrega = fechaEntrega;
        this.revisor = revisor;
        this.fechaRevision = fechaRevision;
        this.resultado = resultado;
        this.observacion = observacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Integer getNumeroEntrega() {
        return numeroEntrega;
    }

    public void setNumeroEntrega(Integer numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
    }

    public Usuario getEntregadoPor() {
        return entregadoPor;
    }

    public void setEntregadoPor(Usuario entregadoPor) {
        this.entregadoPor = entregadoPor;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Usuario getRevisor() {
        return revisor;
    }

    public void setRevisor(Usuario revisor) {
        this.revisor = revisor;
    }

    public LocalDateTime getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(LocalDateTime fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}