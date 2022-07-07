package backendAdministradorCompetenciasFutbolisticas.Dtos;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;

import javax.validation.constraints.*;
import java.util.List;

public class CompetenciaDto {

    @NotNull(message = "El idCategoria no puede estar vacio")
    private Long idCategoria;

    @NotNull(message = "El idAsociacionDeportiva no puede estar vacio")
    private Long idAsociacionDeportiva;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El genero no puede estar vacio")
    private String genero;

    @NotBlank(message = "La temporada no puede estar vacia")
    private String temporada;

    private String descripcion;

    @NotNull(message = "La cantidad de tarjetas amarillas permitidas no puede estar vacia")
    @Min(value = 5, message = "El minimo de tarjetas permitidas es 5")
    @Max(value = 8, message = "El maximo de tarjetas permitidas es 8")
    private Integer tarjetasAmarillasPermitidas;

    @NotEmpty(message = "El listado de clubes participantes no puede estar vacio")
    private List<@NotNull Club> clubesParticipantes;

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getTemporada() {
        return temporada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getTarjetasAmarillasPermitidas() {
        return tarjetasAmarillasPermitidas;
    }

    public List<Club> getClubesParticipantes() {
        return clubesParticipantes;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public Long getIdAsociacionDeportiva() {
        return idAsociacionDeportiva;
    }
}
