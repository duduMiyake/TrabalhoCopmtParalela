package Códigos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.SwingUtilities;

public class QuickSort{

    public static void main(String[] args) {
        int[] tamanhos = {10, 100, 1000, 10000, 100000};
        int[] numeros = gerarNumeros(10000);

        //Usa o swingChart para mostrar o gráfico
        SwingUtilities.invokeLater(() -> new SwingChart("resultados_quicksort.csv"));

        for (int tamanho : tamanhos) {
            int[] numerosSerial = gerarNumeros(tamanho);
            int[] numerosParalelo = numerosSerial.clone();

            System.out.println("Tamanho do array: " + tamanho);

            // Serial
            long tempoInicialSerial = System.currentTimeMillis();
            quicksort_serial(numerosSerial, 0, numerosSerial.length - 1);
            long tempoFinalSerial = System.currentTimeMillis();
            long tempoExecucaoSerial = tempoFinalSerial - tempoInicialSerial;

            System.out.println("Tempo de execução (Serial): " + tempoExecucaoSerial + " milissegundos");

            // Paralelo
            long tempoInicialParalelo = System.currentTimeMillis();
            quicksort_paralelo(numerosParalelo, 0, numerosParalelo.length - 1);
            long tempoFinalParalelo = System.currentTimeMillis();
            long tempoExecucaoParalelo = tempoFinalParalelo - tempoInicialParalelo;

            System.out.println("Tempo de execução (Paralelo): " + tempoExecucaoParalelo + " milissegundos");

            // Escreve os resultados em um arquivo CSV
            escreverCSV("resultados_quicksort.csv", new String[]{"Serial", "Paralelo"}, new long[]{tempoExecucaoSerial, tempoExecucaoParalelo}, tamanho);
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

    public static void quicksort_serial(int[] arr, int inicio, int fim) {
        if (inicio < fim) {
            int indiceParticao = particionar(arr, inicio, fim);
            quicksort_serial(arr, inicio, indiceParticao - 1);
            quicksort_serial(arr, indiceParticao + 1, fim);
        }
    }

    public static void quicksort_paralelo(int[] arr, int inicio, int fim) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new QuicksortParalelo(arr, inicio, fim));
        pool.shutdown();
    }

    public static int particionar(int[] arr, int inicio, int fim) {
        int pivo = arr[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (arr[j] < pivo) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[fim];
        arr[fim] = temp;
        return i + 1;
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

    static class QuicksortParalelo extends RecursiveAction {
        private final int[] array;
        private final int inicio;
        private final int fim;

        public QuicksortParalelo(int[] array, int inicio, int fim) {
            this.array = array;
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        protected void compute() {
            if (inicio < fim) {
                int indiceParticao = particionar(array, inicio, fim);

                QuicksortParalelo leftTask = new QuicksortParalelo(array, inicio, indiceParticao - 1);
                QuicksortParalelo rightTask = new QuicksortParalelo(array, indiceParticao + 1, fim);

                invokeAll(leftTask, rightTask);
            }
        }

        private int particionar(int[] arr, int inicio, int fim) {
            int pivo = arr[fim];
            int i = inicio - 1;
            for (int j = inicio; j < fim; j++) {
                if (arr[j] < pivo) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[fim];
            arr[fim] = temp;
            return i + 1;
        }
    }
}
