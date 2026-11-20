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