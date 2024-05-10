package Quick_sort;
import java.util.Random;

public class QuickSortSerial {
    public static void main(String[] args) {
        int[] numeros = gerarNumeros(10);

        long tempoInicial =  System.currentTimeMillis();
        System.out.println("Antes do Quick Sort serial:");
        imprimir(numeros);

        quick_sort_serial(numeros, 0, numeros.length - 1);

        long tempoFinal = System.currentTimeMillis();

        System.out.println("\nDepois do Quick Sort serial:");
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

    public static void quick_sort_serial(int[] array, int inicio, int fim) {
        if (inicio < fim) {
            int indicePivo = particiona(array, inicio, fim);
            quick_sort_serial(array, inicio, indicePivo - 1);
            quick_sort_serial(array, indicePivo + 1, fim);
        }
    }

    public static int particiona(int[] array, int inicio, int fim) {
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

    public static void imprimir(int[] numeros) {
        for(int numero: numeros) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }
}
