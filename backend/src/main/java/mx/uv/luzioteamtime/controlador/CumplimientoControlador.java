package mx.uv.luzioteamtime.controlador;

import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import mx.uv.luzioteamtime.servicio.CumplimientoServicio;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CumplimientoControlador {

    private final CumplimientoServicio servicio;

    public CumplimientoControlador(CumplimientoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/equipos/{id}/cumplimiento")
    public String mostrar(
        @PathVariable("id") Integer idEquipo,
        @RequestParam(name = "tablero", required = false) Integer idTablero,
        HttpSession sesion,
        Model modelo
    ) {
        Integer idUsuario = (Integer) sesion.getAttribute("usuarioId");

        if (idUsuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inicia sesión.");
        }

        var reporte = servicio.generar(idEquipo, idTablero, idUsuario);
        var filas = new ArrayList<>(reporte.integrantes());
        filas.add(reporte.total());

        modelo.addAttribute("reporte", reporte);
        modelo.addAttribute("filas", filas);
        modelo.addAttribute("idEquipo", idEquipo);
        modelo.addAttribute("idTablero", idTablero);

        return "cumplimiento";
    }
}