package NeuralNetwork;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class NeuronGas {

    public double[][] findCentres(double[][] points, int count){
        double[][] centersCoordinates = initializeCentresForgy(points, count);
        moveCentres(centersCoordinates, points);
        return centersCoordinates;
    }

    public double[][] initializeCentresForgy(double[][] points, int centresCount){
        double[][] centres = new double[centresCount][points[0].length];
        for (int i = 0; i < centres.length; i++) {
            Random random = new Random();
            centres[i] = points[random.nextInt(points.length)];
        }
        return centres;
    }
    
    public void moveCentres(double[][] centres, double[][] points){
        int[] centerWeight = new int[centres.length];
        //Arrays.fill(centerWeight, 1);
        for (int i = 0; i < points.length; i++) {
            int closestCentre = findClosestCenter(centres, points[i]);
            assignPointToCenter(centres[closestCentre], points[i], closestCentre, centerWeight);
        }
    }

    private int findClosestCenter(double[][] centres, double[] point){
        double minDistance = Double.MAX_VALUE;
        int closest = 0;
        for (int i = 0; i < centres.length; i++) {
            double distanceToCurrentCentre = 0;
            for (int j = 0; j < centres[i].length; j++) {
                distanceToCurrentCentre += (point[j] - centres[i][j]) * (point[j] - centres[i][j]);
            }
            if(distanceToCurrentCentre < minDistance) {
                minDistance = distanceToCurrentCentre;
                closest = i;
            }
        }
        return closest;
    }

    private void assignPointToCenter(double[] closestCenterCoordinate, double[] point, int centerIndex, int[] weights){
        for (int i = 0; i < closestCenterCoordinate.length; i++) {
            closestCenterCoordinate[i] = (closestCenterCoordinate[i] * weights[centerIndex] + point[i])/(weights[centerIndex] + 1);
        }
        weights[centerIndex]++;
    }
}
