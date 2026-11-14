
package mx.uv.luzioteamtime.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uv.luzioteamtime.modelo.MiembroEquipo;

public interface IMiembroEquipo extends CrudRepository<MiembroEquipo, Integer> {

    List<MiembroEquipo> findByUsuarioId(Integer idUsuario);

    List<MiembroEquipo> findByEquipoId(Integer idEquipo);

    List<MiembroEquipo> findByUsuarioIdAndEquipo_Archivado(Integer idUsuario, Boolean archivado);

    MiembroEquipo findByUsuario_IdAndEquipo_Id(Integer idUsuario, Integer idEquipo);

}
