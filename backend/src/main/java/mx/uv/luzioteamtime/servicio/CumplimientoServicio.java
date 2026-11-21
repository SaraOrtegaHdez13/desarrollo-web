package mx.uv.luzioteamtime.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.uv.luzioteamtime.modelo.TableroResumen;
import mx.uv.luzioteamtime.modelo.Usuario;
import mx.uv.luzioteamtime.repositorio.IMiembroEquipo;
import mx.uv.luzioteamtime.repositorio.ISesion;
import mx.uv.luzioteamtime.repositorio.ITablero;
import mx.uv.luzioteamtime.repositorio.ITarea;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CumplimientoServicio {

    private final IMiembroEquipo miembros;
    private final ITablero tableros;
    private final ITarea tareas;
    private final ISesion sesiones;
    private final TareaServicio detalleTareas;

    public CumplimientoServicio(
        IMiembroEquipo miembros,
        ITablero tableros,
        ITarea tareas,
        ISesion sesiones,
        TareaServicio detalleTareas
    ) {
        this.miembros = miembros;
        this.tableros = tableros;
        this.tareas = tareas;
        this.sesiones = sesiones;
        this.detalleTareas = detalleTareas;
    }

    @Transactional(readOnly = true)
    public Reporte generar(Integer idEquipo, Integer idTablero, Integer idUsuario) {
        var acceso = miembros.findByUsuario_IdAndEquipo_Id(idUsuario, idEquipo);

        if (acceso == null) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "No perteneces a este equipo."
            );
        }

        var opciones = tableros.findByEquipoId(idEquipo);

        if (idTablero != null
            && opciones.stream().noneMatch(tablero -> idTablero.equals(tablero.getId()))) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "El tablero no pertenece al equipo."
            );
        }

        Map<Integer, Fila> personas = new LinkedHashMap<>();

        for (var integrante : miembros.findByEquipoId(idEquipo)) {
            Usuario usuario = integrante.getUsuario();
            personas.put(usuario.getId(), new Fila(usuario.getNombre(), false));
        }

        Fila total = new Fila("Total del equipo", false);

        
        for (var tablero : opciones) {

            boolean incluido = idTablero == null
                || idTablero.equals(tablero.getId());

            // Ignorar tableros que no fueron seleccionados
            if (!incluido) {
                continue;
            }

            for (var tarea : tareas.findByTablero_Id(tablero.getId())) {

                var detalle = detalleTareas.obtenerDetalleTarea(
                    tarea.getId(), idUsuario
                );

                // Registrar el historial de asignaciones
                for (var asignacion : detalle.getAsignaciones()) {

                    Fila anterior = personas.computeIfAbsent(
                        asignacion.idUsuario(),
                        id -> new Fila(asignacion.nombre(), true)
                    );

                    if (asignacion.fechaFin() != null) {
                        anterior.historial.add(
                            tarea.getTitulo() + " · Traspasada en "
                            + asignacion.estadoAlSalir()
                        );
                    }
                }

                // Contabilizar las tareas
                if (tarea.getResponsable() != null) {

                    boolean hecha = "HECHA".equals(tarea.getEstado());

                    boolean puntual = hecha
                        && tarea.getFechaCompletada() != null
                        && tarea.getFechaLimite() != null
                        && !tarea.getFechaCompletada().toLocalDate()
                            .isAfter(tarea.getFechaLimite());

                    filaDe(personas, tarea.getResponsable())
                        .contar(hecha, puntual);

                    total.contar(hecha, puntual);
                }

                // Registrar el historial de entregas
                for (var revision : detalle.getRevisiones()) {

                    Fila autor = personas.get(revision.idEntregadoPor());

                    if (autor != null) {

                        String observacion = revision.observacion() == null
                            ? "" : " · " + revision.observacion();

                        autor.historial.add(
                            tarea.getTitulo() + " · Entrega "
                            + revision.numeroEntrega()
                            + " · " + revision.fechaEntrega()
                            + " · " + revision.resultado()
                            + observacion
                        );
                    }
                }

                // Sumar las sesiones de Pomodoro
                for (var sesion : sesiones.findByTarea_Id(tarea.getId())) {

                    Integer segundos = sesion.getDuracionSegundos();

                    if (segundos != null) {
                        filaDe(personas, sesion.getUsuario()).segundos += segundos;
                        total.segundos += segundos;
                    }
                }
            }
        }


        List<TableroResumen> resumenTableros = opciones.stream()
            .map(tablero -> new TableroResumen(tablero.getId(), idEquipo, tablero.getNombre()))
            .toList();

        return new Reporte(
            acceso.getEquipo().getNombre(),
            resumenTableros,
            List.copyOf(personas.values()),
            total
        );
    }

    private Fila filaDe(Map<Integer, Fila> personas, Usuario usuario) {
        return personas.computeIfAbsent(
            usuario.getId(), id -> new Fila(usuario.getNombre(), true)
        );
    }

    public record Reporte(
        String nombreEquipo,
        List<TableroResumen> tableros,
        List<Fila> integrantes,
        Fila total
    ) {}

    public static class Fila {

        private final String nombre;
        private final boolean exIntegrante;
        private final List<String> historial = new ArrayList<>();
        private int asignadas;
        private int aprobadas;
        private int puntuales;
        private long segundos;

        Fila(String nombre, boolean exIntegrante) {
            this.nombre = nombre;
            this.exIntegrante = exIntegrante;
        }

        private void contar(boolean hecha, boolean puntual) {
            asignadas++;

            if (hecha) {
                aprobadas++;
            }

            if (puntual) {
                puntuales++;
            }
        }

        public String getNombre() {
            return nombre;
        }

        public boolean isExIntegrante() {
            return exIntegrante;
        }

        public List<String> getHistorial() {
            return historial;
        }

        public int getAsignadas() {
            return asignadas;
        }

        public int getAprobadas() {
            return aprobadas;
        }

        public int getPuntuales() {
            return puntuales;
        }

        public double getPorcentaje() {
            return asignadas == 0 ? 0 : 100.0 * aprobadas / asignadas;
        }

        public double getMinutos() {
            return segundos / 60.0;
        }
    }
}