package NeuralNetwork;

/**
 * Function which calculates output of a neuron
 */
public abstract class SpecialFunction  {

    public SpecialFunction(){}

    /**
     * Value of a function
     * @param a
     * @return
     */
    public abstract double function(double a);

    /**
     * Derivatiev of a special function
     * @param a
     * @return
     */
    public abstract double derivative(double a);

}
