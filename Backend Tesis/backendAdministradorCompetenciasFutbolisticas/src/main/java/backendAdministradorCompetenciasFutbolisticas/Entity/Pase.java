package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Pase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalDate fechaDesde;

    private Integer edadEnPase;

    private LocalDate fechaHasta;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Jugador jugador;

    @NotNull
    @ManyToOne
    private Club club;

    @NotNull
    private String motivo;


    @ManyToOne
    @JsonIgnoreProperties("roles")
    private Usuario usuario;

    public Pase(){

    }

    public Pase(@NotNull LocalDate fechaDesde, LocalDate fechaHasta, @NotNull Jugador jugador, @NotNull Club club, @NotNull String motivo, Usuario usuario) {
        this.fecha = LocalDate.now();
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.jugador = jugador;
        this.club = club;
        this.motivo = motivo;
        this.usuario = usuario;
        this.edadEnPase = Period.between(jugador.getFechaNacimiento(), fechaDesde).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getEdadEnPase() {
        return edadEnPase;
    }
}
