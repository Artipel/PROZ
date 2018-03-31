public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();
        model.setViewAndController(view, controller);
        view.setModelAndController(model, controller);
        controller.setModelAndView(model, view);

        int[] arr = {3, 2, 5, 3};

        model.CreateNetwork(3, arr, 2);
        model.showNetwork();

        double[] inputarr = {1.0, 2.0, 3.0};
        double[] out = model.network.getOutput(inputarr);
        System.out.println(out[0] + " "+ out[1]);

        model.showNetworkScheme();

    }
}
