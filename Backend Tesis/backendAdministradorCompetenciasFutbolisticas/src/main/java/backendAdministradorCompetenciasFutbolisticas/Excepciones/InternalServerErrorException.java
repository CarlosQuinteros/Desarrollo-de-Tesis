package backendAdministradorCompetenciasFutbolisticas.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ResponseBody
public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String mensaje){
        super(mensaje);
    }
}
