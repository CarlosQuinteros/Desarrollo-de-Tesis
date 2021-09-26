package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class EstadoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NombreEstadoJugador estadoJugador;

    public EstadoJugador(){

    }

    public EstadoJugador(@NotNull NombreEstadoJugador estadoJugador){
        this.estadoJugador = estadoJugador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NombreEstadoJugador getNombreEstado() {
        return estadoJugador;
    }

    public void setNombreEstado(NombreEstadoJugador nombreEstado) {
        this.estadoJugador = nombreEstado;
    }
}
