function crearAccionesTarea(tarea, usuario, equipo) {
    const acciones = document.createElement('div');

    if (equipo.archivado) {
        return acciones;
    }

    const esResponsable = usuario.id === tarea.idResponsable;
    const puedeRevisar = equipo.rol === 'LIDER'
        && !esResponsable
        && tarea.estado === 'EN_REVISION';

    function agregarBoton(texto, ruta, confirmacion, obtenerDatos) {
        const boton = document.createElement('button');
        boton.type = 'button';
        boton.textContent = texto;

        boton.addEventListener('click', async () => {
            const mensajeTarea = document.getElementById('mensaje');
            const datos = obtenerDatos?.();

            if (obtenerDatos && !datos) {
                mensajeTarea.textContent = 'Escribe una observación para rechazar la entrega.';
                return;
            }

            const botones = acciones.querySelectorAll('button');
            botones.forEach(control => { control.disabled = true; });
            mensajeTarea.textContent = '';
            let guardada = false;

            try {
                await solicitar('/' + ruta + '/' + tarea.id, 'PUT', datos);
                guardada = true;
                await cargarTareas();
                mensajeTarea.textContent = confirmacion;
            } catch (error) {
                mensajeTarea.textContent = guardada
                    ? 'El cambio se guardó. Recarga el tablero para ver el estado actualizado.'
                    : error.message;

                if (!guardada) {
                    botones.forEach(control => { control.disabled = false; });
                }
            }
        });

        acciones.append(boton);
    }

    if (esResponsable && tarea.estado === 'POR_HACER') {
        agregarBoton('Iniciar', 'iniciarTarea', 'Tarea iniciada.');
    }

    if (esResponsable && tarea.estado === 'EN_PROGRESO') {
        agregarBoton('Enviar a revisión', 'enviarTareaRevision', 'Tarea enviada a revisión.');
    }

    if (puedeRevisar) {
        const etiqueta = document.createElement('label');
        const observacion = document.createElement('input');
        observacion.type = 'text';
        etiqueta.append('Observación del rechazo ', observacion);

        agregarBoton('Aprobar', 'aprobarTarea', 'Tarea aprobada.');
        acciones.append(etiqueta);
        agregarBoton('Rechazar', 'rechazarTarea', 'Entrega rechazada.', () => {
            const texto = observacion.value.trim();
            return texto ? { observacion: texto } : null;
        });
    }

    return acciones;
}