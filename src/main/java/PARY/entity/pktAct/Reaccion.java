package PARY.entity.pktAct;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "datosDactividad")
public class DatosAct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long idPerfil;
    private String textComent;
    private boolean valMeEncanta;
    private boolean valAddMisActividades;

    public DatosAct() {
    }

    public DatosAct(Long id, Long idPerfil, String textComent,
                    boolean valMeEncanta, boolean valAddMisActividades) {
        this.id = id;
        this.idPerfil = idPerfil;
        this.textComent = textComent;
        this.valMeEncanta = valMeEncanta;
        this.valAddMisActividades = valAddMisActividades;
    }

    public DatosAct(Long idPerfil, String textComent) {
        this.idPerfil = idPerfil;
        this.textComent = textComent;
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

    public String getTextComent() {
        return textComent;
    }

    public void setTextComent(String textComent) {
        this.textComent = textComent;
    }

    public boolean getValMeEncanta() {
        return valMeEncanta;
    }

    public void setValMeEncanta(boolean valMeEncanta) {
        this.valMeEncanta = valMeEncanta;
    }

    public boolean getValAddMisActividades() {
        return valAddMisActividades;
    }

    public void setValAddMisActividades(boolean valAddMisActividades) {
        this.valAddMisActividades = valAddMisActividades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosAct datosAct = (DatosAct) o;
        return id.equals(datosAct.id) && idPerfil.equals(datosAct.idPerfil) && Objects.equals(textComent, datosAct.textComent) && Objects.equals(valMeEncanta, datosAct.valMeEncanta) && Objects.equals(valAddMisActividades, datosAct.valAddMisActividades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPerfil, textComent, valMeEncanta, valAddMisActividades);
    }
}
