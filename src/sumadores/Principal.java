package sumadores;

import tecladoIn.TecladoIn;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Principal {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        //numeros que se sumaran
        int[] numeros;
        //arreglo que contiene los sumadores 
        Sumador[] sumadores;
        //variable concurrente que contendra la suma
        AtomicInteger suma = new AtomicInteger(0);
        //variable que contendra la suma hecha de forma procedural
        int sumaReal = 0;

        System.out.println("Ingrese la cantidad de numeros a sumar: ");
        int cantNumeros = TecladoIn.readInt();

        System.out.println("Ingrese la cantidad de sumadores simultaneos para operar la suma: ");
        int cantSumadores = TecladoIn.readInt();
        
        //indica cuantos numero debe sumar cada sumador
        int rango;
        //comprueba que sea posible realizarse la operacion con los valores ingresados
        if ((cantNumeros % cantSumadores) == 0 && cantNumeros >= cantSumadores) {
            //calcula el rango
            rango = (cantNumeros / cantSumadores);
            numeros = new int[cantNumeros];
            sumadores = new Sumador[cantSumadores];

            //genera numero aleatorios del 1 al 10 y los pone en el arreglo
            for (int i = 0; i < cantNumeros; i++) {
                numeros[i] = random.nextInt(10);
            }

            CountDownLatch inicio = new CountDownLatch(1);
            CountDownLatch fin = new CountDownLatch(cantSumadores);

            //crea los sumadores y les envia el rango y la posicion para que 
            //sepan lo que deben sumar
            for (int i = 0; i < cantSumadores; i++) {
                sumadores[i] = new Sumador(numeros, i, inicio, fin, suma, rango);
                Thread tsum = new Thread(sumadores[i]);
                tsum.start();
            }
            //permite a los sumadores comenzar
            inicio.countDown();

            //espera a que los sumadores terminen
            fin.await();
            System.out.println("Suma final concurrente: " + suma);

            //realiza la suma de forma procedural para comprobar resultados
            for (int i = 0; i < cantNumeros; i++) {
                sumaReal += numeros[i];
            }
            System.out.println("Suma final procedural: " + sumaReal);
        } else {
            System.err.println("La cantidad de sumadores no puede ser mayor a la cantidad de numeros."
                    + "\nY la cantidad de numeros debe ser divisible por la cantidad de sumadores.");
        }
    }

}
