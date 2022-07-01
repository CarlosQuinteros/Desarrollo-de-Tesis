package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Partido partido;

    @NotNull
    @ManyToOne
    private Jugador jugador;

    @NotNull
    @ManyToOne
    private Club clubAnota;

    @NotNull
    private Integer minuto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NombreTipoGol tipoGol;

    public Anotacion() {
    }

    public Anotacion(@NotNull Partido partido, @NotNull Jugador jugador, @NotNull Club clubAnota, @NotNull Integer minuto, @NotNull NombreTipoGol tipoGol) {
        this.partido = partido;
        this.jugador = jugador;
        this.clubAnota = clubAnota;
        this.minuto = minuto;
        this.tipoGol = tipoGol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Club getClubAnota() {
        return clubAnota;
    }

    public void setClubAnota(Club clubAnota) {
        this.clubAnota = clubAnota;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public NombreTipoGol getTipoGol() {
        return tipoGol;
    }

    public void setTipoGol(NombreTipoGol tipoGol) {
        this.tipoGol = tipoGol;
    }
}
