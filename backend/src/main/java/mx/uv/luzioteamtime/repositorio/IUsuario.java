
package mx.uv.luzioteamtime.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import mx.uv.luzioteamtime.modelo.Usuario;

public interface IUsuario extends CrudRepository<Usuario, Integer> {

    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreo(String correo);

}
