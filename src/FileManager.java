import NeuralNetwork.Settings;
import javafx.scene.chart.BarChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.xy.*;

import java.awt.*;
import java.io.*;

public class FileManager {
    /**
     * Loads to given array values from file. Requires number of lines
     * @param first first array of destinatino
     * @param second array of destination
     * @param filename file to be open and read
     * @param numberOfLines in a file to be read
     * @param inputSize number of number in single input
     * @param outputSize number of numbers in single output
     */
    public static void loadArrays(double[][] first, double[][] second, String filename, int numberOfLines, int inputSize, int outputSize){

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] inputStrings = sCurrentLine.split(" ");
                for (int j = 0; j < first[i].length; j++) {
                    first[i][j] = Double.parseDouble(inputStrings[j]);
                }
                for (int j = 0; j < second[i].length; j++) {
                    second[i][j] = Double.parseDouble(inputStrings[j+inputSize]);
                }
                ++i;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    /**
     * Creates a pair of 2D arrays with data read from file. Doesnt require number of lines
     * @param filename file to be open and read
     * @param inputSize number of number in single input
     * @param outputSize number of numbers in single output
     * @return
     */
    public static double[][][] loadArrays(String filename, int inputSize, int outputSize){

        java.util.Vector<Double[]> firstVector = new java.util.Vector();
        java.util.Vector<Double[]> secondVector = new java.util.Vector();
        BufferedReader br = null;
        FileReader fr = null;
        double[][] first = new double[0][];
        double[][] second = new double[0][];

        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] inputStrings = sCurrentLine.split(" ");
                Double[] temp = new Double[inputSize];

                for (int j = 0; j < inputSize; j++) {
                    temp[j] = Double.parseDouble(inputStrings[j]);
                }
                Double[] temp2 = new Double[outputSize];
                for (int j = 0; j < outputSize; j++) {
                    temp2[j] = Double.parseDouble(inputStrings[j+inputSize]);
                }
                firstVector.add(temp);
                secondVector.add(temp2);
                ++i;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();
                //double[][][] array = new double[2][][];
                first = new double[firstVector.size()][inputSize];
                second = new double[secondVector.size()][outputSize];
                for (int i = 0; i < firstVector.size(); i++)
                    for (int j = 0; j < inputSize; j++) {
                        first[i][j] = firstVector.elementAt(i)[j];
                    }
                for (int i = 0; i < secondVector.size(); i++) {
                    for (int j = 0; j < outputSize; j++) {
                        second[i][j] = secondVector.elementAt(i)[j];
                    }
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
        return new double[][][]{first, second};
    }

    public static void loadArrays1ofN(double[][] first, double[][] second, String filename, int numberOfLines, int inputSize, int outputSize){

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] inputStrings = sCurrentLine.split(" ");
                int j;
                for (j = 0; j < first[i].length; j++) {
                    first[i][j] = Double.parseDouble(inputStrings[j]);
                }
                for (; j < inputStrings.length; j++) {
                    second[i] = code1ofN(Integer.parseInt(inputStrings[j]), 3);
                }
                ++i;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    public static void loadArrays1ofN(double[][] first, double[][] second, String filename, int numberOfLines, int inputSize, int outputSize, boolean[] features){

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] inputStrings = sCurrentLine.split(" ");
                int j, b = 0;
                for (j = 0; j < 4; j++) {
                    if(features[j] == true)
                        first[i][b++] = Double.parseDouble(inputStrings[j]);
                }
                for (; j < inputStrings.length; j++) {
                    second[i] = code1ofN(Integer.parseInt(inputStrings[j]), 3);
                }
                ++i;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    /**
     * Generates code 1 of N. creates array of double with all zeros except of 1 given number which is equal to one
     * @param i place for 1. Counts from 1...
     * @param n length of a code.
     * @return
     */
    private static double[] code1ofN(int i, int n){
        double[] arr = new double[n];
        for (int j = 0; j < arr.length; j++) {
            arr[j] = 0.0;
        }
        arr[i-1] = 1.0;
        return arr;
    }

