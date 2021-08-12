package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto;

public class EmailValuesDto {
    private String mailTo;
    private String userName;
    private String token;

    public EmailValuesDto(){

    }

    public EmailValuesDto(String mailTo, String userName, String token) {
        this.mailTo = mailTo;
        this.userName = userName;
        this.token = token;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
