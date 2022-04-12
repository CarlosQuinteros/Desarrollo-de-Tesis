package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditarPaseJugadorDto {

    @NotNull(message = "El campo jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "El campo club es obligatorio")
    private Long idClub;

    @NotNull(message = "El campo fecha desde es obligatorio")
    private Date fechaDesde;

    private Date fechaHasta;

    @NotBlank(message = "El campo motivo es obligatorio")
    private String motivo;

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

    public LocalDate getFechaDesde() {
        return fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return this.fechaHasta != null ?  fechaHasta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
