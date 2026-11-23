const idTablero = Number(new URLSearchParams(location.search).get('id'));
const mensaje = document.getElementById('mensaje');
const formulario = document.getElementById('formulario-tarea');
const botonCrear = formulario.querySelector('button');
const cuerpoTabla = document.getElementById('tareas');
const nombresIntegrantes = new Map();
let usuarioActual;
let equipoActual;
let integrantesTablero = [];
let tareasTablero = [];
let idDetalle;
let solicitudDetalle;
let pomodoroActual;
const detalle = document.getElementById('seccion-detalle');
const inspector = document.getElementById('inspeccionar-tarea');
const listado = document.getElementById('listado-tareas');
const filtros = document.getElementById('filtros-tareas');
const botonNuevaTarea = document.getElementById('mostrar-nueva-tarea');
const creacion = document.getElementById('seccion-crear-tarea');
const filtroEstado = document.getElementById('filtro-estado');
const filtroResponsable = document.getElementById('filtro-responsable');
const reasignacion = document.getElementById('form-reasignacion');

const estados = {
    POR_HACER: 'Por hacer',
    EN_PROGRESO: 'En progreso',
    EN_REVISION: 'En revisión',
    HECHA: 'Hecha'
};

const clasesEstado = {
    POR_HACER: 'badge-todo', EN_PROGRESO: 'badge-progress',
    EN_REVISION: 'badge-review', HECHA: 'badge-done'
};

function mostrarEstado(elemento, estado) {
    elemento.className = 'badge ' + (clasesEstado[estado] || 'badge-todo');
    elemento.textContent = '● ' + (estados[estado] ?? estado);
}

function fechaVisible(valor) {
    if (!valor) return '—';
    const fecha = new Date(valor.length === 10 ? valor + 'T00:00:00' : valor);
    if (Number.isNaN(fecha.getTime())) return valor;
    return fecha.toLocaleDateString('es-MX', {
        day: 'numeric', month: 'short', year: 'numeric',
        ...(valor.length > 10 ? { hour: '2-digit', minute: '2-digit' } : {})
    });
}

function botonDetalle(texto, clase, tarea, icono, panel) {
    const boton = document.createElement('button');
    boton.type = 'button';
    boton.className = clase;
    if (icono) rotularConIcono(boton, icono, texto);
    else boton.textContent = texto;
    boton.addEventListener('click', () => {
        abrirDetalle(tarea.id).then(() => {
            if (idDetalle !== tarea.id || detalle.hidden) return;
            if (panel === 'reasignacion') mostrarReasignacion();
            if (panel === 'rechazo') {
                const rechazo = document.getElementById('rechazo-entrega');
                if (!rechazo) return;
                rechazo.open = true;
                rechazo.querySelector('input').focus();
            }
        }).catch(error => { mensaje.textContent = error.message; });
    });
    return boton;
}

async function cargarTareas() {
    tareasTablero = await solicitar('/obtenerTareas/' + idTablero);
    mostrarTareas();
    if (idDetalle !== undefined) await abrirDetalle(idDetalle);
}

function mostrarTareas() {
    const tareas = tareasTablero.filter(tarea =>
        (!filtroEstado.value || tarea.estado === filtroEstado.value)
        && (!filtroResponsable.value || String(tarea.idResponsable) === filtroResponsable.value)
    );
    cuerpoTabla.replaceChildren();

    if (tareas.length === 0) {
        const celda = cuerpoTabla.insertRow().insertCell();
        celda.colSpan = 5;
        celda.textContent = 'Todavía no hay tareas.';
        return;
    }

    for (const tarea of tareas) {
        const fila = cuerpoTabla.insertRow();
        const titulo = fila.insertCell();
        titulo.append(botonDetalle(tarea.titulo, 'tarea-enlace', tarea));
        if (tarea.observacion && tarea.estado === 'EN_PROGRESO') {
            const nota = document.createElement('small');
            nota.className = 'tarea-observacion';
            nota.textContent = 'Con observación de rechazo';
            titulo.append(nota);
        } else if (tarea.fechaCompletada) {
            const nota = document.createElement('small');
            nota.className = 'tarea-fecha';
            nota.textContent = 'Aprobada: ' + fechaVisible(tarea.fechaCompletada.slice(0, 10));
            titulo.append(nota);
        }
        rotularConIcono(fila.insertCell().appendChild(document.createElement('span')), 'persona', nombresIntegrantes.get(tarea.idResponsable) ?? '—');
        const badge = document.createElement('span');
        mostrarEstado(badge, tarea.estado);
        fila.insertCell().append(badge);
        rotularConIcono(fila.insertCell().appendChild(document.createElement('span')), 'calendario', fechaVisible(tarea.fechaLimite));
        const acciones = fila.insertCell();
        const puedeTrabajar = !equipoActual.archivado && usuarioActual.id === tarea.idResponsable
            && tarea.estado === 'EN_PROGRESO';
        const controles = crearAccionesTarea(tarea, usuarioActual, equipoActual, { enTabla: true });
        if (puedeTrabajar) {
            controles.prepend(botonDetalle('Pomodoro', 'btn btn-accent btn-sm', tarea, 'reloj'));
        } else if (!controles.childElementCount) {
            controles.append(botonDetalle(equipoActual.archivado ? 'Consultar' : 'Ver detalle', 'btn btn-ghost btn-sm', tarea, 'ver'));
        }
        acciones.append(controles);
    }
}

