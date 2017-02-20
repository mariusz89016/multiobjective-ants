package pl.agh.edu.intobl.ants.helpers;


import pl.agh.edu.intobl.ants.helpers.configs.ConfigurationReader;

import java.util.*;

public class Ants3 {
    private final double alpha;
    private final double beta;
    private final double weight1;
    private final double weight2;
    private final ConfigurationReader configurationReader;

    public ParetoSet<Path> getFrontPareto() {
        return frontPareto;
    }

    private ParetoSet<Path> frontPareto = new ParetoSetPathImpl();
    private int[] actualPath;


    public Ants3(double alpha, double beta, double weight1, double weight2, ConfigurationReader configurationReader) {
        this.alpha = alpha;
        this.beta = beta;
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.configurationReader = configurationReader;
    }

    public void setPath(int[] path) {
        actualPath = path;
    }


    public int[] buildRoute(int start, double[][] pheromones1, double[][] pheromones2, int[][] distances, int[][] distances2, int iter) {
        int numCities = pheromones1.length;
        int[] route = new int[numCities];
        boolean[] visited = new boolean[numCities];

        route[0] = start;
        visited[start]=true;
        for (int j = 0; j < numCities - 1; j++) {
            int cityX = route[j];
            route[j+1] = nextCity(cityX, visited, pheromones1, pheromones2, distances, distances2, iter);
            visited[route[j+1]] = true;
        }

        final Path path = new Path(route, distances, distances2);
        frontPareto.addParetoElementToSet(path);

        return route;
    }

    private int nextCity(int cityX, boolean[] visited, double[][] pheromones1, double[][] pheromones2, int[][] distances, int[][] distances2, int iter) {
        double[] probabilities = moveProbabilities(cityX, visited, pheromones1, pheromones2, distances, distances2, iter);
        double[] cumulative = calculateCumulative(probabilities);

        final double randomDouble = new Random().nextDouble();
        for (int j = 0; j < cumulative.length-1; j++) {
            if(randomDouble >= cumulative[j] && randomDouble < cumulative[j+1]) {
                return j;
            }
        }
        return pheromones1.length-1;
    }

    private double[] moveProbabilities(int cityX, boolean[] visited, double[][] pheromones1, double[][] pheromones2, int[][] distances, int[][] distances2, int iter) {
//        return new AttractivenessCalculator(dominanceFactor, historyFactor, pheromoneFactor, weight1, weight2)
//                .movesProbability(cityX, visited, pheromones, distances, distances2, historicalPaths, alpha, beta);
        return new AttractivenessCalculator(configurationReader, weight1, weight2)
                .movesProbability(cityX, visited, pheromones1, pheromones2, distances, distances2, new ArrayList<>(frontPareto.getNonDominatedSet()), alpha, beta, iter);
    }

    private double[] calculateCumulative(double[] probabilities) {
        double[] cumulative = new double[probabilities.length+1];
        cumulative[0] = 0;
        for (int i = 1; i < cumulative.length; i++) {
            cumulative[i] = cumulative[i-1] + probabilities[i-1];
        }
        return cumulative;
    }

    public int[] getPath() {
        return actualPath;
    }

    public double getWeight1() {
        return weight1;
    }

    public double getWeight2() {
        return weight2;
    }
}
