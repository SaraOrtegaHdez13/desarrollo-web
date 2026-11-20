package mx.uv.luzioteamtime.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class TareaDetalle extends TareaResumen {

    private final List<Revision> revisiones;
    private final List<Asignacion> asignaciones;

    public TareaDetalle(Tarea tarea, List<Revision> revisiones, List<Asignacion> asignaciones) {
        super(tarea);
        this.revisiones = List.copyOf(revisiones);
        this.asignaciones = List.copyOf(asignaciones);
    }

    public List<Revision> getRevisiones() {
        return revisiones;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public record Revision(
        int numeroEntrega,
        Integer idEntregadoPor,
        LocalDateTime fechaEntrega,
        Integer idRevisor,
        LocalDateTime fechaRevision,
        String resultado,
        String observacion
    ) {}

    public record Asignacion(
        Integer idUsuario,
        String nombre,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        String estadoAlRecibir,
        String estadoAlSalir
    ) {}
}