package juego_de_la_vida_meta;

import java.awt.Point;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Celula implements Runnable {

    private boolean estado;
    private LinkedList<Celula> adyacentes;
    private final CyclicBarrier turno;
    private final Celula[][] mundo;
    private final Point pos;

    /**
     * Constructor de celula.
     *
     * @param estadoInicial
     * @param mundo
     * @param pos
     * @param turno
     */
    public Celula(boolean estadoInicial, Celula[][] mundo, Point pos, CyclicBarrier turno) {
        this.estado = estadoInicial;
        this.turno = turno;
        this.mundo = mundo;
        this.pos = pos;
    }

    /**
     * Cambia el estado de una celula segun sus celulas adyacentes, en el mundo
     * al que pertenece.
     */
    @Override
    public void run() {
        try {
            turno.await();
            calcularAdy();
            while (true) {
                int cantAdy = 0;
                for (Celula adyacente : adyacentes) {
                    if (adyacente.getEstado()) {
                        cantAdy++;
                    }
                }
                boolean nuevoEstado;
                if (cantAdy >= 2 && cantAdy <= 3 && estado) {
                    nuevoEstado = true;
                } else {
                    nuevoEstado = !estado && cantAdy == 3;
                }
                turno.await();
                estado = nuevoEstado;
                turno.await();
                turno.await();
            }
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Celula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve el estado actual de la celula.
     *
     * @return true si esta viva, false si esta muerta.
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Cambia el estado de la celula.
     *
     * @param estado
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * Devuelve la posicion de la celula en el mundo al que pertenece.
     *
     * @return (x,y).
     */
    public String getPos() {
        return "(" + pos.x + "," + pos.y + ")";
    }

    /**
     * Calcula las celulas adyacentes.
     */
    public void calcularAdy() {
        adyacentes = new LinkedList<>();
        int filaIni = (pos.x > 0) ? pos.x - 1 : pos.x;
        int filaFin = (pos.x < mundo.length - 1) ? pos.x + 1 : pos.x;
        int colIni = (pos.y > 0) ? pos.y - 1 : pos.y;
        int colFin = (pos.y < mundo[0].length - 1) ? pos.y + 1 : pos.y;
        for (int i = filaIni; i <= filaFin; i++) {
            for (int j = colIni; j <= colFin; j++) {
                if (!(i == pos.x && j == pos.y)) {
                    adyacentes.add(mundo[i][j]);
                }
            }
        }
    }

}
