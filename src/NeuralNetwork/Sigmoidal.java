package NeuralNetwork;

/**
 * Function: 1/(1+e^x)
 */
public class Sigmoidal extends SpecialFunction {

    public double function(double a){
        return 1.0/(1+Math.pow(Math.E, -a));
    }

    public double derivative(double a){
        return Math.pow(Math.E, -a)/(Math.pow(1+Math.pow(Math.E, -a), 2));
    }
}
