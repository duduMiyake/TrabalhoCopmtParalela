package Quick_sort;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class quickSortParalelo {

    public static void main(String[] args) {
        int[] numeros = gerarNumeros(10000);

        long tempoInicial =  System.currentTimeMillis();
        System.out.println("Antes do Quick Sort paralelo:");
        imprimir(numeros);

        int numProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Número máximo de processadores disponíveis: " + numProcessors);

        int numThreads = numProcessors;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int inicio = 0;
        int fim = numeros.length - 1;
        for (int i = 0; i < numThreads; i++) {
            final int threadStart = inicio + i * (fim - inicio + 1) / numThreads;
            final int threadEnd = i == numThreads - 1 ? fim : inicio + (i + 1) * (fim - inicio + 1) / numThreads - 1;
            executor.execute(() -> quick_sort_paralelo(numeros, threadStart, threadEnd));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        long tempoFinal = System.currentTimeMillis();

        System.out.println("\nDepois do Quick Sort paralelo:");
        imprimir(numeros);

        long tempoExecucao = tempoFinal - tempoInicial;
        System.out.println("\nTempo de execucao: " + tempoExecucao + " milissegundos");
    }

    public static int[] gerarNumeros(int tamanho) {
        int[] numeros = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            numeros[i] = (int) (Math.random() * 1000000);
        }
        return numeros;
    }

    public static void quick_sort_paralelo(int[] numeros, int inicio, int fim) {
        if (inicio < fim) {
            int indicePivo = particiona(numeros, inicio, fim);
            quick_sort_paralelo(numeros, inicio, indicePivo - 1);
            quick_sort_paralelo(numeros, indicePivo + 1, fim);
        }
    }

    public static int particiona(int[] numeros, int inicio, int fim) {
        int pivo = numeros[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (numeros[j] < pivo) {
                i++;
                int temp = numeros[i];
                numeros[i] = numeros[j];
                numeros[j] = temp;
            }
        }
        int temp = numeros[i + 1];
        numeros[i + 1] = numeros[fim];
        numeros[fim] = temp;
        return i + 1;
    }

    public static void imprimir(int[] numeros) {
        for (int numero : numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}