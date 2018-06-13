public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view);
        model.setViewAndController(view, controller);
        view.setModelAndController(model, controller);
    }
}
