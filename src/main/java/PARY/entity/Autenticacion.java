package PARY.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "autenticaciones")
@Inheritance(strategy = InheritanceType.JOINED)
public
abstract class Autenticacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 10, unique = true)
    private String usuario;
    @Column(length = 20, unique = true)
    private String contrasenna;
    @Column(length = 10, unique = true)
    private String numTel;
    @Column(length = 6, unique = true)
    private String codigoToken;

    public Autenticacion() {
    }

    public Autenticacion(long id, String usuario, String contrasenna, String numTel, String codigoToken) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenna = contrasenna;
        this.numTel = numTel;
        this.codigoToken = codigoToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
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
        return id == autenticacion.id && usuario.equals(autenticacion.usuario) && numTel.equals(autenticacion.numTel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, numTel);
    }
}