function mostrarNavegacionDetalle(abierto) {
    document.getElementById('cabecera-tablero').hidden = abierto;
    document.getElementById('nav-detalle').hidden = !abierto;
    const navTablero = document.getElementById('nav-tablero');
    navTablero.classList.toggle('is-active', !abierto);
    if (abierto) navTablero.removeAttribute('aria-current');
    else navTablero.setAttribute('aria-current', 'page');
}

function cerrarDetalle() {
    if (pomodoroActual && !pomodoroActual.puedeSalir()) return false;
    mostrarNavegacionDetalle(false);
    pomodoroActual?.detener();
    pomodoroActual = undefined;
    idDetalle = undefined;
    solicitudDetalle = undefined;
    detalle.hidden = true;
    listado.hidden = false;
    filtros.hidden = false;
    mostrarCreacionTarea(false);
    reasignacion.hidden = true;
    return true;
}

async function abrirDetalle(idTarea) {
    if (!cerrarDetalle()) {
        inspector.value = String(idDetalle);
        return;
    }
    mensaje.textContent = '';
    idDetalle = idTarea;
    const solicitud = Symbol();
    solicitudDetalle = solicitud;
    let tarea;
    let sesiones;
    try {
        [tarea, sesiones] = await Promise.all([
            solicitar('/obtenerTarea/' + idTarea),
            solicitar('/obtenerSesiones/' + idTarea)
        ]);
    } catch (error) {
        if (solicitudDetalle !== solicitud) return;
        cerrarDetalle();
        throw error;
    }
    if (solicitudDetalle !== solicitud) return;

    document.getElementById('titulo-detalle').textContent = tarea.titulo;
    document.getElementById('detalle-descripcion').textContent = tarea.descripcion || '';
    mostrarEstado(document.getElementById('detalle-estado'), tarea.estado);
    document.getElementById('detalle-responsable').textContent =
        nombresIntegrantes.get(tarea.idResponsable) ?? '—';
    document.getElementById('detalle-fecha-limite').textContent = fechaVisible(tarea.fechaLimite);
    document.getElementById('detalle-minutos').textContent =
        (sesiones.reduce((total, sesion) => total + sesion.duracionSegundos, 0) / 60).toLocaleString('es-MX', { maximumFractionDigits: 1 }) + ' min';

    const historial = document.createElement('ul');
    historial.className = 'timeline';
    const eventos = [];
    for (const revision of tarea.revisiones) {
        const numero = revision.numeroEntrega > 0 ? ' #' + revision.numeroEntrega : '';
        if (revision.fechaEntrega) {
            eventos.push([revision.fechaEntrega, 'Entrega' + numero + ' enviada a revisión.',
                null, null, nombresIntegrantes.get(revision.idEntregadoPor)]);
        }
        if (revision.fechaRevision || !revision.fechaEntrega) {
            const resultado = { APROBADA: 'Entrega aprobada', RECHAZADA: 'Entrega rechazada y devuelta a En progreso' };
            eventos.push([revision.fechaRevision, (resultado[revision.resultado] || 'Revisión anterior') + numero,
                revision.resultado, revision.observacion, nombresIntegrantes.get(revision.idRevisor)]);
        }
    }
    for (const asignacion of tarea.asignaciones) {
        eventos.push([asignacion.fechaInicio, 'Asignación a ' + asignacion.nombre]);
        if (asignacion.fechaFin) eventos.push([asignacion.fechaFin, 'Fin de asignación de ' + asignacion.nombre]);
    }
    for (const sesion of sesiones) {
        eventos.push([sesion.fin, 'Sesión Pomodoro completada · ' + sesion.duracionSegundos / 60 + ' min']);
    }
    eventos.sort((a, b) => (a[0] || '').localeCompare(b[0] || ''));
    for (const [fecha, texto, resultado, observacion, autor] of eventos) {
        const item = document.createElement('li');
        item.className = 'timeline-item';
        if (resultado === 'RECHAZADA') item.classList.add('is-rejection');
        if (resultado === 'APROBADA') item.classList.add('is-approved');
        const meta = document.createElement('div');
        meta.className = 'timeline-meta';
        meta.textContent = [fecha ? fechaVisible(fecha) : 'Sin fecha registrada', autor].filter(Boolean).join(' · ');
        const titulo = document.createElement('div');
        titulo.className = 'timeline-title';
        titulo.textContent = texto;
        item.append(meta, titulo);
        if (observacion) {
            const nota = document.createElement('div');
            nota.className = 'timeline-obs';
            nota.textContent = 'Observación: ' + observacion;
            item.append(nota);
        }
        historial.append(item);
    }
    const observacionActual = document.getElementById('detalle-observacion');
    observacionActual.hidden = !tarea.observacion || tarea.estado !== 'EN_PROGRESO';
    observacionActual.textContent = tarea.observacion ? 'Observación de rechazo: ' + tarea.observacion : '';
    const ultimaEntrega = tarea.revisiones.filter(item => item.fechaEntrega).map(item => item.fechaEntrega).sort().at(-1);
    document.getElementById('detalle-entrega').textContent = [
        ultimaEntrega && 'Entregada: ' + fechaVisible(ultimaEntrega.slice(0, 10)),
        tarea.fechaCompletada && 'Aprobada: ' + fechaVisible(tarea.fechaCompletada.slice(0, 10))
    ].filter(Boolean).join(' · ') || '—';
    document.getElementById('detalle-historial').replaceChildren(
        historial.childElementCount ? historial : 'Sin historial registrado.'
    );

    const acciones = document.getElementById('detalle-acciones');
    acciones.replaceChildren(crearAccionesTarea(tarea, usuarioActual, equipoActual));
    const otros = responsablesElegibles().filter(persona => persona.idUsuario !== tarea.idResponsable);
    if (puedeReasignar(tarea)) {
        const selector = document.getElementById('nuevo-responsable');
        selector.replaceChildren(new Option('Selecciona un integrante', ''));
        for (const persona of otros) selector.add(new Option(persona.nombre, persona.idUsuario));
        const reasignar = document.createElement('button');
        reasignar.type = 'button';
        reasignar.dataset.reasignar = '';
        reasignar.className = 'btn btn-secondary btn-sm';
        rotularConIcono(reasignar, 'reasignar', 'Reasignar');
        reasignar.addEventListener('click', mostrarReasignacion);
        acciones.append(reasignar);
    }

    const bloqueado = equipoActual.archivado || tarea.idResponsable !== usuarioActual.id
        || tarea.estado !== 'EN_PROGRESO';
    document.getElementById('pomodoro-tarea').textContent = tarea.titulo;
    document.getElementById('pomodoro-controles').hidden = bloqueado;
    const ayuda = document.getElementById('pomodoro-bloqueado');
    ayuda.hidden = !bloqueado;
    if (equipoActual.archivado) {
        ayuda.textContent = 'Este equipo está archivado. Puedes consultar los minutos registrados.';
    } else if (tarea.idResponsable !== usuarioActual.id) {
        const responsable = nombresIntegrantes.get(tarea.idResponsable) || 'la persona responsable';
        ayuda.textContent = 'El temporizador está disponible para ' + responsable + ' mientras la tarea esté en progreso.';
    } else if (tarea.estado === 'POR_HACER') {
        ayuda.textContent = 'Selecciona «Iniciar tarea» para habilitar el Pomodoro.';
    } else {
        ayuda.textContent = 'El Pomodoro está disponible cuando la tarea está en progreso.';
    }
    if (bloqueado) {
        document.getElementById('reloj-pomodoro').textContent = '25:00';
        rotularConIcono(document.getElementById('fase-pomodoro'), 'reloj', 'Fase: Trabajo (25:00)');
        document.getElementById('fase-pomodoro').className = 'badge badge-progress';
        document.getElementById('pomodoro-barra').style.width = '0%';
        document.getElementById('mensaje-pomodoro').textContent = '';
    } else {
        pomodoroActual = prepararPomodoro(idTarea, (fin, duracionSegundos) => {
            const segundos = sesiones.reduce((total, sesion) => total + sesion.duracionSegundos, 0) + duracionSegundos;
            sesiones.push({ fin, duracionSegundos });
            document.getElementById('detalle-minutos').textContent = (segundos / 60).toLocaleString('es-MX') + ' min';
            const item = document.createElement('li');
            item.className = 'timeline-item is-approved';
            item.textContent = fechaVisible(fin) + ' · Sesión Pomodoro completada (+25 min)';
            historial.append(item);
            document.getElementById('detalle-historial').replaceChildren(historial);
        });
    }
    inspector.replaceChildren(...tareasTablero.map(item => new Option(
        item.titulo + ' (' + (estados[item.estado] ?? item.estado) + ')', item.id
    )));
    inspector.value = String(idTarea);
    listado.hidden = filtros.hidden = creacion.hidden = true;
    detalle.hidden = false;
    mostrarNavegacionDetalle(true);
    document.getElementById('volver-listado').focus();
}

