package Bubble_sort;
import java.util.Random;

public class BubbleSortSerial {

    public static void main(String[] args) {

        int[] numeros = gerarNumeros(100000);

        long tempoInicial =  System.currentTimeMillis();
        System.out.println("Antes do bubble sort serial:");
        imprimir(numeros);

        bubble_sort_serial(numeros);
        long tempoFinal = System.currentTimeMillis();

        System.out.println("\nDepois do bubble sort serial:");
        imprimir(numeros);

        long tempoExecucao = tempoFinal - tempoInicial;
        System.out.println("\nTempo de execucao: " + tempoExecucao + " milissegundos");
    }

    public static int[] gerarNumeros(int tamanho) {
        int[] numeros = new int[tamanho];
        Random aleatorio = new Random();
        for (int i = 0; i < tamanho; i++) {
            numeros[i] = aleatorio.nextInt(1000000);
        }
        return numeros;
    }

    public static void bubble_sort_serial(int[] numeros) {
        for(int i = 0; i < numeros.length - 1; i++) {
            for(int j = 0; j < numeros.length - i - 1; j++) {
                if(numeros[j] > numeros[j + 1]) {
                    int aux = numeros[j];
                    numeros[j] = numeros[j + 1];
                    numeros[j + 1] = aux;
                }
            }
        }
    }

    public static void imprimir(int[] numeros) {
        for(int numero: numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }

}
