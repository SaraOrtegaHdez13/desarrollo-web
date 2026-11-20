function prepararPomodoro(idTarea) {
    const boton = document.getElementById('iniciar-pomodoro');
    const cancelar = document.getElementById('cancelar-pomodoro');
    const fase = document.getElementById('fase-pomodoro');
    const reloj = document.getElementById('reloj-pomodoro');
    const mensaje = document.getElementById('mensaje-pomodoro');

    let esDescanso = false;
    let intervalo;

    function fechaLocal(fecha) {
        const desplazamiento = fecha.getTimezoneOffset() * 60_000;
        return new Date(fecha.getTime() - desplazamiento).toISOString().slice(0, 19);
    }

    function mostrarTiempo(segundos) {
        const minutos = String(Math.floor(segundos / 60)).padStart(2, '0');
        const resto = String(segundos % 60).padStart(2, '0');
        reloj.textContent = minutos + ':' + resto;
    }

    function detenerCuenta() {
        clearInterval(intervalo);
        cancelar.hidden = true;
    }

    cancelar.addEventListener('click', () => {
        detenerCuenta();
        mostrarTiempo(esDescanso ? 300 : 1500);
        mensaje.textContent = 'Temporizador cancelado.';
        boton.disabled = false;
    });

    boton.addEventListener('click', () => {
        const duracionSegundos = esDescanso ? 300 : 1500;
        const inicio = new Date();
        const fin = new Date(inicio.getTime() + duracionSegundos * 1000);

        boton.disabled = true;
        cancelar.hidden = false;
        mensaje.textContent = '';
        fase.textContent = esDescanso ? 'Descanso' : 'Trabajo';
        mostrarTiempo(duracionSegundos);

        intervalo = setInterval(async () => {
            const restantes = Math.max(0, Math.ceil((fin.getTime() - Date.now()) / 1000));
            mostrarTiempo(restantes);

            if (restantes > 0) {
                return;
            }

            detenerCuenta();

            if (esDescanso) {
                mensaje.textContent = 'Descanso terminado.';
            } else {
                try {
                    await solicitar('/crearSesion/' + idTarea, 'POST', {
                        inicio: fechaLocal(inicio),
                        fin: fechaLocal(fin),
                        duracionSegundos
                    });
                    mensaje.textContent = 'Sesión de trabajo guardada.';
                } catch (error) {
                    mensaje.textContent = 'Terminó el tiempo de trabajo. ' + error.message;
                }
            }

            esDescanso = !esDescanso;
            fase.textContent = esDescanso ? 'Descanso' : 'Trabajo';
            mostrarTiempo(esDescanso ? 300 : 1500);
            boton.textContent = esDescanso ? 'Iniciar descanso' : 'Iniciar trabajo';
            boton.disabled = false;
        }, 250);
    });
}