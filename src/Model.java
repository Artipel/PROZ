import NeuralNetwork.Linear;
import NeuralNetwork.Network;
import NeuralNetwork.RBFNetwork;
import NeuralNetwork.Settings;

import java.util.Vector;

public class Model {
    private View view;
    private Controller controller;
    private Network network;
    private Vector<Double> errors;
    private double[][] trainingDataInput;
    private double[][] trainingDataOutput;

    private double[][] trainingData2D;

    public double[][] getTrainingDataOutput() {
        return trainingDataOutput;
    }

    /**
     * Combines input and output data into a two column array.
     */
    private void createTrainingData2D(){
        if(trainingDataInput[0].length == 1 && trainingDataOutput[0].length == 1) {
            trainingData2D = new double[2][trainingDataInput.length];
            for (int i = 0; i < trainingDataInput.length; i++) {
                trainingData2D[0][i] = trainingDataInput[i][0];
                trainingData2D[1][i] = trainingDataOutput[i][0];
            }
            for (int i = 0; i < trainingData2D[0].length; i++) {
                for (int j = 0; j < trainingData2D[0].length-1-i; j++) {
                    if(trainingData2D[0][j] > trainingData2D[0][j+1]){
                        double temp = trainingData2D[0][j];
                        trainingData2D[0][j] = trainingData2D[0][j+1];
                        trainingData2D[0][j+1] = temp;
                        temp = trainingData2D[1][j];
                        trainingData2D[1][j] = trainingData2D[1][j+1];
                        trainingData2D[1][j+1] = temp;
                    }
                }
            }
            System.out.println("Sorted!");
        }
    }

    public void setTrainingDataOutput(double[][] trainingDataOutput) {
        this.trainingDataOutput = trainingDataOutput;
        if(trainingDataInput != null && trainingDataInput.length == trainingDataOutput.length)
            createTrainingData2D();
    }

    public double[][] getTrainingDataInput() {
        return trainingDataInput;
    }

    public void setTrainingDataInput(double[][] trainingData) {
        this.trainingDataInput = trainingData;
        if(trainingDataOutput != null && trainingDataOutput.length == trainingDataInput.length)
            createTrainingData2D();
    }

    public Network getNetwork() {
        return network;
    }

    public void setViewAndController(View v,  Controller c){
        view = v;
        controller = c;
    }

    /**
     * Creates a neuron network.
     * @param layers number of neurons in each layer
     * @param learningRate influence of a gradient on learning process
     * @param momentum influence of a past gradient on learning process
     */
    public void createNetwork(int[] layers, double learningRate, double momentum){
        errors = new Vector<Double>();
        int[] computing = new int[layers.length-2];
        System.arraycopy(layers, 1, computing, 0, layers.length - 1 - 1);
        Settings settings = new Settings(layers[0], computing, layers[layers.length-1], true, 1, 0, learningRate, momentum);
        network = new Network(settings);
    }

    /**
     * creates neuron network
     * @param in number of neurons in input layer
     * @param com number of neurons in hidden layers
     * @param out number of neurons in output layer
     * @param b if bias is to be included
     * @param type1 type of a function in hidden layer
     * @param type2 type of a function in output layer
     */
    public void createNetwork(int in, int com[], int out, boolean b, int type1, int type2){

        network = new Network(in, com, out, b, type1, type2,0.1,0.1);
    }

    public void createRBFNetwork(int in, int com[], int out, double learningRate, double momentum){
        errors = new Vector<Double>();
        network = new RBFNetwork(in, com[0], out, learningRate, new Linear());
    }

    /**
     * display network values on stdout
     */
    public void showNetwork(){
        network.showNetwork();
    }

    /**
     * display synaps value on stdout
     */
    public void showSynaps(){
        double[][][] synaps = network.getSynapsNetwork();
        for(int i = 0; i < synaps.length; ++i)
            for(int j = 0; j < synaps[i].length; ++j)
                for(int k = 0; k < synaps[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " syn:" + synaps[i][j][k]);
    }

    /**
     * display on stdout number of neurons in each layer
     */
    public void showNetworkScheme(){
        int[] scheme = network.getNetworkScheme();
        for (int aScheme : scheme) System.out.println(aScheme);
    }

    /**
     * Display gradient of error function for training data set
     * @param in input training data set
     * @param out output training data set
     */
    public void showPartialDerivatives(double[] in, double[] out){
        double[][][] derivatives = network.getPartialDerivativesForSingleInput(in, out);
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " der:" + derivatives[i][j][k]);
    }

    /**
     * Display gradient of error function for training data set
     * @param in input training data set
     * @param out output training data set
     */
    public void showPartialDerivatives(double[][] in, double[][] out){
        double[][][] derivatives = network.getPartialDerivatives(in, out);
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " der:" + derivatives[i][j][k]);
    }

    /**
     * Creates array of values of approximation made by the network on the specified interval
     * @param start starting value of approximation
     * @param end ending value of approximation
     * @param probes number of probes between starting and ending value
     * @return 2d array of arguments and values of function
     */
    public double[][] getLinearSpaceApproximation(double start, double end, int probes){
        if(start > end) {
            double temp = start;
            start = end;
            end = temp;
        }
        double[][] values = new double[2][probes];
        double epsilon = (end - start) / probes;
        double inputValue = start;
        for (int i = 0; i < probes; i++) {
            values[0][i] = inputValue;
            values[1][i] = network.getOutput(new double[]{inputValue})[0];
            inputValue += epsilon;
        }
        return values;
    }

    /**
     * Creates array of values approximated by the network. Approximated interval is the same as training input.
     * @param probes number of samples in the interval
     * @return array of approximated values
     */
    public double[][] getLinearSpaceApproximation(int probes) {
        double start = trainingData2D[0][0];
        double end = trainingData2D[0][trainingData2D[0].length-1];
        return getLinearSpaceApproximation(start, end, probes);
    }

    /**
     * Executes one epoch of learning process in created network
     */
    public void learnOneEpochOnline(){
        try{
            network.learnOneEpochOnline(trainingDataInput, trainingDataOutput);
            errors.add(network.getSumOfErrors(trainingDataInput, trainingDataOutput)/trainingDataInput.length);
        } catch (NullPointerException npe) {
            System.out.println("Network not initialized or training data not loaded");
        }
    }

    public double[][] getTrainingData2D(){
        return trainingData2D;
    }

    /**
     *
     * @return array of errors made by the network since it was created
     */
    public double[] getErrors(){
        double[] array = new double[errors.size()];
        for (int i = 0; i < errors.size(); i++) {
            array[i] = errors.elementAt(i);
        }
        return array;
    }

    /**
     * sets training input and training output
     * @param data
     */
    public void setTrainingData(double[][][] data) {
        setTrainingDataInput(data[0]);
        setTrainingDataOutput(data[1]);
    }

    /**
     * Check if network is ready to learn. Is it created and data are loaded
     * @return is network ready to learn
     */
    public boolean isReady(){
        return (trainingDataInput != null && trainingDataOutput != null && network != null);
    }
}
