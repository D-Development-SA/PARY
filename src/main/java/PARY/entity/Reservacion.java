package PARY.entity;

import PARY.entity.constantes.Constant_Reservaciones;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "reservaciones")
public class Reservacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean aprobacion;
    @Column(nullable = false)
    private String fechaHora;  //fecha y hora en la que se reservo
    @Enumerated(EnumType.STRING)
    private Constant_Reservaciones estado;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "actividades_id")
    @JsonIgnoreProperties({"cantidad", "reservacion"})
    private Actividad actividad;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "perfilReser", joinColumns = @JoinColumn(name = "reservacion_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private Set<Perfil> perfil;

    public Reservacion() {
    }

    public Reservacion(long id, boolean aprobacion, String fechaHora, Constant_Reservaciones estado,
                       Actividad actividad, Set<Perfil> perfil) {
        this.id = id;
        this.aprobacion = aprobacion;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.actividad = actividad;
        this.perfil = perfil;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(boolean aprobacion) {
        this.aprobacion = aprobacion;
    }

    public Constant_Reservaciones getEstado() {
        return estado;
    }

    public void setEstado(Constant_Reservaciones estado) {
        this.estado = estado;
    }


    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Set<Perfil> getPerfil() {
        return perfil;
    }

    public void setPerfil(Set<Perfil> perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservacion that = (Reservacion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
