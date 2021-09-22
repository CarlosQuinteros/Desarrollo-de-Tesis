package backendAdministradorCompetenciasFutbolisticas.Entity;

import java.time.LocalDate;
import java.time.Period;

public class Jugador {

    private Long id;

    private Integer numeroFicha;

    private String nombre;

    private String Apellidos;

    private String numeroCarnet;

    private String documento;

    private LocalDate fechaNacimiento;

    private Integer edad;

    private Integer tarjetasAmarillasAcumuladas;

    private Direccion direccion;

    private EstadoJugador estadoJugador;

    public Jugador(){

    }

    public Integer getEdad() {
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }
}
