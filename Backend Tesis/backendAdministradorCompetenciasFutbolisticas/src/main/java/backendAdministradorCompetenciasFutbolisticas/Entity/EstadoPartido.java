package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;

import javax.validation.constraints.NotNull;

public class EstadoPartido {
    private Long id;
    private NombreEstadoPartido estadoPartido;

    public EstadoPartido(){

    }
    public EstadoPartido(@NotNull NombreEstadoPartido estadoPartido) {
        this.estadoPartido = estadoPartido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NombreEstadoPartido getEstadoPartido() {
        return estadoPartido;
    }

    public void setEstadoPartido(NombreEstadoPartido estadoPartido) {
        this.estadoPartido = estadoPartido;
    }
}
