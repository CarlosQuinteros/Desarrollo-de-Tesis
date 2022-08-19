package backendAdministradorCompetenciasFutbolisticas.Dtos;

import java.time.LocalDateTime;
import java.util.List;

public class PartidoAntoacionJugadorDto {
    private Long idPartido;

    private LocalDateTime fecha;

    private String estado;

    private String clubLocal;

    private String clubVisitante;

    private Integer cantidadGolesClubLocal;

    private Integer cantidadGolesClubVisitante;

    private String competencia;

    private String jornada;

    private String categoria;

    private String rol;

    private String jugoPara;

    private String ingreso;

    private Integer goles;

    private List<String> tipoGoles;

    public PartidoAntoacionJugadorDto(){}

    public PartidoAntoacionJugadorDto(Long idPartido, LocalDateTime fecha, String estado, String clubLocal, String clubVisitante, Integer cantidadGolesClubLocal, Integer cantidadGolesClubVisitante, String competencia, String jornada, String categoria, String rol, String jugoPara, String ingreso, Integer goles, List<String> tipoGoles) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.estado = estado;
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.cantidadGolesClubLocal = cantidadGolesClubLocal;
        this.cantidadGolesClubVisitante = cantidadGolesClubVisitante;
        this.competencia = competencia;
        this.jornada = jornada;
        this.categoria = categoria;
        this.rol = rol;
        this.jugoPara = jugoPara;
        this.ingreso = ingreso;
        this.goles = goles;
        this.tipoGoles = tipoGoles;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getJugoPara() {
        return jugoPara;
    }

    public void setJugoPara(String jugoPara) {
        this.jugoPara = jugoPara;
    }

    public Integer getGoles() {
        return goles;
    }

    public void setGoles(Integer goles) {
        this.goles = goles;
    }

    public List<String> getTipoGoles() {
        return tipoGoles;
    }

    public void setTipoGoles(List<String> tipoGoles) {
        this.tipoGoles = tipoGoles;
    }
}
