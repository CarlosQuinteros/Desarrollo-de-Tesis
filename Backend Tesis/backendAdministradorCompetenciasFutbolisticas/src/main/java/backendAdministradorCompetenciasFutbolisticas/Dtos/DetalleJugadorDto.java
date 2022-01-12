package backendAdministradorCompetenciasFutbolisticas.Dtos;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class DetalleJugadorDto {

    private Jugador jugador;

    @JsonIgnoreProperties("jugador")
    private List<Pase> historialClubes;

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public List<Pase> getHistorialClubes() {
        return historialClubes;
    }

    public void setHistorialClubes(List<Pase> historialClubes) {
        this.historialClubes = historialClubes;
    }
}
