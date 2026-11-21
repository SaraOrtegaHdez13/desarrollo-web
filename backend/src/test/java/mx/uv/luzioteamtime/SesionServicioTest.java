package mx.uv.luzioteamtime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import mx.uv.luzioteamtime.modelo.Equipo;
import mx.uv.luzioteamtime.modelo.Sesion;
import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.repositorio.ISesion;
import mx.uv.luzioteamtime.servicio.SesionServicio;
import mx.uv.luzioteamtime.servicio.TareaServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SesionServicioTest {

    private TareaServicio tareaServicio;
    private ISesion sesiones;
    private SesionServicio servicio;

    private Tarea tarea;
    private Usuario usuario;

    private final LocalDateTime inicio =
        LocalDateTime.of(2026, 11, 21, 2, 10);

    private final LocalDateTime fin =
        LocalDateTime.of(2026, 11, 21, 2, 35);

    @BeforeEach
    void preparar() {
        tareaServicio = mock(TareaServicio.class);
        sesiones = mock(ISesion.class);

        servicio = new SesionServicio(tareaServicio, sesiones);

        Equipo equipo = new Equipo(
            "Equipo prueba", "Descripción", false
        );

        Tablero tablero = new Tablero(equipo, "Tablero prueba");

        usuario = new Usuario(
            "Usuario prueba",
            "prueba@ejemplo.com",
            "hash"
        );
        usuario.setId(10);

        tarea = new Tarea(
            tablero,
            usuario,
            "Tarea de prueba",
            "Descripción",
            "EN_PROGRESO",
            null,
            null,
            null
        );
        tarea.setId(3);
    }

    // 1. Guardar una sesión correctamente.
    @Test
    void guardarSesionCorrectamente() {
        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        when(sesiones.save(any(Sesion.class)))
            .thenAnswer(peticion -> peticion.getArgument(0));

        Sesion resultado = servicio.guardar(
            3, 10, inicio, fin, 1500
        );

        assertNotNull(resultado);
        assertEquals(tarea, resultado.getTarea());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(inicio, resultado.getInicio());
        assertEquals(fin, resultado.getFin());
        assertEquals(1500, resultado.getDuracionSegundos());

        verify(sesiones).save(any(Sesion.class));
    }

    // 2. Tarea inexistente.
    @Test
    void guardarSesionTareaInexistente() {
        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(null);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1500)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 3. Usuario sin permiso.
    @Test
    void guardarSesionSinPermiso() {
        when(tareaServicio.obtenerTarea(3, 10))
            .thenThrow(new SecurityException("No tienes permiso."));

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1500)
        );

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 4. Equipo archivado.
    @Test
    void guardarSesionEquipoArchivado() {
        tarea.getTablero().getEquipo().setArchivado(true);

        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1500)
        );

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 5. Tarea asignada a otro usuario.
    @Test
    void guardarSesionTareaDeOtroUsuario() {
        Usuario otroUsuario = new Usuario(
            "Otro usuario", "otro@ejemplo.com", "hash"
        );
        otroUsuario.setId(20);
        tarea.setResponsable(otroUsuario);

        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1500)
        );

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 6. Tarea que no está en progreso.
    @Test
    void guardarSesionTareaNoEstaEnProgreso() {
        tarea.setEstado("POR_HACER");

        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1500)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 7. Duración que no coincide con el intervalo.
    @Test
    void guardarSesionDuracionIncorrecta() {
        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, 1200)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 8. Duración nula.
    @Test
    void guardarSesionDuracionNula() {
        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.guardar(3, 10, inicio, fin, null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(sesiones, never()).save(any(Sesion.class));
    }

    // 9. Consultar sesiones correctamente.
    @Test
    void obtenerSesionesCorrectamente() {
        Sesion registro = mock(Sesion.class);

        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(tarea);
        when(sesiones.findByTarea_Id(3)).thenReturn(List.of(registro));

        when(registro.getId()).thenReturn(1);
        when(registro.getTarea()).thenReturn(tarea);
        when(registro.getUsuario()).thenReturn(usuario);
        when(registro.getInicio()).thenReturn(inicio);
        when(registro.getFin()).thenReturn(fin);
        when(registro.getDuracionSegundos()).thenReturn(1500);

        List<Map<String, Object>> resultado =
            servicio.obtenerSesiones(3, 10);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).get("id"));
        assertEquals(3, resultado.get(0).get("idTarea"));
        assertEquals(10, resultado.get(0).get("idUsuario"));
        assertEquals(1500, resultado.get(0).get("duracionSegundos"));

        verify(sesiones).findByTarea_Id(3);
    }

    // 10. Consultar una tarea inexistente.
    @Test
    void obtenerSesionesTareaInexistente() {
        when(tareaServicio.obtenerTarea(3, 10)).thenReturn(null);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.obtenerSesiones(3, 10)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(sesiones, never()).findByTarea_Id(3);
    }

    // 11. Consultar sin permiso.
    @Test
    void obtenerSesionesSinPermiso() {
        when(tareaServicio.obtenerTarea(3, 10))
            .thenThrow(new SecurityException("No tienes permiso."));

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.obtenerSesiones(3, 10)
        );

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(sesiones, never()).findByTarea_Id(3);
    }
}