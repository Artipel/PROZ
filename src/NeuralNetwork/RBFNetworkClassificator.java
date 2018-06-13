package NeuralNetwork;

public class RBFNetworkClassificator extends RBFNetwork {

    boolean individualVariance;

    public RBFNetworkClassificator(Settings settings, double[][] input){
        this(settings.inputN, settings.computingN[0], settings.outputN, settings.learningRate, new Linear(), settings.bias, input);
    }

    public boolean isIndividualVariance() {
        return individualVariance;
    }

    public void setIndividualVariance(boolean individualVariance) {
        this.individualVariance = individualVariance;
    }

    public RBFNetworkClassificator(int input, int computing, int output, double lr, SpecialFunction sf, boolean ind, double[][] trainingInput){
        super(input, computing, output, lr, sf);
        individualVariance = ind;
        double[][] centres = new NeuronGas().findCentres(trainingInput, computing);
        for (int i = 0; i < centres.length; i++) {
            computingNeurons[0][i].setWeights(centres[i]);
        }

    }

    @Override
    protected void createSynaps(SpecialFunction computingNeuronFx, SpecialFunction outputNeuronFx) {
        super.createSynaps(computingNeuronFx, outputNeuronFx);
    }

    public void setCentres(double[][] input){
        NeuronGas gas = new NeuronGas();
        double[][] centers = gas.findCentres(input, computingNeurons[0].length);
        for (int i = 0; i < computingNeurons[0].length; i++) {
            computingNeurons[0][i].setWeights(centers[i]);
        }
    }

    @Override
    public void learnOneEpochOnline(double[][] input, double[][] expectedOutput) {

        //UPDATE CENTERS POSITION//
        double[][] centers = getSynapsNetwork()[0];
        new NeuronGas().moveCentres(centers, input);
        for (int i = 0; i < computingNeurons[0].length; i++) {
            computingNeurons[0][i].setWeights(centers[i]);
        }

        updateVariance();

        //PLACE FOR PERMUTATION//

        //UPDATE WEIGHTS//
        for (int i = 0; i < input.length; i++) {
            double[] output = getOutput(input[i]);
            for (int j = 0; j < outputNeurons.length; j++) {
                for (int k = 0; k < outputNeurons[j].synaps.length; k++) {
                    double correction = getLearningRate() * (expectedOutput[i][j] - output[j]) * outputNeurons[j].sf.derivative(outputNeurons[j].inputsSum)* outputNeurons[j].inputNeurons[k].getOutput();
                    outputNeurons[j].synaps[k] += correction;
                }
            }
        }
    }

    protected void updateVariance(){
        if(individualVariance){
            double[][] centers = getSynapsNetwork()[0];
            for (int i = 0; i < computingNeurons[0].length; i++) {
                double variance = findMinimumDistanceToAnotherCenter(centers, i);
                ((RBFNeuron)computingNeurons[0][i]).setVariance(Math.max(variance, Double.MIN_VALUE));
            }
        }else {
            double variance = findMaximumSquaredDistanceBetweenCentres() / (2 * computingNeurons[0].length);
            for (int i = 0; i < computingNeurons[0].length; i++) {
                ((RBFNeuron) computingNeurons[0][i]).setVariance(variance);
            }
        }
    }

    private double findMinimumDistanceToAnotherCenter(double[][] centers, int i) {
        double minDistance = Double.MAX_VALUE;
        for (int j = 0; j < centers.length; j++) {
            if(j == i)
                continue;
            double distance = 0;
            for (int k = 0; k < centers[j].length; k++) {
                distance += (centers[i][k] - centers[j][k])*(centers[i][k] - centers[j][k]);
            }
            if(distance < minDistance)
                minDistance = distance;
        }
        return minDistance;
    }
}
