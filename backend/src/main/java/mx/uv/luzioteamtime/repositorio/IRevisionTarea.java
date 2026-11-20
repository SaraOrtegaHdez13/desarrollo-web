
package mx.uv.luzioteamtime.repositorio;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import mx.uv.luzioteamtime.modelo.RevisionTarea;

public interface IRevisionTarea
    extends CrudRepository<RevisionTarea, Integer> {
    List<RevisionTarea> findByTarea_IdOrderByFechaAscIdAsc(Integer idTarea);

    // Contar las revisiones registradas de una tarea
    long countByTarea_Id(Integer idTarea);

}
