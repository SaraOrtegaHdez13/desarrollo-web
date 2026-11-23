
package mx.uv.luzioteamtime.repositorio;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.Tarea;

public interface ITarea extends CrudRepository<Tarea, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select tarea from Tarea tarea where tarea.id = :id")
    Optional<Tarea> buscarParaActualizar(@Param("id") Integer id);

    List<Tarea> findByResponsable_IdAndTablero_Equipo_IdAndEstadoIn(
        Integer idResponsable,
        Integer idEquipo,
        List<String> estados
    );

    // Obtener todas las tareas de un tablero
    List<Tarea> findByTablero_Id(Integer idTablero);

}