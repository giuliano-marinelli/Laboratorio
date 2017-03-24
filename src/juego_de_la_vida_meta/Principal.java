package juego_de_la_vida_meta;

import static java.lang.Thread.sleep;
import java.awt.Point;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Principal {

    /**
     *
     * @param args
     * @throws InterruptedException
     * @throws BrokenBarrierException
     */
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        int ancho = 5;
        int alto = 5;
        Celula[][] mundo = new Celula[alto][ancho];
        CyclicBarrier turno = new CyclicBarrier((ancho * alto) + 1);
        Random rand = new Random();

        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                mundo[i][j] = new Celula((rand.nextBoolean()), mundo, new Point(i, j), turno);
                Thread creator = new Thread(mundo[i][j]);
                creator.start();
            }
        }
        turno.await();
        System.out.println(toStringMundo(mundo));
        sleep(2000);
        while (true) {
            turno.await();
            turno.await();
            System.out.println();
            System.out.println(toStringMundo(mundo));
            sleep(2000);
            turno.await();
        }
    }

    /**
     * Devuelve un string con la matriz del mundo para imprimirla por pantalla.
     *
     * @param mundo
     * @return cadena lista para imprimir.
     */
    public static String toStringMundo(Celula[][] mundo) {
        String res = "";
        for (int i = 0; i < mundo.length; i++) {
            for (int j = 0; j < mundo[0].length; j++) {
                res += (mundo[i][j].getEstado()) ? "\033[32mV\033[30m" : "\033[31mM\033[30m";
                if (j < mundo[0].length - 1) {
                    res += ",";
                }
            }
            res += "\n";
        }
        return res;
    }

}