function mostrarReasignacion() {
    const selector = document.getElementById('nuevo-responsable');
    if (!detalle.querySelector('[data-reasignar]')) return;
    reasignacion.hidden = false;
    selector.focus();
}

function puedeReasignar(tarea) {
    return equipoActual.rol === 'LIDER' && !equipoActual.archivado
        && ['POR_HACER', 'EN_PROGRESO'].includes(tarea.estado)
        && responsablesElegibles().some(persona => persona.idUsuario !== tarea.idResponsable);
}

function mostrarCreacionTarea(visible) {
    creacion.hidden = !visible;
    botonNuevaTarea.setAttribute('aria-expanded', String(visible));
    rotularConIcono(botonNuevaTarea, visible ? 'rechazar' : 'agregar', visible ? 'Cerrar formulario' : 'Nueva tarea');
    if (visible) formulario.elements.titulo.focus();
}

botonNuevaTarea.addEventListener('click', () => mostrarCreacionTarea(creacion.hidden));

function responsablesElegibles() {
    const lideres = integrantesTablero.filter(persona => persona.rol === 'LIDER');
    return integrantesTablero.filter(persona => persona.rol !== 'LIDER' || lideres.length >= 2);
}

document.getElementById('volver-listado').addEventListener('click', cerrarDetalle);
inspector.addEventListener('change', () => {
    abrirDetalle(Number(inspector.value)).catch(error => { mensaje.textContent = error.message; });
});
document.getElementById('nav-tablero').addEventListener('click', evento => {
    evento.preventDefault();
    if (cerrarDetalle()) filtroEstado.focus();
});
document.getElementById('cancelar-reasignacion').addEventListener('click', () => {
    reasignacion.hidden = true;
});
reasignacion.addEventListener('submit', async evento => {
    evento.preventDefault();
    const boton = reasignacion.querySelector('button[type="submit"]');
    const datos = new FormData(reasignacion);
    boton.disabled = true;
    try {
        await solicitar('/reasignarTarea/' + idDetalle, 'PUT', {
            idResponsable: Number(datos.get('idResponsable'))
        });
        await cargarTareas();
        mensaje.textContent = 'Tarea reasignada.';
    } catch (error) {
        mensaje.textContent = error.message;
    } finally {
        boton.disabled = false;
    }
});

