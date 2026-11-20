const idTablero = Number(new URLSearchParams(location.search).get('id'));
const mensaje = document.getElementById('mensaje');
const formulario = document.getElementById('formulario-tarea');
const botonCrear = formulario.querySelector('button');
const cuerpoTabla = document.getElementById('tareas');
const nombresIntegrantes = new Map();
let usuarioActual;
let equipoActual;

const estados = {
    POR_HACER: 'Por hacer',
    EN_PROGRESO: 'En progreso',
    EN_REVISION: 'En revisión',
    HECHA: 'Hecha'
};

async function cargarTareas() {
    const tareas = await solicitar('/obtenerTareas/' + idTablero);
    cuerpoTabla.replaceChildren();

    if (tareas.length === 0) {
        const celda = cuerpoTabla.insertRow().insertCell();
        celda.colSpan = 5;
        celda.textContent = 'Todavía no hay tareas.';
        return;
    }

    for (const tarea of tareas) {
        const fila = cuerpoTabla.insertRow();
        fila.insertCell().textContent = tarea.titulo;
        fila.insertCell().textContent = nombresIntegrantes.get(tarea.idResponsable) ?? '—';
        fila.insertCell().textContent = estados[tarea.estado] ?? tarea.estado;
        fila.insertCell().textContent = tarea.fechaLimite ?? '—';
        fila.insertCell().append(
            crearAccionesTarea(tarea, usuarioActual, equipoActual)
        );
    }
}

async function cargarTablero() {
    const tablero = await solicitar('/obtenerTablero/' + idTablero);
    const [equipo, integrantes] = await Promise.all([
        solicitar('/obtenerEquipo/' + tablero.idEquipo),
        solicitar('/obtenerMiembros/' + tablero.idEquipo)
    ]);

    equipoActual = equipo;
    usuarioActual = await solicitar('/obtenerUsuarioActual');

    document.getElementById('nombre-tablero').textContent = tablero.nombre;
    document.getElementById('volver-equipo').href = 'equipo.html?id=' + tablero.idEquipo;

    for (const integrante of integrantes) {
        nombresIntegrantes.set(integrante.idUsuario, integrante.nombre);
    }

    await cargarTareas();

    if (equipo.rol === 'LIDER' && !equipo.archivado) {
        const cantidadLideres = integrantes.filter(integrante => integrante.rol === 'LIDER').length;
        const responsables = integrantes.filter(integrante =>
            integrante.rol !== 'LIDER' || cantidadLideres === 2
        );

        const selector = document.getElementById('responsable');
        selector.add(new Option('Selecciona un integrante', ''));

        for (const integrante of responsables) {
            selector.add(new Option(integrante.nombre, integrante.idUsuario));
        }

        botonCrear.disabled = responsables.length === 0;
        document.getElementById('sin-responsables').hidden = responsables.length > 0;
        document.getElementById('seccion-crear-tarea').hidden = false;
    }
}

formulario.addEventListener('submit', async (evento) => {
    evento.preventDefault();

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
    cargarTablero().catch(error => {
        mensaje.textContent = error.message;
    });
} else {
    mensaje.textContent = 'Abre un tablero desde el detalle de un equipo.';
}