package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.LogAccion;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date fecha;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("roles")
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LogAccion logAccion;

    @NotNull
    private String descripcion;

    @NotNull
    private Long objeto_id;

    public Log(){

    }

    public Log(@NotNull Usuario usuario, @NotNull LogAccion logAccion, @NotNull String descripcion, @NotNull Long objeto_id) {
        this.fecha = null;
        this.usuario = usuario;
        this.logAccion = logAccion;
        this.descripcion = descripcion;
        this.objeto_id = objeto_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LogAccion getLogAccion() {
        return logAccion;
    }

    public void setLogAccion(LogAccion logAccion) {
        this.logAccion = logAccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(Long objeto_id) {
        this.objeto_id = objeto_id;
    }
}
