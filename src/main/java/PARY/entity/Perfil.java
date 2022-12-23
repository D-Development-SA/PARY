package PARY.entity;

import jakarta.persistence.*;

import java.io.Serializable;
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

    @OneToOne(mappedBy = "perfil", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private RegistroAccion RegAccion;

    @OneToOne(mappedBy = "perfil", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Reservacion reservacion;

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<MisActividades> MisActividades;

    public Perfil() {
    }

    public Perfil(long id, String nombre, String apellidos, String CI,
                  RegistroAccion regAccion, Reservacion reservacion, Set<MisActividades> MisActividades) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.CI = CI;
        RegAccion = regAccion;
        this.reservacion = reservacion;
        this.MisActividades = MisActividades;
    }

    public Perfil(long id, String usuario, String contrasenna, String numTel, String codigoToken,
                  long id1, String nombre, String apellidos, String CI, RegistroAccion regAccion,
                  Reservacion reservacion, Set<MisActividades> MisActividades) {
        super(id, usuario, contrasenna, numTel, codigoToken);
        this.id = id1;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.CI = CI;
        RegAccion = regAccion;
        this.reservacion = reservacion;
        this.MisActividades = MisActividades;
    }

    public long getId() {
        return id;
    }

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

    public RegistroAccion getRegAccion() {
        return RegAccion;
    }

    public void setRegAccion(RegistroAccion regAccion) {
        RegAccion = regAccion;
    }

    public Reservacion getReservacion() {
        return reservacion;
    }

    public void setReservacion(Reservacion reservacion) {
        this.reservacion = reservacion;
    }

    public Set<MisActividades> getActividad() {
        return MisActividades;
    }

    public void setActividad(Set<MisActividades> MisActividades) {
        this.MisActividades = MisActividades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return id == perfil.id && nombre.equals(perfil.nombre) && CI.equals(perfil.CI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, CI);
    }
}
