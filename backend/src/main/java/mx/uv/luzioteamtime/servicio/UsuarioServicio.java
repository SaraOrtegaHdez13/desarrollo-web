
package mx.uv.luzioteamtime.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.repositorio.IUsuario;

@Service
public class UsuarioServicio {

    @Autowired
    private IUsuario iusuario;

    private BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    public boolean existeCorreo(String correo) {
        return iusuario.existsByCorreo(correo);
    }

    public String cifrarPassword(String password) {
        return codificador.encode(password);
    }

    public Usuario registrarUsuario(String nombre, String correo, String password) {

        if (nombre == null || nombre.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        if (existeCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario(
            nombre.trim(),
            correo.trim(),
            cifrarPassword(password)
        );

        return iusuario.save(usuario);
    }

    public Usuario validarCredenciales(String correo, String password) {

        Optional<Usuario> resultado = iusuario.findByCorreo(correo);

        if (resultado.isEmpty()) {
            return null;
        }

        Usuario usuario = resultado.get();

        if (!codificador.matches(password, usuario.getPasswordHash())) {
            return null;
        }

        return usuario;
    }


    public Usuario obtenerUsuarioActual(Integer idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        return iusuario.findById(idUsuario).orElse(null);
    }

}
