
package mx.uv.luzioteamtime.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

    @Entity
    @Table(name = "asignaciones_tarea")
    public class AsignacionTarea {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "id_tarea", nullable = false)
        private Tarea tarea;

        @ManyToOne
        @JoinColumn(name = "id_usuario", nullable = false)
        private Usuario usuario;

        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private String estadoAlRecibir;
        private String estadoAlSalir;

        public AsignacionTarea() {
        }

        public AsignacionTarea(
            Tarea tarea,
            Usuario usuario,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String estadoAlRecibir,
            String estadoAlSalir
        ) {
            this.tarea = tarea;
            this.usuario = usuario;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.estadoAlRecibir = estadoAlRecibir;
            this.estadoAlSalir = estadoAlSalir;
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

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public LocalDateTime getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public LocalDateTime getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(LocalDateTime fechaFin) {
            this.fechaFin = fechaFin;
        }

        public String getEstadoAlRecibir() {
            return estadoAlRecibir;
        }

        public void setEstadoAlRecibir(String estadoAlRecibir) {
            this.estadoAlRecibir = estadoAlRecibir;
        }

        public String getEstadoAlSalir() {
            return estadoAlSalir;
        }

        public void setEstadoAlSalir(String estadoAlSalir) {
            this.estadoAlSalir = estadoAlSalir;
        }
    }
