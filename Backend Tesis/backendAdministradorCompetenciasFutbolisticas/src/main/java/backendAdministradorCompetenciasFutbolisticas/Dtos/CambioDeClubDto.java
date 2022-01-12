package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

public class CambioDeClubDto {

    @NotNull(message = "El campo fecha es obligatorio")
    private Date fecha;

    @NotNull(message = "El jugador es obligatorio")
    private Long idJugador;

    @NotNull (message = "El club es obligatorio")
    private Long idClub;

    @NotBlank (message = "El motivo es obligatorio")
    private String motivo;


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public Long getIdClub() {
        return idClub;
    }

    public void setIdClub(Long idClub) {
        this.idClub = idClub;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
