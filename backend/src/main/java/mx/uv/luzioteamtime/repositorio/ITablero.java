
package mx.uv.luzioteamtime.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.Tablero;

public interface ITablero extends CrudRepository<Tablero, Integer> {

    List<Tablero> findByEquipoId(Integer idEquipo);

}
