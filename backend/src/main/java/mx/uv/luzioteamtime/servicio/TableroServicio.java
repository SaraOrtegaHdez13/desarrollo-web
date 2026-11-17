package mx.uv.luzioteamtime.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uv.luzioteamtime.modelo.Equipo;
import mx.uv.luzioteamtime.modelo.MiembroEquipo;
import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.TableroResumen;

import mx.uv.luzioteamtime.repositorio.IEquipo;
import mx.uv.luzioteamtime.repositorio.IMiembroEquipo;
import mx.uv.luzioteamtime.repositorio.ITablero;

@Service
public class TableroServicio {

    @Autowired
    private IEquipo iequipo;

    @Autowired
    private IMiembroEquipo imiembroEquipo;

    @Autowired
    private ITablero itablero;

    public List<TableroResumen> obtenerTableros(
        Integer idEquipo,
        Integer idUsuario
    ) {

        // Validar que exista un usuario en la sesión
        if (idUsuario == null) {
            throw new IllegalArgumentException(
                "No se identificó al usuario"
            );
        }

        // Buscar el equipo
        Equipo equipo = iequipo.findById(idEquipo).orElse(null);

        if (equipo == null) {
            return null;
        }

        // Comprobar que el usuario pertenezca al equipo
        MiembroEquipo miembro =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                idUsuario,
                idEquipo
            );

        if (miembro == null) {
            throw new SecurityException(
                "No tienes acceso a este equipo"
            );
        }

        // Obtener todos los tableros del equipo
        List<Tablero> tableros =
            itablero.findByEquipoId(idEquipo);

        // Preparar la lista de respuestas
        List<TableroResumen> resultados = new ArrayList<>();

        for (Tablero tablero : tableros) {

            TableroResumen resumen = new TableroResumen(
                tablero.getId(),
                equipo.getId(),
                tablero.getNombre()
            );

            resultados.add(resumen);
        }

        return resultados;
    }

    // Crear un tablero dentro de un equipo
public Tablero crearTablero(
    Integer idEquipo,
    Integer idUsuario,
    String nombre
) {

    // Validar que exista un usuario en la sesión
    if (idUsuario == null) {
        throw new IllegalArgumentException(
            "No se identificó al usuario"
        );
    }

    // Validar el nombre del tablero
    if (nombre == null || nombre.trim().isEmpty()) {
        throw new IllegalArgumentException(
            "El nombre del tablero es obligatorio"
        );
    }

    if (nombre.trim().length() > 100) {
        throw new IllegalArgumentException(
            "El nombre no puede superar 100 caracteres"
        );
    }

    // Buscar el equipo
    Equipo equipo = iequipo.findById(idEquipo).orElse(null);

    if (equipo == null) {
        return null;
    }

    // Comprobar que el usuario sea líder del equipo
    MiembroEquipo miembro =
        imiembroEquipo.findByUsuario_IdAndEquipo_Id(
            idUsuario,
            idEquipo
        );

    if (miembro == null || !miembro.getRol().equals("LIDER")) {
        throw new SecurityException(
            "Solo un líder puede crear tableros"
        );
    }

    // Comprobar que el equipo no esté archivado
    if (Boolean.TRUE.equals(equipo.getArchivado())) {
        throw new SecurityException(
            "No se pueden crear tableros en un equipo archivado"
        );
    }

    // Crear y guardar el tablero
    Tablero tablero = new Tablero(
        equipo,
        nombre.trim()
    );

    return itablero.save(tablero);
}
}