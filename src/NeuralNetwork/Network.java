package NeuralNetwork;

import java.util.Random;


public class Network {

    protected Neuron[] inputNeurons;
    protected Neuron[] outputNeurons;
    protected Neuron[][] computingNeurons;

    public double getLearningRate() {
        return learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    private double learningRate = 0.1;
    private double momentum = 0.1;
    protected double[][][] previousCorrection;

    private int bias;

    public Network() {
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    /**
     * Creates perceptron with defined number of neurons on input layer, hidden layers are described by the array, output layer is described by the number.
     * In hidden layer - sigmoidal fx in output layer linear fx
     * @param input number of input neurons
     * @param computing array of numbers of neurons in hidden array (excluding output neurons)
     * @param output number of neurons in output layer
     */
    public Network(int input, int computing[], int output){
        bias = 1;
        inputNeurons = new InputCollector[input];
        computingNeurons = new Neuron[computing.length+1][];
        for(int i = 0; i < computing.length; ++i){
            computingNeurons[i] = new Neuron[computing[i]];
        }
        computingNeurons[computingNeurons.length-1] = new Neuron[output];
        outputNeurons = computingNeurons[computingNeurons.length-1];
        createSynaps(new Sigmoidal(), new Linear());
    }

    /**
     * creates perceptron with defined number of neurons and defined type of a activation function
     * @param input number of neurons in input layer
     * @param computing number of neurons in hidden layer described by an array
     * @param output number of neurons in output layer
     * @param computingNeuronsFxType type of activation function in hidden layer, 1 == sigmoidal else == linear
     * @param outputNeuronsFxType type of activationi finction in output layer 1 == sigmoidal else == linear
     */
    public Network(int input, int computing[], int output, int computingNeuronsFxType, int outputNeuronsFxType){
        bias = 1;
        inputNeurons = new InputCollector[input + bias];
        computingNeurons = new Neuron[computing.length+1][];
        for(int i = 0; i < computing.length; ++i){
            computingNeurons[i] = new Neuron[computing[i]];
        }
        computingNeurons[computingNeurons.length-1] = new Neuron[output];
        outputNeurons = computingNeurons[computingNeurons.length-1];
        SpecialFunction computingNeuronsFx, outputNeuronsFx;
        if(computingNeuronsFxType == 1)
            computingNeuronsFx = new Sigmoidal();
        else
            computingNeuronsFx = new Linear();

        if(outputNeuronsFxType == 1)
            outputNeuronsFx = new Sigmoidal();
        else
            outputNeuronsFx = new Linear();
        createSynaps(computingNeuronsFx, outputNeuronsFx);
    }

    /**
     * creates perceptron with without synaps. Number of neurons in layers are defined by single array
     * @param neurons array of number of neurons in layers
     */

    public Network(int[] neurons){

    }

    /**
     * Creates perceptron with set of parameters defined in the object settings.
     * @param settings set of arameters which defines a perceptron
     */
    public Network(Settings settings){
        this(settings.inputN, settings.computingN, settings.outputN, settings.bias, settings.computingFx, settings.outputFx, settings.learningRate, settings.momentum);
    }

    /**
     * creates perceptron fully defined by the set of parameters
     * @param input number of input neurons
     * @param computing numbers f neurons in hidden layers
     * @param output number of neurons in output layer
     * @param bias sould network use bias
     * @param computingNeuronsFxType type of a function in hidden layer 1==sigmoidal else==linear
     * @param outputNeuronsFxType type of a function in output layer 1==sigmoidal else==linear
     * @param learningRate learning rate value
     * @param momentum momentum value
     */
    public Network(int input, int computing[], int output, boolean bias, int computingNeuronsFxType, int outputNeuronsFxType, double learningRate, double momentum){
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.bias = bias ? 1 : 0;
        inputNeurons = new InputCollector[input + this.bias];
        computingNeurons = new Neuron[computing.length+1][];
        for(int i = 0; i < computing.length; ++i){
            computingNeurons[i] = new Neuron[computing[i]];
        }
        computingNeurons[computingNeurons.length-1] = new Neuron[output];
        outputNeurons = computingNeurons[computingNeurons.length-1];

        SpecialFunction computingNeuronsFx, outputNeuronsFx;
        if(computingNeuronsFxType == 1)
            computingNeuronsFx = new Sigmoidal();
        else
            computingNeuronsFx = new Linear();

        if(outputNeuronsFxType == 1)
            outputNeuronsFx = new Sigmoidal();
        else
            outputNeuronsFx = new Linear();

        createSynaps(computingNeuronsFx, outputNeuronsFx);
    }

    /**
     * creates synaps between all neurons to neurons from prevoius layer.
     * @param computingNeuronFx type of a function in hidden layer
     * @param outputNeuronFx type of a function in output layer
     */
    protected void createSynaps(SpecialFunction computingNeuronFx, SpecialFunction outputNeuronFx){
        if(bias==0) {
            for (int j = 0; j < inputNeurons.length; j++) {
                inputNeurons[j] = new InputCollector();
            }
        }else{
            int j;
            for (j = 0; j < inputNeurons.length-1; j++) {
                inputNeurons[j] = new InputCollector();
            }
            inputNeurons[j] = new BiasNode();
        }
        for(int j = 0; j < computingNeurons[0].length; ++j){
            computingNeurons[0][j] = new Neuron(inputNeurons, computingNeuronFx);
        }
        if(bias == 0) {
            for (int i = 1; i < computingNeurons.length - 1; i++) {
                for (int j = 0; j < computingNeurons[i].length; j++) {
                    computingNeurons[i][j] = new Neuron(computingNeurons[i - 1], computingNeuronFx);
                }
            }
            for (int j = 0; j < computingNeurons[computingNeurons.length - 1].length; ++j) {
                computingNeurons[computingNeurons.length - 1][j] = new Neuron(computingNeurons[computingNeurons.length - 2], outputNeuronFx);
            }
        }else{
            int i;
            for (i = 1; i < computingNeurons.length - 1; i++) {
                Neuron[] inputsArr = new Neuron[computingNeurons[i-1].length+1];
                for (int k = 0; k < inputsArr.length-1; k++) {
                    inputsArr[k] = computingNeurons[i-1][k];
                }
                inputsArr[inputsArr.length-1] = inputNeurons[inputNeurons.length-1];
                for (int j = 0; j < computingNeurons[i].length; j++) {
                    computingNeurons[i][j] = new Neuron(inputsArr, computingNeuronFx);
                }
            }
            Neuron[] inputsArr = new Neuron[computingNeurons[i-1].length+1];
            for (int k = 0; k < inputsArr.length-1; ++k) {
                inputsArr[k] = computingNeurons[i-1][k];
            }
            inputsArr[inputsArr.length-1] = inputNeurons[inputNeurons.length-1];
            for (int j = 0; j < computingNeurons[i].length; ++j) {
                computingNeurons[computingNeurons.length - 1][j] = new Neuron(inputsArr, outputNeuronFx);
            }
        }
        previousCorrection = getSynapsNetwork();
        for (int i = 0; i < previousCorrection.length; i++) {
            for (int j = 0; j < previousCorrection[i].length; j++) {
                for (int k = 0; k < previousCorrection[i][j].length; k++) {
                    previousCorrection[i][j][k] = 0.0;
                }
            }
        }
    }

    /**
     * Propagates data through the network and return an output
     * @param input single set of data
     * @return array of output
     */
    public double[] getOutput(double[] input)  {
        if (input.length != inputNeurons.length - bias){
            System.out.println("Mismatch in input size");
            return new double[]{0.0};
        }
        for(int i = 0; i < input.length; ++i){
            inputNeurons[i].receiveInput(input[i]);
        }
        if(bias == 1)
            inputNeurons[inputNeurons.length-1].receiveInput(1.0);
        for (int i = 0; i < computingNeurons.length; i++) {
            for (int j = 0; j < computingNeurons[i].length; j++) {
                computingNeurons[i][j].doCalculations();
            }
        }
        double[] result = new double[outputNeurons.length];
        for(int i = 0; i < outputNeurons.length; ++i){
            result[i] = outputNeurons[i].getOutput();
        }
        return result;
    }

    /**
     * Prints on std out number of neurons in each layer
     */
    public void showNetwork(){
        System.out.println("Input neurons: " + inputNeurons.length);
        for(Neuron[] o : computingNeurons) {
            System.out.println("Computing neurons: " + o.length);
        }
    }

    /**
     * calculates sum of errors for a single input on every output neuron. It is done by propagating data by hte fx @getOutput(double[] input)
     * @param inputs single array of input data.
     * @param expectedOutputs single array of output which is expected to be given by this input
     * @return sum of errors
     */
    public double assessSingleError(double[] inputs, double[] expectedOutputs){
        double sumOfErrors = 0;
        double[] receivedOutput = getOutput(inputs);
        for(int i = 0; i < receivedOutput.length; ++i){
            sumOfErrors += (receivedOutput[i] - expectedOutputs[i])*(receivedOutput[i] - expectedOutputs[i]);
        }
        return sumOfErrors/2;
    }

    /**
     * Calculates sum of errors for multiple input and output data.
     * @param trainingInput set of input arrays.
     * @param trainingOutput set of exped output array.
     * @return sum of errors mane on every neuron in the output layer in every training example
     */
    public double getSumOfErrors(double[][] trainingInput, double[][] trainingOutput){
        double sumOfErrors = 0;
        for (int i = 0; i < trainingInput.length; i++) {
            sumOfErrors += assessSingleError(trainingInput[i], trainingOutput[i]);
        }
        return sumOfErrors;
    }

    /**
     * calculates MSE for multiple training data
     * @param trainingInput set of input arrays.
     * @param trainingOutput set of expedted output arrays.
     * @return Mean sum of errors
     */
    public double getAverageError(double[][] trainingInput, double[][] trainingOutput){
        return getSumOfErrors(trainingInput, trainingOutput)/trainingInput.length*2;
    }

    /**
     * Returns a 3 dimensional array of synaps weight. this synaps are not references to object, just a trivial double type.
     * @return 3d array of double with synaps weights double[i][j][k] i - layer j - number of destination neuron in ith layer,
     * k - number of source neuron from (i-1)th layer
     */
    public double[][][] getSynapsNetwork(){
        double[][][] allComputingSynaps = new double[computingNeurons.length][][];
        for(int i = 0; i < computingNeurons.length; ++i){
            allComputingSynaps[i] = new double[computingNeurons[i].length][];
            for(int j = 0; j < computingNeurons[i].length; ++j){
                allComputingSynaps[i][j] = computingNeurons[i][j].synaps.clone();
            }
        }
        return allComputingSynaps;
    }

    /**
     * returns array with numbers of neurons in each layer
     * @return array of ints that defines number of neuron in each layer
     */
    public int[] getNetworkScheme(){
        int[] scheme = new int[computingNeurons.length + 1];
        scheme[0] = inputNeurons.length;
        for(int i = 0; i < computingNeurons.length; ++i){
            scheme[i+1] = computingNeurons[i].length;
        }
        return scheme;
    }

    //derivatives[i][j][k] i - layer number, j - neuron number in ith layer, k - input number in jth neuron in ith layer

    /**
     * propagates data, checks error value, calculates delta parameter and returns a gradient of error function with respect to synaps w_i_j_k
     * where i is a number of layer
     * j is a number of neuron which is a destination
     * k is a neuron which is a source of a synaps and is located in previous layer
     * @param inputs single array of input
     * @param expectedOutputs single array of expected output for this input
     * @return gradient of error function
     */
    public double[][][] getPartialDerivativesForSingleInput(double[] inputs, double[] expectedOutputs){
        getOutput(inputs);
        double[][] correctionCoefficients = getCorrectionCoefficientArray(expectedOutputs);
        double[][][] derivatives = new double[computingNeurons.length][][]; //getSynapsNetwork();
        for (int i = 0; i < derivatives.length; i++) {
            derivatives[i] = new double[computingNeurons[i].length][];
            for (int j = 0; j < derivatives[i].length; j++) {
                derivatives[i][j] = new double[computingNeurons[i][j].inputNeurons.length];
            }
        }
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    derivatives[i][j][k] = correctionCoefficients[i][j] * computingNeurons[i][j].activationDerivative(k);
        return derivatives;
    }

    public double[][][] getPartialDerivatives(double[] inputs, double[] expectedOutputs){
        getOutput(inputs);
        double[][] correctionCoefficients = getCorrectionCoefficientArray(expectedOutputs);
        double[][][] derivatives = new double[computingNeurons.length][][]; //getSynapsNetwork();
        for (int i = 0; i < derivatives.length; i++) {
            derivatives[i] = new double[computingNeurons[i].length][];
            for (int j = 0; j < derivatives[i].length; j++) {
                derivatives[i][j] = new double[computingNeurons[i][j].inputNeurons.length];
            }
        }
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    derivatives[i][j][k] = correctionCoefficients[i][j] * computingNeurons[i][j].activationDerivative(k);
        return derivatives;
    }


    /**
     * Partial derivative for multiple input and output. Applicable to offline learning method.
     * It is using loop with single inputs.
     * @param trainingInput
     * @param trainingOutput
     * @return
     */
    public double[][][] getPartialDerivatives(double[][] trainingInput, double[][] trainingOutput){
        double[][][] partialDerivatives = new double[computingNeurons.length][][];
        for (int i = 0; i < computingNeurons.length; i++) {
            partialDerivatives[i] = new double[computingNeurons[i].length][];
            for (int j = 0; j < computingNeurons[i].length; j++) {
                partialDerivatives[i][j] = new double[computingNeurons[i][j].inputNeurons.length];
            }
        }
        //getPartialDerivativesForSingleInput(trainingInput[0], trainingOutput[0]);
        for (int i = 0; i < trainingInput.length; i++) {
            double[][][] singleDerivative = getPartialDerivativesForSingleInput(trainingInput[i], trainingOutput[i]);
            for (int j = 0; j < partialDerivatives.length; j++) {
                for (int k = 0; k < partialDerivatives[j].length; k++) {
                    for (int l = 0; l < partialDerivatives[j][k].length; l++) {
                        partialDerivatives[j][k][l] += singleDerivative[j][k][l];
                    }
                }
            }
        }
        return partialDerivatives;
    }

    /**
     * Creates a 2d array for delta correction parameter value. Data must be previously propagated through the network!
     * @param expectedResults single array of expected output for input previously propagated
     * @return 2d array of delta correction coefficient
     */
    private double[][] getCorrectionCoefficientArray(double[] expectedResults){
        double[][] coefficientArray = new double[computingNeurons.length][];
        for(int i = 0; i < coefficientArray.length; ++i)
            coefficientArray[i] = new double[computingNeurons[i].length];
        for(int i = 0; i < coefficientArray[coefficientArray.length-1].length; ++i){
            Neuron currentNeuron = outputNeurons[i];
            coefficientArray[coefficientArray.length-1][i] = (currentNeuron.getOutput() - expectedResults[i])*currentNeuron.sf.derivative(currentNeuron.inputsSum);
        }

        double[][][] synaps = getSynapsNetwork();

        for(int i = coefficientArray.length-2; i >= 0; --i){
            for(int j = 0; j < coefficientArray[i].length; ++j){
                for(int k = 0; k < computingNeurons[i+1].length; ++k){
                    coefficientArray[i][j] += coefficientArray[i+1][k]*synaps[i+1][k][j];
                }
                coefficientArray[i][j] *= computingNeurons[i][j].sf.derivative(computingNeurons[i][j].inputsSum);
            }
        }
        return coefficientArray;
    }

    /**
     * Set weight of every synaps in network. i - layer j - destination k - source
     * @param weights that are to be set in the network
     */
    public void setWeights(double[][][] weights){
        for(int i = 0; i < computingNeurons.length; ++i)
            for(int j = 0; j < computingNeurons[i].length; ++j)
                computingNeurons[i][j].setWeights(weights[i][j]);

    }

    /**
     * offline learning for one epoch. Propagates all training inputs, compares to expected output and introduces corrections
     * @param input set of training inputs
     * @param output set of training outputs
     */
    public void learnOneEpoch(double[][] input, double[][] output){
        double[][][] gradient = getPartialDerivatives(input, output);
        double[][][] synaps = getSynapsNetwork();
        makeCorrection(gradient, synaps);
        setWeights(synaps);
    }

    /**
     * changes value of weights in a 3d array given as argument according to learning formula.
     * It does not change value of synaps itself, only value in the array.
     * Weights must be set up later.
     * @param gradient value of sum of errors
     * @param synaps array of all synaps
     */
    private void makeCorrection(double[][][] gradient, double[][][] synaps) {
        for (int i = 0; i < synaps.length; i++) {
            for (int j = 0; j < synaps[i].length; j++) {
                for (int k = 0; k < synaps[i][j].length; k++) {
                    synaps[i][j][k] = synaps[i][j][k] - (gradient[i][j][k] * learningRate) + previousCorrection[i][j][k] * momentum;
                    previousCorrection[i][j][k] =  - (gradient[i][j][k] * learningRate) + previousCorrection[i][j][k] * momentum;
                }
            }
        }
    }

    /**
     * Learns for a given number of epochs with online methods. Training inputs are permutated each epoch.
     * learOneEpochOnline is used to learn
     * @param input set of training inputs
     * @param output set of expected training outputs
     * @param numberOfEpochs
     */
    public void learnOnline(double[][] input, double[][] output, int numberOfEpochs){
        for (int i = 0; i < numberOfEpochs; i++) {
            int[] permutation = generatePermutation(input.length);
            double[][] permutedInput = new double[input.length][];
            double[][] permutedOutput = new double[output.length][];
            for (int j = 0; j < permutation.length; j++) {
                permutedInput[j] = input[permutation[j]];
                permutedOutput[j] = output[permutation[j]];
            }
            learnOneEpochOnline(permutedInput, permutedOutput);
        }
    }

    /**
     * one epoch of online learning. Corrections are introduced after each training case not whole set of them
     * @param input set of training inputs
     * @param output set of expected training outputs
     */
    public void learnOneEpochOnline(double[][] input, double[][] output){
        for (int trainingCase = 0; trainingCase < input.length; trainingCase++) {
            double[][][] gradient = getPartialDerivatives(input[trainingCase], output[trainingCase]);
            double[][][] synaps = getSynapsNetwork();
            makeCorrection(gradient, synaps);
            setWeights(synaps);
        }
    }

    public void learnOneEpochOnlinePermuted(double[][] input, double[][] output){
        int[] permutation = generatePermutation(input.length);
        for (int trainingCase = 0; trainingCase < input.length; trainingCase++) {
            double[][][] gradient = getPartialDerivatives(input[permutation[trainingCase]], output[permutation[trainingCase]]);
            double[][][] synaps = getSynapsNetwork();
            makeCorrection(gradient, synaps);
            setWeights(synaps);
        }
    }

    /**
     * learns  offline as long as sum of errors is not lower then given value
     * @param input set of training inputs
     * @param output set of expected training outputs
     * @param desiredError
     */
    public void learnAsLongAsPossible(double[][] input, double[][] output, double desiredError){
        double error = getSumOfErrors(input, output);
        while(error > desiredError) {
            learnOneEpoch(input, output);
            error = getSumOfErrors(input, output);
        }
    }

    /**
     * Offline learning for given number of epochs
     * @param input set of training inputs
     * @param output set of expected training outputs
     * @param n number of epochs to learn
     */
    public void learnForEpochs(double[][] input, double[][] output, int n){
        while(n-->0)
            learnOneEpoch(input, output);
    }

    /**
     * Learns offline for given number of epochs or stops when desired error is achived
     * @param input set of training inputs
     * @param output set of expected training outputs
     * @param n desired number of epochs to learn
     * @param desiredError desired error
     */
    public void learnForEpochs(double[][] input, double[][] output, int n, double desiredError){
        while(n-->0 && getSumOfErrors(input, output) > desiredError)
            learnOneEpoch(input, output);
    }

    /**
     * permutates set of n numbers
     * @param n the biggest number in a set starting from 0.
     * @return permutated array of integers
     */
    protected int[] generatePermutation(int n){
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        Random random = new Random();
        int temp;
        for (int i = 0; i < n; i++) {
            int r = random.nextInt(n-i);
            temp = array[array.length-1-i];
            array[array.length-1-i] = array[r];
            array[r] = temp;
        }
        return array;
    }

    public boolean getBias() {
        return bias > 0;
    }
}