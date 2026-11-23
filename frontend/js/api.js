async function solicitar(ruta, metodo = 'GET', datos) {
    const opciones = {
        method: metodo,
        credentials: 'include'
    };

    if (datos !== undefined) {
        opciones.headers = { 'Content-Type': 'application/json' };
        opciones.body = JSON.stringify(datos);
    }

    let respuesta;

    try {
        respuesta = await fetch('http://localhost:8081' + ruta, opciones);
    } catch {
        throw new Error('No se pudo conectar con Spring Boot.');
    }

    if (respuesta.status === 401) {
        location.replace('index.html');
        throw new Error('Tu sesión terminó. Vuelve a iniciar sesión.');
    }

    const texto = await respuesta.text();
    const tipo = respuesta.headers.get('Content-Type') || '';
    const contenido = texto && tipo.includes('application/json')
        ? JSON.parse(texto)
        : texto;

    if (!respuesta.ok) {
        const detalle = typeof contenido === 'string'
            ? contenido
            : contenido?.mensaje || contenido?.message;

        throw new Error(detalle || 'No se pudo completar la solicitud (' + respuesta.status + ').');
    }

    return contenido;
}

function conectarCierreSesion() {
    const boton = document.getElementById('cerrar-sesion');
    boton.addEventListener('click', async () => {
        boton.disabled = true;
        try {
            await solicitar('/cerrarSesion', 'POST');
            location.replace('index.html');
        } catch (error) {
            document.getElementById('mensaje').textContent = error.message;
            boton.disabled = false;
        }
    });
}

async function cargarUsuarioActual() {
    const usuario = await solicitar('/obtenerUsuarioActual');
    document.getElementById('usuario-actual').textContent = usuario.nombre;
    return usuario;
}

const iconosInterfaz = {
    tablero: 'M3 4h18v16H3z M9 4v16 M15 4v16',
    persona: 'M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2 M16 7a4 4 0 1 0-8 0 4 4 0 0 0 8 0',
    calendario: 'M3 5h18v16H3z M3 10h18 M8 3v4 M16 3v4',
    reloj: 'M22 12a10 10 0 1 0-20 0 10 10 0 0 0 20 0 M12 6v6l4 2',
    reporte: 'M5 20v-5 M12 20V4 M19 20V9',
    ver: 'M2 12s4-7 10-7 10 7 10 7-4 7-10 7-10-7-10-7z M15 12a3 3 0 1 0-6 0 3 3 0 0 0 6 0',
    iniciar: 'M6 3l14 9-14 9z',
    enviar: 'M22 2 9 15 M22 2l-7 20-6-7-7-6z',
    aprobar: 'm4 12 5 5L20 6',
    rechazar: 'M22 12a10 10 0 1 0-20 0 10 10 0 0 0 20 0 M9 9l6 6 M15 9l-6 6',
    reasignar: 'm17 2 4 4-4 4 M3 12V9a3 3 0 0 1 3-3h15 M7 22l-4-4 4-4 M21 12v3a3 3 0 0 1-3 3H3',
    eliminar: 'M3 6h18 M9 6V3h6v3 M5 6l1 15h12l1-15 M10 10v7 M14 10v7',
    agregar: 'M12 5v14 M5 12h14'
};

function rotularConIcono(elemento, nombre, texto) {
    const ns = 'http://www.w3.org/2000/svg';
    const svg = document.createElementNS(ns, 'svg');
    svg.setAttribute('viewBox', '0 0 24 24');
    svg.setAttribute('class', 'icon-svg');
    svg.setAttribute('aria-hidden', 'true');
    const path = document.createElementNS(ns, 'path');
    path.setAttribute('d', iconosInterfaz[nombre]);
    const etiqueta = document.createElement('span');
    etiqueta.textContent = texto;
    svg.append(path);
    elemento.classList.add('con-icono');
    elemento.replaceChildren(svg, etiqueta);
}

function mostrarContextoSesion(equipo) {
    const pie = document.querySelector('.sidebar-user-meta');
    if (!pie) return;
    const rol = equipo.rol === 'LIDER' ? 'Líder' : 'Colaborador';
    pie.textContent = equipo.nombre + ' · ' + rol + (equipo.archivado ? ' · Solo lectura' : '');
}