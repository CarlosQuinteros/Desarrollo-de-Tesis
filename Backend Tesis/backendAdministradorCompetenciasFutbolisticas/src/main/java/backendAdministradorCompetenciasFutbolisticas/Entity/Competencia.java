package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.TipoGenero;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AsociacionDeportiva asociacionDeportiva;

    @ManyToOne
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private TipoGenero genero;

    @NotNull
    private String nombre;

    @NotNull
    private String temporada;

    private String descripcion;

    @NotNull
    private Integer tarjetasAmarillasPermitidas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clubes_participadores", joinColumns = @JoinColumn (name = "competencia_id"), inverseJoinColumns = @JoinColumn (name = "club_participa_id"))
    private List<Club> clubesParticipantes;

    public Competencia(){}

    public Competencia(AsociacionDeportiva asociacionDeportiva, Categoria categoria, TipoGenero genero, @NotNull String nombre, @NotNull String temporada, String descripcion, @NotNull Integer tarjetasAmarillasPermitidas) {
        this.asociacionDeportiva = asociacionDeportiva;
        this.categoria = categoria;
        this.genero = genero;
        this.nombre = nombre;
        this.temporada = temporada;
        this.descripcion = descripcion;
        this.tarjetasAmarillasPermitidas = tarjetasAmarillasPermitidas;
        this.clubesParticipantes = new ArrayList<>();
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
}
