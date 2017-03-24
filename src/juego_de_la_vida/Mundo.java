package juego_de_la_vida;

import static java.lang.Thread.sleep;
import java.util.concurrent.CyclicBarrier;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Mundo implements Runnable {

    private final Celula[][] matrizCelulas;
    private final CyclicBarrier turno;
    private int velocidad;
    private boolean pausa;
    private boolean fin = false;

    public Mundo(Celula[][] matrizCelulas, int velocidad, boolean pausa, CyclicBarrier turno) {
        this.matrizCelulas = matrizCelulas;
        this.velocidad = velocidad;
        this.pausa = pausa;
        this.turno = turno;
    }

    @Override
    public void run() {
        try {
            while (!fin) {
                //espera a que todas las celulas de cada submmundo calculen
                turno.await();
                //espera a que todas las celulas de cada submundo sean actualizadas
                turno.await();
                //Imprime el mundo por consola
                System.out.println(toString(matrizCelulas));
                //Permite que usuario pueda interactuar con la interfaz
                sleep(velocidad * 1000);
                //si el juego es pausado se detendra aqui
                if (pausa) {
                    dormir();
                }
                //libera a los submundos para que puedan volver a procesar los
                //cambios
                turno.await(); //Para interactividad
            }
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Mundo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve un string con la matriz del mundo para imprimirla por pantalla.
     *
     * @param mundo
     * @return String de la matriz.
     */
    public String toString(Celula[][] mundo) {
        int alto = mundo.length;
        int ancho = mundo[0].length;
        String res = "";
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho - 1; j++) {
                res += (mundo[i][j].getEstadoAct()) ? "\033[32mV\033[30m" : "\033[31mM\033[30m";
                res += ",";
            }
            res += (mundo[i][ancho - 1].getEstadoAct()) ? "\033[32mV\033[30m" : "\033[31mM\033[30m";
            res += "\n";
        }
        return res;
    }

    /**
     * Duerme al mundo (simula pausear).
     *
     * @throws InterruptedException
     */
    synchronized public void dormir() throws InterruptedException {
        wait();
    }

    /**
     * Despierta al mundo (simula despausear).
     */
    synchronized public void despertar() {
        notify();
    }

    /**
     * Termina el hilo, seteando fin en true.
     */
    public void terminar() {
        this.fin = true;
        System.out.println("\033[34mFIN\033[30m \n");
    }

    /**
     * Mata todas las celulas y lo indica por consola.
     */
    public void reset() {
        System.out.println("\033[34mRESET\033[30m \n");
        for (int i = 0; i < matrizCelulas.length; i++) {
            for (int j = 0; j < matrizCelulas[0].length; j++) {
                matrizCelulas[i][j].setEstadoAct(false);
            }
        }
    }

    /**
     * Modifica el estado de las celulas de forma aleatoria y lo indica por
     * consola.
     */
    public void random() {
        System.out.println("\033[34mRANDOM\033[30m \n");
        Random random = new Random();
        for (int i = 0; i < matrizCelulas.length; i++) {
            for (int j = 0; j < matrizCelulas[0].length; j++) {
                matrizCelulas[i][j].setEstadoAct((random.nextBoolean()));
            }
        }
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Sube la velocidad del juego si es posible (la velocidad aumenta cuanto
     * menor es).
     */
    public void subirVelocidad() {
        if (this.velocidad > 0) {
            this.velocidad--;
        }
    }

    /**
     * Baja la velocidad del juego (la velocidad disminuye cuanto mayor es).
     */
    public void bajarVelocidad() {
        if (this.velocidad < 9) {
            this.velocidad++;
        }
    }

    public boolean getPausa() {
        return pausa;
    }

    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }

    /**
     * Pausea o despausea y muestra el mensaje por consola.
     */
    public void modPausa() {
        if (!pausa) {
            System.out.println("\033[34mPAUSE\033[30m \n");
        } else {
            System.out.println("\033[34mUNPAUSE\033[30m \n");
        }
        this.pausa = !pausa;
    }

    public Celula[][] getMatrizCelulas() {
        return matrizCelulas;
    }

}
