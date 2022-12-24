package PARY.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "autenticaciones")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Autenticacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 10, unique = true)
    private String Usuario;
    @Column(nullable = false, length = 20, unique = true)
    private String Contrasenna;
    @Column(nullable = false, length = 10, unique = true)
    private String NumTel;
    @Column(nullable = false, length = 6, unique = true)
    private String codigoToken;

    public Autenticacion() {
    }

    public Autenticacion(long id, String usuario, String contrasenna, String numTel, String codigoToken) {
        this.id = id;
        Usuario = usuario;
        Contrasenna = contrasenna;
        NumTel = numTel;
        this.codigoToken = codigoToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContrasenna() {
        return Contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        Contrasenna = contrasenna;
    }

    public String getNumTel() {
        return NumTel;
    }

    public void setNumTel(String numTel) {
        NumTel = numTel;
    }

    public String getCodigoToken() {
        return codigoToken;
    }

    public void setCodigoToken(String codigoToken) {
        this.codigoToken = codigoToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autenticacion autenticacion = (Autenticacion) o;
        return id == autenticacion.id && Usuario.equals(autenticacion.Usuario) && NumTel.equals(autenticacion.NumTel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Usuario, NumTel);
    }
}
