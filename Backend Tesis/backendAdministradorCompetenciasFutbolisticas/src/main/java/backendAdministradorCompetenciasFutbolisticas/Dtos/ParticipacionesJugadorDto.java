package backendAdministradorCompetenciasFutbolisticas.Dtos;

import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ParticipacionesJugadorDto {

    private DetalleGeneralPartidoDto ultimoPartido;
    private String haceTiempo;
    private List<JugadorPartido> participaciones;

    public ParticipacionesJugadorDto(){}

    public ParticipacionesJugadorDto(DetalleGeneralPartidoDto ultimoPartido, List<JugadorPartido> participaciones) {
        this.ultimoPartido = ultimoPartido;
        this.participaciones = participaciones;
    }

    public void setUltimoPartido(DetalleGeneralPartidoDto ultimoPartido) {
        this.ultimoPartido = ultimoPartido;
    }

    public void setParticipaciones(List<JugadorPartido> participaciones) {
        this.participaciones = participaciones;
    }

    public DetalleGeneralPartidoDto getUltimoPartido() {
        return ultimoPartido;
    }

    public String getHaceTiempo() {
        if(ultimoPartido == null || ultimoPartido.getFecha() == null) return "";
        Period periodo = Period.between(ultimoPartido.getFecha().toLocalDate(), LocalDate.now());
        if(periodo.getYears() == 0 && periodo.getMonths()  == 0 && periodo.getDays() > 0) return periodo.getDays() + " dias";
        if(periodo.getYears() == 0 && periodo.getMonths() > 0 && periodo.getDays() > 0)   return periodo.getMonths() + " meses y " + periodo.getDays() + " dias";
        if(periodo.getYears() == 0 && periodo.getMonths() > 0 && periodo.getDays() == 0)  return periodo.getMonths()+ " meses";
        if(periodo.getYears() > 0 && periodo.getMonths() == 0 && periodo.getDays() == 0)  return periodo.getYears() + " años";
        if(periodo.getYears() > 0 && periodo.getMonths() == 0 && periodo.getDays() > 0)   return periodo.getYears() + " años y " + periodo.getDays() + " dias";
        if(periodo.getYears() > 0 && periodo.getMonths() > 0 && periodo.getDays() == 0)   return periodo.getYears() + " años y " + periodo.getMonths()+ " meses";
        return periodo.getYears() + " años, " + periodo.getMonths() + " meses y " + periodo.getDays() + " dias";
    }

    public List<JugadorPartido> getParticipaciones() {
        return participaciones;
    }
}
