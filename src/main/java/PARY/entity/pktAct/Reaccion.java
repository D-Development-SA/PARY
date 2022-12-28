package PARY.entity.pktAct;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reacciones")
public class Reaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long idPerfil;
    private String apellidoP;
    private String nombreP;
    private boolean valMeEncanta;
    private boolean valAddMisActividades;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    public Reaccion() {
    }

    public Reaccion(Long id, Long idPerfil, String apellidoP, String nombreP, boolean valMeEncanta, boolean valAddMisActividades, List<Comentario> comentarios) {
        this.id = id;
        this.idPerfil = idPerfil;
        this.apellidoP = apellidoP;
        this.nombreP = nombreP;
        this.valMeEncanta = valMeEncanta;
        this.valAddMisActividades = valAddMisActividades;
        this.comentarios = comentarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public boolean isValMeEncanta() {
        return valMeEncanta;
    }

    public void setValMeEncanta(boolean valMeEncanta) {
        this.valMeEncanta = valMeEncanta;
    }

    public boolean isValAddMisActividades() {
        return valAddMisActividades;
    }

    public void setValAddMisActividades(boolean valAddMisActividades) {
        this.valAddMisActividades = valAddMisActividades;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaccion reaccion = (Reaccion) o;
        return valMeEncanta == reaccion.valMeEncanta && valAddMisActividades == reaccion.valAddMisActividades && id.equals(reaccion.id) && idPerfil.equals(reaccion.idPerfil) && Objects.equals(apellidoP, reaccion.apellidoP) && nombreP.equals(reaccion.nombreP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPerfil, apellidoP, nombreP, valMeEncanta, valAddMisActividades);
    }
}
