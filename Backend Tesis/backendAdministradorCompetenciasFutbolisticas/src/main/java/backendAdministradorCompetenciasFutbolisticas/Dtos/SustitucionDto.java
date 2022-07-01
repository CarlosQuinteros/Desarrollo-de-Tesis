package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SustitucionDto {

    @NotNull(message = "El partido no puede estar vacio")
    @Min(value = 1,message = "El idPartido debe ser positivo")
    private Long idPartido;

    @NotNull(message = "El club que sustituye no puede estar vacio")
    @Min(value = 1,message = "El idClubSustituye debe ser positivo")
    private Long idClubSustituye;

    @NotNull(message = "El jugador que sale no puede estar vacio")
    @Min(value = 1,message = "El idJugadorSale debe ser positivo")
    private Long idJugadorSale;

    @NotNull(message = "El jugador que entra no puede estar vacio")
    @Min(value = 1,message = "El idJugadorEntra debe ser positivo")
    private Long idJugadorEntra;

    @NotNull(message = "El minuto de sustitucion no puede estar vacio")
    @Min(value = 1,message = "El minuto de sustitucion minimo es 1'")
    @Max(value = 125, message = "El minuto de sustitucion maximo es 125'")
    private Integer minuto;

    public Long getIdPartido() {
        return idPartido;
    }

    public Long getIdClubSustituye() {
        return idClubSustituye;
    }

    public Long getIdJugadorSale() {
        return idJugadorSale;
    }

    public Long getIdJugadorEntra() {
        return idJugadorEntra;
    }

    public Integer getMinuto() {
        return minuto;
    }
}
