package PARY.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "perfiles")
@PrimaryKeyJoinColumn(name = "autenticacion_id")
public class Perfil extends Autenticacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 10)
    private String nombre;
    @Column(length = 30)
    private String apellidos;
    @Column(length = 11, unique = true)
    private String ci;
    //private Imagen imgPerfil;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Direccion direccion;

    @ManyToMany(mappedBy = "perfil", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("perfil")
    private Set<Reservacion> reservacion;

    @OneToMany(mappedBy = "perfil",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JsonIgnoreProperties({"perfil","idPerfil","cantReserv","cantComent","cantMeEncanta"})
    private Set<Actividad> MisActividades;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})
    private List<Notificacion> notificacion;

    public Perfil() {
    }

    public Perfil(long id, String nombre, String apellidos, String ci, Direccion direccion,
                  Set<Reservacion> reservacion, Set<Actividad> misActividades, List<Notificacion> notificacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ci = ci;
        this.direccion = direccion;
        this.reservacion = reservacion;
        MisActividades = misActividades;
        this.notificacion = notificacion;
    }

    public Perfil(long id, String usuario, String contrasenna, String numTel, String codigoToken,
                  long id1, String nombre, String apellidos, String ci, Direccion direccion, Set<Reservacion> reservacion,
                  Set<Actividad> misActividades, List<Notificacion> notificacion) {
        super(id, usuario, contrasenna, numTel, codigoToken);
        this.id = id1;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ci = ci;
        this.direccion = direccion;
        this.reservacion = reservacion;
        MisActividades = misActividades;
        this.notificacion = notificacion;
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

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Set<Reservacion> getReservacion() {
        return reservacion;
    }

    public void setReservacion(Set<Reservacion> reservacion) {
        this.reservacion = reservacion;
    }

    public Set<Actividad> getMisActividades() {
        return MisActividades;
    }

    public void setMisActividades(Set<Actividad> misActividades) {
        MisActividades = misActividades;
    }

    public List<Notificacion> getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(List<Notificacion> notificacion) {
        this.notificacion = notificacion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Perfil perfil = (Perfil) o;
        return id == perfil.id && nombre.equals(perfil.nombre) && Objects.equals(apellidos, perfil.apellidos) && ci.equals(perfil.ci) && Objects.equals(reservacion, perfil.reservacion) && Objects.equals(MisActividades, perfil.MisActividades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, nombre, apellidos, ci, reservacion, MisActividades);
    }
}
