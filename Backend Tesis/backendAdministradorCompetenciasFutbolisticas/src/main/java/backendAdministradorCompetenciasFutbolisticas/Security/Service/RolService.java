package backendAdministradorCompetenciasFutbolisticas.Security.Service;


import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getRolByNombre(RolNombre nombreRol){
        return rolRepository.findByRolNombre(nombreRol);
    }

    public List<Rol> list(){
        return rolRepository.findAll();
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }

}
