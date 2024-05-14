package Códigos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.SwingUtilities;

public class MergeSort {
    public static void main(String[] args) {

        int[] tamanhos = {10, 100, 1000, 10000, 100000};

        int[] numeros = gerarNumeros(10000);

        //Usa o swingChart para mostrar o gráfico
        SwingUtilities.invokeLater(() -> new SwingChart("resultados_mergesort.csv"));

        for (int tamanho : tamanhos) {
            int[] numerosSerial = gerarNumeros(tamanho);
            int[] numerosParalelo = numerosSerial.clone();

            System.out.println("Tamanho do array: " + tamanho);

            // Serial
            long tempoInicialSerial = System.currentTimeMillis();
            merge_sort_serial(numerosSerial, 0, numerosSerial.length - 1);
            long tempoFinalSerial = System.currentTimeMillis();
            long tempoExecucaoSerial = tempoFinalSerial - tempoInicialSerial;

            System.out.println("Tempo de execução (Serial): " + tempoExecucaoSerial + " milissegundos");

            // Paralelo
            long tempoInicialParalelo = System.currentTimeMillis();
            merge_sort_paralelo(numerosParalelo, 0, numerosParalelo.length - 1);
            long tempoFinalParalelo = System.currentTimeMillis();
            long tempoExecucaoParalelo = tempoFinalParalelo - tempoInicialParalelo;

            System.out.println("Tempo de execução (Paralelo): " + tempoExecucaoParalelo + " milissegundos");

            // Escreve os resultados em um arquivo CSV
            escreverCSV("resultados_mergesort.csv", new String[]{"Serial", "Paralelo"}, new long[]{tempoExecucaoSerial, tempoExecucaoParalelo}, tamanho);
        }
    }

    public static int[] gerarNumeros(int tamanho) {
        int[] numeros = new int[tamanho];
        Random aleatorio = new Random();
        for (int i = 0; i < tamanho; i++) {
            numeros[i] = aleatorio.nextInt(1000000);
        }
        return numeros;
    }

    public static void merge_sort_serial(int[] arr, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            merge_sort_serial(arr, inicio, meio);
            merge_sort_serial(arr, meio + 1, fim);
            merge(arr, inicio, meio, fim);
        }
    }

    public static void merge_sort_paralelo(int[] arr, int inicio, int fim) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MergeSortParalelo(arr, inicio, fim));
        pool.shutdown();
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

    public static void escreverCSV(String nomeArquivo, String[] algoritmos, long[] tempos, int tamanho) {
        try {
            FileWriter writer = new FileWriter(nomeArquivo, true); // append true para adicionar ao arquivo existente

            for (int i = 0; i < algoritmos.length; i++) {
                writer.append(tamanho + " elementos," + algoritmos[i] + "," + tempos[i] + "\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class MergeSortParalelo extends RecursiveAction {
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
    }
}
