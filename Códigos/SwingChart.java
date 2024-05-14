package Códigos;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SwingChart extends JFrame {

    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    public SwingChart(String nomeArquivo) {
        super("Gráfico de Tempo de Execução");

        dataset = createDataset(nomeArquivo);

        JFreeChart chart = ChartFactory.createLineChart(
                "Tempo de Execução", // Título do gráfico
                "Tamanho do Array", // Rótulo do eixo x
                "Tempo (ms)", // Rótulo do eixo y
                dataset
        );

        chartPanel = new ChartPanel(chart);

        setContentPane(chartPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Configurar o timer para atualizar o gráfico a cada 100ms (0.1 segundos)
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataset = createDataset(nomeArquivo);
                // Atualizar o gráfico
                chart.getCategoryPlot().setDataset(dataset);
            }
        });
        timer.start(); 
    }

    private DefaultCategoryDataset createDataset(String nomeArquivo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));
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
        SwingUtilities.invokeLater(() -> new SwingChart("resultados_mergesort.csv"));
    }
}