    /**
     * Saves predefined chart to file
     * @param errors MSE values
     * @param settings description of neuron network
     * @param filename output filename
     */
    public static void saveGraph(double[][] errors, Settings settings, String filename){

        XYIntervalSeriesCollection sCollection = new XYIntervalSeriesCollection();
        XYIntervalSeries s1 = null;
        String[] names = new String[]{"1 neuron", "2 neurons", "3 neurons"};

        for (int j = 0; j < errors[0].length; j+=2) {
            s1 = new XYIntervalSeries(names[j/2]);
            for (int i = 0; i < errors.length; i++) {
                s1.add((double)i, (double)i, (double)i, errors[i][j], errors[i][j]-errors[i][j+1],errors[i][j]+errors[i][j+1]);
            }
            sCollection.addSeries(s1);
        }
        JFreeChart chart = ChartFactory.createXYLineChart("MSE vs time, learning rate: " + settings.learningRate + " momentum: " + settings.momentum + " bias: " + settings.bias, "Epoch","MSE", sCollection);
        DeviationRenderer devRenderer = new DeviationRenderer();
        Stroke stroke = new BasicStroke(2.0f);
        Color[] colors = new Color[]{Color.red, Color.GREEN, Color.BLUE};

        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesShapesVisible(i, false);
            devRenderer.setSeriesStroke(i, stroke);
        }
        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesStroke(i, stroke);
            devRenderer.setSeriesPaint(i, colors[i]);
            devRenderer.setSeriesFillPaint(i, colors[i]);
        }
        chart.getXYPlot().setRenderer(devRenderer);
        chart.getXYPlot().setBackgroundPaint(new Color(0.9f, 0.9f, 0.9f));

        chart.getXYPlot().getDomainAxis().setRange(0.0, 5000.0);
        chart.getXYPlot().getRangeAxis().setRange(0.0, 1.0);

        try {

            OutputStream out = new FileOutputStream(filename + ".png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    600,
                    400);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Saves predefined chart with interval renderer
     * @param errors value of mse
     * @param deviations standard deviations of errors
     * @param settings description of network
     * @param filename output filename
     * @param rangeLimit limit of a y axis
     */
    public static void saveGraph(double[][] errors, double[][] deviations, Settings[] settings, String filename, double rangeLimit){

        XYIntervalSeriesCollection sCollection = new XYIntervalSeriesCollection();
        XYIntervalSeries s1 = null;
        String[] names = new String[errors[0].length];
        for (int i = 0; i < names.length; i++) {
            names[i] = "" + settings[i].computingN[0] + " neurons";
        }
        for (int j = 0; j < errors[0].length; ++j) {
            s1 = new XYIntervalSeries(names[j]);
            for (int i = 0; i < errors.length; i++) {
                s1.add((double)i, (double)i, (double)i, errors[i][j], errors[i][j]-deviations[i][j],errors[i][j]+deviations[i][j]);
            }
            sCollection.addSeries(s1);
        }
        JFreeChart chart = ChartFactory.createXYLineChart("learning rate: " + settings[0].learningRate + " momentum: " + settings[0].momentum + " bias: " + settings[0].bias, "Epoch","MSE", sCollection);
        DeviationRenderer devRenderer = new DeviationRenderer();
        Stroke stroke = new BasicStroke(2.0f);
        Color[] colors = new Color[]{Color.red, Color.GREEN, Color.BLUE, Color.orange};

        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesShapesVisible(i, false);
            devRenderer.setSeriesStroke(i, stroke);
        }
        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesStroke(i, stroke);
            devRenderer.setSeriesPaint(i, colors[i]);
            devRenderer.setSeriesFillPaint(i, colors[i]);
        }
        chart.getXYPlot().setRenderer(devRenderer);
        chart.getXYPlot().setBackgroundPaint(new Color(0.9f, 0.9f, 0.9f));

        chart.getXYPlot().getDomainAxis().setRange(0.0, (double)errors.length);
        chart.getXYPlot().getRangeAxis().setRange(0.0, rangeLimit);

        try {

            OutputStream out = new FileOutputStream(filename + ".png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    400,
                    300);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void saveBarGraph(double[] accuracy, double[] deviations, Settings[] settings, String filename, double range){

        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        for (int i = 0; i < accuracy.length; i++) {
            dataset.add((Number) accuracy[i], (Number) deviations[i], 0, ""+settings[i].computingN[0] + " neurons");
            System.out.println("Accuracy i: " + accuracy[i]);
        }
        //DefaultCategoryDataset data = new DefaultCategoryDataset();

        //data.addValue((Number)0.1, 0, 1);

        JFreeChart chart = ChartFactory.createBarChart("Accuracy", "Number of neurons", "Accuracy", dataset);
        chart.getCategoryPlot().getRangeAxis().setRange(0.75, range);

        try {

            OutputStream out = new FileOutputStream(filename + "ind_"+settings[0].bias+".png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    400,
                    300);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void saveGraph1(double[][] errors, double[][] deviations, Settings[] settings, String filename, double rangeLimit){

        XYIntervalSeriesCollection sCollection = new XYIntervalSeriesCollection();
        XYIntervalSeries s1 = null;
        String[] names = new String[errors[0].length];
        for (int i = 0; i < names.length; i++) {
            names[i] = "LR: " + settings[i].learningRate + " momentum: " + settings[i].momentum;
        }
        for (int j = 0; j < errors[0].length; ++j) {
            s1 = new XYIntervalSeries(names[j]);
            for (int i = 0; i < errors.length; i++) {
                s1.add((double)i, (double)i, (double)i, errors[i][j], errors[i][j]-deviations[i][j],errors[i][j]+deviations[i][j]);
            }
            sCollection.addSeries(s1);
        }
        JFreeChart chart = ChartFactory.createXYLineChart("Porownanie tempa uczenia. 4 neurony ukryte.", "Epoch","MSE", sCollection);
        DeviationRenderer devRenderer = new DeviationRenderer();
        Stroke stroke = new BasicStroke(2.0f);
        Color[] colors = new Color[]{Color.red, Color.GREEN, Color.BLUE, Color.orange};

        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesShapesVisible(i, false);
            devRenderer.setSeriesStroke(i, stroke);
        }
        for (int i = 0; i < sCollection.getSeriesCount(); i++) {
            devRenderer.setSeriesStroke(i, stroke);
            devRenderer.setSeriesPaint(i, colors[i]);
            devRenderer.setSeriesFillPaint(i, colors[i]);
        }
        chart.getXYPlot().setRenderer(devRenderer);
        chart.getXYPlot().setBackgroundPaint(new Color(0.9f, 0.9f, 0.9f));

        chart.getXYPlot().getDomainAxis().setRange(0.0, (double)errors.length);
        chart.getXYPlot().getRangeAxis().setRange(0.0, rangeLimit);

        try {

            OutputStream out = new FileOutputStream(filename + ".png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    600,
                    400);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Saves two arrays to csv file separeted by |
     * @param array of double to be saved
     * @param secondArray array as a header
     * @param filename output filename
     */
    public static void saveToCSV(double[][] array, double[] secondArray, String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename + ".csv");

            String line ="Classification:";
            for (int i = 0; i < secondArray.length; i++) {
                line += "|";
                line += secondArray[i];
            }
            out.println(line);
            out.println();
            line = "";
            for (int i = 0; i < array.length; i++) {
                line = String.valueOf(i);
                for (int j = 0; j < array[i].length; j++) {
                    line += "|";
                    line += array[i][j];
                }
                out.println(line);
            }

        }catch (Exception e){
            System.out.println("Output file failed to open");
            System.out.println(e);
        }finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex){
                System.out.println(ex);
            }
        }
    }

    /**
     * Saves array to csv separated by |
     * @param array to be saved
     * @param filename output filename
     */
    public static void saveToCSV(double[][] array, String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename);

            System.out.println("Array at the end: " + array[array.length-1][0]);
            String line = "";
            for (int i = 0; i < array.length; i++) {
                line = String.valueOf(i);
                for (int j = 0; j < array[i].length; j++) {
                    line += "|";
                    line += array[i][j];
                }
                out.println(line);
            }

        }catch (Exception e){
            System.out.println("Output file failed to open");
            System.out.println(e);
        }finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex){
                System.out.println(ex);
            }
        }
    }

    /**
     * Saves predefined chart for approximation
     * @param seriesCollection data seris to be drawn on a chart
     * @param s filename
     */
    public static void saveApproximationGraph(XYSeriesCollection seriesCollection, String s) {
        JFreeChart chart = ChartFactory.createXYLineChart("aproksymacja funkcji przez najlepsze z sieci", "x", "y", seriesCollection);
        XYItemRenderer renderer = chart.getXYPlot().getRenderer();

        Color[] colors = new Color[]{Color.red, Color.green, Color.blue, Color.orange, Color.BLACK};

        for (int i = 0; i < seriesCollection.getSeriesCount(); i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }
        chart.getXYPlot().setRenderer(renderer);
        chart.getXYPlot().setBackgroundPaint(new Color(0.9f, 0.9f,0.9f));
        try {

            OutputStream out = new FileOutputStream(s + ".png");
            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    600,
                    400);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}