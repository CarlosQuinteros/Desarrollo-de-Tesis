package backendAdministradorCompetenciasFutbolisticas.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ResponseBody
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(BindingResult result){
        super(result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(". "))
        );
    }
}
