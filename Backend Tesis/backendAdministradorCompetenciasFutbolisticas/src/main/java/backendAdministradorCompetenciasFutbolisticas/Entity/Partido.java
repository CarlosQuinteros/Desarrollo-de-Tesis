package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    private String observaciones;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NombreEstadoPartido estado;

    @NotNull
    @ManyToOne
    private Club clubLocal;

    @NotNull
    @ManyToOne
    private Club clubVisitante;

    //TODO: faltan jugadores, goles, amarillas, rojas, sustituciones

    public Partido(){}

    public Partido(@NotNull LocalDateTime fechaYHora, @NotNull String observaciones, @NotNull Club clubLocal, @NotNull Club clubVisitante ){
        this.fecha = fechaYHora;
        this.observaciones = observaciones;
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.estado = NombreEstadoPartido.PENDIENTE;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public NombreEstadoPartido getEstado() {
        return estado;
    }

    public Club getClubLocal() {
        return clubLocal;
    }

    public Club getClubVisitante() {
        return clubVisitante;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEstado(NombreEstadoPartido estado) {
        this.estado = estado;
    }

    public void setClubLocal(Club clubLocal) {
        this.clubLocal = clubLocal;
    }

    public void setClubVisitante(Club clubVisitante) {
        this.clubVisitante = clubVisitante;
    }

    public void cambiarEstadoAFinalizado(){
        this.estado = NombreEstadoPartido.FINALIZADO;
    }
}
