package PARY.entity.pktAct;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comentarios")
public class Comentario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String text;
    private LocalDateTime fechaH;
    private String textUFechaH;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comentario(String text, LocalDateTime fechaH, String textUFechaH) {
        this.text = text;
        this.fechaH = fechaH;
        this.textUFechaH = textUFechaH;
    }

    public Comentario() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return id.equals(that.id) && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @PrePersist
    private void generarFechaH(){
        this.fechaH = LocalDateTime.now();
        addComentFecha();
    }
    @PreUpdate
    private void actualizarFechaH(){
        this.fechaH = LocalDateTime.now();
        addComentFecha();
    }

    public void addComentFecha(){
        this.textUFechaH = text + " [" + fechaH.toLocalDate() + " | " + fechaH.toLocalTime() + "]";
    }
}
