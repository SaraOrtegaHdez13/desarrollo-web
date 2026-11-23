const idEquipo = Number(new URLSearchParams(location.search).get('id'));
const mensaje = document.getElementById('mensaje');
const formularioIntegrante = document.getElementById('formulario-integrante');
const formularioTablero = document.getElementById('formulario-tablero');

const botonArchivar = document.getElementById('archivar-equipo');

function crearAccionEquipo(texto, icono, ruta, metodo, confirmacion, datos, pregunta) {
    const boton = document.createElement('button');
    boton.type = 'button';
    boton.className = metodo === 'DELETE' ? 'btn btn-danger btn-sm' : 'btn btn-secondary btn-sm';
    rotularConIcono(boton, icono, texto);

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

    mostrarContextoSesion(equipo);
    document.getElementById('nombre-equipo').textContent = equipo.nombre;
    document.getElementById('descripcion-equipo').textContent = equipo.descripcion || '';

    const puedeAdministrar = equipo.rol === 'LIDER' && !equipo.archivado;
    const columnaAcciones = document.getElementById('columna-acciones-integrantes');
    if (columnaAcciones) columnaAcciones.hidden = !puedeAdministrar;
    formularioIntegrante.hidden = !puedeAdministrar;
    formularioTablero.hidden = !puedeAdministrar;

    document.getElementById('seccion-archivar').hidden = !puedeAdministrar;
    botonArchivar.hidden = !puedeAdministrar;
    const estado = document.getElementById('estado-equipo');
    estado.textContent = equipo.archivado ? 'Archivado · Solo lectura' : '✓ Equipo activo';
    estado.className = equipo.archivado ? 'badge badge-readonly' : 'badge badge-done';

    document.getElementById('titulo-integrantes').textContent = 'Integrantes y roles (' + integrantes.length + ')';
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
        const rol = document.createElement('span');
        rol.className = integrante.rol === 'LIDER' ? 'badge badge-progress' : 'badge badge-todo';
        rol.textContent = integrante.rol === 'LIDER' ? 'Líder' : 'Colaborador';
        fila.insertCell().append(rol);

        const celdaAcciones = fila.insertCell();
        celdaAcciones.hidden = columnaAcciones !== null && !puedeAdministrar;

        if (!puedeAdministrar || integrante.rol !== 'COLABORADOR') {
            continue;
        }

        // Estas rutas usan el ID de la membresía, que es distinto del ID de usuario.
        const idMiembro = integrante.idMiembro;
        const acciones = document.createElement('div');
        acciones.className = 'actions-row acciones-integrante';
        celdaAcciones.append(acciones);

        if (cantidadLideres < 2) {
            acciones.append(crearAccionEquipo(
                'Promover a líder',
                'aprobar',
                '/modificarMiembro/' + idMiembro,
                'PUT',
                'Rol actualizado.',
                { rol: 'LIDER' },
                '¿Promover a ' + integrante.nombre + ' a líder?'
            ));
        }

        acciones.append(crearAccionEquipo(
            'Quitar integrante',
            'eliminar',
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
        item.className = 'card tablero-card';
        const titulo = document.createElement('strong');
        titulo.textContent = tablero.nombre;
        enlace.className = 'btn btn-primary btn-sm';
        rotularConIcono(enlace, 'tablero', equipo.archivado ? 'Consultar tablero' : 'Abrir tablero');
        const info = document.createElement('div');
        info.append(titulo);
        solicitar('/obtenerTareas/' + encodeURIComponent(tablero.id)).then(tareas => {
            const conteo = document.createElement('div');
            conteo.className = 'field-hint';
            conteo.textContent = tareas.length === 1 ? '1 tarea registrada' : tareas.length + ' tareas registradas';
            info.append(conteo);
        }).catch(() => {
            // El acceso al tablero queda disponible si falla el conteo.
        });
        item.append(info, enlace);
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

    conectarCierreSesion();
    Promise.all([cargarEquipo(), cargarUsuarioActual()]).catch(error => {
        mensaje.textContent = error.message;
    });
}