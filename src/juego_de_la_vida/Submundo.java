package juego_de_la_vida;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Submundo implements Runnable {

    private final Celula[][] matrizCelulas;
    private final CyclicBarrier turno;
    private final int fila;

    public Submundo(Celula[][] matrizCelulas, CyclicBarrier turno, int fila) {
        this.matrizCelulas = matrizCelulas;
        this.turno = turno;
        this.fila = fila;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //calcula el estado siguiente de cada celula del submundo
                for (int i = 0; i < matrizCelulas[fila].length; i++) {
                    matrizCelulas[fila][i].calcularEstadoSig();
                }
                //espera a que todas las celulas de cada submmundo calculen
                turno.await();
                //setea el estado actual de las celulas al estado precalculado
                //anteriormente
                for (int i = 0; i < matrizCelulas[fila].length; i++) {
                    matrizCelulas[fila][i].actualizarEstadoAct();
                }
                //espera a que todas las celulas de cada submundo sean actualizadas
                turno.await();
                //espera a que el mundo se imprima y el usuario interactue con 
                //la interfaz
                turno.await(); //Para interactividad
            }
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Celula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
