package NeuralNetwork;

import java.util.Random;

public class Neuron {

    protected SpecialFunction sf;

    protected Neuron[] inputNeurons;
    protected double[] synaps;
    protected double inputsSum;
    protected double output;


    public Neuron(Neuron[] inputNeurons, SpecialFunction specialFunction){
        sf = specialFunction;
        this.inputNeurons = inputNeurons;
        synaps = new double[inputNeurons.length];
        pickInitialWeights();
    }

    public Neuron() {
        sf = new Sigmoidal();
    }

    public void receiveInput(double input){
        inputsSum += input;
    }

    /**
     * Calculates value of a parameter, sum of input, invokes activation function and saves in local variable output
     */
    public void doCalculations(){
        inputsSum = 0;
        for (int i = 0; i < inputNeurons.length; i++) {
            inputsSum += inputNeurons[i].getOutput() * synaps[i];
        }
        output = computeOutput();
    }

    /**
     * initializes weights of synaps,
     * In a random way
     */
    protected void pickInitialWeights(){
        Random random = new Random();
        for (int i = 0; i < synaps.length; ++i) {
            synaps[i] = (random.nextDouble()-0.5);
        }
    }

    /**
     * invoke activation function on input sum value
     * @return value of activation function
     */
    protected double computeOutput(){
        return sf.function(inputsSum);
    }

    public double getOutput(){
        return output;
    }

    /**
     * Set synaps weights
     * @param weights of a synaps to be set
     */
    public void setWeights(double[] weights){
        this.synaps = weights.clone();
    }

    public double activationDerivative(int index){
        return inputNeurons[index].getOutput();
    }

}
