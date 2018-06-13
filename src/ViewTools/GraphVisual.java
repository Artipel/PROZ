package ViewTools;

import NeuralNetwork.Network;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

/**
 * Creates a graph that represents network
 */
public class GraphVisual {
    /**
     * Graph must be prior created with method
     * @return a mxGraph of a network
     */
    public mxGraph getGraph() {
        return graph;
    }

    private mxGraph graph;
    private double[][][] synaps;
    private Object[][][] edgesSynaps;
    private double[][] neurons;
    private Object[][] verticesNeurons;
    private int[] scheme;
    private boolean bias;
    private double maxWeight;
    private double minWeight;

    private int vertexx = 30;
    private int vertexy = vertexx;
    private int width = 400;
    private int height = 300;
    private String shape = "shape=ellipse";

    /**
     * Initializes number of neurons in layer and value of weights in the network
     * @param network to be represented by the graph
     */
    public GraphVisual(Network network){
        updateSynaps(network.getSynapsNetwork());
        scheme = network.getNetworkScheme();
        bias = network.getBias();
        if(bias)
            scheme[0]--;
    }

    /**
     * Generates a graphical representation of a network and saves it in local variable
     */
    public void makeGraph(){
        graph = new mxGraph();

        int numberOfLayers = scheme.length;

        Object parent = graph.getDefaultParent();
        graph.setMinimumGraphSize(new mxRectangle(0,0,width, height));

        graph.setCellsSelectable(false);
        graph.setCellsEditable(false);
        graph.setEdgeLabelsMovable(false);

        initializeEdgesSynaps();

        graph.getModel().beginUpdate();
        try
        {
            verticesNeurons = new Object[numberOfLayers][];
            for (int i = 0; i < numberOfLayers; i++) {
                verticesNeurons[i] = createLayerOfNeurons(i, parent);
            }
            for (int i = 0; i < numberOfLayers-1; i++) {
                connectLayers(i, i+1, parent);
            }

        }
        finally
        {
            graph.getModel().endUpdate();
        }

    }

    /**
     * creates graphical representation of neurons, and bias nodese
     * @param i number of layer of neurons in network
     * @param parent referential point in a graph plane
     * @return array of vertex which represents neurons in this layer
     */
    private Object[] createLayerOfNeurons(int i, Object parent){
        Object[] neurons = new Object[scheme[i]];
        for (int j = 0; j < neurons.length; j++) {
            neurons[j] = graph.insertVertex(parent, ""+i+"|"+j, ""+i+"|"+j, (i+1)*width/(scheme.length+1), (j+1)*height/(neurons.length+1), vertexx, vertexy, shape);

            //Creates extra nodes for bias, and edges for them

            if(bias && i!=0) {
                Object biasNode = graph.insertVertex(parent,
                        "" + i + "|" + j + "|b",
                        "",
                        (i+1)*width/(scheme.length+1) + vertexx/2-vertexx/4,
                        (j+1)*height/(neurons.length+1) - vertexy,
                        vertexx/2,
                        vertexy/2,
                        shape);
                edgesSynaps[i-1][j][edgesSynaps[i-1][j].length-1] = graph.insertEdge(parent, null, "" + String.format("%.2f", synaps[i-1][j][synaps[i-1][j].length-1]),
                        biasNode, neurons[j],
                        "strokeColor="+hexColor(synaps[i-1][j][synaps[i-1][j].length-1]));
            }
        }
        return neurons;
    }

    /**
     * Creates graphical representation of connection between layers
     * @param first array of neurons from
     * @param second array of neurons to
     * @param parent referential point in graph plane
     */
    @Deprecated
    private void connectLayers(Object[] first, Object[] second, Object parent){
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < second.length; j++) {
                graph.insertEdge(parent, null, "", first[i], second[j], "strokeColor="+hexColor(synaps[0][0][0]));
            }
        }
    }

    /**
     * Creates graphical and logical representation of ceonncetions between layers
     * @param numberOfFirstLayer index of layer from
     * @param numberOfSecondLayer index of layer to
     * @param parent cell of reference
     */
    private void connectLayers(int numberOfFirstLayer, int numberOfSecondLayer, Object parent){
        for (int i = 0; i < verticesNeurons[numberOfFirstLayer].length; i++) {
            for (int j = 0; j < verticesNeurons[numberOfSecondLayer].length; j++) {
                edgesSynaps[numberOfSecondLayer-1][j][i] = graph.insertEdge(parent, null, "" + String.format("%.2f", synaps[numberOfSecondLayer-1][j][i]),
                        verticesNeurons[numberOfFirstLayer][i],
                    verticesNeurons[numberOfSecondLayer][j], "strokeColor="+hexColor(synaps[numberOfSecondLayer-1][j][i]));
            }
        }
    }

    /**
     * saves given values to 3d array synaps and finds maximum and minimum of weights
     * @param synaps given value of synaps weights to be saved
     */
    public void updateSynaps(double[][][] synaps){
        this.synaps = synaps;
        minWeight = Double.POSITIVE_INFINITY;
        maxWeight = Double.NEGATIVE_INFINITY;
        for (double[][] arraytwo : synaps){
            for (double[] arrayone : arraytwo) {
                for (double value : arrayone) {
                    if(value > maxWeight)
                        maxWeight = value;
                    if(value < minWeight)
                        minWeight = value;
                }
            }
        }
    }

    /**
     * creates arrays for synaps of a correct lengths
     */
    private void initializeEdgesSynaps(){
        edgesSynaps = new Object[synaps.length][][];
        for (int i = 0; i < edgesSynaps.length; i++) {
            edgesSynaps[i] = new Object[synaps[i].length][];
            for (int j = 0; j < edgesSynaps[i].length; j++) {
                edgesSynaps[i][j] = new Object[synaps[i][j].length];
            }
        }
    }

    /**
     * Converts weight of a synaps on a hex representation of a color it should have on a graph.
     * Blue is a minimal weight, red is a maximal weight.
     * Minimum and maximum values must be prior updated.
     * @param weight of synaps under test
     * @return hex color of a synaps
     */
    private String hexColor(double weight){
        double fraction = (weight - minWeight) / (maxWeight - minWeight);
        int blue = (int)(fraction * 255);
        int red = 255 - blue;
        String blueString = Integer.toHexString(blue);
        String redString = Integer.toHexString(red);
        String color = "";
        color += redString + "00" + blueString;
        return color;
    }

}
