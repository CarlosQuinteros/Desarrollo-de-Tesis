package backendAdministradorCompetenciasFutbolisticas.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombreClub;

    private Date fechaFundacion;

    @Column(unique = true)
    private String email;

    @NotNull
    @ManyToOne
    private Localidad localidad;

    @OneToOne(cascade = CascadeType.ALL)
    private Responsable responsableClub;

    @ManyToOne
    private AsociacionDeportiva asociacionDeportiva;

    public Club(){

    }

    public Club(@NotNull String nombreClub, Date fechaFundacion, String email, @NotNull Localidad localidad, Responsable responsableClub, AsociacionDeportiva asociacionDeportiva) {
        this.nombreClub = nombreClub;
        this.fechaFundacion = fechaFundacion;
        this.email = email;
        this.localidad = localidad;
        this.responsableClub = responsableClub;
        this.asociacionDeportiva = asociacionDeportiva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreClub() {
        return nombreClub;
    }

    public void setNombreClub(String nombreClub) {
        this.nombreClub = nombreClub;
    }

    public Date getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(Date fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public Responsable getResponsableClub() {
        return responsableClub;
    }

    public void setResponsableClub(Responsable responsableClub) {
        this.responsableClub = responsableClub;
    }

    public AsociacionDeportiva getAsociacionDeportiva() {
        return asociacionDeportiva;
    }

    public void setAsociacionDeportiva(AsociacionDeportiva asociacionDeportiva) {
        this.asociacionDeportiva = asociacionDeportiva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return
                Objects.equals(nombreClub, club.nombreClub) &&
                Objects.equals(fechaFundacion, club.fechaFundacion) &&
                Objects.equals(email, club.email) &&
                Objects.equals(localidad, club.localidad) &&
                Objects.equals(responsableClub, club.responsableClub) &&
                Objects.equals(asociacionDeportiva, club.asociacionDeportiva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreClub, fechaFundacion, email, localidad, responsableClub, asociacionDeportiva);
    }
}
