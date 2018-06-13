package NeuralNetwork;

/**
 * Set of parameters that defines network
 */
public class Settings {

    public int inputN, outputN, computingFx, outputFx;
    public boolean bias;
    public int[] computingN;
    public double learningRate, momentum;

    public Settings(int inputN, int[] computingN, int outputN, boolean bias, int computingFx, int outputFx, double learningRate, double momentum){
        this.inputN = inputN;
        this.computingN = computingN.clone();
        this.outputN = outputN;
        this.bias = bias;
        this.computingFx = computingFx;
        this.outputFx = outputFx;
        this.learningRate = learningRate;
        this.momentum = momentum;
    }

}
