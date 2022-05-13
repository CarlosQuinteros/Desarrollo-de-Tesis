package backendAdministradorCompetenciasFutbolisticas.Entity;

import javax.validation.constraints.NotNull;

public class Categoria {

    private Long id;

    private String nombre;

    private String descripcion;

    private Integer edadMaxima;

    public Categoria(){}

    public Categoria(@NotNull  String nombre, @NotNull String descripcion, @NotNull Integer edadMaxima){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.edadMaxima = edadMaxima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }
}
