const formulario = document.getElementById('formulario-equipo');
const crear = formulario.querySelector('button');
const salir = document.getElementById('cerrar-sesion');
const mensaje = document.getElementById('mensaje');

function mostrarEquipos(idLista, equipos) {
    const lista = document.getElementById(idLista);
    lista.replaceChildren();

    if (equipos.length === 0) {
        const item = document.createElement('li');
        item.textContent = 'Sin equipos.';
        lista.append(item);
        return;
    }

    for (const equipo of equipos) {
        const item = document.createElement('li');
        const enlace = document.createElement('a');
        const rol = equipo.rol === 'LIDER' ? 'Líder' : 'Colaborador';

        enlace.href = 'equipo.html?id=' + encodeURIComponent(equipo.id);
        enlace.textContent = equipo.nombre;
        item.append(enlace, ' · ' + rol);
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

salir.addEventListener('click', async () => {
    salir.disabled = true;

    try {
        await solicitar('/cerrarSesion', 'POST');
        location.replace('index.html');
    } catch (error) {
        mensaje.textContent = error.message;
        salir.disabled = false;
    }
});

cargarEquipos()
    .then(() => {
        crear.disabled = false;
        salir.disabled = false;
    })
    .catch(error => {
        mensaje.textContent = error.message;
    });