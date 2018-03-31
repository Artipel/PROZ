package NeuralNetwork;

public class InputCollector extends Neuron {

    public InputCollector(){

    }

    public InputCollector(Neuron[] outputs){
        this.outputs = outputs;
    }

    public void receiveInput(double d){
        for(Neuron n : outputs){
            n.receiveInput(d);
        }
    }
}