filtroEstado.addEventListener('change', mostrarTareas);
filtroResponsable.addEventListener('change', mostrarTareas);
document.getElementById('limpiar-filtros').addEventListener('click', () => {
    filtroEstado.value = '';
    filtroResponsable.value = '';
    mostrarTareas();
});

async function cargarTablero() {
    const tablero = await solicitar('/obtenerTablero/' + idTablero);
    const [equipo, integrantes] = await Promise.all([
        solicitar('/obtenerEquipo/' + tablero.idEquipo),
        solicitar('/obtenerMiembros/' + tablero.idEquipo)
    ]);

    equipoActual = equipo;
    integrantesTablero = integrantes;
    usuarioActual = await cargarUsuarioActual();

    mostrarContextoSesion(equipo);
    rotularConIcono(document.getElementById('nombre-tablero'), 'tablero', tablero.nombre);
    rotularConIcono(document.getElementById('nav-detalle').querySelector('.nav-link'), 'reloj', 'Detalle y Pomodoro');
    for (const [selector, icono] of [['#detalle-responsable', 'persona'], ['#detalle-fecha-limite', 'calendario'], ['#detalle-minutos', 'reloj']]) {
        const etiqueta = document.querySelector(selector).previousElementSibling;
        rotularConIcono(etiqueta, icono, etiqueta.textContent);
    }
    const reporte = document.getElementById('reporte-tablero');
    reporte.href = 'http://localhost:8081/equipos/' + encodeURIComponent(tablero.idEquipo)
        + '/cumplimiento?tablero=' + idTablero;
    rotularConIcono(reporte, 'reporte', 'Reporte de cumplimiento');
    reporte.hidden = false;
    const navReporte = document.getElementById('nav-reporte');
    navReporte.href = reporte.href;
    rotularConIcono(navReporte, 'reporte', 'Reporte de cumplimiento');
    navReporte.hidden = false;
    document.getElementById('volver-listado').textContent = '← Volver al tablero ' + tablero.nombre;
    const enlaceEquipo = 'equipo.html?id=' + tablero.idEquipo;
    document.getElementById('volver-equipo').href = enlaceEquipo;
    document.getElementById('enlace-equipo').href = enlaceEquipo;
    document.getElementById('contexto-equipo').textContent = equipo.archivado
        ? equipo.nombre + ' · Solo lectura' : equipo.nombre;

    for (const integrante of integrantes) {
        nombresIntegrantes.set(integrante.idUsuario, integrante.nombre);
        filtroResponsable.add(new Option(integrante.nombre, integrante.idUsuario));
    }

    await cargarTareas();

    if (equipo.rol === 'LIDER' && !equipo.archivado) {
        const responsables = responsablesElegibles();

        const selector = document.getElementById('responsable');
        selector.add(new Option('Selecciona un integrante', ''));

        for (const integrante of responsables) {
            selector.add(new Option(integrante.nombre, integrante.idUsuario));
        }

        botonCrear.disabled = responsables.length === 0;
        document.getElementById('sin-responsables').hidden = responsables.length > 0;
        mostrarCreacionTarea(false);
        botonNuevaTarea.hidden = false;
    }
}

