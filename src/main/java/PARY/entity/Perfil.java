package PARY.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "perfiles")
@PrimaryKeyJoinColumn(name = "registro_id")
public class Perfil extends Autenticacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 10)
    private String nombre;
    @Column(nullable = false, length = 30)
    private String apellidos;
    @Column(nullable = false, length = 11, unique = true)
    private String CI;
    //private Imagen imgPerfil;

    @ManyToMany(mappedBy = "perfil", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservacion> reservacion;

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<MisActividades> MisActividades;

    public Perfil() {
    }

    public Perfil(long id, String nombre, String apellidos, String CI,
                  List<Reservacion> reservacion, Set<PARY.entity.MisActividades> misActividades) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.CI = CI;
        this.reservacion = reservacion;
        MisActividades = misActividades;
    }

    public Perfil(long id, String usuario, String contrasenna, String numTel, String codigoToken,
                  long id1, String nombre, String apellidos, String CI, List<Reservacion> reservacion,
                  Set<PARY.entity.MisActividades> misActividades) {
        super(id, usuario, contrasenna, numTel, codigoToken);
        this.id = id1;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.CI = CI;
        this.reservacion = reservacion;
        MisActividades = misActividades;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCI() {
        return CI;
    }

    public void setCI(String CI) {
        this.CI = CI;
    }

    public List<Reservacion> getReservacion() {
        return reservacion;
    }

    public void setReservacion(List<Reservacion> reservacion) {
        this.reservacion = reservacion;
    }

    public Set<PARY.entity.MisActividades> getMisActividades() {
        return MisActividades;
    }

    public void setMisActividades(Set<PARY.entity.MisActividades> misActividades) {
        MisActividades = misActividades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Perfil perfil = (Perfil) o;
        return id == perfil.id && nombre.equals(perfil.nombre) && Objects.equals(apellidos, perfil.apellidos) && CI.equals(perfil.CI) && Objects.equals(reservacion, perfil.reservacion) && Objects.equals(MisActividades, perfil.MisActividades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, nombre, apellidos, CI, reservacion, MisActividades);
    }
}
