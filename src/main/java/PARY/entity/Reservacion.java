package PARY.entity;

import PARY.entity.constantes.Constant_Reservaciones;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

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

    @OneToOne(mappedBy = "reservacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MisActividades actividades;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_id", foreignKey = @ForeignKey(name = "FK_PERFIL_ID3"))
    private Perfil perfil;

    public Reservacion() {
    }

    public Reservacion(long id, boolean aprobacion, Constant_Reservaciones estado,
                       MisActividades actividades, String fechaHora, Perfil perfil) {
        this.id = id;
        this.aprobacion = aprobacion;
        this.estado = estado;
        this.actividades = actividades;
        this.fechaHora = fechaHora;
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

    public MisActividades getActividades() {
        return actividades;
    }

    public void setActividades(MisActividades actividades) {
        this.actividades = actividades;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
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
