package sumadores;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Sumador implements Runnable {

    final private int[] numeros;
    final private int pos;
    final private CountDownLatch inicio;
    final private CountDownLatch fin;
    final private AtomicInteger suma;
    final private int rango;

    /**
     * Constructor del hilo sumador.
     * 
     * @param numeros
     * @param pos
     * @param inicio
     * @param fin
     * @param suma
     * @param rango
     */
    public Sumador(int[] numeros, int pos, CountDownLatch inicio, CountDownLatch fin, AtomicInteger suma, int rango) {
        this.numeros = numeros;
        this.pos = pos;
        this.inicio = inicio;
        this.fin = fin;
        this.suma = suma;
        this.rango = rango;
    }

    /**
     * Realiza, concurrentemente con otros hilos sumadores, la suma parcial de 
     * los numeros ingresados.
     */
    @Override
    public void run() {
        try {
            //espera a que todos los sumadores esten listos
            inicio.await();
            int sumaParcial = 0;
            //realiza la suma parcial que le corresponde
            for (int i = (pos * rango); i < (pos * rango + rango); i++) {
                sumaParcial += numeros[i];
            }
            System.out.println("Suma " + pos + " = " + sumaParcial);
            //suma a la variable concurrente lo que calculo
            suma.getAndAdd(sumaParcial);
            //indica que termino su trabajo
            fin.countDown();
        } catch (InterruptedException ex) {
            Logger.getLogger(Sumador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
