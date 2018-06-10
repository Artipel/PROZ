package NeuralNetwork;

public class RDFNeuron extends Neuron {

    //private double[] centres;
    private double variance;

    public RDFNeuron(){
        sf = new Gaussian(variance);
    }

    @Override
    public void doCalculations() {
        inputsSum = 0;
        for (int i = 0; i < inputNeurons.length; i++) {
            double tempInput = (inputNeurons[i].getOutput() - synaps[i]);
            inputsSum += tempInput * tempInput;
        }
        computeOutput();
    }
}
