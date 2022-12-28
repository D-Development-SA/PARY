package PARY.entity;

import PARY.entity.constantes.Constant_Act;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "actividades")
public class misActividades implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private double precio;
    @Embedded
    @Column(unique = true, nullable = false)
    private Direccion direccion;
//    private Imagen imgs;
    @Column(unique = true, nullable = false)
    private String coordenadas;
    @Column(length = 1000)
    private String info;
    @Column(nullable = false)
    private String fechaHora;   //fecha y hora de la Actividad
    @Enumerated(EnumType.STRING)
    private Constant_Act tipoAct;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reservacion_id", foreignKey = @ForeignKey(name = "FK_RESERVACION_ID"))
    @JsonIgnoreProperties({"actividades"})
    private Reservacion reservacion;

    @OneToOne(mappedBy = "actividad", cascade = CascadeType.ALL)
    private DatosAct datosAct;

    public misActividades() {
    }

    public misActividades(long id, String nombre, double precio, Direccion direccion,
                          String coordenadas, String info, String fechaHora, Constant_Act tipoAct,
                          Reservacion reservacion, DatosAct datosAct) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        this.info = info;
        this.fechaHora = fechaHora;
        this.tipoAct = tipoAct;
        this.reservacion = reservacion;
        this.datosAct = datosAct;
    }

    public misActividades(long id, String nombre, double precio, Direccion direccion,
                          String coordenadas, String info, String fechaHora,
                          Reservacion reservacion)
    {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        this.info = info;
        this.fechaHora = fechaHora;
        this.reservacion = reservacion;
    }

    public Reservacion getReservacion() {
        return reservacion;
    }

    public void setReservacion(Reservacion reservacion) {
        this.reservacion = reservacion;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Constant_Act getTipoAct() {
        return tipoAct;
    }

    public void setTipoAct(Constant_Act tipoAct) {
        this.tipoAct = tipoAct;
    }

    public DatosAct getDatosAct() {
        return datosAct;
    }

    public void setDatosAct(DatosAct datosAct) {
        this.datosAct = datosAct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        misActividades MisActividades = (misActividades) o;
        return id == MisActividades.id && nombre.equals(MisActividades.nombre) && coordenadas.equals(MisActividades.coordenadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, coordenadas);
    }
}
