package backendAdministradorCompetenciasFutbolisticas.Dtos;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jornada;

import java.util.List;

public class JornadaPartidosDto {
    private Jornada jornada;

    private List<DetalleGeneralPartidoDto> partidos;

    public JornadaPartidosDto(){}

    public JornadaPartidosDto(Jornada jornada, List<DetalleGeneralPartidoDto> partidos) {
        this.jornada = jornada;
        this.partidos = partidos;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public List<DetalleGeneralPartidoDto> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<DetalleGeneralPartidoDto> partidos) {
        this.partidos = partidos;
    }
}
