package mx.uv.luzioteamtime.repositorio;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import mx.uv.luzioteamtime.modelo.Sesion;

import org.springframework.data.repository.CrudRepository;

public interface ISesion extends CrudRepository<Sesion, Integer> {

    Optional<Sesion> findFirstByTarea_IdAndUsuario_IdAndInicioAndFinAndDuracionSegundosOrderByIdAsc(
        Integer idTarea, Integer idUsuario, LocalDateTime inicio, LocalDateTime fin, Integer duracionSegundos
    );

    long countByTarea_Id(Integer idTarea);
    List<Sesion> findByTarea_Id(Integer idTarea);
}