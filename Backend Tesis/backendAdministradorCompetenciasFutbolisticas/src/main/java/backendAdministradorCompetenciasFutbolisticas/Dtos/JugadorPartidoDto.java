package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JugadorPartidoDto {

    @NotNull(message = "El partido no debe estar vacio")
    private Long idPartido;

    @NotNull(message = "El jugador no debe estar vacio")
    private Long idJugador;

    @NotNull(message = "El club no debe estar vacio")
    private Long idClub;

    @Min(value = 1, message = "El minimo nro de camiseta es 1")
    private Integer nroCamiseta;

    @NotBlank(message = "La posicion no debe estar vacia")
    private String posicion;

    public Long getIdPartido() {
        return idPartido;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public Long getIdClub() {
        return idClub;
    }

    public Integer getNroCamiseta() {
        return nroCamiseta;
    }

    public String getPosicion() {
        return posicion;
    }
}
