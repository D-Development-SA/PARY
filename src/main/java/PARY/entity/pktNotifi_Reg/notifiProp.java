package PARY.entity.pktNotifi_Reg;

public class notifiProp {
    public final static int RESER_ACEPT = 0;
    public final static int RESER_ELIM = 1;
    public final static int RESER_ANUL = 2;
    public final static int ACT_NUEVA_LOCAL = 3;
    public final static int EVENT_NUEVO = 4;
    public final static int CAPAC_AGOT = 5;
    public final static int EVENT_START = 6;
    public final static int EVENT_FINISH = 7;
    public final static int ME_ENCANTA = 8;
    public final static int OFERTAS_NUEVAS = 9;
    public final static int CAPAC_AUMENT = 10;
    public final static int RESER_POCAS = 11;
    public final static int ACT_LIKE = 12;
    public final static int QUE_ESPERAS = 13;
    public final static int NO_PERDER = 14;
    public final static int GRAN_FIESTA = 15;
    public final static int ACT_UPDATE = 16;

    public static final String[] notifi = {
            "Reservacion aceptada",
            "Reservacion eliminada",
            "Reservacion anulada",
            "Nueva actividad en tu localidad",
            "Nuevo evento",
            "Capacidades agotadas",
            "Comenzo evento",
            "Termino evento",
            "Me encanta",
            "Nuevas ofertas",
            "Aumento de las Capacidades",
            "Quedan pocas reservaciones",
            "Puede que",
            "¿Qué esperas?",
            "No te lo puedes perder",
            "Gran fiesta",
            "Actividad actualizada"
    };
    
    public static String crearInfo(String NombreAct, int constant){
        return switch (constant) {
            case 0 -> NombreAct + " ha aceptado su reservación";
            case 1 -> "Has eliminado la reservacion de " + NombreAct;
            case 2 -> NombreAct + " ha anulado su reservación";
            case 3 -> NombreAct + " en tu localidad 'Presiona para ver'";
            case 4 -> NombreAct + " abrira sus puertas pronto 'Ver dia'";
            case 5 -> "Están agotadas las reservaciones de " + NombreAct;
            case 6 -> NombreAct + " !!!Ya ha comenzado¡¡¡";
            case 7 -> NombreAct + " ha terminado por hoy";
            case 8 -> "A " + NombreAct.split("::")[0] + " les gusta el centro recreativo " + NombreAct.split("::")[1];
            case 9 -> NombreAct + " ofrece nuevas ofertas";
            case 10 -> NombreAct + " aumentó sus capacidades 'Oportunidad'";
            case 11 -> "Quedan pocas reservaciones de " + NombreAct;
            case 12 -> "Puede que te guste esta Actividad" + NombreAct + " 'Ver'";
            case 13 -> "Hay actividades esperando por ti";
            case 14 -> NombreAct + " se pone como te gusta";
            case 15 -> "Muchas personas les gusta" + NombreAct;
            case 16 -> "Hubieron cambios en " + NombreAct + " 'Ver'";
            default -> "";
        };
    }

}
