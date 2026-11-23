function prepararPomodoro(idTarea, alGuardar) {
    const trabajo = document.getElementById('iniciar-pomodoro');
    const descanso = document.getElementById('iniciar-descanso');
    const reintentar = document.getElementById('reintentar-pomodoro');
    const cancelar = document.getElementById('cancelar-pomodoro');
    const fase = document.getElementById('fase-pomodoro');
    const reloj = document.getElementById('reloj-pomodoro');
    const mensaje = document.getElementById('mensaje-pomodoro');
    const progreso = document.getElementById('pomodoro-barra');
    const eventos = new AbortController();
    const opciones = { signal: eventos.signal };

    let intervalo;
    let pendiente;
    let guardando = false;

    function detenerCuenta() {
        clearInterval(intervalo);
        intervalo = undefined;
    }

    function tieneCambios() {
        return intervalo !== undefined || pendiente !== undefined;
    }

    window.addEventListener('beforeunload', evento => {
        if (!tieneCambios()) return;
        evento.preventDefault();
        evento.returnValue = '';
    }, opciones);
    let esDescanso = false;
    let descansoDisponible = false;

    function fechaLocal(fecha) {
        const desplazamiento = fecha.getTimezoneOffset() * 60_000;
        return new Date(fecha.getTime() - desplazamiento).toISOString().slice(0, 19);
    }

    function mostrarTiempo(segundos) {
        const minutos = String(Math.floor(segundos / 60)).padStart(2, '0');
        reloj.textContent = minutos + ':' + String(segundos % 60).padStart(2, '0');
        const total = esDescanso ? 300 : 1500;
        progreso.style.width = ((total - segundos) / total * 100) + '%';
    }

    function mostrarFase(descansando) {
        esDescanso = descansando;
        fase.className = 'badge ' + (descansando ? 'badge-done' : 'badge-progress');
        rotularConIcono(fase, 'reloj', descansando ? 'Fase: Descanso (05:00)' : 'Fase: Trabajo (25:00)');
        mostrarTiempo(descansando ? 300 : 1500);
    }

    function avisar(texto, tipo = 'info') {
        mensaje.className = 'banner banner-' + tipo;
        mensaje.textContent = texto;
    }

    function habilitarBotones() {
        trabajo.disabled = false;
        descanso.disabled = !descansoDisponible;
        cancelar.hidden = true;
    }

    async function guardarPendiente() {
        if (!pendiente || guardando) return;
        guardando = true;
        reintentar.hidden = true;
        cancelar.hidden = true;
        avisar('Guardando sesión de trabajo…');
        try {
            await solicitar('/crearSesion/' + idTarea, 'POST', pendiente);
        } catch (error) {
            if (eventos.signal.aborted) return;
            avisar('No se pudo confirmar el guardado. ' + error.message, 'danger');
            reintentar.hidden = false;
            cancelar.hidden = false;
            return;
        } finally {
            guardando = false;
        }
        if (eventos.signal.aborted) return;
        const { fin, duracionSegundos } = pendiente;
        pendiente = undefined;
        descansoDisponible = true;
        mostrarFase(true);
        avisar('Sesión guardada (+25 min). Ya puedes iniciar el descanso.', 'success');
        habilitarBotones();
        alGuardar(fin, duracionSegundos);
    }

    function iniciar(descansando) {
        mostrarFase(descansando);
        const duracionSegundos = descansando ? 300 : 1500;
        const inicio = new Date();
        const fin = new Date(inicio.getTime() + duracionSegundos * 1000);
        trabajo.disabled = descanso.disabled = true;
        cancelar.hidden = false;
        avisar(descansando ? 'Descanso en curso.' : 'Bloque de trabajo en curso.');

        intervalo = setInterval(async () => {
            const restantes = Math.max(0, Math.ceil((fin.getTime() - Date.now()) / 1000));
            mostrarTiempo(restantes);
            if (restantes > 0) return;

            detenerCuenta();
            cancelar.hidden = true;
            if (descansando) {
                descansoDisponible = false;
                mostrarFase(false);
                avisar('Descanso terminado. Puedes iniciar otro bloque de trabajo.', 'success');
                habilitarBotones();
                return;
            }

            pendiente = { inicio: fechaLocal(inicio), fin: fechaLocal(fin), duracionSegundos };
            await guardarPendiente();
        }, 250);
    }

    mostrarFase(false);
    habilitarBotones();
    avisar('Listo para iniciar un bloque de trabajo de 25 minutos.');
    trabajo.addEventListener('click', () => iniciar(false), opciones);
    descanso.addEventListener('click', () => iniciar(true), opciones);
    reintentar.hidden = true;
    reintentar.addEventListener('click', guardarPendiente, opciones);
    cancelar.addEventListener('click', () => {
        if (pendiente && !window.confirm('¿Salir del intento de guardado? La sesión podría haberse registrado.')) return;
        pendiente = undefined;
        reintentar.hidden = true;
        detenerCuenta();
        mostrarFase(esDescanso);
        avisar('Temporizador cerrado.');
        habilitarBotones();
    }, opciones);

    return {
        puedeSalir() {
            if (guardando) {
                avisar('Espera a que termine el guardado antes de salir.');
                return false;
            }
            return !tieneCambios() || window.confirm(pendiente
                ? 'El guardado sigue sin confirmar. ¿Salir del detalle?'
                : 'Salir del detalle detendrá el temporizador. ¿Continuar?');
        },
        detener() {
            detenerCuenta();
            eventos.abort();
        }
    };
}