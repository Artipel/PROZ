package NeuralNetwork;


public class Network {

    private Neuron[] inputNeurons;
    private Neuron[] outputNeurons;
    private Neuron[][] computingNeurons;

    public Network(int input, int computing[], int output){
        inputNeurons = new InputCollector[input];
        computingNeurons = new Neuron[computing.length+1][];
        for(int i = 0; i < computing.length; ++i){
            computingNeurons[i] = new Neuron[computing[i]];
        }
        computingNeurons[computingNeurons.length-1] = new Neuron[output];
        outputNeurons = computingNeurons[computingNeurons.length-1];
        createSynaps();
    }

    private void createSynaps(){
        for (int j = 0; j < inputNeurons.length; j++) {
            inputNeurons[j] = new InputCollector();
        }
        for(int j = 0; j < computingNeurons[0].length; ++j){
            computingNeurons[0][j] = new Neuron(inputNeurons, new Sigmoidal());
        }
        for (int i = 1; i < computingNeurons.length-1; i++) {
            for (int j = 0; j < computingNeurons[i].length; j++) {
                computingNeurons[i][j] = new Neuron(computingNeurons[i-1], new Sigmoidal());
            }
        }
        for(int j = 0; j < computingNeurons[computingNeurons.length-1].length; ++j){
            computingNeurons[computingNeurons.length-1][j] = new Neuron(computingNeurons[computingNeurons.length-2], new Linear());
        }
    }

    public double[] getOutput(double[] input)  {
        if (input.length != inputNeurons.length){
            System.out.println("Mismatch in input size");
            return new double[]{0.0};
        }
        for(int i = 0; i < inputNeurons.length; ++i){
            inputNeurons[i].receiveInput(input[i]);
        }
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

    public void showNetwork(){
        System.out.println("Input neurons: " + inputNeurons.length);
        for(Neuron[] o : computingNeurons) {
            System.out.println("Computing neurons: " + o.length);
        }
    }

    public double assessSingleError(double[] inputs, double[] expectedOutputs){
        double sumOfErrors = 0;
        double[] receivedOutput = getOutput(inputs);
        for(int i = 0; i < receivedOutput.length; ++i){
            sumOfErrors += (receivedOutput[i] - expectedOutputs[i])*(receivedOutput[i] - expectedOutputs[i]);
        }
        return sumOfErrors/2;
    }

    public double getSumOfErrors(double[][] trainingInput, double[][] trainingOutput){
        double sumOfErrors = 0;
        for (int i = 0; i < trainingInput.length; i++) {
            sumOfErrors += assessSingleError(trainingInput[i], trainingOutput[i]);
        }
        return sumOfErrors;
    }

    public double[][][] getSynapsNetwork(){
        double[][][] allComputingSynaps = new double[computingNeurons.length][][];
        for(int i = 0; i < computingNeurons.length; ++i){
            allComputingSynaps[i] = new double[computingNeurons[i].length][];
            for(int j = 0; j < computingNeurons[i].length; ++j){
                allComputingSynaps[i][j] = computingNeurons[i][j].synaps;
            }
        }
        return allComputingSynaps;
    }

    public int[] getNetworkScheme(){
        int[] scheme = new int[computingNeurons.length + 1];
        scheme[0] = inputNeurons.length;
        for(int i = 0; i < computingNeurons.length; ++i){
            scheme[i+1] = computingNeurons[i].length;
        }
        return scheme;
    }

    //derivatives[i][j][k] i - layer number, j - neuron number in ith layer, k - input number in jth neuron in ith layer
    public double[][][] getPartialDerivativesForSingleInput(double[] inputs, double[] expectedOutputs){
        double[] receivedOutput = getOutput(inputs);
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
                    derivatives[i][j][k] = correctionCoefficients[i][j] * computingNeurons[i][0].inputNeurons[k].getOutput();
        return derivatives;
    }

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

    public void setWeights(double[][][] weights){
        for(int i = 0; i < computingNeurons.length; ++i)
            for(int j = 0; j < computingNeurons[i].length; ++j)
                computingNeurons[i][j].setWeights(weights[i][j]);
    }

    private void normalizeGradient(double[][][] gradient){
        double sumOfSquares = 0;
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                for (int k = 0; k < gradient[i][j].length; k++) {
                    sumOfSquares += gradient[i][j][k] * gradient[i][j][k];
                }
            }
        }
        sumOfSquares = Math.sqrt(sumOfSquares);
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                for (int k = 0; k < gradient[i][j].length; k++) {
                    gradient[i][j][k] /= sumOfSquares;
                }
            }
        }
    }

    public void learnOneEpoch(double[][] input, double[][] output){
        //System.out.println("Error before epoch: " + getSumOfErrors(input, output));
        double[][][] gradient = getPartialDerivatives(input, output);
        normalizeGradient(gradient);
        double[][][] synaps = getSynapsNetwork();
        double[][][] synapsBackup = new double[synaps.length][][];
        for (int i = 0; i < synaps.length; i++) {
            synapsBackup[i] = new double[synaps[i].length][];
            for (int j = 0; j < synaps[i].length; j++) {
                synapsBackup[i][j] = new double[synaps[i][j].length];
                for (int k = 0; k < synaps[i][j].length; k++) {
                    synapsBackup[i][j][k] = synaps[i][j][k];
                }
            }
        }
        double ratio = (Math.sqrt(5) - 1.0)/2.0;
        double a = 0.0;
        double b = 100.0;
        double xL = b - ratio * (b - a);
        double xR = a + ratio * (b - a);
        double epsilon = 0.01;
        double xLvalue, xRvalue;
        while((b-a) > epsilon){ //Use xLValue and xRvalue retyping.
            for (int i = 0; i < synaps.length; i++) {
                for (int j = 0; j < synaps[i].length; j++) {
                    for (int k = 0; k < synaps[i][j].length; k++) {
                        synaps[i][j][k] = synapsBackup[i][j][k] - (gradient[i][j][k] * xL);
                    }
                }
            }
            xLvalue = getSumOfErrors(input, output);
            for (int i = 0; i < synaps.length; i++) {
                for (int j = 0; j < synaps[i].length; j++) {
                    for (int k = 0; k < synaps[i][j].length; k++) {
                        synaps[i][j][k] = synapsBackup[i][j][k] - (gradient[i][j][k] * xR);
                    }
                }
            }
            xRvalue = getSumOfErrors(input, output);
            if(xLvalue < xRvalue){
                b = xR;
                xR = xL;
                xL = b - ratio * (b - a);
            }
            else
            {
                a = xL;
                xL = xR;
                xR = a + ratio * ( b - a );
            }
        }
        for (int i = 0; i < synaps.length; i++) {
            for (int j = 0; j < synaps[i].length; j++) {
                for (int k = 0; k < synaps[i][j].length; k++) {
                    synaps[i][j][k] = synapsBackup[i][j][k] - (gradient[i][j][k] * (a + b) / 2.0);
                }
            }
        }
        //setWeights(synaps);
        //System.out.println("Error after epoch: " + getSumOfErrors(input, output));
    }
}