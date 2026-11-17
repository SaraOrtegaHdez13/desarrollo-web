package mx.uv.luzioteamtime.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.TableroResumen;
import jakarta.servlet.http.HttpSession;
import mx.uv.luzioteamtime.servicio.TableroServicio;

@RestController
public class TableroControlador {

    @Autowired
    private TableroServicio tableroServicio;

    @GetMapping("/obtenerTableros/{idEquipo}")
    public ResponseEntity<?> obtenerTableros(
        @PathVariable Integer idEquipo,
        HttpSession session
    ) {

        try {

            // Obtener al usuario autenticado
            Integer idUsuario =
                (Integer) session.getAttribute("usuarioId");

            List<TableroResumen> tableros =
                tableroServicio.obtenerTableros(
                    idEquipo,
                    idUsuario
                );

            if (tableros == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tableros);

        } catch (SecurityException error) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error.getMessage());

        } catch (IllegalArgumentException error) {

            return ResponseEntity.badRequest()
                .body(error.getMessage());
        }
    }

    // B1: Crear un tablero dentro de un equipo
@PostMapping("/crearTablero/{idEquipo}")
public ResponseEntity<?> crearTablero(
    @PathVariable Integer idEquipo,
    @RequestBody TableroResumen datos,
    HttpSession session
) {

    try {

        // Obtener el usuario de la sesión
        Integer idUsuario =
            (Integer) session.getAttribute("usuarioId");

        // Crear el tablero mediante el servicio
        Tablero tablero = tableroServicio.crearTablero(
            idEquipo,
            idUsuario,
            datos.getNombre()
        );

        if (tablero == null) {
            return ResponseEntity.notFound().build();
        }

        // Devolver los datos del tablero creado
        TableroResumen resultado = new TableroResumen(
            tablero.getId(),
            idEquipo,
            tablero.getNombre()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(resultado);

    } catch (SecurityException error) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(error.getMessage());

    } catch (IllegalArgumentException error) {

        return ResponseEntity.badRequest()
            .body(error.getMessage());
    }
}
}