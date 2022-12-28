package PARY.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String calle;
    private String coordenadas;
    private String entreCalle;
    private String provincia;
    private String municipio;
    private String reparto;

    public Direccion(String calle, String entreCalle, String provincia, String municipio, String reparto) {
        this.calle = calle;
        this.entreCalle = entreCalle;
        this.provincia = provincia;
        this.municipio = municipio;
        this.reparto = reparto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Direccion() {
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getEntreCalle() {
        return entreCalle;
    }

    public void setEntreCalle(String entreCalle) {
        this.entreCalle = entreCalle;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return calle.equals(direccion.calle) && entreCalle.equals(direccion.entreCalle) && provincia.equals(direccion.provincia) && municipio.equals(direccion.municipio) && reparto.equals(direccion.reparto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, entreCalle, provincia, municipio, reparto);
    }
}
