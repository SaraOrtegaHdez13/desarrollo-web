
package mx.uv.luzioteamtime.repositorio;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.Sesion;

public interface ISesion extends CrudRepository<Sesion, Integer> {

    // Contar las sesiones de pomodoro de una tarea
    long countByTarea_Id(Integer idTarea);

}
