package PARY.entity.pktAct;

import jakarta.persistence.*;

@Entity
@Table(name = "cantidades")
public class Cantidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private int cantReserv;
    private int cantComent;
    private int cantMeEncanta;

    public Cantidad() {
    }

    public Cantidad(Long id, int cantReserv, int cantComent, int cantMeEncanta) {
        this.id = id;
        this.cantReserv = cantReserv;
        this.cantComent = cantComent;
        this.cantMeEncanta = cantMeEncanta;
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
}
