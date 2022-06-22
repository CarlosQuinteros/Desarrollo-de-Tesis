package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.PosicionJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;

public class JugadorPartido {

    private Long id;

    private Partido partido;

    private Club club;

    private Jugador jugador;

    private TipoRolJugador rol;

    private Integer nroCamiseta;

    private PosicionJugador posicion;


    public JugadorPartido(Partido partido, Club club, Jugador jugador, TipoRolJugador rol, Integer nroCamiseta, PosicionJugador posicion) {
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

    public Integer getNroCamiseta() {
        return nroCamiseta;
    }

    public PosicionJugador getPosicion() {
        return posicion;
    }
}
