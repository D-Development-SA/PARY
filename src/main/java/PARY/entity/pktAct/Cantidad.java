package PARY.entity.pktAct;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cantidades")
public class Cantidad implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private int cantReserv;
    private int cantComent;
    private int cantMeEncanta;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Reaccion> reaccions;

    public Cantidad() {
    }

    public Cantidad(int cantReserv, int cantComent, int cantMeEncanta) {
        this.cantReserv = cantReserv;
        this.cantComent = cantComent;
        this.cantMeEncanta = cantMeEncanta;
    }

    public Cantidad(Long id, int cantReserv, int cantComent, int cantMeEncanta, List<Reaccion> reaccions) {
        this.id = id;
        this.cantReserv = cantReserv;
        this.cantComent = cantComent;
        this.cantMeEncanta = cantMeEncanta;
        this.reaccions = reaccions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantReserv() {
        return cantReserv;
    }

    public void setCantReserv(int cantReserv) {
        this.cantReserv = cantReserv;
    }

    public int getCantComent() {
        return cantComent;
    }

    public void setCantComent(int cantComent) {
        this.cantComent = cantComent;
    }

    public int getCantMeEncanta() {
        return cantMeEncanta;
    }

    public void setCantMeEncanta(int cantMeEncanta) {
        this.cantMeEncanta = cantMeEncanta;
    }

    public List<Reaccion> getReaccions() {
        return reaccions;
    }

    public void setReaccions(List<Reaccion> reaccions) {
        this.reaccions = reaccions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cantidad cantidad = (Cantidad) o;
        return cantReserv == cantidad.cantReserv && cantComent == cantidad.cantComent && cantMeEncanta == cantidad.cantMeEncanta && id.equals(cantidad.id) && Objects.equals(reaccions, cantidad.reaccions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantReserv, cantComent, cantMeEncanta, reaccions);
    }
}
