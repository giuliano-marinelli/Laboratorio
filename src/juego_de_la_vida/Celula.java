package juego_de_la_vida;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Observable;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Celula extends Observable {

    private boolean estadoAct;
    private boolean estadoSig;
    private final Point pos;
    private LinkedList<Celula> adyacentes;

    public Celula(Point pos) {
        this.pos = pos;
    }

    /**
     * Calcula y setea el estado siguiente de la celula, segun las reglas del
     * juego.
     */
    public void calcularEstadoSig() {
        int adyVivos = 0;
        for (int i = 0; i < adyacentes.size(); i++) {
            if (adyacentes.get(i).getEstadoAct()) {
                adyVivos++;
            }
        }
        if (estadoAct) {
            estadoSig = (adyVivos >= 2 && adyVivos <= 3);
        } else {
            estadoSig = (adyVivos == 3);
        }
    }

    /**
     * Setea al estado actual el estado siguiente precalculado.
     */
    public void actualizarEstadoAct() {
        estadoAct = estadoSig;
        setChanged();
        notifyObservers(estadoAct);
    }

    public boolean getEstadoAct() {
        return estadoAct;
    }

    public void setEstadoAct(boolean estado) {
        this.estadoAct = estado;
        setChanged();
        notifyObservers(estadoAct);
    }

    public void modEstadoAct() {
        this.estadoAct = !estadoAct;
        setChanged();
        notifyObservers(estadoAct);
    }

    public Point getPos() {
        return pos;
    }

    public void setAdyacentes(LinkedList<Celula> adyacentes) {
        this.adyacentes = adyacentes;
    }

}
