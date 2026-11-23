function crearAccionesTarea(tarea, usuario, equipo, { enTabla = false } = {}) {
    const acciones = document.createElement('div');
    acciones.className = 'actions-row';

    if (equipo.archivado) {
        return acciones;
    }

    const esResponsable = usuario.id === tarea.idResponsable;
    const puedeRevisar = equipo.rol === 'LIDER'
        && !esResponsable
        && tarea.estado === 'EN_REVISION';

    function agregarBoton(texto, ruta, confirmacion, obtenerDatos, destino = acciones) {
        const boton = document.createElement('button');
        boton.type = 'button';
        const iconos = { iniciarTarea: 'iniciar', enviarTareaRevision: 'enviar', aprobarTarea: 'aprobar', rechazarTarea: 'rechazar', eliminarTarea: 'eliminar' };
        const estilo = { aprobarTarea: 'btn-accent', eliminarTarea: 'btn-danger', rechazarTarea: 'btn-danger' };
        boton.className = 'btn btn-sm ' + (estilo[ruta] || 'btn-primary');
        rotularConIcono(boton, iconos[ruta], texto);

        boton.addEventListener('click', async () => {
            if (ruta === 'eliminarTarea' && !window.confirm('¿Eliminar «' + tarea.titulo + '»?')) return;
            const mensajeTarea = document.getElementById('mensaje');
            const datos = obtenerDatos?.();

            if (obtenerDatos && !datos) {
                mensajeTarea.textContent = 'Escribe una observación para rechazar la entrega.';
                return;
            }

            const botones = acciones.querySelectorAll('button');
            botones.forEach(control => { control.disabled = true; });
            mensajeTarea.className = 'banner banner-info';
            mensajeTarea.textContent = '';
            let guardada = false;

            try {
                await solicitar('/' + ruta + '/' + tarea.id, ruta === 'eliminarTarea' ? 'DELETE' : 'PUT', datos);
                guardada = true;
                await cargarTareas();
                mensajeTarea.className = 'banner banner-success';
                mensajeTarea.textContent = confirmacion;
            } catch (error) {
                mensajeTarea.className = 'banner banner-danger';
                mensajeTarea.textContent = guardada
                    ? 'El cambio se guardó. Recarga el tablero para ver el estado actualizado.'
                    : error.message;

                if (!guardada) {
                    botones.forEach(control => { control.disabled = false; });
                }
            }
        });

        destino.append(boton);
    }

    if (esResponsable && tarea.estado === 'POR_HACER') {
        agregarBoton('Iniciar tarea', 'iniciarTarea', 'Tarea iniciada.');
    }

    if (esResponsable && tarea.estado === 'EN_PROGRESO') {
        agregarBoton('Enviar a revisión', 'enviarTareaRevision', 'Tarea enviada a revisión.');
    }

    if (puedeRevisar) {
        agregarBoton(enTabla ? 'Aprobar' : 'Aprobar entrega', 'aprobarTarea', 'Tarea aprobada.');
        if (enTabla) {
            acciones.append(botonDetalle('Rechazar', 'btn btn-danger btn-sm', tarea, 'rechazar', 'rechazo'));
            return acciones;
        }
        const etiqueta = document.createElement('label');
        const observacion = document.createElement('input');
        observacion.type = 'text';
        observacion.className = 'form-control';
        etiqueta.className = 'form-label';
        etiqueta.append('Observación del rechazo', observacion);
        const rechazo = document.createElement('details');
        const abrir = document.createElement('summary');
        abrir.className = 'btn btn-danger btn-sm';
        rotularConIcono(abrir, 'rechazar', 'Rechazar con observación');
        rechazo.id = 'rechazo-entrega';
        rechazo.append(abrir, etiqueta);

        acciones.append(rechazo);
        agregarBoton('Confirmar rechazo', 'rechazarTarea', 'Entrega rechazada.', () => {
            const texto = observacion.value.trim();
            return texto ? { observacion: texto } : null;
        }, rechazo);
    }

    if (enTabla && equipo.rol === 'LIDER') {
        if (puedeReasignar(tarea)) {
            acciones.append(botonDetalle('Reasignar', 'btn btn-secondary btn-sm', tarea, 'reasignar', 'reasignacion'));
        }
        if (tarea.estado === 'POR_HACER') {
            agregarBoton('Eliminar', 'eliminarTarea', 'Tarea eliminada.');
        }
    }

    return acciones;
}