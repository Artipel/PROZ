package NeuralNetwork;

import java.util.Random;

public class Neuron {

    protected SpecialFunction sf;

    //protected Neuron[] outputs;
    protected Neuron[] inputNeurons;
    protected double[] synaps;
    //protected int inputs;
    //protected int inputsReceived;
    protected double inputsSum;
    protected double output;
    //protected double lastSumOfInputs;

    /*public Neuron(Neuron[] out, int input){
        sf = new Sigmoidal();
        outputs = out;
        synaps = new double[out.length];
        inputs = input;
        inputsSum = 0.0;
        inputsReceived = 0;
        pickInitialWeights();
    }*/

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

    public void doCalculations(){
        inputsSum = 0;
        for (int i = 0; i < inputNeurons.length; i++) {
            inputsSum += inputNeurons[i].getOutput() * synaps[i];
        }
        output = computeOutput();
    }

    protected void pickInitialWeights(){
        Random random = new Random();
        for (int i = 0; i < synaps.length; ++i) {
            synaps[i] = random.nextDouble()-0.5;
        }
    }

    protected double computeOutput(){
        return sf.function(inputsSum);
    }

    public double getLastSumOfInputs(){
        return inputsSum;
    }

    public double getOutput(){
        return output;
    }

    public void setWeights(double[] weights){
        this.synaps = weights.clone();
    }

}