const campoFechaLimite = formulario.elements.fechaLimite;
const formatoFechaLimite = new Intl.DateTimeFormat('en-CA', {
    timeZone: 'America/Mexico_City',
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
});

function actualizarFechaMinima() {
    const partes = formatoFechaLimite.formatToParts(new Date());
    const fecha = Object.fromEntries(partes.map(({ type, value }) => [type, value]));
    campoFechaLimite.min = [fecha.year, fecha.month, fecha.day].join('-');
}

actualizarFechaMinima();
formulario.addEventListener('focusin', actualizarFechaMinima);

formulario.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    actualizarFechaMinima();
    if (campoFechaLimite.validity.rangeUnderflow) {
        mensaje.textContent = 'La fecha límite debe ser hoy o una fecha posterior.';
        campoFechaLimite.reportValidity();
        return;
    }

    const campos = new FormData(formulario);
    const datos = {
        titulo: campos.get('titulo').trim(),
        descripcion: campos.get('descripcion').trim(),
        fechaLimite: campos.get('fechaLimite'),
        idResponsable: Number(campos.get('idResponsable'))
    };

    if (!datos.titulo || !datos.descripcion) {
        mensaje.textContent = 'Escribe el título y la descripción de la tarea.';
        return;
    }

    botonCrear.disabled = true;
    mensaje.textContent = '';
    let guardada = false;

    try {
        await solicitar('/crearTarea/' + idTablero, 'POST', datos);
        guardada = true;
        formulario.reset();
        mostrarCreacionTarea(false);
        botonNuevaTarea.focus();
        await cargarTareas();
        mensaje.textContent = 'Tarea creada correctamente.';
    } catch (error) {
        mensaje.textContent = guardada
            ? 'La tarea se guardó. Recarga para actualizar la tabla.'
            : error.message;
    } finally {
        botonCrear.disabled = false;
    }
});

if (Number.isSafeInteger(idTablero) && idTablero > 0) {
    conectarCierreSesion();
    cargarTablero().catch(error => {
        mensaje.textContent = error.message;
    });
} else {
    mensaje.textContent = 'Abre un tablero desde el detalle de un equipo.';
}