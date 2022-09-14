package backendAdministradorCompetenciasFutbolisticas.Dtos;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Entity.Categoria;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoGenero;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

public class DetalleCompetenciaDto {
    private Long id;

    private AsociacionDeportiva asociacionDeportiva;

    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private TipoGenero genero;

    private String nombre;

    private String temporada;

    private String descripcion;

    private Integer tarjetasAmarillasPermitidas;

    private List<Club> clubesParticipantes;

    @Enumerated(EnumType.STRING)
    private String estado;

    public DetalleCompetenciaDto(){}

    public DetalleCompetenciaDto(Long id, AsociacionDeportiva asociacionDeportiva, Categoria categoria, TipoGenero genero, String nombre, String temporada, String descripcion, Integer tarjetasAmarillasPermitidas, List<Club> clubesParticipantes, String estado) {
        this.id = id;
        this.asociacionDeportiva = asociacionDeportiva;
        this.categoria = categoria;
        this.genero = genero;
        this.nombre = nombre;
        this.temporada = temporada;
        this.descripcion = descripcion;
        this.tarjetasAmarillasPermitidas = tarjetasAmarillasPermitidas;
        this.clubesParticipantes = clubesParticipantes;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsociacionDeportiva getAsociacionDeportiva() {
        return asociacionDeportiva;
    }

    public void setAsociacionDeportiva(AsociacionDeportiva asociacionDeportiva) {
        this.asociacionDeportiva = asociacionDeportiva;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoGenero getGenero() {
        return genero;
    }

    public void setGenero(TipoGenero genero) {
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTarjetasAmarillasPermitidas() {
        return tarjetasAmarillasPermitidas;
    }

    public void setTarjetasAmarillasPermitidas(Integer tarjetasAmarillasPermitidas) {
        this.tarjetasAmarillasPermitidas = tarjetasAmarillasPermitidas;
    }

    public List<Club> getClubesParticipantes() {
        return clubesParticipantes;
    }

    public void setClubesParticipantes(List<Club> clubesParticipantes) {
        this.clubesParticipantes = clubesParticipantes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
