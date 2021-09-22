package backendAdministradorCompetenciasFutbolisticas.Entity;

import java.time.LocalDate;

public class Transferencia {

    private Long id;

    private LocalDate fechaTransferencia;

    private TipoTransferencia tipoTransferencia;

    private Jugador jugador;

    private Club clubOrigen;

    private Club clubDestino;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;


}
