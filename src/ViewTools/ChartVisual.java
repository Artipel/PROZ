package ViewTools;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

public class ChartVisual {
    private JFreeChart chart;

    public JFreeChart getChart() {
        return chart;
    }

    public void createChart() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] data = new double[2][3];
        data[0][0] = 1;
        data[1][0] = 2;
        data[0][1] = 5;
        data[1][1] = 6;
        data[0][2] = 3;
        data[1][2] = 4;
        dataset.addSeries(1, data);
        chart = ChartFactory.createXYLineChart("Chart", "XAxis", "YAxis", dataset);

    }

    public void createChart(String title, String x, String y, double[][] values, double[][] expectedValues) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Approximation", values);
        dataset.addSeries("Expected", expectedValues);
        chart = ChartFactory.createXYLineChart(title, x, y, dataset);
    }

    public void createErrorsChart(double[] errors, int numberOfEpochs) {
        double[][] values = new double[2][errors.length];
        for (int i = 0; i < values[0].length; i++) {
            values[0][i] = i;
        }
        values[1] = errors;
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Errors", values);
        chart = ChartFactory.createXYLineChart("Mean square error", "Epoch", "MSE", dataset);
        //chart.getXYPlot().getDomainAxis().setRange(0.0, numberOfEpochs);
        //chart.getXYPlot().getRangeAxis().setRange(0.0, 0.1);
    }
}