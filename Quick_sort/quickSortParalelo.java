package Quick_sort;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSortParalelo extends RecursiveAction {
    private int[] array;
    private int inicio;
    private int fim;

    public QuickSortParalelo(int[] array, int inicio, int fim) {
        this.array = array;
        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    protected void compute() {
        if (inicio < fim) {
            int indicePivo = particiona(array, inicio, fim);
            // Cria sub-tarefas
            QuickSortParalelo esquerda = new QuickSortParalelo(array, inicio, indicePivo - 1);
            QuickSortParalelo direita = new QuickSortParalelo(array, indicePivo + 1, fim);
            // Executa as sub-tarefas em paralelo
            invokeAll(esquerda, direita);
        }
    }

    private int particiona(int[] array, int inicio, int fim) {
        int pivo = array[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (array[j] < pivo) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[fim];
        array[fim] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        int[] numeros = gerarNumeros(100000);

        long tempoInicial = System.currentTimeMillis();
        System.out.println("Antes do Quick Sort paralelo:");
        imprimir(numeros);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new QuickSortParalelo(numeros, 0, numeros.length - 1));

        long tempoFinal = System.currentTimeMillis();

        System.out.println("\nDepois do Quick Sort paralelo:");
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

    public static void imprimir(int[] numeros) {
        for(int numero: numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}
