package NeuralNetwork;

import static org.junit.jupiter.api.Assertions.*;

class NeuronGasTest {

    @org.junit.jupiter.api.Test
    void findCentres() {
        double[][] points = new double[][]{
                {0,1},
                {1,2},
                {3,2},
                {7,3},
                {5,0}
        };
        NeuronGas gas = new NeuronGas();
        double[][] centers = gas.findCentres(points, 2);
        System.out.println(centers);
    }

    @org.junit.jupiter.api.Test
    void initializeCentresForgy() {
        double[][] points = new double[][]{
                {0,1},
                {1,2},
                {3,2},
                {7,3},
                {5,0}
        };
        NeuronGas gas = new NeuronGas();
        double[][] centers = gas.initializeCentresForgy(points, 2);
        boolean[] isForgy = {false, false};
        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if(centers[i].equals(points[j])){
                    isForgy[i] = true;
                }
            }
        }
        assertArrayEquals (new boolean[]{true, true}, isForgy);
    }

    @org.junit.jupiter.api.Test
    void moveCentres() {
    }
}