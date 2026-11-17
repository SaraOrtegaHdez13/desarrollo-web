
package mx.uv.luzioteamtime.modelo;

// Datos enviados por el líder para rechazar una tarea
public class TareaRechazo {

    private String observacion;

    public TareaRechazo() {
    }

    public TareaRechazo(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
