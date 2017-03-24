package juego_de_la_vida;

import java.awt.Point;
import java.util.LinkedList;
import tecladoIn.TecladoIn;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Principal {

    public static void main(String[] args) {
        Random random = new Random();

        System.out.println("Ingrese ancho del mundo: ");
        int ancho = TecladoIn.readInt();
        System.out.println("Ingrese alto del mundo: ");
        int alto = TecladoIn.readInt();
        System.out.println("Ingrese velocidad: ");
        int velocidad = TecladoIn.readInt();

        Celula[][] matrizCelulas = new Celula[alto][ancho];
        Submundo[] submundo = new Submundo[alto];
        CyclicBarrier turno = new CyclicBarrier(alto + 1);

        //crea las celulas con un estado aleatorio y las agrega a la matriz
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                matrizCelulas[i][j] = new Celula(new Point(i, j));
                matrizCelulas[i][j].setEstadoAct((random.nextBoolean()));
            }
        }

        //llama a un metodo que setea a cada celula cuales son sus adyacentes
        setAdyacentes(matrizCelulas);

        //crea y lanza el mundo del juego
        Mundo mundo = new Mundo(matrizCelulas, velocidad, false, turno);
        Thread mundoThread = new Thread(mundo);
        mundoThread.start();

        //crea y lanza los submundos del juego
        Thread submundoThread;
        for (int i = 0; i < alto; i++) {
            submundo[i] = new Submundo(matrizCelulas, turno, i);
            submundoThread = new Thread(submundo[i]);
            submundoThread.start();
        }
    }

    /**
     * Setea a cada celula una lista con las celulas adyacentes a ella.
     *
     * @param matrizCelulas
     */
    public static void setAdyacentes(Celula[][] matrizCelulas) {
        int alto = matrizCelulas.length;
        int ancho = matrizCelulas[0].length;
        int filaIni;
        int filaFin;
        int colIni;
        int colFin;
        Point pos;
        LinkedList<Celula> adyacentes;
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                pos = matrizCelulas[i][j].getPos();
                adyacentes = new LinkedList<>();
                filaIni = (pos.x > 0) ? pos.x - 1 : pos.x;
                filaFin = (pos.x < alto - 1) ? pos.x + 1 : pos.x;
                colIni = (pos.y > 0) ? pos.y - 1 : pos.y;
                colFin = (pos.y < ancho - 1) ? pos.y + 1 : pos.y;
                for (int f = filaIni; f <= filaFin; f++) {
                    for (int c = colIni; c <= colFin; c++) {
                        if (!(f == pos.x && c == pos.y)) {
                            adyacentes.add(matrizCelulas[f][c]);
                        }
                    }
                }
                matrizCelulas[i][j].setAdyacentes(adyacentes);
            }
        }
    }

}
