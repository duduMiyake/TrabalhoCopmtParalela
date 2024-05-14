package Códigos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.SwingUtilities;

public class BubbleSort {

    public static void main(String[] args) {
        int[] tamanhos = {10, 100, 1000, 10000, 100000};
        int[] numeros = gerarNumeros(10000);

        //Usa o swingChart para mostrar o gráfico
        SwingUtilities.invokeLater(() -> new SwingChart("resultados_bubblesort.csv"));

        for (int tamanho : tamanhos) {
            int[] numerosSerial = gerarNumeros(tamanho);
            int[] numerosParalelo = numerosSerial.clone();

            System.out.println("Tamanho do array: " + tamanho);

            // Serial
            long tempoInicialSerial = System.currentTimeMillis();
            bubbleSort_serial(numerosSerial);
            long tempoFinalSerial = System.currentTimeMillis();
            long tempoExecucaoSerial = tempoFinalSerial - tempoInicialSerial;

            System.out.println("Tempo de execução (Serial): " + tempoExecucaoSerial + " milissegundos");

            // Paralelo
            long tempoInicialParalelo = System.currentTimeMillis();
            bubbleSort_paralelo(numerosParalelo);
            long tempoFinalParalelo = System.currentTimeMillis();
            long tempoExecucaoParalelo = tempoFinalParalelo - tempoInicialParalelo;

            System.out.println("Tempo de execução (Paralelo): " + tempoExecucaoParalelo + " milissegundos");

            // Escreve os resultados em um arquivo CSV
            escreverCSV("resultados_bubblesort.csv", new String[]{"Serial", "Paralelo"}, new long[]{tempoExecucaoSerial, tempoExecucaoParalelo}, tamanho);
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

    public static void bubbleSort_serial(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Troca arr[j] e arr[j+1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void bubbleSort_paralelo(int[] arr) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new BubbleSortParalelo(arr, 0, arr.length - 1));
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

    static class BubbleSortParalelo extends RecursiveAction {
        private final int[] array;
        private final int inicio;
        private final int fim;

        public BubbleSortParalelo(int[] array, int inicio, int fim) {
            this.array = array;
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        protected void compute() {
            for (int i = inicio; i < fim; i++) {
                for (int j = inicio; j < fim - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        // Troca array[j] e array[j+1]
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                }
            }
        }
    }
}
