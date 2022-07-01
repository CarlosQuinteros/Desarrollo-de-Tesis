package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.JuezRolDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Juez;
import backendAdministradorCompetenciasFutbolisticas.Entity.JuezRol;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Repository.JuezRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.JuezRolRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JuezRolService {

    @Autowired
    private JuezRolRepository juezRolRepository;

    @Autowired
    private  JuezService juezService;

    @Autowired
    private PartidoService partidoService;

    public List<NombreRolJuez> getListadoNombreRolJuez(){
        List<NombreRolJuez> roles = Arrays.stream(NombreRolJuez.values()).collect(Collectors.toList());
        return roles;
    }

    public List<String> getListadoStringRolJuez(){
        return  getListadoNombreRolJuez().stream().map(NombreRolJuez::name).collect(Collectors.toList());
    }

    public NombreRolJuez getNombreRolJuezPorNombre(String rol){
        return getListadoNombreRolJuez().stream()
                .filter( nombreRol -> nombreRol.name().equals(rol.toUpperCase()))
                .findFirst()
                .orElseThrow(()-> new BadRequestException("No existe el rol: " + rol));
    }

    public JuezRol getDetalleJuezRolPorId(Long id){
        JuezRol juezRol = juezRolRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("No existe la participacion del juez con ID: "+ id));
        return juezRol;
    }

    public JuezRol guardarJuezRol(JuezRol juezRol){
        return juezRolRepository.save(juezRol);
    }

    public JuezRol crearJuezRol(JuezRol juezRol){
        if(existeParticipacionDeJuezEnPartido(juezRol.getJuez().getId(), juezRol.getPartido().getId())){
            throw new BadRequestException("El juez solo puede tener un rol en un partido");
        }
        if(juezRol.getRol().equals(NombreRolJuez.ARBITRO_PRINCIPAL) && existeArbitroPrincipalEnPartido(juezRol.getPartido().getId())){
            throw new BadRequestException("Ya existe el arbitro principal");
        }
        if(juezRol.getRol().equals(NombreRolJuez.PRIMER_ASISTENTE) && existePrimerAsistenteEnPartido(juezRol.getPartido().getId())){
            throw new BadRequestException("Ya existe el primer asistente");
        }
        if(juezRol.getRol().equals(NombreRolJuez.SEGUNDO_ASISTENTE) && existeSegundoAsistenteEnPartido(juezRol.getPartido().getId())){
            throw new BadRequestException("Ya existe el segundo asistente");
        }
        return juezRolRepository.save(juezRol);
    }

    public void eliminarJuezRol(Long id){
        JuezRol juezRol = getDetalleJuezRolPorId(id);
        juezRolRepository.deleteById(juezRol.getId());
    }

    public List<JuezRol> getAllJuecesRoles(){
        List<JuezRol> listado = juezRolRepository.findAll();
        return listado;
    }

    public List<JuezRol> getParticipacionesPorIdPartido(Long idPartido){
        Partido partido = partidoService.getDetallePartido(idPartido);
        List<JuezRol> listado = juezRolRepository.findByPartido_Id(partido.getId());
        return listado;
    }

    public List<JuezRol> getParticipacionesPorIdJuez(Long idJuez){
        Juez juez = juezService.getJuezPorId(idJuez);
        List<JuezRol> listado = juezRolRepository.findByJuez_Id(juez.getId());
        return listado;
    }

    public List<JuezRol> getParticipacionesPorDocumentoJuezOLegajoJuez(String documentoOrLegajo){
        Juez juez = juezService.getJuezPorDocumentoOrLegajo(documentoOrLegajo);
        List<JuezRol> listado = juezRolRepository.findByJuez_DocumentoOrJuez_Legajo(documentoOrLegajo, documentoOrLegajo);
        return listado;
    }

    public boolean existeParticipacionDeJuezEnPartido(Long idJuez, Long idPartido){
        boolean result = juezRolRepository.existsByJuez_IdAndPartido_Id(idJuez,idPartido);
        return result;
    }

    public boolean existeArbitroPrincipalEnPartido(Long idPartido){
        boolean result = juezRolRepository.existsByPartido_IdAndRol(idPartido, NombreRolJuez.ARBITRO_PRINCIPAL);
        return result;
    }

    public boolean existePrimerAsistenteEnPartido(Long idPartido){
        boolean result = juezRolRepository.existsByPartido_IdAndRol(idPartido, NombreRolJuez.PRIMER_ASISTENTE);
        return result;
    }

    public boolean existeSegundoAsistenteEnPartido(Long idPartido){
        boolean result = juezRolRepository.existsByPartido_IdAndRol(idPartido, NombreRolJuez.SEGUNDO_ASISTENTE);
        return result;
    }

    public boolean existeParticipacionDeJuez(Long idJuez){
        boolean result = juezRolRepository.existsByJuez_Id(idJuez);
        return result;
    }
}
