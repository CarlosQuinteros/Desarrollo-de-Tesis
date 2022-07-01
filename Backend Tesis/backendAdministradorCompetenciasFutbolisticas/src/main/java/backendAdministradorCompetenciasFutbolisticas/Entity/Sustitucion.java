package backendAdministradorCompetenciasFutbolisticas.Entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Sustitucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Partido partido;

    @NotNull
    @ManyToOne
    private Club clubSustituye;

    @NotNull
    private Integer minuto;

    @NotNull
    @ManyToOne
    private Jugador jugadorSale;

    @NotNull
    @ManyToOne
    private Jugador jugadorEntra;

    public  Sustitucion(){}

    public Sustitucion(@NotNull Partido partido, @NotNull Club clubSustituye, @NotNull Integer minuto, @NotNull Jugador jugadorSale, @NotNull Jugador jugadorEntra) {
        this.partido = partido;
        this.clubSustituye = clubSustituye;
        this.minuto = minuto;
        this.jugadorSale = jugadorSale;
        this.jugadorEntra = jugadorEntra;
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

    public Club getClubSustituye() {
        return clubSustituye;
    }

    public void setClubSustituye(Club clubSustituye) {
        this.clubSustituye = clubSustituye;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public Jugador getJugadorSale() {
        return jugadorSale;
    }

    public void setJugadorSale(Jugador jugadorSale) {
        this.jugadorSale = jugadorSale;
    }

    public Jugador getJugadorEntra() {
        return jugadorEntra;
    }

    public void setJugadorEntra(Jugador jugadorEntra) {
        this.jugadorEntra = jugadorEntra;
    }
}
