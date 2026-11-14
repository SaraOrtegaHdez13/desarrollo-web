
package mx.uv.luzioteamtime.repositorio;

import org.springframework.data.repository.CrudRepository;

import mx.uv.luzioteamtime.modelo.Tablero;

public interface ITablero extends CrudRepository<Tablero, Integer> {

    Tablero findByEquipoId(Integer idEquipo);

}
