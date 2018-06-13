package NeuralNetwork;

/**
 * Gaussian normal distribution as a special function
 */
public class Gaussian extends SpecialFunction {

    double variance;

    public Gaussian(double var){
        variance = var;
    }

    public void setVariance(double var){
        variance = var;
    }

    /**
     * Calculate normal distribution value for parameter
     * @param a argument
     * @return value of normal distribution
     */
    @Override
    public double function(double a) {
        return Math.pow(Math.E, -a/(2*variance));
    }

    /**
     * Derivative of nrmal distribution at point a
     * @param a point
     * @return value of derivative
     */
    @Override
    public double derivative(double a) {
        return -function(a) * 1/(2*variance);
    }
}
