package mx.uv.luzioteamtime.repositorio;

import org.springframework.data.repository.CrudRepository;
import mx.uv.luzioteamtime.modelo.AsignacionTarea;
import java.util.List;

// Repositorio para guardar y consultar asignaciones de tareas
        public interface IAsignacionTarea extends CrudRepository<AsignacionTarea, Integer> {
            // Obtener las asignaciones registradas de una tarea
            List<AsignacionTarea> findByTarea_Id(Integer idTarea);    
}