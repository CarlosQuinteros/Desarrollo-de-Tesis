package backendAdministradorCompetenciasFutbolisticas.Dtos;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

public class PartidoDto {

    //@JsonFormat("")
    private LocalDateTime fecha;
    private String observaciones;

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
}
