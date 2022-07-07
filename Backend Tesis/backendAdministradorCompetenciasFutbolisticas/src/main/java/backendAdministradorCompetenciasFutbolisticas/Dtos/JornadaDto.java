package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class JornadaDto {

    @NotNull(message = "El numero de jornada no puede estar vacio")
    @Min(value = 1, message = "El minimo de jornada es 1")
    private Integer numero;

    private String descripcion;

    @NotNull(message = "El idCompetencia no puede estar vacio")
    @Min(value = 1, message = "El minimo valor de idCompetencia es 1")
    private Long idCompetencia;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(Long idCompetencia) {
        this.idCompetencia = idCompetencia;
    }
}
