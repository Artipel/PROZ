package NeuralNetwork;

public class ResultReceiver extends Neuron {

    public ResultReceiver(Neuron[] inputs){
        sf = new Linear();
        inputNeurons = inputs;
        synaps = new double[inputs.length];
        pickInitialWeights();
    }

    protected double computeOutput(){
        return super.computeOutput();
    }

    public double getOutput(){
        return output;
    }

    protected void pickInitialWeights(){
        super.pickInitialWeights();
    }

}
