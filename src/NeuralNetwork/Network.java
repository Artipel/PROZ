package NeuralNetwork;


public class Network {

    private Neuron[] inputNeurons;
    private Neuron[] outputNeurons;
    private Neuron[][] computingNeurons;

    public Network(int input, int computing[], int output){
        inputNeurons = new InputCollector[input];
        outputNeurons = new ResultReceiver[output];
        computingNeurons = new Neuron[computing.length][];
        for(int i = 0; i < computing.length; ++i){
            computingNeurons[i] = new Neuron[computing[i]];
        }
        createSynaps();
        inputNeurons[0].receiveInput(1.0);
        System.out.println(outputNeurons[0].getOutput());
    }

    private void createSynaps(){
        for(int i = 0; i < outputNeurons.length; ++i){
            outputNeurons[i] = new ResultReceiver(computingNeurons[computingNeurons.length-1].length);
        }

        for(int i = 0; i < computingNeurons[computingNeurons.length-1].length; ++i){
            if(computingNeurons.length > 1)
                computingNeurons[computingNeurons.length-1][i] = new Neuron(outputNeurons, computingNeurons[computingNeurons.length-2].length);
            else
                computingNeurons[computingNeurons.length-1][i] = new Neuron(outputNeurons, inputNeurons.length);
        }

        for(int i = computingNeurons.length-2; i > 0; --i){
            for(int j = 0; j < computingNeurons[i].length; ++j){
                computingNeurons[i][j] = new Neuron(computingNeurons[i+1], computingNeurons[i-1].length);
            }
        }

        if(computingNeurons.length > 1)
            for(int i = 0; i < computingNeurons[0].length; ++i){
                computingNeurons[0][i] = new Neuron(computingNeurons[1], inputNeurons.length);
            }

        for(int i = 0; i < inputNeurons.length; ++i){
            inputNeurons[i] = new InputCollector(computingNeurons[0]);
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
        System.out.println("Output neurons: " + outputNeurons.length);
    }

    public double assessSingleError(double[] inputs, double[] expectedOutputs){
        double sumOfErrors = 0;
        double[] receivedOutput = getOutput(inputs);
        for(int i = 0; i < receivedOutput.length; ++i){
            sumOfErrors += (receivedOutput[i] - expectedOutputs[i])*(receivedOutput[i] - expectedOutputs[i]);
        }
        return sumOfErrors/2;
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
        int[] scheme = new int[computingNeurons.length + 2];
        scheme[0] = inputNeurons.length;
        for(int i = 0; i < computingNeurons.length; ++i){
            scheme[i+1] = computingNeurons[i].length;
        }
        scheme[scheme.length-1] = outputNeurons.length;
        return scheme;
    }

    private double[][] getCorrecionCoefficientArray(double[] expectedResults){
        double[][] coefficentArray = new double[computingNeurons.length][];
        for(int i = 0; i < coefficentArray.length; ++i)
            coefficentArray[i] = new double[computingNeurons[i].length];

        for(int i = 0; i < coefficentArray[coefficentArray.length-1].length; ++i){
            Neuron currentNeuron = computingNeurons[coefficentArray.length-1][i];
            coefficentArray[coefficentArray.length-1][i] = (currentNeuron.getOutput() - expectedResults[i])*SpecialFunction.derivative(currentNeuron.lastSumOfInputs);
        }

        return coefficentArray;
    }

}
