package backendAdministradorCompetenciasFutbolisticas.Dtos;

import java.time.LocalDateTime;

public class DetalleGeneralPartidoDto {

    private Long idPartido;

    private LocalDateTime fecha;

    private String observaciones;

    private String estado;

    private String clubLocal;

    private String clubVisitante;

    private Integer cantidadGolesClubLocal;

    private Integer cantidadGolesClubVisitante;

    private String competencia;

    private String jornada;

    private String categoria;

    public DetalleGeneralPartidoDto(){}

    public DetalleGeneralPartidoDto(Long idPartido, LocalDateTime fecha, String observaciones, String estado, String clubLocal, String clubVisitante, Integer cantidadGolesClubLocal, Integer cantidadGolesClubVisitante, String competencia, String jornada, String categoria) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.estado = estado;
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.cantidadGolesClubLocal = cantidadGolesClubLocal;
        this.cantidadGolesClubVisitante = cantidadGolesClubVisitante;
        this.competencia = competencia;
        this.jornada = jornada;
        this.categoria = categoria;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClubLocal() {
        return clubLocal;
    }

    public void setClubLocal(String clubLocal) {
        this.clubLocal = clubLocal;
    }

    public String getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(String clubVisitante) {
        this.clubVisitante = clubVisitante;
    }

    public Integer getCantidadGolesClubLocal() {
        return cantidadGolesClubLocal;
    }

    public void setCantidadGolesClubLocal(Integer cantidadGolesClubLocal) {
        this.cantidadGolesClubLocal = cantidadGolesClubLocal;
    }

    public Integer getCantidadGolesClubVisitante() {
        return cantidadGolesClubVisitante;
    }

    public void setCantidadGolesClubVisitante(Integer cantidadGolesClubVisitante) {
        this.cantidadGolesClubVisitante = cantidadGolesClubVisitante;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
