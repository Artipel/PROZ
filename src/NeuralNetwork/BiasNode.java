package NeuralNetwork;

/**
 * A node with constant output equal to 1
 */
public class BiasNode extends InputCollector {

    /**
     * Output is always equal 1
     * @param d omitted parameter, needed to override
     */
    public void receiveInput(double d){
        output = 1.0;
    }

}
