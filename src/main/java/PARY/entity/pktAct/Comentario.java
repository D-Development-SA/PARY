package PARY.entity.pktAct;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String text;
    private LocalDateTime fechaH;
    private String textUFechaH;

    public Comentario(Long id, String text, LocalDateTime fechaH, String textUFechaH) {
        this.id = id;
        this.text = text;
        this.fechaH = fechaH;
        this.textUFechaH = textUFechaH;
    }

    public Comentario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getFechaH() {
        return fechaH;
    }

    public void setFechaH(LocalDateTime fechaH) {
        this.fechaH = fechaH;
    }

    public String getTextUFechaH() {
        return textUFechaH;
    }

    public void setTextUFechaH(String textUFechaH) {
        this.textUFechaH = textUFechaH;
    }

    @PrePersist
    public void generarFechaH(){
        addComentFecha();
        this.fechaH = LocalDateTime.now();
    }
    @PreUpdate
    public void actualizarFechaH(){
        addComentFecha();
        this.fechaH = LocalDateTime.now();
    }

    public void addComentFecha(){
        this.textUFechaH = text + " [" + fechaH.toLocalDate() + " | " + fechaH.toLocalTime() + "]";
    }
}
