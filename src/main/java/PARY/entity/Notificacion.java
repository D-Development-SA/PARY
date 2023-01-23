package PARY.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String titulo;
    private String info;
    private LocalDate fecha;
    private LocalTime hora;
    private boolean visto;
    private boolean nueva = true;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    //private Imagen img;

    public Notificacion() {
    }

    public Notificacion(Long id, String titulo, String info, LocalDate fecha,
                        LocalTime hora, boolean visto, boolean nueva, Actividad actividad) {
        this.id = id;
        this.titulo = titulo;
        this.info = info;
        this.fecha = fecha;
        this.hora = hora;
        this.visto = visto;
        this.nueva = nueva;
        this.actividad = actividad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividades) {
        this.actividad = actividades;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public boolean isNueva() {
        return nueva;
    }

    public void setNueva(boolean nueva) {
        this.nueva = nueva;
    }

    @PrePersist
    public void generarFechaHora(){
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
}
