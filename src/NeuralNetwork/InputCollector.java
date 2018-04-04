package NeuralNetwork;

public class InputCollector extends Neuron {

    public InputCollector(){
        sf = new Linear();
    }

    public void receiveInput(double d){
        output = d;
    }
}
