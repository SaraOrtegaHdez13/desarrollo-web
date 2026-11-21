package mx.uv.luzioteamtime.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import org.springframework.transaction.annotation.Transactional;

import mx.uv.luzioteamtime.repositorio.ITarea;
import mx.uv.luzioteamtime.repositorio.ITablero;
import mx.uv.luzioteamtime.repositorio.IUsuario;
import mx.uv.luzioteamtime.repositorio.IMiembroEquipo;
import mx.uv.luzioteamtime.repositorio.IAsignacionTarea;
import mx.uv.luzioteamtime.repositorio.ISesion;
import mx.uv.luzioteamtime.repositorio.IRevisionTarea;

import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.modelo.MiembroEquipo;
import mx.uv.luzioteamtime.modelo.TareaDetalle;
import mx.uv.luzioteamtime.modelo.RevisionTarea;
import mx.uv.luzioteamtime.modelo.AsignacionTarea;


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

    @Autowired
    private ISesion isesion;

    @Autowired
    private IRevisionTarea irevisionTarea;

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

        if (titulo.trim().length() > 150) {
            throw new IllegalArgumentException(
                "El título no puede superar 150 caracteres"
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

        // Validar que el estado solicitado sea válido
        if (estado != null && !estado.isBlank()) {

            if (!estado.equals("POR_HACER") &&
                !estado.equals("EN_PROGRESO") &&
                !estado.equals("EN_REVISION") &&
                !estado.equals("HECHA")) {

                throw new IllegalArgumentException(
                    "El estado indicado no es válido"
                );
            }
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

    @Transactional(readOnly = true)
    public TareaDetalle obtenerDetalleTarea(
        Integer idTarea,
        Integer idUsuario
    ) {
        Tarea tarea = obtenerTarea(idTarea, idUsuario);

        if (tarea == null) {
            return null;
        }

        List<TareaDetalle.Revision> revisiones = new ArrayList<>();

        List<RevisionTarea> entregas =
            irevisionTarea.findByTarea_IdOrderByFechaEntregaAscIdAsc(
                idTarea
            );

        for (RevisionTarea entrega : entregas) {

            Integer idRevisor = null;

            if (entrega.getRevisor() != null) {
                idRevisor = entrega.getRevisor().getId();
            }

            revisiones.add(new TareaDetalle.Revision(
                entrega.getNumeroEntrega(),
                entrega.getEntregadoPor().getId(),
                entrega.getFechaEntrega(),
                idRevisor,
                entrega.getFechaRevision(),
                entrega.getResultado(),
                entrega.getObservacion()
            ));
        }

        List<TareaDetalle.Asignacion> asignaciones =
            iasignacionTarea.findByTarea_Id(idTarea).stream()
                .sorted(Comparator.comparing(
                    AsignacionTarea::getFechaInicio
                ).thenComparing(AsignacionTarea::getId))
                .map(asignacion -> new TareaDetalle.Asignacion(
                    asignacion.getUsuario().getId(),
                    asignacion.getUsuario().getNombre(),
                    asignacion.getFechaInicio(),
                    asignacion.getFechaFin(),
                    asignacion.getEstadoAlRecibir(),
                    asignacion.getEstadoAlSalir()
                ))
                .toList();

        return new TareaDetalle(tarea, revisiones, asignaciones);
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

        if (titulo.trim().length() > 150) {
            throw new IllegalArgumentException(
                "El título no puede superar 150 caracteres"
            );
        }

        // Actualizar los datos permitidos
        tarea.setTitulo(titulo.trim());
        tarea.setDescripcion(descripcion);
        tarea.setFechaLimite(fechaLimite);

        // Guardar los cambios
        return itarea.save(tarea);
    }


    // B8: Eliminar una tarea si cumple todas las condiciones
    @Transactional
    public boolean eliminarTarea(Integer idTarea, Integer idUsuario) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return false;
        }

        // Obtener el equipo de la tarea
        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Verificar que el usuario sea líder
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null || !"LIDER".equals(miembro.getRol())) {
            throw new SecurityException(
                "Solo un líder puede eliminar tareas"
            );
        }

        // Verificar que el equipo no esté archivado
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden eliminar tareas de equipos archivados"
            );
        }

        // Solo se pueden eliminar tareas POR_HACER
        if (!"POR_HACER".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden eliminar tareas POR_HACER"
            );
        }

        // Comprobar que no existan sesiones de pomodoro
        if (isesion.countByTarea_Id(idTarea) > 0) {
            throw new IllegalArgumentException(
                "La tarea tiene sesiones de pomodoro registradas"
            );
        }

        // Comprobar que no existan revisiones
        if (irevisionTarea.countByTarea_Id(idTarea) > 0) {
            throw new IllegalArgumentException(
                "La tarea tiene historial de revisiones"
            );
        }

        // Eliminar las asignaciones relacionadas
        List<AsignacionTarea> asignaciones =
            iasignacionTarea.findByTarea_Id(idTarea);

        iasignacionTarea.deleteAll(asignaciones);

        // Eliminar la tarea
        itarea.delete(tarea);

        return true;
    }

    
    // B9: Iniciar una tarea asignada al usuario
    public Tarea iniciarTarea(Integer idTarea, Integer idUsuario) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden iniciar tareas de equipos archivados"
            );
        }

        // Comprobar que sea el responsable de la tarea
        if (idUsuario == null ||
            !idUsuario.equals(tarea.getResponsable().getId())) {

            throw new SecurityException(
                "Solo el responsable puede iniciar esta tarea"
            );
        }

        // Verificar el estado actual
        if (!"POR_HACER".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden iniciar tareas en estado POR_HACER"
            );
        }

        // Cambiar el estado
        tarea.setEstado("EN_PROGRESO");

        // Guardar el cambio
        return itarea.save(tarea);
    }


    
    // B10: Enviar una tarea a revisión
    @Transactional
    public Tarea enviarTareaRevision(
        Integer idTarea,
        Integer idUsuario
    ) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Comprobar que el usuario sea el responsable
        if (idUsuario == null ||
            !idUsuario.equals(tarea.getResponsable().getId())) {

            throw new SecurityException(
                "Solo el responsable puede enviar esta tarea a revisión"
            );
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden entregar tareas de equipos archivados"
            );
        }

        // La tarea debe estar en progreso
        if (!"EN_PROGRESO".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden enviar tareas EN_PROGRESO"
            );
        }

        // Buscar al usuario que realiza la entrega
        Usuario usuario = iusuario.findById(idUsuario).orElse(null);

        if (usuario == null) {
            throw new IllegalArgumentException(
                "El usuario no existe"
            );
        }

        // Calcular el número de la nueva entrega
        Integer numeroEntrega =
            (int) irevisionTarea.countByTarea_Id(idTarea) + 1;

        // Registrar la fecha de entrega
        LocalDateTime fechaEntrega = LocalDateTime.now();

        // Crear el registro de entrega pendiente
        RevisionTarea revision = new RevisionTarea(
            tarea,
            numeroEntrega,
            usuario,
            fechaEntrega,
            null,
            null,
            "PENDIENTE",
            null
        );

        // Guardar la entrega
        irevisionTarea.save(revision);

        // Cambiar el estado de la tarea
        tarea.setEstado("EN_REVISION");

        return itarea.save(tarea);
    }


    // B11: Aprobar una tarea enviada a revisión
    @Transactional
    public Tarea aprobarTarea(Integer idTarea, Integer idUsuario) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Obtener el equipo de la tarea
        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Comprobar que el usuario sea líder del equipo
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null || !"LIDER".equals(miembro.getRol())) {
            throw new SecurityException(
                "Solo un líder puede aprobar tareas"
            );
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden aprobar tareas de equipos archivados"
            );
        }

        // Impedir que el líder apruebe su propia tarea
        if (idUsuario.equals(tarea.getResponsable().getId())) {
            throw new SecurityException(
                "No puedes aprobar una tarea asignada a ti mismo"
            );
        }

        // La tarea debe estar en revisión
        if (!"EN_REVISION".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden aprobar tareas EN_REVISION"
            );
        }

        // Buscar al usuario que aprueba
        Usuario usuario = iusuario.findById(idUsuario).orElse(null);

        if (usuario == null) {
            throw new IllegalArgumentException(
                "El usuario no existe"
            );
        }

        // Obtener la última entrega pendiente
        List<RevisionTarea> entregas =
            irevisionTarea.findByTarea_IdOrderByFechaEntregaAscIdAsc(
                idTarea
            );

        RevisionTarea entregaPendiente = null;

        for (RevisionTarea entrega : entregas) {
            if ("PENDIENTE".equals(entrega.getResultado())) {
                entregaPendiente = entrega;
            }
        }

        if (entregaPendiente == null) {
            throw new IllegalStateException(
                "No se encontró una entrega pendiente de revisión"
            );
        }

        // Registrar los datos de la revisión
        LocalDateTime fecha = LocalDateTime.now();

        entregaPendiente.setRevisor(usuario);
        entregaPendiente.setFechaRevision(fecha);
        entregaPendiente.setResultado("APROBADA");
        entregaPendiente.setObservacion(null);

        irevisionTarea.save(entregaPendiente);

        // Marcar la tarea como terminada
        tarea.setEstado("HECHA");
        tarea.setFechaCompletada(fecha);
        tarea.setObservacion(null);

        return itarea.save(tarea);
    }



    // B12: Rechazar una tarea y solicitar correcciones

    @Transactional
    public Tarea rechazarTarea(
        Integer idTarea,
        Integer idUsuario,
        String observacion
    ) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Obtener el equipo de la tarea
        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Comprobar que el usuario sea líder del equipo
        MiembroEquipo miembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (miembro == null || !"LIDER".equals(miembro.getRol())) {
            throw new SecurityException(
                "Solo un líder puede rechazar tareas"
            );
        }

        // Comprobar que el equipo no esté archivado
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden rechazar tareas de equipos archivados"
            );
        }

        // Impedir que el líder revise su propia tarea
        if (idUsuario.equals(tarea.getResponsable().getId())) {
            throw new SecurityException(
                "No puedes rechazar una tarea asignada a ti mismo"
            );
        }

        // La tarea debe estar en revisión
        if (!"EN_REVISION".equals(tarea.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden rechazar tareas EN_REVISION"
            );
        }

        // La observación es obligatoria
        if (observacion == null || observacion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Debes escribir una observación"
            );
        }

        // Validar la longitud de la observación
        if (observacion.trim().length() > 500) {
            throw new IllegalArgumentException(
                "La observación no puede superar 500 caracteres"
            );
        }

        // Buscar al usuario que rechaza
        Usuario usuario = iusuario.findById(idUsuario).orElse(null);

        if (usuario == null) {
            throw new IllegalArgumentException(
                "El usuario no existe"
            );
        }

        // Obtener la última entrega pendiente
        List<RevisionTarea> entregas =
            irevisionTarea.findByTarea_IdOrderByFechaEntregaAscIdAsc(
                idTarea
            );

        RevisionTarea entregaPendiente = null;

        for (RevisionTarea entrega : entregas) {
            if ("PENDIENTE".equals(entrega.getResultado())) {
                entregaPendiente = entrega;
            }
        }

        if (entregaPendiente == null) {
            throw new IllegalStateException(
                "No se encontró una entrega pendiente de revisión"
            );
        }

        // Registrar los datos del rechazo
        LocalDateTime fecha = LocalDateTime.now();

        entregaPendiente.setRevisor(usuario);
        entregaPendiente.setFechaRevision(fecha);
        entregaPendiente.setResultado("RECHAZADA");
        entregaPendiente.setObservacion(observacion.trim());

        irevisionTarea.save(entregaPendiente);

        // Devolver la tarea a progreso
        tarea.setEstado("EN_PROGRESO");
        tarea.setObservacion(observacion.trim());

        return itarea.save(tarea);
    }


    // B13: Reasignar una tarea conservando su historial
    @Transactional
    public Tarea reasignarTarea(
        Integer idTarea,
        Integer idUsuario,
        Integer idNuevoResponsable
    ) {

        // Buscar la tarea
        Tarea tarea = itarea.findById(idTarea).orElse(null);

        if (tarea == null) {
            return null;
        }

        // Comprobar que exista una sesión válida
        if (idUsuario == null) {
            throw new SecurityException(
                "Debes iniciar sesión"
            );
        }

        Integer idEquipo = tarea.getTablero().getEquipo().getId();

        // Solo un líder del equipo puede reasignar
        MiembroEquipo lider = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (lider == null || !"LIDER".equals(lider.getRol())) {
            throw new SecurityException(
                "Solo un líder puede reasignar tareas"
            );
        }

        // El equipo debe estar activo
        if (Boolean.TRUE.equals(
            tarea.getTablero().getEquipo().getArchivado()
        )) {
            throw new SecurityException(
                "No se pueden reasignar tareas de equipos archivados"
            );
        }

        // Solo permitir reasignar antes de la revisión
        if (!"POR_HACER".equals(tarea.getEstado()) &&
            !"EN_PROGRESO".equals(tarea.getEstado())) {

            throw new IllegalArgumentException(
                "Solo se pueden reasignar tareas POR_HACER o EN_PROGRESO"
            );
        }

        if (idNuevoResponsable == null) {
            throw new IllegalArgumentException(
                "Debes indicar un nuevo responsable"
            );
        }

        // Evitar asignar la tarea al mismo usuario
        if (idNuevoResponsable.equals(
            tarea.getResponsable().getId()
        )) {
            throw new IllegalArgumentException(
                "La tarea ya pertenece a ese usuario"
            );
        }

        // Comprobar que el nuevo responsable sea miembro
        MiembroEquipo nuevoMiembro = imiembroEquipo
            .findByUsuario_IdAndEquipo_Id(
                idNuevoResponsable,
                idEquipo
            );

        if (nuevoMiembro == null) {
            throw new IllegalArgumentException(
                "El nuevo responsable no pertenece al equipo"
            );
        }

        // Buscar al nuevo responsable
        Usuario nuevoResponsable = iusuario
            .findById(idNuevoResponsable)
            .orElse(null);

        if (nuevoResponsable == null) {
            throw new IllegalArgumentException(
                "El nuevo responsable no existe"
            );
        }

        // Si el nuevo responsable es líder,
        // debe existir otro líder para revisar su trabajo
        if ("LIDER".equals(nuevoMiembro.getRol())) {

            boolean hayOtroLider = false;

            Iterable<MiembroEquipo> miembros =
                imiembroEquipo.findAll();

            for (MiembroEquipo miembro : miembros) {

                if (miembro.getEquipo().getId().equals(idEquipo) &&
                    "LIDER".equals(miembro.getRol()) &&
                    !miembro.getUsuario().getId()
                        .equals(idNuevoResponsable)) {

                    hayOtroLider = true;
                    break;
                }
            }

            if (!hayOtroLider) {
                throw new IllegalArgumentException(
                    "Debe existir otro líder para revisar esta tarea"
                );
            }
        }

        // Buscar el historial de asignaciones
        List<AsignacionTarea> asignaciones =
            iasignacionTarea.findByTarea_Id(idTarea);

        AsignacionTarea asignacionActual = null;

        // La asignación vigente no tiene fecha de fin
        for (AsignacionTarea asignacion : asignaciones) {

            if (asignacion.getFechaFin() == null) {

                if (asignacionActual != null) {
                    throw new IllegalStateException(
                        "La tarea tiene varias asignaciones activas"
                    );
                }

                asignacionActual = asignacion;
            }
        }

        // Evitar perder el historial si falta la asignación vigente
        if (asignacionActual == null) {
            throw new IllegalStateException(
                "La tarea no tiene una asignación activa"
            );
        }

        LocalDateTime fecha = LocalDateTime.now();

        // Cerrar la asignación anterior
        asignacionActual.setFechaFin(fecha);
        asignacionActual.setEstadoAlSalir(tarea.getEstado());

        iasignacionTarea.save(asignacionActual);

        // Actualizar el responsable sin modificar el estado
        tarea.setResponsable(nuevoResponsable);

        Tarea tareaGuardada = itarea.save(tarea);

        // Crear la nueva asignación
        AsignacionTarea nuevaAsignacion = new AsignacionTarea(
            tareaGuardada,
            nuevoResponsable,
            fecha,
            null,
            tareaGuardada.getEstado(),
            null
        );

        iasignacionTarea.save(nuevaAsignacion);

        return tareaGuardada;
    }

}