
package mx.uv.luzioteamtime.modelo;

// Datos necesarios para reasignar una tarea
public class TareaReasignacion {

    private Integer idResponsable;

    public TareaReasignacion() {
    }

    public TareaReasignacion(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }
}
