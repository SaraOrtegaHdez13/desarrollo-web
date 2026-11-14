
package mx.uv.luzioteamtime.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uv.luzioteamtime.modelo.Equipo;
import mx.uv.luzioteamtime.modelo.EquipoResumen;
import mx.uv.luzioteamtime.modelo.MiembroEquipo;
import mx.uv.luzioteamtime.modelo.MiembroResumen;
import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.Usuario;

import mx.uv.luzioteamtime.repositorio.IEquipo;
import mx.uv.luzioteamtime.repositorio.IMiembroEquipo;
import mx.uv.luzioteamtime.repositorio.ITarea;
import mx.uv.luzioteamtime.repositorio.IUsuario;

@Service
public class EquipoServicio {

    @Autowired
    private IEquipo iequipo;

    @Autowired
    private IMiembroEquipo imiembroEquipo;

    @Autowired
    private IUsuario iusuario;

    @Autowired
    private ITarea itarea;

    public Equipo crearEquipo(String nombre, String descripcion, Integer idUsuario) {

        // Validar los datos recibidos
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres");
        }

        if (descripcion != null && descripcion.length() > 255) {
            throw new IllegalArgumentException("La descripción no puede superar 255 caracteres");
        }

        // Buscar al usuario que inició sesión
        Usuario usuario = iusuario.findById(idUsuario).orElse(null);

        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario de la sesión");
        }

        // Crear y guardar el equipo
        Equipo equipo = new Equipo(
            nombre.trim(),
            descripcion,
            false
        );

        equipo = iequipo.save(equipo);

        // Registrar al creador como líder
        MiembroEquipo miembro = new MiembroEquipo(
            usuario,
            equipo,
            "LIDER"
        );

        imiembroEquipo.save(miembro);

        return equipo;
    }


    
    public List<EquipoResumen> obtenerEquipos(
        Integer idUsuario,
        Boolean archivado
    ) {

        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        // Consultar todas las membresías o aplicar el filtro
        List<MiembroEquipo> membresias;

        if (archivado == null) {
            membresias = imiembroEquipo.findByUsuarioId(idUsuario);
        } else {
            membresias = imiembroEquipo.findByUsuarioIdAndEquipo_Archivado(
                idUsuario,
                archivado
            );
        }

        List<EquipoResumen> equipos = new ArrayList<>();

        // Preparar los datos de cada equipo
        for (MiembroEquipo miembro : membresias) {

            Equipo equipo = miembro.getEquipo();

            EquipoResumen resumen = new EquipoResumen(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getDescripcion(),
                equipo.getArchivado(),
                miembro.getRol()
            );

            equipos.add(resumen);
        }

        return equipos;
    }

    public EquipoResumen obtenerEquipo(Integer idEquipo, Integer idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        // Buscar el equipo
        Equipo equipo = iequipo.findById(idEquipo).orElse(null);

        if (equipo == null) {
            return null;
        }

        // Comprobar que el usuario pertenece al equipo
        MiembroEquipo miembro = imiembroEquipo.findByUsuario_IdAndEquipo_Id(
            idUsuario,
            idEquipo
        );

        if (miembro == null) {
            throw new SecurityException("No tienes acceso a este equipo");
        }

        // Preparar los datos del equipo y el rol del usuario
        EquipoResumen resumen = new EquipoResumen(
            equipo.getId(),
            equipo.getNombre(),
            equipo.getDescripcion(),
            equipo.getArchivado(),
            miembro.getRol()
        );

        return resumen;
    }

    public List<MiembroResumen> obtenerMiembros(Integer idEquipo, Integer idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        // Buscar el equipo
        Equipo equipo = iequipo.findById(idEquipo).orElse(null);

        if (equipo == null) {
            return null;
        }

        // Comprobar que el usuario pertenece al equipo
        MiembroEquipo membresia = imiembroEquipo.findByUsuario_IdAndEquipo_Id(
            idUsuario,
            idEquipo
        );

        if (membresia == null) {
            throw new SecurityException("No tienes acceso a este equipo");
        }

        // Consultar las membresías del equipo
        List<MiembroEquipo> integrantes = imiembroEquipo.findByEquipoId(idEquipo);

        List<MiembroResumen> miembros = new ArrayList<>();

        // Preparar los datos de cada integrante
        for (MiembroEquipo miembro : integrantes) {

            Usuario usuario = miembro.getUsuario();

            MiembroResumen resumen = new MiembroResumen(
                miembro.getId(),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                miembro.getRol()
            );

            miembros.add(resumen);
        }

        return miembros;
    }

    public MiembroResumen agregarMiembro(
        Integer idEquipo,
        Integer idUsuario,
        String correo,
        String rol) {

        // Validar que exista un usuario en la sesión
        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        // Validar el correo
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        correo = correo.trim();

        // Validar el rol solicitado
        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        rol = rol.trim();

        if (!rol.equals("LIDER") && !rol.equals("COLABORADOR")) {
            throw new IllegalArgumentException("El rol debe ser LIDER o COLABORADOR");
        }

        // Buscar el equipo
        Equipo equipo = iequipo.findById(idEquipo).orElse(null);

        if (equipo == null) {
            return null;
        }

        // Comprobar que el usuario que realiza la operación sea líder
        MiembroEquipo lider = imiembroEquipo.findByUsuario_IdAndEquipo_Id(
            idUsuario,
            idEquipo
        );

        if (lider == null || !lider.getRol().equals("LIDER")) {
            throw new SecurityException("Solo un líder puede agregar integrantes");
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(equipo.getArchivado())) {
            throw new SecurityException("No se pueden agregar integrantes a un equipo archivado");
        }

        // Buscar al usuario que se desea agregar
        Usuario usuario = iusuario.findByCorreo(correo).orElse(null);

        if (usuario == null) {
            throw new NoSuchElementException("No existe un usuario registrado con ese correo");
        }

        // Comprobar que el usuario no pertenezca ya al equipo
        MiembroEquipo membresiaExistente =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                usuario.getId(),
                idEquipo
            );

        if (membresiaExistente != null) {
            throw new IllegalArgumentException("El usuario ya pertenece a este equipo");
        }

        // Comprobar el límite de líderes
        if (rol.equals("LIDER")) {

            List<MiembroEquipo> integrantes =
                imiembroEquipo.findByEquipoId(idEquipo);

            int cantidadLideres = 0;

            for (MiembroEquipo miembro : integrantes) {

                if (miembro.getRol().equals("LIDER")) {
                    cantidadLideres++;
                }
            }

            if (cantidadLideres >= 2) {
                throw new IllegalArgumentException("El equipo ya tiene dos líderes");
            }
        }

        // Guardar la nueva membresía
        MiembroEquipo nuevoMiembro = new MiembroEquipo(
            usuario,
            equipo,
            rol
        );

        nuevoMiembro = imiembroEquipo.save(nuevoMiembro);

        // Devolver solo los datos públicos del integrante
        return new MiembroResumen(
            nuevoMiembro.getId(),
            usuario.getId(),
            usuario.getNombre(),
            usuario.getCorreo(),
            nuevoMiembro.getRol()
        );
    }

    public MiembroResumen modificarMiembro(
    Integer idMiembro,
    Integer idUsuario,
    String rol
    ) {

        // Validar que exista un usuario en la sesión
        if (idUsuario == null) {
            throw new IllegalArgumentException("No se identificó al usuario");
        }

        // Validar el rol solicitado
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        rol = rol.trim();

        if (!rol.equals("LIDER") && !rol.equals("COLABORADOR")) {
            throw new IllegalArgumentException("El rol debe ser LIDER o COLABORADOR");
        }

        // Buscar la membresía que se desea modificar
        MiembroEquipo miembro = imiembroEquipo.findById(idMiembro).orElse(null);

        if (miembro == null) {
            return null;
        }

        // Obtener el equipo de la membresía
        Equipo equipo = miembro.getEquipo();

        // Comprobar que quien realiza la operación sea líder del equipo
        MiembroEquipo lider = imiembroEquipo.findByUsuario_IdAndEquipo_Id(
            idUsuario,
            equipo.getId()
        );

        if (lider == null || !lider.getRol().equals("LIDER")) {
            throw new SecurityException("Solo un líder puede modificar los roles");
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(equipo.getArchivado())) {
            throw new SecurityException("No se pueden modificar miembros de un equipo archivado");
        }

        // Aplicar la regla RN-05: ningún líder puede ser degradado
        if (miembro.getRol().equals("LIDER") && rol.equals("COLABORADOR")) {
            throw new IllegalArgumentException("No se puede degradar a un líder");
        }

        // Comprobar el límite de dos líderes al promover un colaborador
        if (miembro.getRol().equals("COLABORADOR") && rol.equals("LIDER")) {

            List<MiembroEquipo> integrantes =
                imiembroEquipo.findByEquipoId(equipo.getId());

            int cantidadLideres = 0;

            for (MiembroEquipo integrante : integrantes) {

                if (integrante.getRol().equals("LIDER")) {
                    cantidadLideres++;
                }
            }

            if (cantidadLideres >= 2) {
                throw new IllegalArgumentException("El equipo ya tiene dos líderes");
            }
        }

        // Actualizar y guardar el rol
        miembro.setRol(rol);
        miembro = imiembroEquipo.save(miembro);

        Usuario usuario = miembro.getUsuario();

        // Devolver solo los datos públicos del integrante
        return new MiembroResumen(
            miembro.getId(),
            usuario.getId(),
            usuario.getNombre(),
            usuario.getCorreo(),
            miembro.getRol()
        );
    }


    public boolean eliminarMiembro(
        Integer idMiembro,
        Integer idUsuario
    ) {

        // Validar que exista un usuario en la sesión
        if (idUsuario == null) {
            throw new IllegalArgumentException(
                "No se identificó al usuario"
            );
        }

        // Buscar la membresía que se desea eliminar
        MiembroEquipo miembro =
            imiembroEquipo.findById(idMiembro).orElse(null);

        if (miembro == null) {
            return false;
        }

        // Obtener el equipo de la membresía
        Equipo equipo = miembro.getEquipo();

        // Comprobar que quien realiza la operación sea líder
        MiembroEquipo lider =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                idUsuario,
                equipo.getId()
            );

        if (lider == null || !lider.getRol().equals("LIDER")) {
            throw new SecurityException(
                "Solo un líder puede eliminar integrantes"
            );
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(equipo.getArchivado())) {
            throw new SecurityException(
                "No se pueden eliminar integrantes de un equipo archivado"
            );
        }

        // Aplicar RN-05: no se puede eliminar a un líder
        if (miembro.getRol().equals("LIDER")) {
            throw new IllegalArgumentException(
                "No se puede eliminar a un líder"
            );
        }

        // Definir los estados que impiden eliminar al integrante
        List<String> estadosPendientes = new ArrayList<>();

        estadosPendientes.add("POR_HACER");
        estadosPendientes.add("EN_PROGRESO");
        estadosPendientes.add("EN_REVISION");

        // Consultar las tareas pendientes del integrante en ese equipo
        List<Tarea> tareasPendientes =
            itarea.findByResponsable_IdAndTablero_Equipo_IdAndEstadoIn(
                miembro.getUsuario().getId(),
                equipo.getId(),
                estadosPendientes
            );

        // Aplicar RN-14
        if (!tareasPendientes.isEmpty()) {
            throw new IllegalArgumentException(
                "No se puede eliminar al integrante porque tiene tareas sin terminar"
            );
        }

        // Eliminar la membresía
        imiembroEquipo.deleteById(idMiembro);

        return true;
    }


    public Equipo archivarEquipo(Integer idEquipo, Integer idUsuario) {

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

        // Comprobar que quien realiza la operación sea líder
        MiembroEquipo lider =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                idUsuario,
                idEquipo
            );

        if (lider == null || !lider.getRol().equals("LIDER")) {
            throw new SecurityException(
                "Solo un líder puede archivar el equipo"
            );
        }

        // Archivar el equipo
        equipo.setArchivado(true);

        // Guardar los cambios
        equipo = iequipo.save(equipo);

        return equipo;
    }




}
