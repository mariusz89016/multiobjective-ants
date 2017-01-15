package pl.agh.edu.intobl.ants.helpers;


import java.util.*;

public class Ants3 {
    private final double alpha;
    private final double beta;
    private final double weight1;
    private final double weight2;
    private final double pheromoneFactor;
    private final double historyFactor;
    private final double dominanceFactor;

    public ParetoSet<Path> getFrontPareto() {
        return frontPareto;
    }

    private ParetoSet<Path> frontPareto = new ParetoSetPathImpl();
    private int[] actualPath;


    public Ants3(double dominanceFactor, double historyFactor, double pheromoneFactor, double alpha, double beta, double weight1, double weight2) {
        this.dominanceFactor = dominanceFactor;
        this.historyFactor = historyFactor;
        this.pheromoneFactor = pheromoneFactor;
        this.alpha = alpha;
        this.beta = beta;
        this.weight1 = weight1;
        this.weight2 = weight2;
    }

    public void setPath(int[] path) {
        actualPath = path;
    }


    public int[] buildRoute(int start, double[][] pheromones, int[][] distances, int[][] distances2) {
        int numCities = pheromones.length;
        int[] route = new int[numCities];
        boolean[] visited = new boolean[numCities];

        route[0] = start;
        visited[start]=true;
        for (int j = 0; j < numCities - 1; j++) {
            int cityX = route[j];
            route[j+1] = nextCity(cityX, visited, pheromones, distances, distances2);
            visited[route[j+1]] = true;
        }

        final Path path = new Path(route, distances, distances2);
        frontPareto.addParetoElementToSet(path);

        return route;
    }

    private int nextCity(int cityX, boolean[] visited, double[][] pheromones, int[][] distances, int[][] distances2) {
        double[] probabilities = moveProbabilities(cityX, visited, pheromones, distances, distances2);
        double[] cumulative = calculateCumulative(probabilities);

        final double randomDouble = new Random().nextDouble();
        for (int j = 0; j < cumulative.length-1; j++) {
            if(randomDouble >= cumulative[j] && randomDouble < cumulative[j+1]) {
                return j;
            }
        }
        return pheromones.length-1;
    }

    private double[] moveProbabilities(int cityX, boolean[] visited, double[][] pheromones, int[][] distances, int[][] distances2) {
//        return new AttractivenessCalculator(dominanceFactor, historyFactor, pheromoneFactor, weight1, weight2)
//                .movesProbability(cityX, visited, pheromones, distances, distances2, historicalPaths, alpha, beta);
        return new AttractivenessCalculator(dominanceFactor, historyFactor, pheromoneFactor, weight1, weight2)
                .movesProbability(cityX, visited, pheromones, distances, distances2, new ArrayList<>(frontPareto.getNonDominatedSet()), alpha, beta);
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
