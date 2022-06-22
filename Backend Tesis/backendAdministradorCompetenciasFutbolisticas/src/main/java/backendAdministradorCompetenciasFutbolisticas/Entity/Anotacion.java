package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;

import javax.persistence.ManyToOne;

public class Anotacion {

    private Long id;

    private Partido partido;

    private Jugador jugador;

    private Club clubAnota;

    private Integer minuto;

    private NombreTipoGol tipoGol;
}
