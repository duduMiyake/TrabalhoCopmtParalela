package Insertion_sort;
import java.util.Random;

public class InsertionSortSerial {
    public static void main(String[] args) {
        int[] numeros = gerarNumeros(10);

        System.out.println("Antes do insertion sort serial:");
        imprimir(numeros);

        long tempoInicial =  System.currentTimeMillis();
        insertion_sort_serial(numeros);
        long tempoFinal = System.currentTimeMillis();

        System.out.println("Depois do insertion sort serial:");
        imprimir(numeros);

        long tempoExecucao = tempoFinal - tempoInicial;
        System.out.println("\nTempo de execução: " + tempoExecucao + " milissegundos");
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
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int chave = arr[i];
            int j = i - 1;
            
            while (j >= 0 && arr[j] > chave) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = chave;
        }
    }

    public static void imprimir(int[] numeros) {
        for (int numero : numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}
