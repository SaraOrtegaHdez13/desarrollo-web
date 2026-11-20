
package mx.uv.luzioteamtime.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import mx.uv.luzioteamtime.modelo.TareaModificacion;
import mx.uv.luzioteamtime.modelo.TareaDetalle;

import jakarta.servlet.http.HttpSession;

import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.TareaRegistro;
import mx.uv.luzioteamtime.servicio.TareaServicio;
import mx.uv.luzioteamtime.modelo.TareaRechazo;
import mx.uv.luzioteamtime.modelo.TareaReasignacion;
import mx.uv.luzioteamtime.modelo.TareaResumen;
import org.springframework.web.bind.annotation.DeleteMapping;


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


    // B5: Obtener las tareas de un tablero con filtros opcionales
    @GetMapping("/obtenerTareas/{idTablero}")
    public ResponseEntity<?> obtenerTareas(
        @PathVariable Integer idTablero,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) Integer responsable,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Consultar las tareas desde el servicio
            List<Tarea> tareas = tareaServicio.obtenerTareas(
                idTablero,
                idUsuario,
                estado,
                responsable
            );

            // El tablero no existe
            if (tareas == null) {
                return ResponseEntity.notFound().build();
            }

            // Convertir las tareas a datos sencillos para el frontend
            List<TareaResumen> resumenes = new ArrayList<>();

            for (Tarea tarea : tareas) {
                resumenes.add(new TareaResumen(tarea));
            }

            return ResponseEntity.ok(resumenes);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B6: Obtener una tarea por su ID
    @GetMapping("/obtenerTarea/{id}")
    public ResponseEntity<?> obtenerTarea(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            TareaDetalle detalle = tareaServicio.obtenerDetalleTarea(id, idUsuario);

            if (detalle == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(detalle);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B7: Modificar los datos de una tarea
    @PutMapping("/modificarTarea/{id}")
    public ResponseEntity<?> modificarTarea(
        @PathVariable Integer id,
        @RequestBody TareaModificacion datos,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Solicitar la modificación al servicio
            Tarea tarea = tareaServicio.modificarTarea(
                id,
                idUsuario,
                datos.getTitulo(),
                datos.getDescripcion(),
                datos.getFechaLimite()
            );

            // La tarea no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // Devolver los datos actualizados
            TareaResumen resumen = new TareaResumen(tarea);

            return ResponseEntity.ok(resumen);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B8: Eliminar una tarea
    @DeleteMapping("/eliminarTarea/{id}")
    public ResponseEntity<?> eliminarTarea(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Solicitar la eliminación al servicio
            boolean eliminada = tareaServicio.eliminarTarea(
                id,
                idUsuario
            );

            if (!eliminada) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                "Tarea eliminada correctamente"
            );

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B9: Iniciar una tarea asignada al usuario
    @PutMapping("/iniciarTarea/{id}")
    public ResponseEntity<?> iniciarTarea(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Solicitar el cambio de estado al servicio
            Tarea tarea = tareaServicio.iniciarTarea(
                id,
                idUsuario
            );

            // La tarea no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // Devolver la tarea con su nuevo estado
            TareaResumen resumen = new TareaResumen(tarea);

            return ResponseEntity.ok(resumen);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B10: Enviar una tarea a revisión
    @PutMapping("/enviarTareaRevision/{id}")
    public ResponseEntity<?> enviarTareaRevision(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Solicitar el envío a revisión al servicio
            Tarea tarea = tareaServicio.enviarTareaRevision(
                id,
                idUsuario
            );

            // La tarea no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // Devolver la tarea con su nuevo estado
            TareaResumen resumen = new TareaResumen(tarea);

            return ResponseEntity.ok(resumen);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B11: Aprobar una tarea enviada a revisión
    @PutMapping("/aprobarTarea/{id}")
    public ResponseEntity<?> aprobarTarea(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Solicitar la aprobación al servicio
            Tarea tarea = tareaServicio.aprobarTarea(
                id,
                idUsuario
            );

            // La tarea no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // Devolver la tarea aprobada
            TareaResumen resumen = new TareaResumen(tarea);

            return ResponseEntity.ok(resumen);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B12: Rechazar una tarea con observación
    @PutMapping("/rechazarTarea/{id}")
    public ResponseEntity<?> rechazarTarea(
        @PathVariable Integer id,
        @RequestBody TareaRechazo datos,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Comprobar que se haya recibido la observación
            if (datos == null) {
                return ResponseEntity.badRequest()
                    .body("Debes enviar una observación");
            }

            // Solicitar el rechazo al servicio
            Tarea tarea = tareaServicio.rechazarTarea(
                id,
                idUsuario,
                datos.getObservacion()
            );

            // La tarea no existe
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            // Devolver la tarea con el nuevo estado
            TareaResumen resumen = new TareaResumen(tarea);

            return ResponseEntity.ok(resumen);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    // B13: Reasignar una tarea
    @PutMapping("/reasignarTarea/{id}")
    public ResponseEntity<?> reasignarTarea(
        @PathVariable Integer id,
        @RequestBody TareaReasignacion datos,
        HttpSession session
    ) {

        try {

            // Obtener el usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión");
            }

            // Comprobar que se hayan enviado los datos
            if (datos == null) {
                return ResponseEntity.badRequest()
                    .body("Debes indicar el nuevo responsable");
            }

            // Solicitar la reasignación al servicio
            Tarea tarea = tareaServicio.reasignarTarea(
                id,
                idUsuario,
                datos.getIdResponsable()
            );

            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                new TareaResumen(tarea)
            );

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());

        } catch (IllegalStateException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }

}
