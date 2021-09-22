package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;

import javax.validation.constraints.NotNull;

public class EstadoJugador {

    private Long id;

    private NombreEstadoJugador nombreEstado;

    public EstadoJugador(){

    }

    public EstadoJugador(@NotNull NombreEstadoJugador nombreEstadoJugador){
        this.nombreEstado = nombreEstadoJugador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NombreEstadoJugador getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(NombreEstadoJugador nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
