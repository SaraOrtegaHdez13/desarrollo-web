package mx.uv.luzioteamtime.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;


import mx.uv.luzioteamtime.repositorio.ITarea;
import mx.uv.luzioteamtime.repositorio.ITablero;
import mx.uv.luzioteamtime.repositorio.IUsuario;
import mx.uv.luzioteamtime.repositorio.IMiembroEquipo;
import mx.uv.luzioteamtime.modelo.AsignacionTarea;
import mx.uv.luzioteamtime.repositorio.IAsignacionTarea;

import java.time.LocalDate;

import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.modelo.MiembroEquipo;

@Service
public class TareaServicio {

    @Autowired
    private ITarea itarea;

    @Autowired
    private ITablero itablero;

    @Autowired
    private IUsuario iusuario;

    @Autowired
    private IMiembroEquipo imiembroEquipo;

    @Autowired
    private IAsignacionTarea iasignacionTarea;

    // B4: Crear una tarea en un tablero
    // Guardar la tarea y su asignación como una sola operación
    @Transactional
    public Tarea crearTarea(
        Integer idTablero,
        Integer idUsuario,
        Integer idResponsable,
        String titulo,
        String descripcion,
        LocalDate fechaLimite
    ) {

        // Validar que exista una sesión
        if (idUsuario == null) {
            throw new IllegalArgumentException(
                "No se identificó al usuario"
            );
        }

        // Validar que se haya indicado un responsable
        if (idResponsable == null) {
            throw new IllegalArgumentException(
                "El responsable es obligatorio"
            );
        }

        // Validar el título
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El título es obligatorio"
            );
        }

        if (titulo.trim().length() > 100) {
            throw new IllegalArgumentException(
                "El título no puede superar 100 caracteres"
            );
        }

        // Buscar el tablero
        Tablero tablero = itablero.findById(idTablero).orElse(null);

        if (tablero == null) {
            return null;
        }

        Integer idEquipo = tablero.getEquipo().getId();

        // Verificar que el usuario sea líder
        MiembroEquipo lider =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                idUsuario, idEquipo
            );

        if (lider == null || !lider.getRol().equals("LIDER")) {
            throw new SecurityException(
                "Solo un líder puede crear tareas"
            );
        }

        // No permitir cambios en equipos archivados
        if (Boolean.TRUE.equals(tablero.getEquipo().getArchivado())) {
            throw new SecurityException(
                "El equipo está archivado"
            );
        }

        // Verificar que el responsable pertenezca al equipo
        MiembroEquipo miembro =
            imiembroEquipo.findByUsuario_IdAndEquipo_Id(
                idResponsable, idEquipo
            );

        if (miembro == null) {
            throw new IllegalArgumentException(
                "El responsable no pertenece al equipo"
            );
        }

        // Buscar al usuario responsable
        Usuario responsable =
            iusuario.findById(idResponsable).orElse(null);

        if (responsable == null) {
            throw new IllegalArgumentException(
                "El responsable no existe"
            );
        }

        // Un líder solo puede recibir tareas si hay dos líderes
        if ("LIDER".equals(miembro.getRol())) {

            List<MiembroEquipo> miembros =
                imiembroEquipo.findByEquipoId(idEquipo);

            int cantidadLideres = 0;

            for (MiembroEquipo integrante : miembros) {

                if ("LIDER".equals(integrante.getRol())) {
                    cantidadLideres++;
                }
            }

            if (cantidadLideres < 2) {
                throw new IllegalArgumentException(
                    "Se necesitan al menos dos líderes para asignar una tarea a un líder"
                );
            }
        }

        // Crear la tarea con su estado inicial
        Tarea tarea = new Tarea(
            tablero,
            responsable,
            titulo.trim(),
            descripcion,
            "POR_HACER",
            fechaLimite,
            null,
            null
        );

        // Guardar primero la tarea
        Tarea tareaGuardada = itarea.save(tarea);

        // Registrar quién recibió la tarea
        AsignacionTarea asignacion = new AsignacionTarea(
            tareaGuardada,
            responsable,
            LocalDateTime.now(),
            null,
            "POR_HACER",
            null
        );

        // Guardar la primera asignación
        iasignacionTarea.save(asignacion);

        return tareaGuardada;
    }


    // B5: Consultar las tareas de un tablero
    public List<Tarea> obtenerTareas(
        Integer idTablero,
        Integer idUsuario,
        String estado,
        Integer idResponsable
    ) {

        // Buscar el tablero
        Tablero tablero = itablero.findById(idTablero).orElse(null);

        if (tablero == null) {
            return null;
        }

        // Obtener el equipo al que pertenece
        Integer idEquipo = tablero.getEquipo().getId();

        // Verificar que el usuario pertenezca al equipo
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null) {
            throw new SecurityException(
                "No perteneces a este equipo"
            );
        }

        // Consultar las tareas del tablero
        List<Tarea> tareas = itarea.findByTablero_Id(idTablero);

        // Crear una lista para los resultados
        List<Tarea> resultado = new java.util.ArrayList<>();

        // Aplicar los filtros solicitados
        for (Tarea tarea : tareas) {

            if (estado != null && !estado.isBlank()
                && !tarea.getEstado().equals(estado)) {
                continue;
            }

            if (idResponsable != null
                && !tarea.getResponsable().getId().equals(idResponsable)) {
                continue;
            }

            resultado.add(tarea);
        }

        return resultado;
    }

    
    // B6: Obtener una tarea por su ID
    public Tarea obtenerTarea(Integer idTarea, Integer idUsuario) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Identificar el equipo al que pertenece la tarea
        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Verificar que el usuario pertenezca al equipo
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null) {
            throw new SecurityException(
                "No perteneces al equipo de esta tarea"
            );
        }

        // Devolver la tarea encontrada
        return tarea;
    }


    // B7: Modificar los datos de una tarea
    public Tarea modificarTarea(
        Integer idTarea,
        Integer idUsuario,
        String titulo,
        String descripcion,
        LocalDate fechaLimite
    ) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Obtener el equipo de la tarea
        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Verificar que el usuario sea líder
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null || !"LIDER".equals(miembro.getRol())) {
            throw new SecurityException(
                "Solo un líder puede modificar tareas"
            );
        }

        // No modificar tareas de equipos archivados
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "El equipo está archivado"
            );
        }

        // No modificar tareas en revisión o terminadas
        if ("EN_REVISION".equals(tarea.getEstado())
            || "HECHA".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "No se puede modificar una tarea en revisión o terminada"
            );
        }

        // Validar el título
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El título es obligatorio"
            );
        }

        if (titulo.trim().length() > 100) {
            throw new IllegalArgumentException(
                "El título no puede superar 100 caracteres"
            );
        }

        // Actualizar los datos permitidos
        tarea.setTitulo(titulo.trim());
        tarea.setDescripcion(descripcion);
        tarea.setFechaLimite(fechaLimite);

        // Guardar los cambios
        return itarea.save(tarea);
    }



}