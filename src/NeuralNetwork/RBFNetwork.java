package NeuralNetwork;

/**
 * Network with 1 hidden layer, and Gaussian special function. Under development.
 */
public class RBFNetwork extends Network {

    private double sigma;

    /**
     * This is a simplified method for a RBF network with 1 input and 1 output neuron.
     * Advanced example is to be developed in the future
     * @param input set of training inputs. Input must consists of single number
     * @param output set of training outputs. Output must consists of single number
     */
/*    @Override
    public void learnOneEpoch(double[][] input, double[][] output) {
        if(input[0].length != 1 || output[0].length != 1) {
            System.out.println("Wrong input size!");
            return;
        }
        double[] receivedOutput = getOutput(input[0]);
        double[] outputGradient = new double[getNetworkScheme()[2]];
        double[] gradient = new double[getNetworkScheme()[1]];
        for (int i = 0; i < outputGradient.length; i++) {
            //outputGradient[i] = ()
        }
    }
*/

    public RBFNetwork(Settings settings){
        this(settings.inputN, settings.computingN[0], settings.outputN, settings.learningRate, new Linear());
    }
    public RBFNetwork(int input, int[] computing, int output) {
        super(input, computing, output);
    }

    public RBFNetwork(int input, int computing, int output, double learningRate, SpecialFunction outputNeuronFxType){
        inputNeurons = new InputCollector[input];
        computingNeurons = new Neuron[][]{new RBFNeuron[computing], new Neuron[output]};
        outputNeurons = computingNeurons[1];
        setLearningRate(learningRate);
        setMomentum(0.0);
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i] = new InputCollector();
        }
        for (int i = 0; i < computingNeurons[0].length; i++) {
            computingNeurons[0][i] = new RBFNeuron(10.0, inputNeurons);

        }
        for (int i = 0; i < outputNeurons.length; i++) {
            outputNeurons[i] = new Neuron(computingNeurons[0], outputNeuronFxType);
        }
        previousCorrection = getSynapsNetwork();
        for (int i = 0; i < previousCorrection.length; i++) {
            for (int j = 0; j < previousCorrection[i].length; j++) {
                for (int k = 0; k < previousCorrection[i][j].length; k++) {
                    previousCorrection[i][j][k] = 0.0;
                }
            }
        }
    }

    @Override
    public void learnOneEpoch(double[][] input, double[][] output) {
        super.learnOneEpoch(input, output);
        double maxDistance = findMaximumSquaredDistanceBetweenCentres();
        double variance = maxDistance / 2 / computingNeurons[0].length;
        for (int i = 0; i < computingNeurons[0].length; i++) {
            ((RBFNeuron)computingNeurons[0][i]).setVariance(variance);
        }
    }

    @Override
    public void learnOneEpochOnline(double[][] input, double[][] output) {
        super.learnOneEpochOnline(input, output);
        double maxDistance = findMaximumSquaredDistanceBetweenCentres();
        double variance = maxDistance / (2 * computingNeurons[0].length);
        for (int i = 0; i < computingNeurons[0].length; i++) {
            ((RBFNeuron)computingNeurons[0][i]).setVariance(variance);
        }
    }

/*
    public void learnOneEpochOnline(double[][] input, double[][] expectedOutput){
        int[] permutation = super.generatePermutation(input.length);
        for (int i = 0; i < permutation.length; i++) {
            permutation[i] = i;
        }
        for (int i = 0; i < permutation.length; i++) {
            double output = getOutput(input[permutation[i]])[0];
            for (int j = 0; j < computingNeurons[0].length; j++) {
                double outputCorrection = getLearningRate()*(expectedOutput[permutation[i]][0] - output)*computingNeurons[0][j].getOutput();
                outputNeurons[0].synaps[j] += outputCorrection;
                double hiddenCorrection = outputCorrection
                        *outputNeurons[0].synaps[j]/
                        ((RBFNeuron)computingNeurons[0][j]).getVariance()*
                        (input[permutation[i]][0] - computingNeurons[0][j].synaps[0]);
                computingNeurons[0][j].synaps[0] += hiddenCorrection;
            }
        }
        double maxDistance = findMaximumSquaredDistanceBetweenCentres();
        double variance = maxDistance / 2 / computingNeurons[0].length;
        for (int i = 0; i < computingNeurons[0].length; i++) {
            ((RBFNeuron)computingNeurons[0][i]).setVariance(variance);
        }
    }
*/
    protected double findMaximumSquaredDistanceBetweenCentres(){
        double[][] centers = getSynapsNetwork()[0];
        double maxDistance = -Double.MAX_VALUE;
        for (int i = 0; i < centers.length; i++) {
            for (int j = i+1; j < centers.length; j++) {
                if(distanceBetweenPoints(centers[i], centers[j]) > maxDistance)
                    maxDistance = distanceBetweenPoints(centers[i], centers[j]);
            }
        }
        return maxDistance;
        /*

        if(inputNeurons.length == 1){
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (int i = 0; i < computingNeurons[0].length; i++) {
                if(computingNeurons[0][i].synaps[0] < min)
                    min = computingNeurons[0][i].synaps[0];
                if(computingNeurons[0][i].synaps[0] > max)
                    max = computingNeurons[0][i].synaps[0];
            }
            return max - min;
        }
        System.out.println("Not 1 dimensional task. distance not calculated");
        return 1.0;*/
    }

    private double distanceBetweenPoints(double[] firstPoint, double[] secondPoint) {
        double distance = 0;
        for (int i = 0; i < firstPoint.length; i++) {
            distance += (secondPoint[i] - firstPoint[i]) * (secondPoint[i] - firstPoint[i]);
        }
        return distance;
    }
}
