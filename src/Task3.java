import NeuralNetwork.Network;
import NeuralNetwork.Settings;

public class Task3 {
    public static void main(String[] args) {

        final int sampleSize = 5, numberOfEpochs = 1000;
        final int numberOfTrainingCases = 90;
        final int inputSize = 2;
        final int outputSize = 3;
        final int testSize = 93;
        final double learnignRate = 0.1;
        final double momentum = 0.1;
        final boolean bias = true;
        Settings[] settings = new Settings[4];
        settings[0] = new Settings(inputSize, new int[]{4}, outputSize, bias, 1, 1, learnignRate, momentum);
        settings[1] = new Settings(inputSize, new int[]{8}, outputSize, bias, 1, 1, learnignRate, momentum);
        settings[2] = new Settings(inputSize, new int[]{12}, outputSize, bias, 1, 1, learnignRate, momentum);
        settings[3] = new Settings(inputSize, new int[]{16}, outputSize, bias, 1, 1, learnignRate, momentum);

        String codeS = "1100";
        boolean[] code = new boolean[4];
        for (int i = 0; i < code.length; i++) {
            if(codeS.charAt(i) == '1')
                code[i] = true;
            else
                code[i] = false;
        }
        String inputFilename = "data/classification_train.txt",
                outputFilename = "data/classification/classification"+codeS,
                testFilename = "data/classification_test.txt";

        double[][] trainingInput = new double[numberOfTrainingCases][inputSize];
        double[][] expectedTrainingOutput = new double[numberOfTrainingCases][outputSize];
        double[][] testInput = new double[testSize][inputSize];
        double[][] expectedTestOutput = new double[testSize][outputSize];
        FileManager.loadArrays1ofN(trainingInput, expectedTrainingOutput, inputFilename, numberOfTrainingCases, inputSize, outputSize, code);
        FileManager.loadArrays1ofN(testInput, expectedTestOutput, testFilename, testSize, inputSize, outputSize, code);

        Network[] networks = null;
        double[][] errors = new double[numberOfEpochs][settings.length];
        double[][] deviations = new double[numberOfEpochs][settings.length];
        double[][] testErrors = new double[numberOfEpochs][settings.length];
        double[][] testDeviations = new double[numberOfEpochs][settings.length];
        double[] errorsForOneEpoch = new double[sampleSize];
        double[] testErrorsForOneEpoch = new double[sampleSize];

        double[] singleOutput;
        int choice;
        double[] numberOfCorrectAnswers = new double[settings.length];
        double[] deviationNumberOfCorrectAnswers = new double[settings.length];
        double[] numberOfCorrectAnswersForOneSample = new double[sampleSize];

        double[][] classificationMatrix = new double[3][3];
        int[] typeNumber = new int[3];


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
            } //Learning has finished
            for (int i = 0; i < sampleSize; i++) {
                for (int j = 0; j < expectedTestOutput.length; j++) {
                    singleOutput = networks[i].getOutput(testInput[j]);
                    choice = classification(singleOutput);
                    classificationMatrix[classification(expectedTestOutput[j])][choice]++;
                    typeNumber[classification(expectedTestOutput[j])]++;
                    if(choice == classification(expectedTestOutput[j]))
                        numberOfCorrectAnswersForOneSample[i]++;
                }
            }
            for (int i = 0; i < numberOfCorrectAnswersForOneSample.length; i++) {
                numberOfCorrectAnswersForOneSample[i] /= expectedTestOutput.length;
            }

            numberOfCorrectAnswers[setNum] = Statistics.mean(numberOfCorrectAnswersForOneSample);
            deviationNumberOfCorrectAnswers[setNum] = Statistics.standardDeviation(numberOfCorrectAnswersForOneSample);
            numberOfCorrectAnswersForOneSample = new double[sampleSize];
        }
        for (int i = 0; i < classificationMatrix.length; i++) {
            for (int j = 0; j < classificationMatrix[i].length; j++) {
                classificationMatrix[i][j] /= typeNumber[i];
            }
        }
        //FileManager.saveToCSV(errors, numberOfCorrectAnswers, outputFilename);
        //FileManager.saveGraph(errors, settings[0], outputFilename);
        for (int i = 0; i < classificationMatrix.length; i++) {
            System.out.println(classificationMatrix[i][0] + " " + classificationMatrix[i][1] + " " + classificationMatrix[i][2]);
        }

        //FileManager.saveGraph(errors, deviations, settings, outputFilename, 1.0);
        //FileManager.saveGraph(testErrors, testDeviations, settings, outputFilename + "TEST",1.0);
        //FileManager.saveBarGraph(numberOfCorrectAnswers, deviationNumberOfCorrectAnswers, settings, outputFilename + "ACC",1.0);


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
        double max = -1;
        int candidate = -1;
        for (int i = 0; i < output.length; i++) {
            if(output[i] > max) {
                max = output[i];
                candidate = i;
            }
        }
        return candidate;
    }

}
