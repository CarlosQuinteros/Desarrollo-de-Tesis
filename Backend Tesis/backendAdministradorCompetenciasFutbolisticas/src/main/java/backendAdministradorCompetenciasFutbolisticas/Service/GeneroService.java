package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Enums.TipoGenero;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroService {

    public List<TipoGenero> getListadoTipoGenero(){
        return Arrays.stream(TipoGenero.values()).collect(Collectors.toList());
    }

    public List<String> getListadoStringTipoGenero(){
        return getListadoTipoGenero()
                .stream()
                .map(TipoGenero::name)
                .collect(Collectors.toList());
    }

    public TipoGenero getTipoGeneroPorNombre(String nombre){
        return getListadoTipoGenero()
                .stream()
                .filter(genero -> genero.name().equals(nombre.toUpperCase()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("No existe el genero: " + nombre));
    }
}
