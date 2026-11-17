
package mx.uv.luzioteamtime.repositorio;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.RevisionTarea;

public interface IRevisionTarea
    extends CrudRepository<RevisionTarea, Integer> {

    // Contar las revisiones registradas de una tarea
    long countByTarea_Id(Integer idTarea);

}
