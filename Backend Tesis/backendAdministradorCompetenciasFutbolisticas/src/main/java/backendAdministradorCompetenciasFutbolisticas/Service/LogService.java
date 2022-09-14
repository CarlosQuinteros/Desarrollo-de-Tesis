package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.LogAccion;
import backendAdministradorCompetenciasFutbolisticas.Repository.LogRepository;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogService {

    @Autowired
    LogRepository logRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    public List<Log> getListado(){
        List<Log> listado = logRepository.findAllByOrderByFechaDesc();
        return listado;
    }

    public List<String> getListadoAcciones(){
        List<String> listado = Arrays.stream(LogAccion.values()).map(logAccion -> logAccion.name()).collect(Collectors.toList());
        return listado;
    }

    public List<Log> logsPorUsuario(Long id){
        List<Log> listaLogs = logRepository.findByUsuario_IdOrderByFechaAsc(id);
        return listaLogs;
    }

    public  List<Log> logsPorNombreDeUsuario(String username){
        List<Log> listaLogs = logRepository.findByUsuario_NombreUsuarioOrderByFechaDesc(username);
        return listaLogs;
    }

    public Log ultimoLogDeUsuario(Long id){
        List<Log> listaLogs = logsPorUsuario(id);
        int size = listaLogs.size();
        Log ulitmoLog = listaLogs.get(size - 1);
        return ulitmoLog;
    }

    public void eliminarActividadDeUsuario(Long id){
        List<Log> actividad = logsPorUsuario(id);
        logRepository.deleteAll(actividad);
    }

    // Metodos para log CLUBES

    public void guardarLogCreacionClub(Club club, Usuario usuario){
        Log log = new Log(usuario, LogAccion.CLUBES_CREACION, "Se creo el Club ID: " + club.getId(),club.getId());
        logRepository.save(log);
    }

    public void guardarLogActualizacionClub(Club club, Usuario usuario){
        Log log = new Log(usuario, LogAccion.CLUBES_MODIFICACION, "Se modifico el Club ID: " + club.getId(),club.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionClub(Long idClub, Usuario usuario){
        Log log = new Log(usuario, LogAccion.CLUBES_ELIMINACION, "Se elimino el Club ID: " + idClub, idClub);
        logRepository.save(log);
    }

    // Metodos para Logs de JUGADORES

    public void guardarLogCreacionJugador(Jugador jugador, Pase primerClub, Usuario usuario){
        Log log = new Log(usuario, LogAccion.JUGADORES_CREACION, "Se creo el Jugador ID: "+ jugador.getId(), jugador.getId());
        logRepository.save(log);
        guardarLogCreacionPase(primerClub, usuario);
    }

    public void guardarLogModificacionJugador(Jugador jugador, Usuario usuario){
        Log log = new Log(usuario,LogAccion.JUGADORES_MODIFICACION, "Se modifico el Jugador ID: "+ jugador.getId(),jugador.getId());
        logRepository.save(log);
    }

    //  Metodos para Logs de PASES (HISTORIAL DE CLUBES DE UN JUGADOR)

    public void guardarLogCreacionPase(Pase paseJugador, Usuario usuario){
        Log log = new Log(usuario, LogAccion.PASES_CREACION, "Se creo el Pase ID: " + paseJugador.getId(),paseJugador.getId());
        logRepository.save(log);
    }

    public void guardarLogEdicionPase(Pase paseJugador, Usuario usuario){
        Log log = new Log(usuario, LogAccion.PASES_MODIFICACION, "Se modifico el pase ID: "+ paseJugador.getId(), paseJugador.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionPase(Pase paseJugador, Usuario usuario){

        Log log = new Log(usuario, LogAccion.PASES_ELIMINACION, "Se elimino el Pase ID: " + paseJugador.getId() + " del jugador ID: " + paseJugador.getJugador().getId(), paseJugador.getId());
        logRepository.save(log);
    }

    // Metodos para Logs de USUARIOS

    public void guardarLogLoginUsuario(Usuario usuario){
        Log log = new Log(usuario, LogAccion.USUARIO_LOGIN, "Acceso al sistema", usuario.getId());
        logRepository.save(log);
    }

    public  void guardarLogCreacionUsuario(Usuario usuarioCreado, Usuario usuarioCreador){
        Log log = new Log(usuarioCreador, LogAccion.USUARIO_CREACION, "Se creo el Usuario ID: "+usuarioCreado.getId(), usuarioCreado.getId());
        logRepository.save(log);
    }

    public void guardarLogModificacionUsuario(Usuario usuario, Usuario usuarioEditado){
        Log log = new Log(usuario, LogAccion.USUARIO_MODIFICACION, "Se edito el Usuario ID: " + usuarioEditado.getId(), usuarioEditado.getId());
        logRepository.save(log);
    }

    public void guardarLogErrorLogin(Usuario usuario){
        Log log = new Log(usuario, LogAccion.USUARIO_ERROR_LOGIN, "El Usuario no logro acceder al sistema", usuario.getId());
        logRepository.save(log);
    }

    public void guardarLogErrorLoginDeUsuarioInactivo(Usuario usuario){
        Log log = new Log(usuario, LogAccion.USUARIO_ERROR_LOGIN_USUARIO_INACTIVO, "El Usuario intento acceder al sistema estando inactivo", usuario.getId());
        logRepository.save(log);
    }

    public void guardarLogBajaUsuarioLuegoDeNErrorLoginSeguidos(Usuario usuario){
        Log log = new Log(usuario, LogAccion.USUARIO_BAJA, "El Usuario fue dado de baja por realizar muchos intentos fallidos de inicio de sesión", usuario.getId());
        logRepository.save(log);
    }

    public List<Log> getUltimosNLogPorUsuario(Long idUsuario, Integer cantidadMaxima){
        List<Log> ultimosN = logRepository.findByUsuario_IdOrderByFechaDesc(idUsuario).stream().limit(cantidadMaxima).collect(Collectors.toList());
        return ultimosN;
    }

    public boolean cantidadLogUsuarioMayorOIgualAN(Usuario usuario, Integer n){
        Integer cantidad = logRepository.countByUsuario_Id(usuario.getId());
        return cantidad >= n;
    }

    public boolean ultimosNLogSonErrorLogin(Usuario usuario, Integer cantidadMax){
        boolean resultado = true;
        List<Log> ulitmos = getUltimosNLogPorUsuario(usuario.getId(), cantidadMax);
        //ulitmos.forEach( l -> System.out.println(l.getId()));
        for (Log log : ulitmos ) {
            resultado = resultado && log.getLogAccion().equals(LogAccion.USUARIO_ERROR_LOGIN);
            //System.out.println(resultado);
        }
        return resultado;
    }

    public  void guardarAltaUsuario(Usuario usuarioDadoDeAlta, Usuario usuarioEncargado){
        Log log = new Log(usuarioEncargado, LogAccion.USUARIO_ALTA, "El Usuario ID: " + usuarioEncargado.getId() + " dio de alta al usuario ID: " + usuarioDadoDeAlta.getId(),usuarioDadoDeAlta.getId());
        logRepository.save(log);
    }

    public  void guardarBajaUsuario(Usuario usuarioDadoDeBaja, Usuario usuarioEncargado){
        Log log = new Log(usuarioEncargado, LogAccion.USUARIO_BAJA, "El Usuario ID: " + usuarioEncargado.getId() + " dio de baja al usuario ID: "+ usuarioDadoDeBaja.getId(), usuarioDadoDeBaja.getId());
        logRepository.save(log);
    }

    //Metodos para logs de jueces

    public void guardarLogCreacionJuez(Juez juezCreado, Usuario usuario){
        Log log = new Log(usuario, LogAccion.JUEZ_CREACION, "Se creo el Juez ID: " + juezCreado.getId(),juezCreado.getId());
        logRepository.save(log);
    }

    public void guardarLogEdicionJuez(Juez juezEditado, Usuario usuario){
        Log log = new Log(usuario, LogAccion.JUEZ_MODIFICACION, "Se edito el Juez ID: " + juezEditado.getId(),juezEditado.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionJuez(Long idJuez, Usuario usuario){
        Log log = new Log(usuario, LogAccion.JUEZ_ELIMINACION, "Se elimino el Juez ID: " + idJuez, idJuez);
        logRepository.save(log);
    }

    //Metodos para logs de asociaciones deportivas
    public void guardarLogCreacionAsociacion(AsociacionDeportiva asociacionNueva, Usuario usuario){
        Log log = new Log(usuario, LogAccion.ASOCIACION_DEP_CREACION, "Se creo la Asociacion Dept. ID: " + asociacionNueva.getId(), asociacionNueva.getId());
        logRepository.save(log);
    }

    public void guardarLogEdicionAsociacion(AsociacionDeportiva asociacionEditada, Usuario usuario){
        Log log = new Log(usuario, LogAccion.ASOCIACION_DEP_MODIFICACION, "Se edito la Asociacion Dept. ID: " + asociacionEditada.getId(), asociacionEditada.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionAsociacion(Long idAsociacion, Usuario usuario){
        Log log = new Log(usuario, LogAccion.ASOCIACION_DEP_ELIMINACION, "Se elimino la Asociacion Dept. ID: " + idAsociacion, idAsociacion);
        logRepository.save(log);
    }

    //Metodos para logs de partidos
    public void guardarLogCreacionPartido(Partido nuevoPartido, Usuario usuario){
        Log log = new Log(usuario, LogAccion.PARTIDO_CREACION, "Se creo el Partido ID: " + nuevoPartido.getId(), nuevoPartido.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionPartido(Long idPartido, Usuario usuario){
        Log log = new Log(usuario, LogAccion.PARTIDO_ELIMINACION, "Se elimino el partido ID: " + idPartido, idPartido);
        logRepository.save(log);
    }

    public void guardarLogEdicionPartido(Long idPartido, Usuario usuario){
        Log log = new Log(usuario, LogAccion.PARTIDO_MODIFICACION, "Se edito el partido ID: " + idPartido, idPartido);
        logRepository.save(log);
    }

    //Metodos para logs de competencias
    public void guardarLogCreacionCompetencia(Competencia competencia, Usuario usuario){
        Log log = new Log(usuario, LogAccion.COMPETENCIA_CREACION, "Se creo la competencia ID: " + competencia.getId(), competencia.getId());
        logRepository.save(log);
    }

    public void guardarLogEdicionCompetencia(Competencia competenciaEditada, Usuario usuario){
        Log log = new Log(usuario, LogAccion.COMPETENCIA_MODIFICACION, "Se edito la competencia ID: " + competenciaEditada.getId(), competenciaEditada.getId());
        logRepository.save(log);
    }

    public void guardarLogEliminacionCompetencia(Long idCompetencia, Usuario usuario){
        Log log = new Log(usuario, LogAccion.COMPETENCIA_ELIMINACION, "Se elimino la competencia ID: " + idCompetencia, idCompetencia);
        logRepository.save(log);
    }
}
