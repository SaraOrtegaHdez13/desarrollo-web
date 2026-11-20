const idEquipo = Number(new URLSearchParams(location.search).get('id'));
const mensaje = document.getElementById('mensaje');
const formularioIntegrante = document.getElementById('formulario-integrante');
const formularioTablero = document.getElementById('formulario-tablero');

const botonArchivar = document.getElementById('archivar-equipo');

function crearAccionEquipo(texto, ruta, metodo, confirmacion, datos, pregunta) {
    const boton = document.createElement('button');
    boton.type = 'button';
    boton.textContent = texto;

    boton.addEventListener('click', () => {
        if (pregunta && !confirm(pregunta)) {
            return;
        }

        ejecutarAccionEquipo(boton, ruta, metodo, confirmacion, datos);
    });

    return boton;
}

async function ejecutarAccionEquipo(boton, ruta, metodo, confirmacion, datos) {
    boton.disabled = true;
    mensaje.textContent = '';
    let guardado = false;

    try {
        await solicitar(ruta, metodo, datos);
        guardado = true;
        await cargarEquipo();
        mensaje.textContent = confirmacion;
    } catch (error) {
        mensaje.textContent = guardado
            ? 'El cambio se guardó. Recarga la página para actualizar el equipo.'
            : error.message;
    } finally {
        boton.disabled = false;
    }
}

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

        botonArchivar.hidden = !puedeAdministrar;
    document.getElementById('estado-equipo').textContent =
        equipo.archivado ? 'Equipo archivado · Solo lectura' : '';

    const cantidadLideres = integrantes.filter(persona => persona.rol === 'LIDER').length;
    const enlaceReporte = document.getElementById('reporte-cumplimiento');
    enlaceReporte.href = `http://localhost:8081/equipos/${idEquipo}/cumplimiento`;
    enlaceReporte.hidden = false;

        const selectorRol = formularioIntegrante.elements.rol;
        const opcionLider = selectorRol.querySelector('option[value="LIDER"]');

        opcionLider.disabled = cantidadLideres >= 2;
        if (opcionLider.disabled && selectorRol.value === 'LIDER') {
            selectorRol.value = 'COLABORADOR';
        }

    const cuerpo = document.getElementById('integrantes');
    cuerpo.replaceChildren();
    
        for (const integrante of integrantes) {
        const fila = cuerpo.insertRow();
        fila.insertCell().textContent = integrante.nombre;
        fila.insertCell().textContent = integrante.correo;
        fila.insertCell().textContent = integrante.rol === 'LIDER' ? 'Líder' : 'Colaborador';

        const acciones = fila.insertCell();

        if (!puedeAdministrar || integrante.rol !== 'COLABORADOR') {
            continue;
        }

        // Estas rutas usan el ID de la membresía, que es distinto del ID de usuario.
        const idMiembro = integrante.idMiembro;

        if (cantidadLideres < 2) {
            acciones.append(crearAccionEquipo(
                'Promover a líder',
                '/modificarMiembro/' + idMiembro,
                'PUT',
                'Rol actualizado.',
                { rol: 'LIDER' },
                '¿Promover a ' + integrante.nombre + ' a líder?'
            ));
        }

        acciones.append(crearAccionEquipo(
            'Quitar integrante',
            '/eliminarMiembro/' + idMiembro,
            'DELETE',
            'Integrante retirado del equipo.',
            undefined,
            '¿Quitar a ' + integrante.nombre + ' del equipo?'
        ));
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

botonArchivar.addEventListener('click', () => {
    if (!confirm('¿Archivar este equipo? Quedará en modo de solo lectura.')) {
        return;
    }

    ejecutarAccionEquipo(
        botonArchivar,
        '/archivarEquipo/' + idEquipo,
        'PUT',
        'Equipo archivado.'
    );
});

if (!Number.isSafeInteger(idEquipo) || idEquipo < 1) {
    mensaje.textContent = 'Selecciona un equipo desde Mis equipos.';
} else {
    conectarFormulario(
        formularioIntegrante,
        '/agregarMiembro/' + idEquipo,
        datos => ({ correo: datos.get('correo').trim(), rol: datos.get('rol') }),
        'Integrante agregado.'
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