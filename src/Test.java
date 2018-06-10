import java.util.Scanner;
import NeuralNetwork.Network;
import sun.nio.ch.Net;

public class Test {

    public static void main(String[] args){

        Model model = new Model();

        int[] arr = {2};

        model.createNetwork(4, arr, 4, true, 1,1);
        model.showNetwork();
        model.showSynaps();

        double[][] inputarr = new double[4][];
        double d = 0;
        inputarr[0] = new double[]{1.0, 0.0, 0.0, 0.0};
        inputarr[1] = new double[]{0.0, 1.0, 0.0, 0.0};
        inputarr[2] = new double[]{0.0, 0.0, 1.0, 0.0};
        inputarr[3] = new double[]{0.0, 0.0, 0.0, 1.0};
        /*for (int i = 0; i < 31; ++i) {
            inputarr[i][0] = d;
            outputarr[i][0] = 2*d;//(double)((-5/(1+Math.pow(Math.E, -(double)(5*i))))+3/(1+Math.pow(Math.E, -(double)(0.5*(double)i))));
            d+=0.1;
        }*/
        double[][] outputarr = inputarr;
        model.showSynaps();
        System.out.println("Sum of errors: " + model.getNetwork().getSumOfErrors(inputarr, inputarr));
        model.showPartialDerivatives(inputarr, inputarr);
        for (int i = 0; i < 10000; i++) {
            model.getNetwork().learnOnline(inputarr, inputarr,1);
            if(model.getNetwork().getSumOfErrors(inputarr, inputarr) < 0.005){
                System.out.println("Sucess! Epoch: " + i);
                break;
            }
        }
        //model.showPartialDerivatives(inputarr, outputarr);
        //model.showSynaps();
        System.out.println("Sum of errors: " + model.getNetwork().getSumOfErrors(inputarr, outputarr));
        Scanner scanner = new Scanner(System.in);
        d = scanner.nextDouble();
        while(d != 100) {
            if(d==50){
                for (int i = 0; i < 10000; i++) {
                    model.getNetwork().learnOnline(inputarr, outputarr,1);
                    if(model.getNetwork().getSumOfErrors(inputarr, outputarr) < 0.00001){
                        System.out.println("Sucess! Epoch: " + i);
                        break;
                    }
                }
                System.out.println("Sum of errors: " + model.getNetwork().getSumOfErrors(inputarr, outputarr));
            }
            d = scanner.nextInt();
        }
        double singleoutput[] = model.getNetwork().getOutput(inputarr[0]);
        System.out.println("Output: " + singleoutput[0] + " " + singleoutput[1] + " " + singleoutput[2] + " " + singleoutput[3]);
        singleoutput = model.getNetwork().getOutput(inputarr[1]);
        System.out.println("Output: " + singleoutput[0] + " " + singleoutput[1] + " " + singleoutput[2] + " " + singleoutput[3]);
        singleoutput = model.getNetwork().getOutput(inputarr[2]);
        System.out.println("Output: " + singleoutput[0] + " " + singleoutput[1] + " " + singleoutput[2] + " " + singleoutput[3]);
        singleoutput = model.getNetwork().getOutput(inputarr[3]);
        System.out.println("Output: " + singleoutput[0] + " " + singleoutput[1] + " " + singleoutput[2] + " " + singleoutput[3]);

    }
}
