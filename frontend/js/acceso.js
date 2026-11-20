const formulario = document.getElementById('formulario-acceso');
const mensaje = document.getElementById('mensaje');
const boton = formulario.querySelector('button');
const esRegistro = document.body.dataset.pagina === 'registro';
const ruta = esRegistro ? '/crearUsuario' : '/iniciarSesion';

formulario.addEventListener('submit', async (evento) => {
    evento.preventDefault();
    mensaje.textContent = '';
    boton.disabled = true;

    const datos = Object.fromEntries(new FormData(formulario));

    try {
        const respuesta = await fetch('http://localhost:8081' + ruta, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        if (!respuesta.ok) {
            const tipo = respuesta.headers.get('Content-Type') || '';
            const detalle = tipo.includes('application/json')
                ? await respuesta.json()
                : await respuesta.text();

            mensaje.textContent = typeof detalle === 'string'
                ? detalle || 'No se pudo completar la solicitud.'
                : detalle.mensaje || detalle.message || 'No se pudo completar la solicitud.';
            return;
        }

        if (esRegistro) {
            formulario.reset();
            mensaje.textContent = 'Cuenta creada. Ya puedes volver e iniciar sesión.';
        } else {
            location.assign('equipos.html');
        }
    } catch {
        mensaje.textContent = 'No se pudo completar la comunicación con Spring Boot.';
    } finally {
        boton.disabled = false;
    }
});