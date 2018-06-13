package NeuralNetwork;

/**
 * Linear special funtion. Output is equal to sum of inputs.
 */
public class Linear extends SpecialFunction {

    public double function(double a){
        return a;
    }

    public double derivative(double a){
        return 1.0;
    }

}
