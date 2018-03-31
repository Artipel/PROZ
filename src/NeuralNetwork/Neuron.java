package NeuralNetwork;

import java.util.Random;

public class Neuron {

    protected Neuron[] outputs;
    protected double[] synaps;
    protected int inputs;
    protected int inputsReceived;
    protected double inputsSum;
    protected double output;
    protected double lastSumOfInputs;

    public Neuron(Neuron[] out, int input){
        outputs = out;
        synaps = new double[out.length];
        inputs = input;
        inputsSum = 0.0;
        inputsReceived = 0;
        pickInitialWeights();
    }

    public Neuron() {
    }

    public void receiveInput(double input){
        ++inputsReceived;
        inputsSum+=input;
        if(inputsReceived == inputs){
            output = computeOutput();
            for(int i = 0; i < outputs.length; ++i){
                outputs[i].receiveInput(output * synaps[i]);
            }
            lastSumOfInputs = inputsSum;
            inputsReceived = 0;
            inputsSum = 0;
        }
    }

    private void pickInitialWeights(){
        Random random = new Random();
        for (int i = 0; i < synaps.length; ++i) {
            synaps[i] = random.nextDouble();
        }
    }

    protected double computeOutput(){
        return SpecialFunction.function(inputsSum);
    }

    public double getLastSumOfInputs(){
        return lastSumOfInputs;
    }

    public double getOutput(){
        return output;
    }

}
