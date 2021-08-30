package backendAdministradorCompetenciasFutbolisticas.Security.Dto;


public class JwtDto {
    private String token;
    /* corrigiendo errores de vulnerabilidad, ahora solo enviamos el token
        el front deberá recuperar el username y roles mediante el token

    private String bearer = "Bearer";
    private String nombreUsuario;
    private Collection<? extends GrantedAuthority> authorities; */

    public JwtDto(){

    }
    public JwtDto(String token) {
        this.token = token;
        /*this.nombreUsuario = nombreUsuario;
        this.authorities = authorities; */
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
