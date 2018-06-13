import NeuralNetwork.Network;
import ViewTools.*;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class View {
    private Model model;
    private Controller controller;

    private JFrame mainFrame;

    private JPanel neuronsPanel;
    private mxGraphComponent graphComponent;

    private JPanel chartPanel;
    private ChartPanel errorChartPanel;

    private JPanel topPanel;
    private JLabel topLabel;
    private JTextField setupField;
    private JButton createNetworkButton;
    private JButton createRBFNetworkButton;

    private JPanel leftPanel;
    private JLabel leftLabel;
    private JButton learnOneEpochButton;
    private JButton loadTrainingDataButton;
    private JButton learnOnlineButton;
    private JButton stopLearnOnlineButton;
    private JSlider learningRateSlider;
    private JLabel momentumExplainLabel;
    private JSlider momentumSlider;
    private JTextField dataPathField;
    private JLabel statusLabel;
    private JButton pickFileButton;
    private JFileChooser fc;

    //private JFrame dataTableFrame;
    //private JTable dataTable;


    private JPanel rightPanel;
    private JLabel rightLabel;
    private ChartPanel approximationChartPanel;

    public View() {
        mainFrame = new JFrame("Perceptron sim");
        mainFrame.setSize(1200, 900);
        Container pane = mainFrame.getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        //mainFrame.setLayout(new BorderLayout());

        arrangeTopPanel();
        arrangeNeuronPanel();
        arrangeChartPanel();
        arrangeLeftPanel();
        arrangeRightPanel();

        /*mainFrame.add(topPanel);
        mainFrame.add(neuronsPanel);*/

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        pane.add(topPanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        pane.add(leftPanel, c);

        c.gridx = 1;
        c.weighty = 0.5;
        pane.add(chartPanel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.5;
        pane.add(neuronsPanel, c);

        c.gridx = 1;
        c.weighty = 0.5;
        pane.add(rightPanel, c);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setStatus("waiting");
        mainFrame.setVisible(true);
    }

    public void setModelAndController(Model m,  Controller c){
        model = m;
        controller = c;
    }

    /**
     * Arranges layout and componens of a top panel
     */
    private void arrangeTopPanel(){
        topPanel = new JPanel();
        topLabel = new JLabel("Define number of neurons in layers");
        setupField = new JTextField("1 4 1", 10);
        createNetworkButton = new JButton("Create network!");
        createRBFNetworkButton = new JButton("Create RBF network");
        topPanel.add(topLabel);
        topPanel.add(setupField);
        topPanel.add(createNetworkButton);
        topPanel.add(createRBFNetworkButton);
        //topPanel.setSize(800, 40);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        topPanel.setAlignmentY(0);
        //topPanel.setBackground(Color.red);
    }

    /**
     * Update graph which represents network
     * @param network to be represented
     */
    public void updateGraphics(Network network){
        GraphVisual gv = new GraphVisual(network);
        gv.makeGraph();
        graphComponent.setGraph(gv.getGraph());
        neuronsPanel.updateUI();
    }

    /**
     * arranges layout and design of a panel with graph
     */
    private void arrangeNeuronPanel(){
        neuronsPanel = new JPanel();
        neuronsPanel.setLayout(new BorderLayout());
        neuronsPanel.setBackground(Color.lightGray);
        graphComponent = new mxGraphComponent(new mxGraph());
        neuronsPanel.add(graphComponent);
        //neuronsPanel.setPreferredSize(new Dimension(mainFrame.getWidth()/2, mainFrame.getHeight()/2));

    }

    /**
     * arranges layout and design of a panel with error chart
     */
    private void arrangeChartPanel(){
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBackground(Color.yellow);

        ChartVisual cv = new ChartVisual();
        cv.createErrorsChart(new double[]{0.0}, 2000);

        errorChartPanel = new ChartPanel(cv.getChart());
        chartPanel.add(errorChartPanel);
    }

    /**
     * arranges layout and design of a panel with control buttons
     */
    private void arrangeLeftPanel(){
        leftPanel = new JPanel();
        //Container container = new Container();
        //leftPanel.setLayout(new FlowLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        //leftPanel.setPreferredSize(new Dimension(390, 150));
        leftLabel = new JLabel("Control panel");
        leftLabel.setFont(new Font(leftLabel.getFont().getName(), Font.PLAIN, 33));
        leftLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        learnOneEpochButton = new JButton("Learn one epoch");
        learnOneEpochButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        dataPathField = new JTextField("C:\\Users\\Artur\\Documents\\Politechnika Warszawska\\PROZ\\PROZ\\data\\approximation_train_1.txt");
        dataPathField.setMaximumSize(new Dimension(400,23));
        dataPathField.setAlignmentX(Component.CENTER_ALIGNMENT);

        pickFileButton = new JButton("Pick file with training data");
        pickFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadTrainingDataButton = new JButton("Load training data");
        loadTrainingDataButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        learnOnlineButton = new JButton("Learn online");
        learnOnlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel learningRateExplainLabel = new JLabel("Learning rate:");
        learningRateExplainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        learningRateSlider = new JSlider(0,100,10);
        learningRateSlider.setMaximumSize(new Dimension(300,60));
        learningRateSlider.setPaintLabels(true);
        learningRateSlider.setMajorTickSpacing(25);
        learningRateSlider.setMinorTickSpacing(5);
        learningRateSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel momentumExplainLabel = new JLabel("Momentum:");
        momentumExplainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        momentumSlider = new JSlider(0,100,10);
        momentumSlider.setMaximumSize(new Dimension(300,60));
        momentumSlider.setMajorTickSpacing(25);
        momentumSlider.setMinorTickSpacing(5);
        momentumSlider.setPaintLabels(true);
        momentumSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("");
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.PLAIN, 20));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        stopLearnOnlineButton = new JButton("Stop learning process");
        stopLearnOnlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //learnOnlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(leftLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(loadTrainingDataButton);
        leftPanel.add(dataPathField);
        leftPanel.add(pickFileButton);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(learnOneEpochButton);
        leftPanel.add(learnOnlineButton);
        leftPanel.add(stopLearnOnlineButton);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(learningRateExplainLabel);
        leftPanel.add(learningRateSlider);
        leftPanel.add(momentumExplainLabel);
        leftPanel.add(momentumSlider);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(statusLabel);
        leftPanel.setBackground(Color.lightGray);

        momentumSlider.setBackground(momentumSlider.getParent().getBackground());
        learningRateSlider.setBackground(learningRateSlider.getParent().getBackground());

    }

    /**
     * arranges layout and design of a panel with approximation chart
     */
    private void arrangeRightPanel(){
        rightPanel = new JPanel();
        rightLabel = new JLabel("Right text");
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(rightLabel);
        rightPanel.setBackground(Color.darkGray);
        ChartVisual cv = new ChartVisual();
        cv.createChart();

        approximationChartPanel = new ChartPanel(cv.getChart());
        rightPanel.add(approximationChartPanel);
    }

    /**
     * Draws approximation chart. Two data series, expected values and a result of neuron approximation
     * @param values results of network approximation
     * @param expectedValues of a function be approximated
     */
    public void drawApproximationChart(double[][] values, double[][] expectedValues){
        ChartVisual cv = new ChartVisual();
        cv.createChart("Approximation chart", "x", "y", values, expectedValues);
        approximationChartPanel.setChart(cv.getChart());
        rightPanel.updateUI();
    }

    /**
     * Draws chart of mean square error of the network
     * @param errors array of errors made by the network
     * @param time scale of a horizontal axis
     */
    public void drawErrorsChart(double[] errors, int time){
        ChartVisual cv = new ChartVisual();
        cv.createErrorsChart(errors, time);
        errorChartPanel.setChart(cv.getChart());
        chartPanel.updateUI();
    }

    public void addActionListenerToLearnOneEpochButton(ActionListener actionListener){
        learnOneEpochButton.addActionListener(actionListener);
    }

    public void addActionListenerToLoadTrainingData(ActionListener actionListener){
        loadTrainingDataButton.addActionListener(actionListener);
    }

    public void addActionListenerToCreateNetworkButton(ActionListener actionListener){
        createNetworkButton.addActionListener(actionListener);
    }

    public void addActionListenerToLearnOnlineButton(ActionListener actionListener){
        learnOnlineButton.addActionListener(actionListener);
    }

    public void addActionListenerToStopLearnOnlineButton(ActionListener actionListener) {
        stopLearnOnlineButton.addActionListener(actionListener);
    }

    public void addActionListenerToPickFileButton(ActionListener actionListener) {
        pickFileButton.addActionListener(actionListener);
    }

    public void addActionListenerToCreateRBFNetworkButton(ActionListener actionListener){
        createRBFNetworkButton.addActionListener(actionListener);
    }
    /**
     *
     * @return a string which defines layers of network
     */
    public String getInputFieldData() {
        return setupField.getText();
    }

    /**
     *
     * @return string from a field with file path
     */
    public String getFilePath(){
        return dataPathField.getText();
    }

    /**
     *
     * @return value of a learning rate slider in range 0 - 100
     */
    public int getLearningRate(){
        return learningRateSlider.getValue();
    }

    /**
     *
     * @return value of a momentum slider in range 0 - 100
     */
    public int getMomentum(){
        return momentumSlider.getValue();
    }

    /**
     * Defines in which state network is. Adjusts state of component in frame to the situation
     * @param status waiting, ready, learning, three possible states
     */
    public void setStatus(String status){
        switch (status) {
            case "learning":
                statusLabel.setText("Learning...");
                stopLearnOnlineButton.setEnabled(true);
                learnOneEpochButton.setEnabled(false);
                learnOnlineButton.setEnabled(false);
                createNetworkButton.setEnabled(false);
                loadTrainingDataButton.setEnabled(false);
                break;
            case "ready":
                statusLabel.setText("Network ready, data loaded");
                stopLearnOnlineButton.setEnabled(false);
                learnOneEpochButton.setEnabled(true);
                learnOnlineButton.setEnabled(true);
                createNetworkButton.setEnabled(true);
                loadTrainingDataButton.setEnabled(true);
                break;
            case "waiting":
                stopLearnOnlineButton.setEnabled(false);
                statusLabel.setText("Set learning parameters, create network and load data");
                learnOneEpochButton.setEnabled(false);
                learnOnlineButton.setEnabled(false);
                break;
        }
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Sets text of a tex field with file path
     * @param pathText text to be inserted into field
     */
    public void setPathText(String pathText) {
        dataPathField.setText(pathText);
    }
}
