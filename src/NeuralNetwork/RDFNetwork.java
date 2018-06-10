package NeuralNetwork;

public class RDFNetwork extends Network {

    private double sigma;

    /**
     * This is a simplified method for a RDF network with 1 input and 1 output neuron.
     * Advanced example is to be developed in the future
     * @param input set of training inputs. Input must consists of single number
     * @param output set of training outputs. Output must consists of single number
     */
    @Override
    public void learnOneEpoch(double[][] input, double[][] output) {
        if(input[0].length != 1 || output[0].length != 1) {
            System.out.println("Wrong input size!");
            return;
        }
        double[] gradient = new double[getNetworkScheme()[1]];
        //for (int i = 0; i < ; i++) {
            //double[] receivedOutput = getOutput(input[i]);


        //}
    }

    public RDFNetwork(int input, int[] computing, int output) {
        super(input, computing, output);
    }


}
