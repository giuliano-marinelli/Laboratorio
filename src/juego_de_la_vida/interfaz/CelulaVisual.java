package juego_de_la_vida.interfaz;

import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class CelulaVisual implements Observer {

    JButton grafico;

    public CelulaVisual() {
        grafico = new JButton();
        grafico.setContentAreaFilled(false);
        grafico.setBorderPainted(false);
        grafico.setRolloverEnabled(true);
    }

    public JButton getButton() {
        return grafico;
    }

    /**
     * Actualiza el objeto visual de la celula segun el estado que adquiere su
     * objeto logico.
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            grafico.setIcon(new ImageIcon(getClass().getResource("../recursos/vivo.png")));
            grafico.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/vivo-rollover.png")));
        } else {
            grafico.setIcon(new ImageIcon(getClass().getResource("../recursos/muerto.png")));
            grafico.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/muerto-rollover.png")));
        }
    }

}
