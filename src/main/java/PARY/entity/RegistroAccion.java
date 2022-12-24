package PARY.entity;

import PARY.entity.constantes.Constant_RegAcciones;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "registroAcciones")
public class RegistroAccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idTipo;
    private String tipo;    //Tipo: Actividad o Reservacion
    private String nombreAct_Reserv;
    @Column(nullable = false, length = 13)
    private String accion;
    @Column(nullable = false)
    private String fechaHora;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_id", foreignKey = @ForeignKey(name = "FK_PERFIL_ID2"))
    private Perfil perfil;

    public RegistroAccion() {
    }

    public RegistroAccion(long id, long idTipo, String tipo, String nombreAct_Reserv, Constant_RegAcciones accion, String fechaHora, Perfil perfil) {
        this.id = id;
        this.idTipo = idTipo;
        this.tipo = tipo;
        this.nombreAct_Reserv = nombreAct_Reserv;
        this.accion = String.valueOf(accion);
        this.fechaHora = fechaHora;
        this.perfil = perfil;
    }

    public RegistroAccion(long idTipo, String tipo, Constant_RegAcciones accion, String nombreAct_Reserv) {
        this.idTipo = idTipo;
        this.tipo = tipo;
        this.accion = String.valueOf(accion);
        this.nombreAct_Reserv = nombreAct_Reserv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(long idTipo) {
        this.idTipo = idTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreAct_Reserv() {
        return nombreAct_Reserv;
    }

    public void setNombreAct_Reserv(String nombreAct_Reserv) {
        this.nombreAct_Reserv = nombreAct_Reserv;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(Constant_RegAcciones accion) {
        this.accion = String.valueOf(accion);
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @PrePersist
    public void antesPersistir(){
        DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' hh:mm");
        this.fechaHora = d.format(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroAccion that = (RegistroAccion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
