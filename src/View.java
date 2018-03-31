import javax.swing.*;

public class View {
    private Model model;
    private Controller controller;

    public View(){
        //JFrame jFrame = new JFrame("Neuron Network");
    }

    public void setModelAndController(Model m,  Controller c){
        model = m;
        controller = c;
    }
}
