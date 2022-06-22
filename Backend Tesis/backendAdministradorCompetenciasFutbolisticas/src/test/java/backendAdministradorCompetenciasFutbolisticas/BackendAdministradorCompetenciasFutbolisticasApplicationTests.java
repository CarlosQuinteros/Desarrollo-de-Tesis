package backendAdministradorCompetenciasFutbolisticas;

import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.LogAccion;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezRolService;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Test
	public void historialEntre2Clubes(){
		Long clubL = new Long(3);
		Long clubV = new Long(5);
		List<Partido> historial = partidoService.historialEntreClubes(clubL,clubV);
		historial.forEach(p-> System.out.println(p.getClubLocal().getNombreClub() + " [vs] " + p.getClubVisitante().getNombreClub()));
		Assertions.assertTrue(!historial.isEmpty());
	}


}
