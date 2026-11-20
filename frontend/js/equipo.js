const idEquipo = Number(new URLSearchParams(location.search).get('id'));
const mensaje = document.getElementById('mensaje');
const formularioIntegrante = document.getElementById('formulario-integrante');
const formularioTablero = document.getElementById('formulario-tablero');

async function cargarEquipo() {
    const [equipo, integrantes, tableros] = await Promise.all([
        solicitar('/obtenerEquipo/' + idEquipo),
        solicitar('/obtenerMiembros/' + idEquipo),
        solicitar('/obtenerTableros/' + idEquipo)
    ]);

    document.getElementById('nombre-equipo').textContent = equipo.nombre;
    document.getElementById('descripcion-equipo').textContent = equipo.descripcion || '';

    const puedeAdministrar = equipo.rol === 'LIDER' && !equipo.archivado;
    formularioIntegrante.hidden = !puedeAdministrar;
    formularioTablero.hidden = !puedeAdministrar;

    const cuerpo = document.getElementById('integrantes');
    cuerpo.replaceChildren();

    for (const integrante of integrantes) {
        const fila = cuerpo.insertRow();
        fila.insertCell().textContent = integrante.nombre;
        fila.insertCell().textContent = integrante.correo;
        fila.insertCell().textContent = integrante.rol === 'LIDER' ? 'Líder' : 'Colaborador';
    }

    const lista = document.getElementById('tableros');
    lista.replaceChildren();

    if (tableros.length === 0) {
        const item = document.createElement('li');
        item.textContent = 'Todavía no hay tableros.';
        lista.append(item);
        return;
    }

    for (const tablero of tableros) {
        const item = document.createElement('li');
        const enlace = document.createElement('a');

        enlace.href = 'tablero.html?id=' + encodeURIComponent(tablero.id);
        enlace.textContent = tablero.nombre;
        item.append(enlace);
        lista.append(item);
    }
}

function conectarFormulario(formulario, ruta, prepararDatos, confirmacion) {
    formulario.addEventListener('submit', async (evento) => {
        evento.preventDefault();

        const boton = formulario.querySelector('button');
        boton.disabled = true;
        mensaje.textContent = '';
        let guardado = false;

        try {
            const datos = prepararDatos(new FormData(formulario));
            await solicitar(ruta, 'POST', datos);
            guardado = true;
            formulario.reset();
            await cargarEquipo();
            mensaje.textContent = confirmacion;
        } catch (error) {
            mensaje.textContent = guardado
                ? 'Se guardó el cambio. Recarga la página para actualizar la lista.'
                : error.message;
        } finally {
            boton.disabled = false;
        }
    });
}

if (!Number.isSafeInteger(idEquipo) || idEquipo < 1) {
    mensaje.textContent = 'Selecciona un equipo desde Mis equipos.';
} else {
    conectarFormulario(
        formularioIntegrante,
        '/agregarMiembro/' + idEquipo,
        datos => ({ correo: datos.get('correo').trim(), rol: 'COLABORADOR' }),
        'Colaborador agregado.'
    );

    conectarFormulario(
        formularioTablero,
        '/crearTablero/' + idEquipo,
        datos => {
            const nombre = datos.get('nombre').trim();

            if (!nombre) {
                throw new Error('Escribe un nombre para el tablero.');
            }

            return { nombre };
        },
        'Tablero creado.'
    );

    cargarEquipo().catch(error => {
        mensaje.textContent = error.message;
    });
}