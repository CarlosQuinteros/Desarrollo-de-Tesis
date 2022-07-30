package backendAdministradorCompetenciasFutbolisticas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String apellidos;

    @NotNull
    @Column(unique = true)
    private String documento;

    @NotNull
    private LocalDate fechaNacimiento;

    @Transient
    private Integer edad;

    private Integer tarjetasAmarillasAcumuladas;

    @ManyToOne
    private EstadoJugador estadoJugador;

    /*
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "jugador", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("jugador")
    private List<JugadorClub> historialClubes = new ArrayList<JugadorClub>();
    */

    @ManyToOne
    private Club clubActual;

    public Jugador(){

    }

    public Jugador(@NotNull String nombre, @NotNull String apellidos, @NotNull String documento, @NotNull LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.tarjetasAmarillasAcumuladas = 0;
    }

    public Integer getEdadActual() {
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    public Integer getEdadEnFecha(LocalDate fecha){

        //System.out.println("cn dias chonos: " + ChronoUnit.DAYS.between(this.getFechaNacimiento(),fecha));
        //System.out.println("cn años con period: " +Period.between(this.fechaNacimiento, fecha).getYears());
        //System.out.println("cn años con chronos: " + ChronoUnit.YEARS.between(this.getFechaNacimiento(), fecha));
        //return (int) (long)ChronoUnit.YEARS.between(this.fechaNacimiento, fecha);
        return Period.between(this.fechaNacimiento, fecha).getYears();
    }

    public Club getClubActual(){
        return this.clubActual;
    }

    public void setClubActual(Club clubActual){
        this.clubActual = clubActual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }


    public String getFechaNacimientoParsed() {
        DateTimeFormatter  dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(fechaNacimiento.atStartOfDay(ZoneId.of("UTC-3")));

    }

    @JsonIgnore
    public LocalDate getFechaNacimiento(){
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getTarjetasAmarillasAcumuladas() {
        return tarjetasAmarillasAcumuladas;
    }

    public void setTarjetasAmarillasAcumuladas(Integer tarjetasAmarillasAcumuladas) {
        this.tarjetasAmarillasAcumuladas = tarjetasAmarillasAcumuladas;
    }

    public EstadoJugador getEstadoJugador() {
        return estadoJugador;
    }

    public void setEstadoJugador(EstadoJugador estadoJugador) {
        this.estadoJugador = estadoJugador;
    }

    @JsonIgnore
    public String nombreCompleto(){
        return apellidos + ", " + nombre;
    }
}
