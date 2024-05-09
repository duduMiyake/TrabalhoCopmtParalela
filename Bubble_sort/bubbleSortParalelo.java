package Bubble_sort;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class bubbleSortParalelo {

    public static void main(String[] args) {

        int[] numeros = gerarNumeros(10000);

        System.out.println("Antes do bubble sort paralelo:");
        imprimir(numeros);

        long tempoInicial =  System.currentTimeMillis();
        bubble_sort_paralelo(numeros);
        long tempoFinal = System.currentTimeMillis();

        System.out.println("Depois do bubble sort paralelo:");
        imprimir(numeros);

        long tempoExecucao = tempoFinal - tempoInicial;
        System.out.println("\nExecution time: " + tempoExecucao + " milisseconds");
    }

    public static int[] gerarNumeros(int tamanho) {
        int[] numeros = new int[tamanho];
        Random aleatorio = new Random();
        for (int i = 0; i < tamanho; i++) {
            numeros[i] = aleatorio.nextInt(1000000);
        }
        return numeros;
    }

    public static void bubble_sort_paralelo(int[] numeros) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numeros.length - 1; i++) {
            for (int j = 0; j < numeros.length - i - 1; j++) {
                final int a = j;
                final int b = j + 1;
                executor.execute(() -> {
                    if (numeros[a] > numeros[b]) {
                        int temp = numeros[a];
                        numeros[a] = numeros[b];
                        numeros[b] = temp;
                    }
                });
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    public static void imprimir(int[] numeros) {
        for (int numero : numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}
