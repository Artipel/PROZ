public class Statistics {

    public static double sum(double[] array){
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static double mean(double[] array){
        return sum(array)/array.length;
    }

    public static double standardDeviation(double[] array){
        double[] arrayCopy = array.clone();
        for (int i = 0; i < arrayCopy.length; i++) {
            arrayCopy[i] *= arrayCopy[i];
        }
        return Math.sqrt(mean(arrayCopy) - mean(array) * mean(array));
    }

}
