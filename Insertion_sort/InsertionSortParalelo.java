package Insertion_sort;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class InsertionSortParalelo extends RecursiveAction {
    private final int[] array;
    private final int inicio;
    private final int fim;

    public InsertionSortParalelo(int[] array, int inicio, int fim) {
        this.array = array;
        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    protected void compute() {
        if (inicio < fim) {
            if (fim - inicio <= 10) {
                insertionSort(array, inicio, fim);
            } else {
                int meio = (inicio + fim) / 2;

                InsertionSortParalelo leftTask = new InsertionSortParalelo(array, inicio, meio);
                InsertionSortParalelo rightTask = new InsertionSortParalelo(array, meio + 1, fim);

                invokeAll(leftTask, rightTask);
            }
        }
    }

    public static void insertionSort(int[] arr, int inicio, int fim) {
        for (int i = inicio + 1; i <= fim; i++) {
            int chave = arr[i];
            int j = i - 1;

            while (j >= inicio && arr[j] > chave) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = chave;
        }
    }

    public static void main(String[] args) {
        int[] array = gerarNumeros(100000);

        System.out.println("Antes do insertion sort paralelo:");
        imprimir(array);

        // Início do tempo
        long tempoInicial = System.currentTimeMillis();

        // Número de threads pré-definido
        int numThreads = 4;

        ForkJoinPool pool = new ForkJoinPool(numThreads);

        InsertionSortParalelo insertionSort = new InsertionSortParalelo(array, 0, array.length - 1);

        pool.invoke(insertionSort);

        long tempoFinal = System.currentTimeMillis();
        // Fim do tempo

        System.out.println("Depois do insertion sort paralelo:");
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
