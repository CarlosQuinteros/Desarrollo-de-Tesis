package backendAdministradorCompetenciasFutbolisticas.Dtos;

public class Mensaje {
    private String mensaje;

    private Object datos;

    public Mensaje(String  pMensaje){
        this.mensaje = pMensaje;
    }

    public Mensaje(String pMensaje, Object objeto){
        this.mensaje = pMensaje;
        this.datos = objeto;
    }

    public void setMensaje(String pMensaje){
        this.mensaje = pMensaje;
    }

    public String getMensaje(){
        return  this.mensaje;
    }

    public Object getDatos(){
        return this.datos;
    }


}
