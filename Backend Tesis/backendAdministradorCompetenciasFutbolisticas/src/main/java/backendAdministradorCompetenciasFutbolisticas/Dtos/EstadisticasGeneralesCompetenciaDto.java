package backendAdministradorCompetenciasFutbolisticas.Dtos;

public class EstadisticasGeneralesCompetenciaDto {
    private Integer cantidadGolesLocales;
    private Integer cantidadGolesVisitantes;
    private Double mediaGoles;
    private Integer victoriasLocales;
    private Integer victoriasVisitantes;
    private Integer empates;

    public EstadisticasGeneralesCompetenciaDto(){
    }

    public Integer getCantidadGolesLocales() {
        return cantidadGolesLocales;
    }

    public void setCantidadGolesLocales(Integer cantidadGolesLocales) {
        this.cantidadGolesLocales = cantidadGolesLocales;
    }

    public Integer getCantidadGolesVisitantes() {
        return cantidadGolesVisitantes;
    }

    public void setCantidadGolesVisitantes(Integer cantidadGolesVisitantes) {
        this.cantidadGolesVisitantes = cantidadGolesVisitantes;
    }

    public Double getMediaGoles() {
        return mediaGoles;
    }

    public void setMediaGoles(Double mediaGoles) {
        this.mediaGoles = mediaGoles;
    }

    public Integer getVictoriasLocales() {
        return victoriasLocales;
    }

    public void setVictoriasLocales(Integer victoriasLocales) {
        this.victoriasLocales = victoriasLocales;
    }

    public Integer getVictoriasVisitantes() {
        return victoriasVisitantes;
    }

    public void setVictoriasVisitantes(Integer victoriasVisitantes) {
        this.victoriasVisitantes = victoriasVisitantes;
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
    }
}
