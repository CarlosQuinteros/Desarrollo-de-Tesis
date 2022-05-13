package backendAdministradorCompetenciasFutbolisticas.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class AsociacionDeportiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String nombre;

    @NotNull
    private String alias;

    @NotNull
    @Column(unique = true)
    private String email;

    public AsociacionDeportiva(){

    }

    public AsociacionDeportiva(@NotNull String nombre, @NotNull String alias, @NotNull String email) {
        this.nombre = nombre;
        this.alias = alias;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
