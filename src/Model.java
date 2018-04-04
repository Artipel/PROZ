import NeuralNetwork.Network;

public class Model {
    private View view;
    private Controller controller;
    public Network network;

    public void setViewAndController(View v,  Controller c){
        view = v;
        controller = c;
    }

    public void CreateNetwork(int in, int com[], int out){
        network = new Network(in, com, out);
    }

    public void showNetwork(){
        network.showNetwork();
    }

    public void showSynaps(){
        double[][][] synaps = network.getSynapsNetwork();
        for(int i = 0; i < synaps.length; ++i)
            for(int j = 0; j < synaps[i].length; ++j)
                for(int k = 0; k < synaps[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " syn:" + synaps[i][j][k]);
    }

    public void showNetworkScheme(){
        int[] scheme = network.getNetworkScheme();
        for (int aScheme : scheme) System.out.println(aScheme);
    }

    public void showPartialDerivatives(double[] in, double[] out){
        double[][][] derivatives = network.getPartialDerivativesForSingleInput(in, out);
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " der:" + derivatives[i][j][k]);
    }

    public void showPartialDerivatives(double[][] in, double[][] out){
        double[][][] derivatives = network.getPartialDerivatives(in, out);
        for(int i = 0; i < derivatives.length; ++i)
            for(int j = 0; j < derivatives[i].length; ++j)
                for(int k = 0; k < derivatives[i][j].length; ++k)
                    System.out.println("i:" + i + " j:" + j + " k:" + k + " der:" + derivatives[i][j][k]);
    }
}
