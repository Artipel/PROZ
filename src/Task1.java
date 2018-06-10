import NeuralNetwork.Network;
import NeuralNetwork.Settings;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Task1 {

    public static void main(String[] args) {

        final int sampleSize = 5, numberOfEpochs = 5000;
        final int numberOfTrainingCases = 4;
        final int inputSize = 4;
        final int outputSize = 4;
        final int testSize = 0;
        final double learnignRate = 0.1;
        final double momentum = 0.0;
        final boolean bias = false;
        Settings[] settings = new Settings[3];
        settings[0] = new Settings(inputSize, new int[]{1}, outputSize, bias, 1, 1, learnignRate, momentum);
        settings[1] = new Settings(inputSize, new int[]{2}, outputSize, bias, 1, 1, learnignRate, momentum);
        settings[2] = new Settings(inputSize, new int[]{3}, outputSize, bias, 1, 1, learnignRate, momentum);

        String inputFilename = null, outputFilename = null;
        try{
            inputFilename = args[0];
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("no input filename");
        }
        try {
            outputFilename = args[1] + "Lr" + learnignRate + "Mom" + momentum + "Bias" + bias;
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("no output filename");
        }
        double[][] trainingInput = new double[numberOfTrainingCases][inputSize];
        double[][] expectedTrainingOutput = new double[numberOfTrainingCases][outputSize];
        double[][] testInput = new double[testSize][outputSize];
        FileManager.loadArrays(trainingInput, expectedTrainingOutput, inputFilename, numberOfTrainingCases, inputSize, outputSize);

        Network[] networks = null;
        double[][] errors = new double[numberOfEpochs][2*settings.length];
        double[] errorsForOneEpoch = new double[sampleSize];

        double[] singleOutput;
        int choice;
        double[] numberOfCorrectAnswers = new double[settings.length];


        for(int setNum = 0; setNum < settings.length; ++setNum) {
            networks = createSampleOfNetworks(settings[setNum], sampleSize);
            for (int i = 0; i < numberOfEpochs; i++) {
                for (int j = 0; j < networks.length; j++) {
                    networks[j].learnOneEpochOnline(trainingInput, expectedTrainingOutput);
                    errorsForOneEpoch[j] = networks[j].getAverageError(trainingInput, expectedTrainingOutput);
                }
                errors[i][2*setNum] = Statistics.mean(errorsForOneEpoch);
                errors[i][2*setNum + 1] = Statistics.standardDeviation(errorsForOneEpoch);
            } //Learning has finished
            for (int i = 0; i < networks.length; i++) {
                for (int j = 0; j < expectedTrainingOutput.length; j++) {
                    singleOutput = networks[i].getOutput(trainingInput[j]);
                    choice = classification(singleOutput);
                    if(choice == j)
                        numberOfCorrectAnswers[setNum]++;
                }
            }
            numberOfCorrectAnswers[setNum] /= networks.length;
        }
        FileManager.saveToCSV(errors, numberOfCorrectAnswers, outputFilename);
        FileManager.saveGraph(errors, settings[0], outputFilename);
        double[] output = networks[0].getOutput(new double[]{1.0,0.0,0.0,0.0});
        for (int i = 0; i < output.length; i++) {
            System.out.println(output[i]);
        }

    }

    private static Network[] createSampleOfNetworks(Settings settings, int n){
        Network[] networks = new Network[n];
        for (int i = 0; i < networks.length; i++) {
            networks[i] = new Network(settings);
        }
        return networks;
    }

    //private static double numberOfCorrectClassifications(double[][] expectedOutput, )

    private static int classification(double[] output){
        int candidate = -1;
        for (int i = 0; i < output.length; i++) {
            if(output[i] > 0.5)
                if(candidate == -1)
                    candidate = i;
                else
                    return -1;

        }
        return candidate;
    }
}
