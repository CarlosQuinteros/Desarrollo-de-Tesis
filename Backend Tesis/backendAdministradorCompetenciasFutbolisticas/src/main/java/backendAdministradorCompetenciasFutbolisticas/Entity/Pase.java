package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
public class Pase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalDate fechaDesde;

    @Transient
    private Integer edadEnPase;

    private LocalDate fechaHasta;

    @NotNull
    @ManyToOne
    private Jugador jugador;

    @NotNull
    @ManyToOne
    private Club club;

    @NotNull
    private String motivo;


    public Pase(){

    }

    public Pase(@NotNull LocalDate fechaDesde, LocalDate fechaHasta, @NotNull Jugador jugador, @NotNull Club club, @NotNull String motivo) {
        this.fecha = LocalDate.now();
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.jugador = jugador;
        this.club = club;
        this.motivo = motivo;
        this.edadEnPase = Period.between(jugador.getFechaNacimiento(), fechaDesde).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    @JsonIgnore
    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @JsonIgnore
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

    public Integer getEdadEnPase() {
        return Period.between(this.getJugador().getFechaNacimiento(), this.fechaDesde).getYears();
    }

    public void setEdadEnPase(Integer edadEnPase) {
        this.edadEnPase = edadEnPase;
    }

    public String getFechaParsed( ){

        return DateTimeFormatter.RFC_1123_DATE_TIME.format(fecha.atStartOfDay(ZoneId.of("UTC-3")));
    };

    public String getFechaHastaParsed(){
        return fechaHasta!=null ? DateTimeFormatter.RFC_1123_DATE_TIME.format(fechaHasta.atStartOfDay(ZoneId.of("UTC-3"))) : null;
    };

    public String getFechaDesdeParsed(){
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(fechaDesde.atStartOfDay(ZoneId.of("UTC-3")));
    };


}
