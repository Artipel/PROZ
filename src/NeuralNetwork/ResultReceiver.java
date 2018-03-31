package NeuralNetwork;

public class ResultReceiver extends Neuron {

    public ResultReceiver(int inputCount){
        inputs = inputCount;
        inputsReceived = 0;
        inputsSum = 0.0;
    }

    public void receiveInput(double d){
        ++inputsReceived;
        inputsSum =+ d;
        if(inputsReceived == inputs){
            output = computeOutput();
            inputsReceived = 0;
        }
    }

    protected double computeOutput(){
        return super.computeOutput();
    }

    public double getOutput(){
        return output;
    }

}
