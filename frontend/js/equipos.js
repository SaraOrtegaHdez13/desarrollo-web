const formulario = document.getElementById('formulario-equipo');
const crear = formulario.querySelector('button');
const salir = document.getElementById('cerrar-sesion');
const mensaje = document.getElementById('mensaje');

const panelCreacion = document.getElementById('crear-equipo');
const mostrarCreacion = document.getElementById('mostrar-creacion');

function mostrarFormulario(visible) {
    panelCreacion.hidden = !visible;
    mostrarCreacion.setAttribute('aria-expanded', String(visible));
    mostrarCreacion.textContent = visible ? 'Cerrar formulario' : '＋ Crear equipo';
    if (visible) formulario.elements.nombre.focus();
}

if (mostrarCreacion && panelCreacion) {
    mostrarCreacion.addEventListener('click', () => mostrarFormulario(panelCreacion.hidden));
}

const trazosEquipo = {
    grupo: 'M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2 M13 7a4 4 0 1 0-8 0 4 4 0 0 0 8 0 M22 21v-2a4 4 0 0 0-3-3.9 M16 3a4 4 0 0 1 0 8',
    archivo: 'M3 8h18V4H3z M5 8v12h14V8 M10 12h4',
    persona: 'M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2 M16 7a4 4 0 1 0-8 0 4 4 0 0 0 8 0',
    candado: 'M5 11h14v10H5z M8 11V7a4 4 0 0 1 8 0v4',
    escudo: 'M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z',
    tablero: 'M3 4h18v16H3z M9 4v16 M15 4v16',
    reporte: 'M5 20v-5 M12 20V4 M19 20V9',
    consultar: 'M2 12s4-7 10-7 10 7 10 7-4 7-10 7-10-7-10-7z M15 12a3 3 0 1 0-6 0 3 3 0 0 0 6 0'
};

function ponerEtiqueta(elemento, icono, texto) {
    const ns = 'http://www.w3.org/2000/svg';
    const svg = document.createElementNS(ns, 'svg');
    svg.setAttribute('viewBox', '0 0 24 24');
    svg.setAttribute('class', 'icon-svg');
    svg.setAttribute('aria-hidden', 'true');
    const trazo = document.createElementNS(ns, 'path');
    trazo.setAttribute('d', trazosEquipo[icono]);
    svg.append(trazo);
    const etiqueta = document.createElement('span');
    etiqueta.textContent = texto;
    elemento.replaceChildren(svg, etiqueta);
}

function mostrarEquipos(idLista, equipos) {
    const lista = document.getElementById(idLista);
    lista.replaceChildren();

    if (equipos.length === 0) {
        const item = document.createElement('div');
        item.textContent = 'Sin equipos.';
        lista.append(item);
        return;
    }

    for (const equipo of equipos) {
        const item = document.createElement('article');
        item.className = 'card equipo-card' + (equipo.archivado ? ' equipo-archivado' : '');
        const titulo = document.createElement('h3');
        ponerEtiqueta(titulo, equipo.archivado ? 'archivo' : 'grupo', equipo.nombre);
        const cabecera = document.createElement('div');
        cabecera.className = 'equipo-card-header';
        const rol = equipo.rol === 'LIDER' ? 'Líder' : 'Colaborador';
        const badge = document.createElement('span');
        badge.className = equipo.archivado ? 'badge badge-readonly' : 'badge badge-progress';
        ponerEtiqueta(badge, equipo.archivado ? 'candado' : 'persona', equipo.archivado ? 'Solo lectura' : 'Rol: ' + rol);
        cabecera.append(titulo, badge);
        item.append(cabecera);

        if (equipo.archivado) {
            const historico = document.createElement('p');
            historico.className = 'equipo-rol-historico';
            historico.textContent = 'Tu rol histórico: ' + rol;
            item.append(historico);
        }
        if (equipo.descripcion) {
            const descripcion = document.createElement('p');
            descripcion.className = 'field-hint';
            descripcion.textContent = equipo.descripcion;
            item.append(descripcion);
        }
        const acciones = document.createElement('div');
        acciones.className = 'actions-row';
        const id = encodeURIComponent(equipo.id);
        const abrir = document.createElement('a');
        abrir.className = equipo.archivado ? 'btn btn-secondary btn-sm' : 'btn btn-primary btn-sm';
        abrir.href = 'equipo.html?id=' + id + (equipo.archivado ? '#seccion-tableros' : '');
        ponerEtiqueta(abrir, equipo.archivado ? 'consultar' : 'escudo', equipo.archivado ? 'Consultar tableros' : 'Abrir equipo');
        const reporte = document.createElement('a');
        reporte.className = 'btn btn-secondary btn-sm';
        reporte.href = 'http://localhost:8081/equipos/' + id + '/cumplimiento';
        ponerEtiqueta(reporte, 'reporte', 'Ver reporte');
        acciones.append(abrir, reporte);
        if (!equipo.archivado) {
            solicitar('/obtenerTableros/' + id).then(([tablero]) => {
                if (!tablero) return;
                const acceso = document.createElement('a');
                acceso.className = 'btn btn-secondary btn-sm';
                acceso.href = 'tablero.html?id=' + encodeURIComponent(tablero.id);
                ponerEtiqueta(acceso, 'tablero', 'Tablero: ' + tablero.nombre);
                reporte.before(acceso);
            }).catch(() => {
                // El tablero sigue disponible desde el detalle del equipo.
            });
        }
        item.append(acciones);
        lista.append(item);
    }
}

async function cargarEquipos() {
    const equipos = await solicitar('/obtenerEquipos');
    mostrarEquipos('equipos-activos', equipos.filter(equipo => !equipo.archivado));
    mostrarEquipos('equipos-archivados', equipos.filter(equipo => equipo.archivado));
}

formulario.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formulario));
    datos.nombre = datos.nombre.trim();

    if (!datos.nombre) {
        mensaje.textContent = 'Escribe un nombre para el equipo.';
        return;
    }

    crear.disabled = true;
    mensaje.textContent = '';
    let guardado = false;

    try {
        await solicitar('/crearEquipo', 'POST', datos);
        guardado = true;
        formulario.reset();
        if (mostrarCreacion && panelCreacion) {
            mostrarFormulario(false);
            mostrarCreacion.focus();
        }
        await cargarEquipos();
        mensaje.textContent = 'Equipo creado correctamente.';
    } catch (error) {
        mensaje.textContent = guardado
            ? 'El equipo se guardó. Recarga la página para actualizar la lista.'
            : error.message;
    } finally {
        crear.disabled = false;
    }
});

conectarCierreSesion();

Promise.all([cargarEquipos(), cargarUsuarioActual()])
    .then(([, usuario]) => {
        const sesion = document.getElementById('sesion-equipo');
        if (sesion) sesion.textContent = 'Sesión activa como ' + usuario.nombre + ' (' + usuario.correo + ')';
        if (mostrarCreacion && panelCreacion) mostrarCreacion.disabled = false;
        crear.disabled = false;
        salir.disabled = false;
    })
    .catch(error => {
        mensaje.textContent = error.message;
    });