package backendAdministradorCompetenciasFutbolisticas.Excepciones;

public class LocalidadNoExisteException extends Exception {
    public LocalidadNoExisteException(String mensaje){
        super(mensaje);
    }
}
