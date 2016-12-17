package pl.agh.edu.intobl.ants;

public class Utils {
    public static double evaluate(double[][] matrix, int[] ant, int numberOfCities) {
        double fitness = 0.0;
        for (int i = 0; i < (numberOfCities - 1); i++) {
            int x = ant[i];
            int y = ant[i + 1];

            fitness += matrix[x][y];
        }
        int firstCity = ant[0];
        int lastCity = ant[numberOfCities - 1];

        fitness += matrix[firstCity][lastCity];

        return fitness;
    }
}
