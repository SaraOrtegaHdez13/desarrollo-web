package mx.uv.luzioteamtime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import mx.uv.luzioteamtime.controlador.ConfiguracionWeb;
import mx.uv.luzioteamtime.controlador.SesionControlador;
import mx.uv.luzioteamtime.controlador.SesionInterceptor;
import mx.uv.luzioteamtime.modelo.Sesion;
import mx.uv.luzioteamtime.servicio.SesionServicio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SesionControlador.class)
@Import({ConfiguracionWeb.class, SesionInterceptor.class})
class SesionControladorTest {

    @Autowired
    private MockMvc peticion;

    @MockitoBean
    private SesionServicio servicio;

    @Test
    void crearSesionCorrectamente() throws Exception {
        Sesion sesion = mock(Sesion.class);

        when(sesion.getId()).thenReturn(1);
        when(sesion.getDuracionSegundos()).thenReturn(1500);

        when(servicio.guardar(
            eq(3),
            eq(10),
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            eq(1500)
        )).thenReturn(sesion);

        peticion.perform(post("/crearSesion/3")
                .sessionAttr("usuarioId", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "inicio": "2026-11-21T02:10:00",
                        "fin": "2026-11-21T02:35:00",
                        "duracionSegundos": 1500
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.idTarea").value(3))
            .andExpect(jsonPath("$.duracionSegundos").value(1500));

        verify(servicio).guardar(
            eq(3), eq(10),
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            eq(1500)
        );
    }

    @Test
    void crearSesionSinIniciarSesion() throws Exception {
        peticion.perform(post("/crearSesion/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "inicio": "2026-11-21T02:10:00",
                        "fin": "2026-11-21T02:35:00",
                        "duracionSegundos": 1500
                    }
                    """))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(servicio);
    }

    @Test
    void obtenerSesionesCorrectamente() throws Exception {
        List<Map<String, Object>> sesiones = List.of(
            Map.of(
                "id", 1,
                "idTarea", 3,
                "idUsuario", 10,
                "inicio", "2026-11-21T02:10:00",
                "fin", "2026-11-21T02:35:00",
                "duracionSegundos", 1500
            )
        );

        when(servicio.obtenerSesiones(3, 10)).thenReturn(sesiones);

        peticion.perform(get("/obtenerSesiones/3")
                .sessionAttr("usuarioId", 10))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].idTarea").value(3))
            .andExpect(jsonPath("$[0].duracionSegundos").value(1500));

        verify(servicio).obtenerSesiones(3, 10);
    }

    @Test
    void obtenerSesionesSinIniciarSesion() throws Exception {
        peticion.perform(get("/obtenerSesiones/3"))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(servicio);
    }

    @Test
    void obtenerSesionesSinPermiso() throws Exception {
        when(servicio.obtenerSesiones(3, 10))
            .thenThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.FORBIDDEN,
                "No perteneces al equipo de esta tarea"
            ));

        peticion.perform(get("/obtenerSesiones/3")
                .sessionAttr("usuarioId", 10))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.mensaje")
                .value("No perteneces al equipo de esta tarea"));
    }
}