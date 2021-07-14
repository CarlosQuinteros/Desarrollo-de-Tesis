package backendAdministradorCompetenciasFutbolisticas.Security.Jwt;

import com.sun.deploy.net.HttpResponse;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//comprueba si hay un token valida, de lo contrario devuelve una respuesta 401 no autorizado
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    private final static Logger logger  = LoggerFactory.getLogger(JwtEntryPoint.class);
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        logger.error("Faild en el metodo commence"); //esto solo se usa en desarrollo para ver cual es el metodo que tira error
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }
}
