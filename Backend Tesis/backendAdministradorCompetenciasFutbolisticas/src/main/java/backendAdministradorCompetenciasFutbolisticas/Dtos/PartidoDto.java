package backendAdministradorCompetenciasFutbolisticas.Dtos;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

public class PartidoDto {

    //@JsonFormat("")
    private LocalDateTime fecha;

    private String observaciones;

    @NotNull(message = "El idJornada no puede estar vacio")
    @Min(value = 1, message = "El minimo valor de idJornada es 1")
    private Long idJornada;

    @NotNull(message = "El club local no puede estar vacio")
    private Long idClubLocal;

    @NotNull(message = "El club visitante no puede estar vacio")
    private Long idClubVisitante;

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Long getIdClubLocal() {
        return idClubLocal;
    }

    public Long getIdClubVisitante() {
        return idClubVisitante;
    }

    public Long getIdJornada() {
        return idJornada;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setIdClubLocal(Long idClubLocal) {
        this.idClubLocal = idClubLocal;
    }

    public void setIdClubVisitante(Long idClubVisitante) {
        this.idClubVisitante = idClubVisitante;
    }

    public void setIdJornada(Long idJornada) {
        this.idJornada = idJornada;
    }
}
