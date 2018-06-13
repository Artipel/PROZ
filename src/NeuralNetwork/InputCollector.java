package NeuralNetwork;

/**
 * Neuron that passes input to output without changes
 */
public class InputCollector extends Neuron {

    public InputCollector(){
        sf = new Linear();
    }

    public void receiveInput(double d){
        output = d;
    }
}
