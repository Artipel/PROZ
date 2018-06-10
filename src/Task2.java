import NeuralNetwork.Network;
import NeuralNetwork.Settings;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Task2 {

    public static void main(String[] args) {

        final int sampleSize = 5, numberOfEpochs = 2000;
        final int numberOfTrainingCases = 81;
        final int inputSize = 1;
        final int outputSize = 1;
        final int testSize = 1000;
        final double learnignRate = 0.1;
        final double momentum = 0.0;
        final boolean bias = true;
        Settings[] settings = new Settings[4];
        final int computingFX = 1;
        final int outputFX = 0;
        settings[0] = new Settings(inputSize, new int[]{2}, outputSize, bias, computingFX, outputFX, learnignRate, momentum);
        settings[1] = new Settings(inputSize, new int[]{4}, outputSize, bias, computingFX, outputFX, learnignRate, momentum);
        settings[2] = new Settings(inputSize, new int[]{6}, outputSize, bias, computingFX, outputFX, learnignRate, momentum);
        settings[3] = new Settings(inputSize, new int[]{8}, outputSize, bias, computingFX, outputFX, learnignRate, momentum);

        String inputFilename = null, outputFilename = null, testFilename = null;
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
        testFilename = "data/approximation_test.txt";
        double[][] trainingInput = new double[numberOfTrainingCases][inputSize];
        double[][] expectedTrainingOutput = new double[numberOfTrainingCases][outputSize];
        double[][] testInput = new double[testSize][outputSize];
        double[][] expectedTestOutput = new double[testSize][outputSize];
        FileManager.loadArrays(trainingInput, expectedTrainingOutput, inputFilename, numberOfTrainingCases, inputSize, outputSize);
        FileManager.loadArrays(testInput, expectedTestOutput, testFilename, testSize, inputSize, outputSize);

        Network[] networks = null;
        Network[] champions = new Network[settings.length];
        double[][] errors = new double[numberOfEpochs][settings.length];
        double[][] deviations = new double[numberOfEpochs][settings.length];
        double[][] testErrors = new double[numberOfEpochs][settings.length];
        double[][] testDeviations = new double[numberOfEpochs][settings.length];
        double[] errorsForOneEpoch = new double[sampleSize];
        double[] testErrorsForOneEpoch = new double[sampleSize];

        double[] singleOutput;

        for(int setNum = 0; setNum < settings.length; ++setNum) {
            networks = createSampleOfNetworks(settings[setNum], sampleSize);
            for (int i = 0; i < numberOfEpochs; i++) {
                for (int j = 0; j < networks.length; j++) {
                    networks[j].learnOneEpochOnline(trainingInput, expectedTrainingOutput);
                    errorsForOneEpoch[j] = networks[j].getAverageError(trainingInput, expectedTrainingOutput);
                    testErrorsForOneEpoch[j] = networks[j].getAverageError(testInput, expectedTestOutput);
                }
                errors[i][setNum] = Statistics.mean(errorsForOneEpoch);
                deviations[i][setNum] = Statistics.standardDeviation(errorsForOneEpoch);
                testErrors[i][setNum] = Statistics.mean(testErrorsForOneEpoch);
                testDeviations[i][setNum] = Statistics.standardDeviation(testErrorsForOneEpoch);
                if(i % 500 == 0)
                    System.out.println("Learning... Epoch: " + i + " set num: " + setNum);
            } //Learning for one set has finished
            double min = 1000;
            int champion = -1;
            double tempError;
            for (int i = 0; i < networks.length; i++) {
                tempError = networks[i].getAverageError(trainingInput, expectedTrainingOutput);
                if(tempError < min){
                    min = tempError;
                    champion = i;
                }
            }
            champions[setNum] = networks[champion];
        }
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        XYSeries s = null;


        for (int i = 0; i < settings.length; i++) {
            s = new XYSeries("" + settings[i].computingN[0] + " neurons");
            for (int j = 0; j < testSize; j++) {
                s.add(testInput[j][0], champions[i].getOutput(testInput[j])[0]);
            }
            seriesCollection.addSeries(s);
        }
        s = new XYSeries("Model");
        for (int i = 0; i < testSize; i++) {
            s.add(testInput[i][0], expectedTestOutput[i][0]);
        }
        seriesCollection.addSeries(s);
        FileManager.saveToCSV(errors, outputFilename);
        FileManager.saveGraph(errors, deviations, settings, outputFilename, 3.0);
        FileManager.saveGraph(testErrors, testDeviations, settings, outputFilename + "TEST",3.0);
        FileManager.saveApproximationGraph(seriesCollection, outputFilename + "PREVIEW");
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
