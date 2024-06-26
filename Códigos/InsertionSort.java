package Códigos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.SwingUtilities;

public class InsertionSort {

    public static void main(String[] args) {
        int[] tamanhos = {10, 100, 1000, 10000, 100000};
        int[] numeros = gerarNumeros(10000);

        //Usa o swingChart para mostrar o gráfico
        SwingUtilities.invokeLater(() -> new SwingChart("resultados_insertionsort.csv"));

        for (int tamanho : tamanhos) {
            int[] numerosSerial = gerarNumeros(tamanho);
            int[] numerosParalelo = numerosSerial.clone();

            System.out.println("Tamanho do array: " + tamanho);

            // Serial
            long tempoInicialSerial = System.currentTimeMillis();
            insertion_sort_serial(numerosSerial);
            long tempoFinalSerial = System.currentTimeMillis();
            long tempoExecucaoSerial = tempoFinalSerial - tempoInicialSerial;

            System.out.println("Tempo de execução (Serial): " + tempoExecucaoSerial + " milissegundos");

            // Paralelo
            long tempoInicialParalelo = System.currentTimeMillis();
            insertion_sort_paralelo(numerosParalelo);
            long tempoFinalParalelo = System.currentTimeMillis();
            long tempoExecucaoParalelo = tempoFinalParalelo - tempoInicialParalelo;

            System.out.println("Tempo de execução (Paralelo): " + tempoExecucaoParalelo + " milissegundos");

            // Escreve os resultados em um arquivo CSV
            escreverCSV("resultados_insertionsort.csv", new String[]{"Serial", "Paralelo"}, new long[]{tempoExecucaoSerial, tempoExecucaoParalelo}, tamanho);
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

    public static void insertion_sort_serial(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int chave = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > chave) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = chave;
        }
    }

    public static void insertion_sort_paralelo(int[] arr) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new InsertionSortParalelo(arr, 0, arr.length - 1));
        pool.shutdown();
    }

    public static void escreverCSV(String nomeArquivo, String[] algoritmos, long[] tempos, int tamanho) {
        try {
            FileWriter writer = new FileWriter(nomeArquivo, true);

            for (int i = 0; i < algoritmos.length; i++) {
                writer.append(tamanho + " elementos," + algoritmos[i] + "," + tempos[i] + "\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class InsertionSortParalelo extends RecursiveAction {
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
            for (int i = inicio + 1; i <= fim; i++) {
                int chave = array[i];
                int j = i - 1;
                while (j >= inicio && array[j] > chave) {
                    array[j + 1] = array[j];
                    j--;
                }
                array[j + 1] = chave;
            }
        }
    }
}
