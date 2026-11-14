
package mx.uv.luzioteamtime.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.Tarea;

public interface ITarea extends CrudRepository<Tarea, Integer> {

    List<Tarea> findByResponsable_IdAndTablero_Equipo_IdAndEstadoIn(
        Integer idResponsable,
        Integer idEquipo,
        List<String> estados
    );

}
