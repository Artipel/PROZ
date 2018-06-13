/**
 * Descriptive statistics function for double arrays
 */
public class Statistics {
    /**
     * Sums an array
     * @param array of data
     * @return sum of array
     */
    public static double sum(double[] array){
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    /**
     * Average of set
     * @param array of number
     * @return average
     */
    public static double mean(double[] array){
        return sum(array)/array.length;
    }

    /**
     * Standard deviation of a set
     * @param array of number
     * @return standard deviation
     */
    public static double standardDeviation(double[] array){
        double[] arrayCopy = array.clone();
        for (int i = 0; i < arrayCopy.length; i++) {
            arrayCopy[i] *= arrayCopy[i];
        }
        return Math.sqrt(mean(arrayCopy) - mean(array) * mean(array));
    }

}
