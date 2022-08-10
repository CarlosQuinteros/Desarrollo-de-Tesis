package backendAdministradorCompetenciasFutbolisticas;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.ParticipacionesJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Posicion;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.LogAccion;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import backendAdministradorCompetenciasFutbolisticas.Service.*;
import javafx.geometry.Pos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class BackendAdministradorCompetenciasFutbolisticasApplicationTests {

	@Autowired
	private JuezService juezService;

	@Autowired
	private JuezRolService juezRolService;

	@Autowired
	private LogService logService;

	@Autowired
	private PartidoService partidoService;

	@Autowired
	private CompetenciaService competenciaService;

	@Test
	void contextLoads() {
	}

	@Test
	void ListaOrdenadaRoleJuezEnPartido(){
		juezService.getListadoNombreRolJuez().forEach(r -> System.out.println(r));
	}


	@Test
	public void pruebaGroupingStream(){
		List<Log> logs = logService.getListado();
		Map<LogAccion, List<Log>> logsByAccion = logs.stream()
				.collect(Collectors.groupingBy(Log::getLogAccion));
		logsByAccion.forEach((key,valor)-> System.out.println(key + ": " + valor.stream().count()));
	}

	/*@Test
	public void historialEntre2Clubes(){
		Long clubL = new Long(3);
		Long clubV = new Long(5);
		List<Partido> historial = partidoService.historialEntreClubes(clubL,clubV);
		historial.forEach(p-> System.out.println(p.getClubLocal().getNombreClub() + " [vs] " + p.getClubVisitante().getNombreClub()));
		Assertions.assertTrue(!historial.isEmpty());
	}*/

	@Test
	public void listadoUnicoDeClubesParticipantesEnCompetencia(){
		Long idCompetencia = Long.valueOf(3);
		Competencia competencia = competenciaService.getCompetencia(idCompetencia);
		List<DetalleGeneralPartidoDto> partidosPorCompetencia = partidoService.listadoDePartidosPorCompetencia(idCompetencia);
		Stream<String> clubesStream = Stream.concat(Stream.concat(partidosPorCompetencia.stream().map(DetalleGeneralPartidoDto::getClubLocal), partidosPorCompetencia.stream().map(DetalleGeneralPartidoDto::getClubVisitante)),competencia.getClubesParticipantes().stream().map(Club::getNombreClub));
		List<String> clubes = clubesStream.distinct().collect(Collectors.toList());
		List<Posicion> posicions = new ArrayList<>();
		Comparator<Posicion> comparadorMultiple = Comparator.comparing(Posicion::getPTS).reversed().thenComparing(Posicion::getDIF).reversed().thenComparing(Posicion::getV).reversed().thenComparing(Posicion::getClub);
		clubes.forEach(club -> {
			Posicion posicion = new Posicion(club);
		 	partidosPorCompetencia.stream().forEach(partido -> {
				if(partido.getClubLocal().equals(club)){
					if (partido.getCantidadGolesClubLocal() > partido.getCantidadGolesClubVisitante()) {
						posicion.sumarPuntosVictoria();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
						posicion.actualizarDiferencia();
					}
					if(partido.getCantidadGolesClubLocal() < partido.getCantidadGolesClubVisitante()){
						posicion.sumarPuntosDerrota();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
						posicion.actualizarDiferencia();
					}
					if (partido.getCantidadGolesClubLocal() == partido.getCantidadGolesClubVisitante()){
						posicion.sumarPuntosEmpate();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
						posicion.actualizarDiferencia();
					}
				}
				if(partido.getClubVisitante().equals(club)){
					if(partido.getCantidadGolesClubVisitante() > partido.getCantidadGolesClubLocal()){
						posicion.sumarPuntosVictoria();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
						posicion.actualizarDiferencia();
					}
					if(partido.getCantidadGolesClubVisitante() < partido.getCantidadGolesClubLocal()){
						posicion.sumarPuntosDerrota();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
						posicion.actualizarDiferencia();
					}
					if(partido.getCantidadGolesClubLocal() == partido.getCantidadGolesClubVisitante()){
						posicion.sumarPuntosEmpate();
						posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
						posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
						posicion.actualizarDiferencia();
					}
				}
			});
		 	posicions.add(posicion);
		});
		List<Posicion> tabla = posicions.stream().sorted(comparadorMultiple).collect(Collectors.toList());
		tabla.forEach(p -> System.out.println(p.toString()));

	}

	@Test
	public void timeAgoPartido(){
		LocalDateTime fecha = LocalDateTime.of(2020,8,7,15,0);
		ParticipacionesJugadorDto participaciones = new ParticipacionesJugadorDto();
		DetalleGeneralPartidoDto partido = new DetalleGeneralPartidoDto();
		partido.setFecha(fecha);
		participaciones.setUltimoPartido(null);
		System.out.println("hace " + participaciones.getHaceTiempo());

	}


}
