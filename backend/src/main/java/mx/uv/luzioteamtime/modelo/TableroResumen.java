package mx.uv.luzioteamtime.modelo;

public class TableroResumen {

    private Integer id;
    private Integer idEquipo;
    private String nombre;

    public TableroResumen() {

    }

    public TableroResumen(Integer id, Integer idEquipo, String nombre) {
        this.id = id;
        this.idEquipo = idEquipo;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}