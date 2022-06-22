package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class JuezRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Juez juez;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NombreRolJuez rol;

    @NotNull
    @ManyToOne
    private Partido partido;

    public JuezRol(){}

    public JuezRol(@NotNull Juez juez, @NotNull NombreRolJuez rol, @NotNull Partido partido) {
        this.juez = juez;
        this.rol = rol;
        this.partido = partido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public NombreRolJuez getRol() {
        return rol;
    }

    public void setRol(NombreRolJuez rol) {
        this.rol = rol;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    @Override
    public String toString() {
        return "JuezRol{" +
                "juez=" + juez +
                ", rol=" + rol +
                '}';
    }
}
