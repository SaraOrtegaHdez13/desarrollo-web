package mx.uv.luzioteamtime.repositorio;

import java.util.List;

import mx.uv.luzioteamtime.modelo.Sesion;

import org.springframework.data.repository.CrudRepository;

public interface ISesion extends CrudRepository<Sesion, Integer> {

    long countByTarea_Id(Integer idTarea);
    List<Sesion> findByTarea_Id(Integer idTarea);
}