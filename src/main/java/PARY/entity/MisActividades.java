package PARY.entity;

import jakarta.persistence.*;

@Entity
public class MisActividades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actividades_id")
    private Actividad actividad;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
                fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    public MisActividades(Long id, Actividad actividad, Perfil perfil) {
        this.id = id;
        this.actividad = actividad;
        this.perfil = perfil;
    }

    public MisActividades() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actividad getActividades() {
        return actividad;
    }

    public void setActividades(Actividad actividad) {
        this.actividad = actividad;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
