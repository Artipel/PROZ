package NeuralNetwork;

import java.util.Random;

/**
 * Neuron with Gausian activation funtion
 */
public class RBFNeuron extends Neuron {

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
        ((Gaussian)sf).setVariance(variance);
    }

    //private double[] centres;
    private double variance;

    public RBFNeuron(double var, Neuron[] inputs){
        inputNeurons = inputs;
        synaps = new double[inputs.length];
        pickInitialWeights();
        variance = var;
        sf = new Gaussian(variance);
    }

    /**
     * Value of a is equal to sum of squares of defferences between input and synaps (centre position)
     * Output is function of a
     */
    @Override
    public void doCalculations() {
        inputsSum = 0;
        for (int i = 0; i < inputNeurons.length; i++) {
            double tempInput = (inputNeurons[i].getOutput() - synaps[i]);
            inputsSum += tempInput * tempInput;
        }
        output = computeOutput();
    }

    @Override
    public double activationDerivative(int index) {
        return -2*(inputNeurons[index].getOutput() - synaps[index]);
    }

    @Override
    protected void pickInitialWeights() {
        Random random = new Random();
        for (int i = 0; i < synaps.length; i++) {
            synaps[i] = (random.nextDouble()-0.5)*8;
        }
    }
}
