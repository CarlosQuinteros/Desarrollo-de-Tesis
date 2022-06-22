package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoriaDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;

    @NotBlank(message = "El tipo de categoria no puede estar vacio")
    private String tipo;

    @NotNull(message = "La edad minima no puede ser vacia")
    @Min(value = 5,message = "La edad minima debe ser mayor o igual a 5")
    @Max(value = 65, message = "La edad minima debe ser menor a igual a 65")
    private Integer edadMinima;

    @NotNull(message = "La edad maxima no puede ser vacia")
    @Min(value = 5,message = "La edad maxima debe ser mayor o igual a 5")
    @Max(value = 65, message = "La edad maxima debe ser menor a igual a 65")
    private Integer edadMaxima;

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public String getTipo() {
        return tipo;
    }
}
