
package mx.uv.luzioteamtime.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;

import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.TareaRegistro;
import mx.uv.luzioteamtime.servicio.TareaServicio;

@RestController
public class TareaControlador {

    @Autowired
    private TareaServicio tareaServicio;

    // B4: Crear una tarea dentro de un tablero
    @PostMapping("/crearTarea/{idTablero}")
    public ResponseEntity<?> crearTarea(
        @PathVariable Integer idTablero,
        @RequestBody TareaRegistro datos,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            // Enviar los datos al servicio
            Tarea tarea = tareaServicio.crearTarea(
                idTablero,
                idUsuario,
                datos.getIdResponsable(),
                datos.getTitulo(),
                datos.getDescripcion(),
                datos.getFechaLimite()
            );

            // El tablero solicitado no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // La tarea fue creada correctamente
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tarea creada con ID: " + tarea.getId());

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }
}
