package backendAdministradorCompetenciasFutbolisticas.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ResponseBody
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }
}
