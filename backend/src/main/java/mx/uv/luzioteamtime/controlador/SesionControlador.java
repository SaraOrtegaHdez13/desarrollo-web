package mx.uv.luzioteamtime.controlador;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import mx.uv.luzioteamtime.servicio.SesionServicio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SesionControlador {

    private final SesionServicio servicio;

    public SesionControlador(SesionServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/crearSesion/{idTarea}")
    public ResponseEntity<?> crearSesion(
        @PathVariable("idTarea") Integer idTarea,
        @RequestBody DatosSesion datos,
        HttpSession sesion
    ) {
        Integer idUsuario = (Integer) sesion.getAttribute("usuarioId");

        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensaje", "Inicia sesión."));
        }

        try {
            var guardada = servicio.guardar(
                idTarea, idUsuario, datos.inicio(), datos.fin(), datos.duracionSegundos()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", guardada.getId(),
                "idTarea", idTarea,
                "duracionSegundos", guardada.getDuracionSegundos()
            ));
        } catch (ResponseStatusException error) {
            return ResponseEntity.status(error.getStatusCode())
                .body(Map.of("mensaje", error.getReason()));
        }
    }

    // Consultar las sesiones de una tarea
    @GetMapping("/obtenerSesiones/{idTarea}")
    public ResponseEntity<?> obtenerSesiones(
        @PathVariable("idTarea") Integer idTarea,
        HttpSession sesion
    ) {

        Integer idUsuario = (Integer) sesion.getAttribute("usuarioId");

        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensaje", "Inicia sesión."));
        }

        try {
            return ResponseEntity.ok(
                servicio.obtenerSesiones(idTarea, idUsuario)
            );

        } catch (ResponseStatusException error) {
            return ResponseEntity.status(error.getStatusCode())
                .body(Map.of("mensaje", error.getReason()));
        }
    }

    public record DatosSesion(
        LocalDateTime inicio,
        LocalDateTime fin,
        Integer duracionSegundos
    ) {}

}