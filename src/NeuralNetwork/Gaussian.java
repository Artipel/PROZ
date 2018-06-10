package NeuralNetwork;

public class Gaussian extends SpecialFunction {

    double variance;

    public Gaussian(double var){
        variance = var;
    }

    public void setVariance(double var){
        variance = var;
    }

    @Override
    public double function(double a) {
        return Math.pow(Math.E, -a/(2*variance));
    }

    @Override
    public double derivative(double a) {
        return function(a) * 1/(2*variance);
    }
}
