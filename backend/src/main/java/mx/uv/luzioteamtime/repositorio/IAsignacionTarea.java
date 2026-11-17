package mx.uv.luzioteamtime.repositorio;

import org.springframework.data.repository.CrudRepository;
import mx.uv.luzioteamtime.modelo.AsignacionTarea;

// Repositorio para guardar y consultar asignaciones de tareas
public interface IAsignacionTarea
        extends CrudRepository<AsignacionTarea, Integer> {

}