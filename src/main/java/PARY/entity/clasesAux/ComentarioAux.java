package PARY.entity.clasesAux;

import org.springframework.stereotype.Component;

@Component
public class ComentarioAux {
    private String textNuevo;
    private String textViejo;

    public ComentarioAux(String textNuevo, String textViejo) {
        this.textNuevo = textNuevo;
        this.textViejo = textViejo;
    }

    public ComentarioAux() {
    }

    public String getTextNuevo() {
        return textNuevo;
    }

    public void setTextNuevo(String textNuevo) {
        this.textNuevo = textNuevo;
    }

    public String getTextViejo() {
        return textViejo;
    }

    public void setTextViejo(String textViejo) {
        this.textViejo = textViejo;
    }
}
