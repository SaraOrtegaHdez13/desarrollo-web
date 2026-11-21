
package mx.uv.luzioteamtime.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.RevisionTarea;

public interface IRevisionTarea
    extends CrudRepository<RevisionTarea, Integer> {

    // Obtener las entregas de una tarea en orden cronológico
    List<RevisionTarea> findByTarea_IdOrderByFechaEntregaAscIdAsc(
        Integer idTarea
    );

    // Contar las entregas registradas de una tarea
    long countByTarea_Id(Integer idTarea);
}
