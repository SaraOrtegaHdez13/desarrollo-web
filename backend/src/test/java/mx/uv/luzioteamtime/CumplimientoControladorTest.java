
package mx.uv.luzioteamtime;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import mx.uv.luzioteamtime.controlador.ConfiguracionWeb;
import mx.uv.luzioteamtime.controlador.SesionInterceptor;
import mx.uv.luzioteamtime.controlador.CumplimientoControlador;

import mx.uv.luzioteamtime.servicio.CumplimientoServicio;

@WebMvcTest(CumplimientoControlador.class)
@Import({ConfiguracionWeb.class, SesionInterceptor.class})
public class CumplimientoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CumplimientoServicio servicio;

    // ==========================================
    // PRUEBAS DEL REPORTE DE CUMPLIMIENTO
    // ==========================================

    @Test
    public void mostrarReporteCorrectamente() throws Exception {

        CumplimientoServicio.Fila integrante =
            org.mockito.Mockito.mock(CumplimientoServicio.Fila.class);

        CumplimientoServicio.Reporte reporte =
            new CumplimientoServicio.Reporte(
                "Equipo de prueba",
                List.of(),
                List.of(integrante),
                integrante
            );

        when(servicio.generar(1, null, 10))
            .thenReturn(reporte);

        mockMvc.perform(
            get("/equipos/1/cumplimiento")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("cumplimiento"))
        .andExpect(model().attributeExists("reporte"))
        .andExpect(model().attributeExists("filas"))
        .andExpect(model().attribute("idEquipo", 1));

        verify(servicio).generar(1, null, 10);
    }

    @Test
    public void mostrarReporteSinSesion() throws Exception {

        mockMvc.perform(
            get("/equipos/1/cumplimiento")
        )
        .andExpect(status().isUnauthorized());

        verify(servicio, never()).generar(1, null, null);
    }

    @Test
    public void mostrarReporteConFiltroDeTablero() throws Exception {

        CumplimientoServicio.Fila integrante =
            org.mockito.Mockito.mock(CumplimientoServicio.Fila.class);

        CumplimientoServicio.Reporte reporte =
            new CumplimientoServicio.Reporte(
                "Equipo de prueba",
                List.of(),
                List.of(integrante),
                integrante
            );

        when(servicio.generar(1, 5, 10))
            .thenReturn(reporte);

        mockMvc.perform(
            get("/equipos/1/cumplimiento")
                .param("tablero", "5")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("cumplimiento"))
        .andExpect(model().attribute("idTablero", 5));

        verify(servicio).generar(1, 5, 10);
    }

    @Test
    public void mostrarReporteSinPermiso() throws Exception {

        when(servicio.generar(1, null, 10))
            .thenThrow(
                new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No perteneces a este equipo."
                )
            );

        mockMvc.perform(
            get("/equipos/1/cumplimiento")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isForbidden());
    }

    @Test
    public void mostrarReporteConTableroInexistente() throws Exception {

        when(servicio.generar(1, 99, 10))
            .thenThrow(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El tablero no pertenece al equipo."
                )
            );

        mockMvc.perform(
            get("/equipos/1/cumplimiento")
                .param("tablero", "99")
                .sessionAttr("usuarioId", 10)
        )
        .andExpect(status().isNotFound());
    }
}
