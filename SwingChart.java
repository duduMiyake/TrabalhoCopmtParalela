import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwingChart extends JFrame {

    public SwingChart() {
        super("Gráfico de Tempo de Execução");

        // Criar o conjunto de dados
        DefaultCategoryDataset dataset = createDataset();

        // Criar o gráfico
        JFreeChart chart = ChartFactory.createLineChart(
                "Tempo de Execução", // Título do gráfico
                "Tamanho do Array", // Rótulo do eixo x
                "Tempo (ms)", // Rótulo do eixo y
                dataset
        );

        // Adicionar o gráfico a um painel de gráfico
        ChartPanel chartPanel = new ChartPanel(chart);

        // Configurações da janela
        setContentPane(chartPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resultados.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String tamanhoString = parts[0].split(" ")[0]; // Extrai o tamanho do array da primeira parte
                int tamanho = Integer.parseInt(tamanhoString); // Converte o tamanho para um número inteiro
                long tempo = Long.parseLong(parts[2]); // Converte o tempo para um número longo
    
                dataset.addValue(tempo, parts[1], String.valueOf(tamanho)); // Adiciona os valores ao conjunto de dados
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    
        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingChart::new);
    }
}
