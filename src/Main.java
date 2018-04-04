public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();
        model.setViewAndController(view, controller);
        view.setModelAndController(model, controller);
        controller.setModelAndView(model, view);

        int[] arr = {2};

        model.CreateNetwork(1, arr, 1);
        model.showNetwork();
        model.showSynaps();
        System.out.println(model.network.getOutput(new double[]{1.0})[0]);

        double[][] inputarr = new double[6][1];
        double[][] outputarr = new double[6][1];
        for (int i = 0; i < 6; i++) {
            inputarr[i][0] = (double)i;
            outputarr[i][0] = (double)((-5/(1+Math.pow(Math.E, -(double)(5*i))))+3/(1+Math.pow(Math.E, -(double)(0.5*(double)i))));
        }
        /*double[][][] weight = new double[2][1][1];
        weight[0][0][0] = 2.0;
        weight[1][0][0] = 2.0;
        model.network.setWeights(weight);*/
        model.showSynaps();
        System.out.println("Sum of errors: " + model.network.getSumOfErrors(inputarr, outputarr));
        model.showPartialDerivatives(inputarr, outputarr);
        /*for(double d = 1.0; d < 10.0; d++)
            System.out.println("x = " + d + " y = " + model.network.getOutput(new double[]{d})[0]);*/
        for (int i = 0; i < 100000; i++) {
            //model.showPartialDerivatives(inputarr, outputarr);
            model.network.learnOneEpoch(inputarr, outputarr);
            if(model.network.getSumOfErrors(inputarr, outputarr) < 0.01){
                System.out.println("Sucess! Epoch: " + i);
                break;
            }
            /*for(double d = 1.0; d < 10.0; d++)
                System.out.println("x = " + d + " y = " + model.network.getOutput(new double[]{d})[0]);*/
        }
        model.showPartialDerivatives(inputarr, outputarr);
        model.showSynaps();
        System.out.println("Sum of errors: " + model.network.getSumOfErrors(inputarr, outputarr));
        /*for(double d = 0; d < 4.0; d++)
            System.out.println("x = " + d + " y = " + model.network.getOutput(new double[]{d})[0]);
*/

    }
}
