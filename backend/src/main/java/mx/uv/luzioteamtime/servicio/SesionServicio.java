package mx.uv.luzioteamtime.servicio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import mx.uv.luzioteamtime.modelo.Sesion;
import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.repositorio.ISesion;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SesionServicio {

    private final TareaServicio tareaServicio;
    private final ISesion sesiones;

    public SesionServicio(TareaServicio tareaServicio, ISesion sesiones) {
        this.tareaServicio = tareaServicio;
        this.sesiones = sesiones;
    }

    @Transactional
    public Sesion guardar(
        Integer idTarea,
        Integer idUsuario,
        LocalDateTime inicio,
        LocalDateTime fin,
        Integer duracionSegundos
    ) {
        Tarea tarea;

        try {
            tarea = tareaServicio.obtenerTareaParaActualizar(idTarea, idUsuario);
        } catch (SecurityException error) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, error.getMessage());
        }

        if (tarea == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tarea no existe.");
        }

        if (Boolean.TRUE.equals(tarea.getTablero().getEquipo().getArchivado())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El equipo está archivado.");
        }

        if (tarea.getResponsable() == null
            || !idUsuario.equals(tarea.getResponsable().getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Solo el responsable puede guardar un pomodoro."
            );
        }

        if (inicio == null || fin == null || duracionSegundos == null
            || duracionSegundos <= 0
            || !Duration.between(inicio, fin).equals(Duration.ofSeconds(duracionSegundos))) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El inicio, el fin y la duración no coinciden."
            );
        }

        var existente = sesiones.findFirstByTarea_IdAndUsuario_IdAndInicioAndFinAndDuracionSegundosOrderByIdAsc(
            idTarea, idUsuario, inicio, fin, duracionSegundos
        );
        if (existente.isPresent()) {
            return existente.get();
        }

        if (!"EN_PROGRESO".equals(tarea.getEstado())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La tarea debe estar en progreso."
            );
        }

        return sesiones.save(
            new Sesion(tarea, tarea.getResponsable(), inicio, fin, duracionSegundos)
        );
    }


    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerSesiones(
        Integer idTarea,
        Integer idUsuario
    ) {

        // Verificar que el usuario tenga acceso a la tarea
        try {
            Tarea tarea = tareaServicio.obtenerTarea(idTarea, idUsuario);

            if (tarea == null) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La tarea no existe."
                );
            }

        } catch (SecurityException error) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                error.getMessage()
            );
        }

        // Obtener las sesiones registradas
        return sesiones.findByTarea_Id(idTarea).stream()
            .map(sesion -> Map.<String, Object>of(
                "id", sesion.getId(),
                "idTarea", sesion.getTarea().getId(),
                "idUsuario", sesion.getUsuario().getId(),
                "inicio", sesion.getInicio(),
                "fin", sesion.getFin(),
                "duracionSegundos", sesion.getDuracionSegundos()
            ))
            .toList();
    }

}