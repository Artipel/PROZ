package NeuralNetwork;

public class SpecialFunction {

    public SpecialFunction(){}

    public static double function(double a){
        return 1.0/(1+Math.pow(Math.E, -a));
    }

    public static double derivative(double a){
        return Math.pow(Math.E, -a)/(Math.pow(1+Math.pow(Math.E, -a), 2));
    }

}
