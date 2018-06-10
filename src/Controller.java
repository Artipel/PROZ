import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Controller {

    private Model model;
    private View view;

    private Thread learningThread;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
        addActionListeners();
    }

    public void setModelAndView(Model m, View v){
        model = m;
        view = v;
    }

    /**
     * Add action listeners to all buttons in the main frame.
     */
    public void addActionListeners(){
        view.addActionListenerToCreateNetworkButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = view.getInputFieldData();
                int[] layers = parseStringToIntArray(data);
                double learningRate = view.getLearningRate()/100.0;
                double momentum = view.getMomentum()/100.0;
                model.createNetwork(layers, learningRate, momentum);
                view.updateGraphics(model.getNetwork());
                view.drawErrorsChart(new double[]{0,0}, 2000);
                if(model.isReady()) {
                    view.setStatus("ready");
                    view.drawApproximationChart(model.getLinearSpaceApproximation(100), model.getTrainingData2D());
                }
            }
        });

        view.addActionListenerToLoadTrainingData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double[][][] data = FileManager.loadArrays(view.getFilePath(), 1, 1);
                model.setTrainingData(data);
                if(model.isReady()) {
                    view.setStatus("ready");
                    view.drawApproximationChart(model.getLinearSpaceApproximation(100), model.getTrainingData2D());
                }
            }
        });
        view.addActionListenerToLearnOneEpochButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                learnOneEpoch();
            }
        });

        view.addActionListenerToLearnOnlineButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setStatus("learning");
                learningThread = new Thread(new LearnInBackground());
                learningThread.start();
            }
        });
        view.addActionListenerToStopLearnOnlineButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                learningThread.interrupt();
            }
        });

        view.addActionListenerToPickFileButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(view.getMainFrame());
                File file = fc.getSelectedFile();
                view.setPathText(file.getAbsolutePath());
            }
        });
    }

    /**
     * Splits string into array of ints. Used to create network.
     * @param data string with numbers
     * @return array of ints from the string
     */
    private int[] parseStringToIntArray(String data) {
        String[] items = data.split(" ");
        int[] array = new int[items.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(items[i]);
        }
        return array;
    }

    public class LearnInBackground implements Runnable {
        public void run(){
            for (int i = 0; i < 2000; i++) {
                learnOneEpoch();
                if(Thread.interrupted())
                    break;
            }
            view.setStatus("ready");
            System.out.println("Thread finished");
        }
    }

    /**
     * makes created network learn on the basis of loaded training data, updates graph, error chart adn approximation chart.
     */
    private void learnOneEpoch(){
        model.learnOneEpochOnline();
        view.drawApproximationChart(model.getLinearSpaceApproximation(100), model.getTrainingData2D());
        view.drawErrorsChart(model.getErrors(), 2000);
        view.updateGraphics(model.getNetwork());
    }

}


