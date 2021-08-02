package backendAdministradorCompetenciasFutbolisticas.Dtos;

public class Mensaje {
    private String mensaje;

    public Mensaje(String  pMensaje){
        this.mensaje = pMensaje;
    }

    public void setMensaje(String pMensaje){
        this.mensaje = pMensaje;
    }

    public String getMensaje(){
        return  this.mensaje;
    }


}
