package backendAdministradorCompetenciasFutbolisticas.Dtos;


import javax.validation.constraints.NotNull;
import java.util.Date;

public class PartidoDto {

    private Date fecha;
    private String observaciones;

    @NotNull(message = "El club local no puede estar vacio")
    private Long idClubLocal;

    @NotNull(message = "El club visitante no puede estar vacio")
    private Long idClubVisitante;

    public Date getFecha() {
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

    public void setFecha(Date fecha) {
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
