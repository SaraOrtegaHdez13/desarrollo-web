
package mx.uv.luzioteamtime;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import mx.uv.luzioteamtime.controlador.ConfiguracionWeb;
import mx.uv.luzioteamtime.controlador.SesionInterceptor;
import mx.uv.luzioteamtime.controlador.TareaControlador;

import mx.uv.luzioteamtime.modelo.Tarea;
import mx.uv.luzioteamtime.modelo.Tablero;
import mx.uv.luzioteamtime.modelo.Usuario;

import mx.uv.luzioteamtime.servicio.TareaServicio;


@WebMvcTest(TareaControlador.class)
@Import({ConfiguracionWeb.class, SesionInterceptor.class})
public class TareaControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TareaServicio tareaServicio;


    // Crear una tarea simulada para las pruebas
    private Tarea crearTareaSimulada() {

        Tarea tarea = mock(Tarea.class);
        Tablero tablero = mock(Tablero.class);
        Usuario responsable = mock(Usuario.class);

        when(tarea.getId()).thenReturn(7);
        when(tarea.getTablero()).thenReturn(tablero);
        when(tablero.getId()).thenReturn(1);
        when(tarea.getResponsable()).thenReturn(responsable);
        when(responsable.getId()).thenReturn(10);

        when(tarea.getTitulo()).thenReturn("Tarea modificada");
        when(tarea.getDescripcion()).thenReturn("Descripción nueva");
        when(tarea.getEstado()).thenReturn("POR_HACER");
        when(tarea.getFechaLimite()).thenReturn(
            LocalDate.parse("2026-12-15")
        );

        return tarea;
    }


    // ==========================================
    // PRUEBAS DE CONSULTA Y FILTROS
    // ==========================================

    @Test
    public void obtenerTareasCorrectamente() throws Exception {

        when(tareaServicio.obtenerTareas(
            1, 10, null, null
        )).thenReturn(List.of());

        mockMvc.perform(
            get("/obtenerTareas/1")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
    }


    @Test
    public void obtenerTareasSinSesion() throws Exception {

        mockMvc.perform(
            get("/obtenerTareas/1")
        )
        .andExpect(status().isUnauthorized());
    }


    @Test
    public void obtenerTareasTableroInexistente() throws Exception {

        when(tareaServicio.obtenerTareas(
            99, 10, null, null
        )).thenReturn(null);

        mockMvc.perform(
            get("/obtenerTareas/99")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isNotFound());
    }


    @Test
    public void obtenerTareasSinPermiso() throws Exception {

        when(tareaServicio.obtenerTareas(
            1, 10, null, null
        )).thenThrow(
            new SecurityException("No perteneces a este equipo")
        );

        mockMvc.perform(
            get("/obtenerTareas/1")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isForbidden());
    }


    @Test
    public void obtenerTareasConFiltroInvalido() throws Exception {

        when(tareaServicio.obtenerTareas(
            1, 10, "INVALIDO", null
        )).thenThrow(
            new IllegalArgumentException("El estado no es válido")
        );

        mockMvc.perform(
            get("/obtenerTareas/1")
                .param("estado", "INVALIDO")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isBadRequest());
    }


    // ==========================================
    // PRUEBAS DE MODIFICACIÓN (PUT)
    // ==========================================

    @Test
    public void modificarTareaCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.modificarTarea(
            7,
            10,
            "Tarea modificada",
            "Descripción nueva",
            LocalDate.parse("2026-12-15")
        )).thenReturn(tarea);

        mockMvc.perform(
            put("/modificarTarea/7")
                .sessionAttr("usuarioId", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "titulo": "Tarea modificada",
                        "descripcion": "Descripción nueva",
                        "fechaLimite": "2026-12-15"
                    }
                    """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Tarea modificada"))
        .andExpect(jsonPath("$.estado").value("POR_HACER"));
    }


    @Test
    public void modificarTareaInexistente() throws Exception {

        when(tareaServicio.modificarTarea(
            99,
            10,
            "Tarea modificada",
            "Descripción nueva",
            LocalDate.parse("2026-12-15")
        )).thenReturn(null);

        mockMvc.perform(
            put("/modificarTarea/99")
                .sessionAttr("usuarioId", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "titulo": "Tarea modificada",
                        "descripcion": "Descripción nueva",
                        "fechaLimite": "2026-12-15"
                    }
                    """)
        )
        .andExpect(status().isNotFound());
    }


    @Test
    public void modificarTareaSinSesion() throws Exception {

        mockMvc.perform(
            put("/modificarTarea/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "titulo": "Tarea modificada",
                        "descripcion": "Descripción nueva",
                        "fechaLimite": "2026-12-15"
                    }
                    """)
        )
        .andExpect(status().isUnauthorized());
    }


    // ==========================================
    // PRUEBAS DE ELIMINACIÓN (DELETE)
    // ==========================================

    @Test
    public void eliminarTareaCorrectamente() throws Exception {

        when(tareaServicio.eliminarTarea(7, 10))
            .thenReturn(true);

        mockMvc.perform(
            delete("/eliminarTarea/7")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string("Tarea eliminada correctamente")
        );
    }


    @Test
    public void eliminarTareaInexistente() throws Exception {

        when(tareaServicio.eliminarTarea(99, 10))
            .thenReturn(false);

        mockMvc.perform(
            delete("/eliminarTarea/99")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isNotFound());
    }


    // ==========================================
    // PRUEBAS DE INICIAR TAREA
    // ==========================================

    @Test
    public void iniciarTareaCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.iniciarTarea(7, 10))
            .thenReturn(tarea);

        mockMvc.perform(
            put("/iniciarTarea/7")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk());
    }


    @Test
    public void iniciarTareaInexistente() throws Exception {

        when(tareaServicio.iniciarTarea(99, 10))
            .thenReturn(null);

        mockMvc.perform(
            put("/iniciarTarea/99")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isNotFound());
    }


    @Test
    public void iniciarTareaSinPermiso() throws Exception {

        when(tareaServicio.iniciarTarea(7, 10))
            .thenThrow(
                new SecurityException("No tienes permiso")
            );

        mockMvc.perform(
            put("/iniciarTarea/7")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isForbidden());
    }


    // ==========================================
    // PRUEBAS DE ENVIAR A REVISIÓN
    // ==========================================

    @Test
    public void enviarTareaRevisionCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.enviarTareaRevision(7, 10))
            .thenReturn(tarea);

        mockMvc.perform(
            put("/enviarTareaRevision/7")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk());
    }


    @Test
    public void enviarTareaRevisionInexistente() throws Exception {

        when(tareaServicio.enviarTareaRevision(99, 10))
            .thenReturn(null);

        mockMvc.perform(
            put("/enviarTareaRevision/99")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isNotFound());
    }


    // ==========================================
    // PRUEBAS DE APROBAR TAREA
    // ==========================================

    @Test
    public void aprobarTareaCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.aprobarTarea(7, 20))
            .thenReturn(tarea);

        mockMvc.perform(
            put("/aprobarTarea/7")
                .sessionAttr("usuarioId", 20)
        )
        .andExpect(status().isOk());
    }


    @Test
    public void aprobarTareaSinPermiso() throws Exception {

        when(tareaServicio.aprobarTarea(7, 10))
            .thenThrow(
                new SecurityException("Solo un líder puede aprobar")
            );

        mockMvc.perform(
            put("/aprobarTarea/7")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isForbidden());
    }


    // ==========================================
    // PRUEBAS DE RECHAZAR TAREA Y OBSERVACIONES
    // ==========================================

    @Test
    public void rechazarTareaCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.rechazarTarea(
            7,
            20,
            "Falta corregir la documentación"
        )).thenReturn(tarea);

        mockMvc.perform(
            put("/rechazarTarea/7")
                .sessionAttr("usuarioId", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "observacion": "Falta corregir la documentación"
                    }
                    """)
        )
        .andExpect(status().isOk());
    }


    @Test
    public void rechazarTareaSinObservacion() throws Exception {

        when(tareaServicio.rechazarTarea(7, 20, ""))
            .thenThrow(
                new IllegalArgumentException(
                    "La observación es obligatoria"
                )
            );

        mockMvc.perform(
            put("/rechazarTarea/7")
                .sessionAttr("usuarioId", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "observacion": ""
                    }
                    """)
        )
        .andExpect(status().isBadRequest());
    }


    @Test
    public void rechazarTareaSinDatos() throws Exception {

        mockMvc.perform(
            put("/rechazarTarea/7")
                .sessionAttr("usuarioId", 20)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());
    }


    // ==========================================
    // PRUEBAS DE REASIGNAR TAREA
    // ==========================================

    @Test
    public void reasignarTareaCorrectamente() throws Exception {

        Tarea tarea = crearTareaSimulada();

        when(tareaServicio.reasignarTarea(7, 20, 30))
            .thenReturn(tarea);

        mockMvc.perform(
            put("/reasignarTarea/7")
                .sessionAttr("usuarioId", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "idResponsable": 30
                    }
                    """)
        )
        .andExpect(status().isOk());
    }


    @Test
    public void reasignarTareaSinPermiso() throws Exception {

        when(tareaServicio.reasignarTarea(7, 10, 30))
            .thenThrow(
                new SecurityException(
                    "Solo un líder puede reasignar tareas"
                )
            );

        mockMvc.perform(
            put("/reasignarTarea/7")
                .sessionAttr("usuarioId", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "idResponsable": 30
                    }
                    """)
        )
        .andExpect(status().isForbidden());
    }

}
