package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnotacionDto {

    @NotNull(message = "El partido no debe estar vacio")
    @Min(value = 1, message = "El minimo valor de idPartido es 1")
    private Long idPartido;

    @NotNull(message = "El club que anota no debe estar vacio")
    @Min(value = 1, message = "El minimo valor de idClub es 1")
    private Long idClubAnota;

    @NotNull(message = "El jugador que anota no debe estar vacio")
    @Min(value = 1, message = "El minimo valor de idJugador es 1")
    private Long idJugadorAnota;

    @NotNull(message = "El minuto de anotacion no debe estar vacio")
    @Min(value = 1, message = "El minuto de anotacion minimo es 1'")
    @Max(value = 125, message = "El minuto de anotacion maximo es 125'")
    private Integer minuto;

    @NotBlank(message = "El tipo de gol no debe estar vacio")
    private String tipoGol;

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public Long getIdClubAnota() {
        return idClubAnota;
    }

    public void setIdClubAnota(Long idClubAnota) {
        this.idClubAnota = idClubAnota;
    }

    public Long getIdJugadorAnota() {
        return idJugadorAnota;
    }

    public void setIdJugadorAnota(Long idJugadorAnota) {
        this.idJugadorAnota = idJugadorAnota;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public String getTipoGol() {
        return tipoGol;
    }

    public void setTipoGol(String tipoGol) {
        this.tipoGol = tipoGol;
    }
}
