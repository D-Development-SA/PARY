package PARY.entity;

import PARY.entity.constantes.Constant_Act;
import PARY.entity.pktAct.Cantidad;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "actividades")
public class Actividad implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private double precio;
    //    private Imagen imgs;
    @Column(length = 1000)
    private String info;
    @Column(nullable = false)
    private String fechaHora;   //fecha y hora de la Actividad
    @Enumerated(EnumType.STRING)
    private Constant_Act tipoAct;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Direccion direccion;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservacion> reservacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Cantidad cantidad;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "perfil_id", foreignKey = @ForeignKey(name = "FK_PERFIL_ACTIVIDAD_ID"))
    @JsonIgnore
    private Perfil perfil;

    public Actividad() {
    }

    public Actividad(long id, String nombre, double precio, String info, String fechaHora,
                     Constant_Act tipoAct, Direccion direccion, List<Reservacion> reservacion,
                     Cantidad cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.info = info;
        this.fechaHora = fechaHora;
        this.tipoAct = tipoAct;
        this.direccion = direccion;
        this.reservacion = reservacion;
        this.cantidad = cantidad;
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

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<Reservacion> getReservacion() {
        return reservacion;
    }

    public void setReservacion(List<Reservacion> reservacion) {
        this.reservacion = reservacion;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public void setCantidad(Cantidad cantidad) {
        this.cantidad = cantidad;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}