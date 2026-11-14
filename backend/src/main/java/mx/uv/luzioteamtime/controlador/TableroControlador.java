package mx.uv.luzioteamtime.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

import mx.uv.luzioteamtime.modelo.TableroResumen;
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
}