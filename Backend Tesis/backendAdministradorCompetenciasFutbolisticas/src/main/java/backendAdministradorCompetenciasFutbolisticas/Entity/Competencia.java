package backendAdministradorCompetenciasFutbolisticas.Entity;

import java.util.List;

public class Competencia {

    private Long id;

    private AsociacionDeportiva asociacionDeportiva;

    private Categoria categoria;

    private String genero;

    private String nombre;

    private String temporada;

    private String descripcion;

    private Integer tarjetasAmarillasPermitidas;

    private List<Club> clubesParticipantes;

    //private list de jornadas. Las jornadas trendan el list de partidos



}
