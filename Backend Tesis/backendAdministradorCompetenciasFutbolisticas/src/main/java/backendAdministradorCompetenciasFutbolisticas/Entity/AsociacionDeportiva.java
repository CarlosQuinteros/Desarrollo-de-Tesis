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

    public AsociacionDeportiva(){

    }

    public AsociacionDeportiva(@NotNull String nombre) {

        this.nombre = nombre;
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
}
