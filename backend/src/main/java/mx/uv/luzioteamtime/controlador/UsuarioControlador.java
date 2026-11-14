
package mx.uv.luzioteamtime.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mx.uv.luzioteamtime.servicio.UsuarioServicio;
import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.modelo.UsuarioActual;
import mx.uv.luzioteamtime.modelo.UsuarioLogin;
import mx.uv.luzioteamtime.modelo.UsuarioRegistro;

@RestController
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/crearUsuario")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioRegistro datos) {

        try {
            usuarioServicio.registrarUsuario(
                datos.getNombre(),
                datos.getCorreo(),
                datos.getPassword()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");

        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody UsuarioLogin datos, HttpSession session) {

        if (datos.getCorreo() == null || datos.getCorreo().trim().isEmpty() ||
            datos.getPassword() == null || datos.getPassword().isEmpty()) {

            return ResponseEntity.badRequest().body("Todos los campos son obligatorios");
        }

        Usuario usuario = usuarioServicio.validarCredenciales(
            datos.getCorreo().trim(),
            datos.getPassword()
        );

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Correo o contraseña incorrectos");
        }

        session.setAttribute("usuarioId", usuario.getId());

        return ResponseEntity.ok("Inicio de sesión correcto");
    }

    @PostMapping("/cerrarSesion")
    public ResponseEntity<?> cerrarSesion(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok("Sesión cerrada correctamente");
    }


    @GetMapping("/obtenerUsuarioActual")
    public ResponseEntity<?> obtenerUsuarioActual(HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("usuarioId");

        Usuario usuario = usuarioServicio.obtenerUsuarioActual(idUsuario);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró el usuario");
        }

        UsuarioActual respuesta = new UsuarioActual(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getCorreo()
        );

        return ResponseEntity.ok(respuesta);
    }

}
