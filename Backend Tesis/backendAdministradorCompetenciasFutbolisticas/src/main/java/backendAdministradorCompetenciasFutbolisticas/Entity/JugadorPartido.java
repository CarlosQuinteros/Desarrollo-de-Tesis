package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.PosicionJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class JugadorPartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Partido partido;

    @ManyToOne
    private Club club;

    @ManyToOne
    private Jugador jugador;

    @Enumerated(EnumType.STRING)
    private TipoRolJugador rol;

    private Integer nroCamiseta;

    @Enumerated(EnumType.STRING)
    private PosicionJugador posicion;

    @Transient
    private Integer edadEnPartido;


    public JugadorPartido(){

    }

    public JugadorPartido(@NotNull Partido partido, @NotNull Club club, @NotNull Jugador jugador, @NotNull TipoRolJugador rol, @NotNull Integer nroCamiseta, @NotNull PosicionJugador posicion) {
        this.partido = partido;
        this.club = club;
        this.jugador = jugador;
        this.rol = rol;
        this.nroCamiseta = nroCamiseta;
        this.posicion = posicion;
    }

    public Long getId() {
        return id;
    }

    public Partido getPartido() {
        return partido;
    }

    public Club getClub() {
        return club;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public TipoRolJugador getRol() {
        return rol;
    }

    public Integer getNroCamiseta() {
        return nroCamiseta;
    }

    public PosicionJugador getPosicion() {
        return posicion;
    }

    public Integer getEdadEnPartido(){
        return this.jugador.getEdadEnFecha(this.partido.getFecha().toLocalDate());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setRol(TipoRolJugador rol) {
        this.rol = rol;
    }

    public void setNroCamiseta(Integer nroCamiseta) {
        this.nroCamiseta = nroCamiseta;
    }

    public void setPosicion(PosicionJugador posicion) {
        this.posicion = posicion;
    }

    public void setEdadEnPartido(Integer edadEnPartido) {
        this.edadEnPartido = edadEnPartido;
    }
}
