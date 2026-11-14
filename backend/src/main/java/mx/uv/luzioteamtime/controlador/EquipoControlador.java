
package mx.uv.luzioteamtime.controlador;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import mx.uv.luzioteamtime.modelo.Equipo;
import mx.uv.luzioteamtime.modelo.EquipoRegistro;
import mx.uv.luzioteamtime.modelo.EquipoResumen;
import mx.uv.luzioteamtime.modelo.MiembroRegistro;
import mx.uv.luzioteamtime.modelo.MiembroResumen;
import mx.uv.luzioteamtime.servicio.EquipoServicio;

@RestController
public class EquipoControlador {

    @Autowired
    private EquipoServicio equipoServicio;

    @PostMapping("/crearEquipo")
    public ResponseEntity<?> crearEquipo(@RequestBody EquipoRegistro datos, HttpSession session) {

        try {

            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            Equipo equipo = equipoServicio.crearEquipo(datos.getNombre(), datos.getDescripcion(), idUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(equipo);

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }


    @GetMapping("/obtenerEquipos")
    public ResponseEntity<?> obtenerEquipos(
        @RequestParam(required = false) Boolean archivado,
        HttpSession session
    ) {

        try {

            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            List<EquipoResumen> equipos =
                equipoServicio.obtenerEquipos(idUsuario, archivado);

            return ResponseEntity.ok(equipos);

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @GetMapping("/obtenerEquipo/{id}")
    public ResponseEntity<?> obtenerEquipo(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            EquipoResumen equipo = equipoServicio.obtenerEquipo(id, idUsuario);

            if (equipo == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(equipo);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @GetMapping("/obtenerMiembros/{idEquipo}")
    public ResponseEntity<?> obtenerMiembros(
        @PathVariable Integer idEquipo,
        HttpSession session
    ) {

        try {

            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            List<MiembroResumen> miembros =
                equipoServicio.obtenerMiembros(idEquipo, idUsuario);

            if (miembros == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(miembros);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PostMapping("/agregarMiembro/{idEquipo}")
    public ResponseEntity<?> agregarMiembro(
        @PathVariable Integer idEquipo,
        @RequestBody MiembroRegistro datos,
        HttpSession session
    ) {

        try {

            // Obtener al usuario autenticado
            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            MiembroResumen miembro = equipoServicio.agregarMiembro(
                idEquipo,
                idUsuario,
                datos.getCorreo(),
                datos.getRol()
            );

            if (miembro == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(miembro);

        } catch (NoSuchElementException error) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PutMapping("/modificarMiembro/{idMiembro}")
    public ResponseEntity<?> modificarMiembro(
        @PathVariable Integer idMiembro,
        @RequestBody MiembroRegistro datos,
        HttpSession session
    ) {

        try {

            // Obtener al usuario autenticado
            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            MiembroResumen miembro = equipoServicio.modificarMiembro(
                idMiembro,
                idUsuario,
                datos.getRol()
            );

            if (miembro == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(miembro);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }


    @DeleteMapping("/eliminarMiembro/{idMiembro}")
    public ResponseEntity<?> eliminarMiembro(
        @PathVariable Integer idMiembro,
        HttpSession session
    ) {

        try {

            // Obtener al usuario autenticado
            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            boolean eliminado = equipoServicio.eliminarMiembro(
                idMiembro,
                idUsuario
            );

            if (!eliminado) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("Integrante eliminado correctamente");

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }


    @PutMapping("/archivarEquipo/{id}")
    public ResponseEntity<?> archivarEquipo(
        @PathVariable Integer id,
        HttpSession session
    ) {

        try {

            // Obtener al usuario autenticado
            Integer idUsuario = (Integer) session.getAttribute("usuarioId");

            Equipo equipo = equipoServicio.archivarEquipo(
                id,
                idUsuario
            );

            if (equipo == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(equipo);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }



}
