package Merge_sort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortParalelo extends RecursiveAction {
    private final int[] array;
    private final int inicio;
    private final int fim;

    public MergeSortParalelo(int[] array, int inicio, int fim) {
        this.array = array;
        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    protected void compute() {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;

            MergeSortParalelo leftTask = new MergeSortParalelo(array, inicio, meio);
            MergeSortParalelo rightTask = new MergeSortParalelo(array, meio + 1, fim);

            invokeAll(leftTask, rightTask);

            merge(array, inicio, meio, fim);
        }
    }

    public static void merge(int[] arr, int inicio, int meio, int fim) {
        int n1 = meio - inicio + 1;
        int n2 = fim - meio;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[inicio + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[meio + 1 + j];

        int i = 0, j = 0;

        int k = inicio;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void main(String[] args) {
        int[] array = gerarNumeros(100000);

        System.out.println("Antes do merge sort paralelo:");
        imprimir(array);

        // inicio do tempo
        long tempoInicial = System.currentTimeMillis();

        // NUMERO DE THREADS PRE DEFINIDOS
        int numThreads = 12;

        ForkJoinPool pool = new ForkJoinPool(numThreads);

        MergeSortParalelo mergeSort = new MergeSortParalelo(array, 0, array.length - 1);

        pool.invoke(mergeSort);

        long tempoFinal = System.currentTimeMillis();
        // fim do tempo

        System.out.println("Depois do merge sort paralelo:");
        imprimir(array);

        long tempoExecucao = tempoFinal - tempoInicial;
        System.out.println("\nTempo de execução: " + tempoExecucao + " milissegundos");

        pool.shutdown();
    }

    public static int[] gerarNumeros(int tamanho) {
        int[] numeros = new int[tamanho];
        Random aleatorio = new Random();
        for (int i = 0; i < tamanho; i++) {
            numeros[i] = aleatorio.nextInt(1000000);
        }
        return numeros;
    }

    public static void imprimir(int[] numeros) {
        for (int numero : numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}
