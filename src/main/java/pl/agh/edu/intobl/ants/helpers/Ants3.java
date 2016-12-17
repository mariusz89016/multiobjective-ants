package pl.agh.edu.intobl.ants.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ants3 {
    private final double alpha;
    private final double beta;
    private final double rho;
    private final double q0;
    private final int numAnts;
    private final double tau0;
    private final Random random;
    private final double weight1;
    private final double weight2;
    private List<Path> historicalPaths = new ArrayList<>();
    private ParetoSet<Path> frontPareto = new ParetoSetPathImpl();


    public Ants3(double alpha, double beta, double rho, double q0, double tau0, int numAnts, double weight1, double weight2) {
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.q0 = q0;
        this.tau0 = tau0;
        this.numAnts = numAnts;
        this.weight1 = weight1;
        this.weight2 = weight2;
        random = new Random();
    }

    public int[][] initializeAnts(int numCities) {
        return IntStream.range(0, numCities)
                .boxed()
                .map(i -> randomRoute(random.nextInt(numCities), numCities))
                .collect(Collectors.toList())
                .toArray(new int[][]{new int[0]});
    }

    public double[][] initializePheromones(int numCities) {
        double[][] pheromones = new double[numCities][];
        for (int i = 0; i < pheromones.length; i++) {
            pheromones[i] = new double[numCities];
            for (int j = 0; j < pheromones[i].length; j++) {
//                pheromones[i][j] = tau0;
                pheromones[i][j] = rho;
            }
        }
        return pheromones;
    }

    public void updateAnts(int[][] ants, double[][] pheromones, int[][] distances, int[][] distances2) {
        int numCities = pheromones.length;
        for (int i = 0; i < ants.length; i++) {
            final int start = random.nextInt(numCities);
            ants[i] = buildRoute(i, start, pheromones, distances, distances2);
        }
    }

    private int[] buildRoute(int i, int start, double[][] pheromones, int[][] distances, int[][] distances2) {
        int numCities = pheromones.length;
        int[] route = new int[numCities];
        boolean[] visited = new boolean[numCities];

        route[0] = start;
        visited[start]=true;
        for (int j = 0; j < numCities - 1; j++) {
            int cityX = route[j];
            route[j+1] = nextCity(i, cityX, visited, pheromones, distances, distances2);
            visited[route[j+1]] = true;
        }

        final Path path = new Path(route, distances, distances2);
        historicalPaths.add(path);
        frontPareto.addParetoElementToSet(path);

        return route;
    }

    private int nextCity(int i, int cityX, boolean[] visited, double[][] pheromones, int[][] distances, int[][] distances2) {
        double[] probabilities = moveProbabilities(i, cityX, visited, pheromones, distances, distances2);
        double[] cumulative = calculateCumulative(probabilities);

        final double randomDouble = random.nextDouble();
        for (int j = 0; j < cumulative.length-1; j++) {
            if(randomDouble >= cumulative[j] && randomDouble < cumulative[j+1]) {
                return j;
            }
        }
        return pheromones.length-1;
    }

    private double[] moveProbabilities(int i, int cityX, boolean[] visited, double[][] pheromones, int[][] distances, int[][] distances2) {
        return new AttractivenessCalculator(0.0, 0.0, 1.0, weight1, weight2)
                .movesProbability(cityX, visited, pheromones, distances, distances2, frontPareto, alpha, beta);
//        int numCities = pheromones.length;
//        double[] taueta = new double[numCities];
//        double sum = 0;
//
//        for (int j = 0; j < taueta.length; j++) {
//            if(j==cityX || visited[j]) {
//                taueta[j] = 0;
//            } else {
//                //todo - normalize input data - preconditions!
//                taueta[j] = Math.pow(pheromones[cityX][j], alpha) * Math.pow(1.0/(weight1*distances[cityX][j] + weight2*distances2[cityX][j]), beta); // TODO: 12/12/16 formula for 2 objectives
//
//                //todo another version
////                val sum = alfa * Math.pow(1.0/(distances[cityX][j]), beta);
////                sum += gamma * Math.pow(1.0/(distances2[cityX][j]), beta);
//
//                taueta[j] = Math.max(taueta[j], 0.0001);
//                taueta[j] = Math.min(taueta[j], Double.MAX_VALUE/(numCities*100));
//            }
//            sum += taueta[j];
//        }
//
//        double[] probabilites = new double[numCities];
//        for (int j = 0; j < probabilites.length; j++) {
//            probabilites[j] = taueta[j]/sum;
//        }
//        return probabilites;
    }

    private double[] calculateCumulative(double[] probabilities) {
        double[] cumulative = new double[probabilities.length+1];
        cumulative[0] = 0;
        for (int i = 1; i < cumulative.length; i++) {
            cumulative[i] = cumulative[i-1] + probabilities[i-1];
        }
        return cumulative;
    }


    public double length(int[] route, double[][] distances) {
        double totalDistance = 0;
        for (int i = 0; i < route.length-1; i++) {
            totalDistance += distances[route[i]][route[i + 1]];
        }
        return totalDistance;
    }

    private int[] randomRoute(int startCity, int numCities) {
        final List<Integer> cities = IntStream.range(0, numCities)
                .boxed()
                .filter(x -> x != startCity)
                .collect(Collectors.toList());
        Collections.shuffle(cities);
        cities.add(startCity);

        return cities.stream().mapToInt(i->i).toArray();
    }

    public void updatePheromones(double[][] pheromones, int[][] ants, double[][] distances, double[][] distances2) {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = i+1; j < pheromones[i].length; j++) {
                double decrease = 0;
                double increase = 0;
                for (int k = 0; k < ants.length; k++) {
                    double length = length(ants[k], distances); // TODO: 12/12/16 check - added!
                    double length2 = length(ants[k], distances2);
                    decrease = (1.0 - rho)*pheromones[i][j];
                    increase = 0.0;
                    if(edgeInRoute(i, j, ants[k])) {
                        increase = q0 /(weight1*length + weight2*length2);
                    }
                }
                pheromones[i][j] = decrease+increase;
                pheromones[i][j] = Math.max(pheromones[i][j], 0.0001);
                pheromones[i][j] = Math.min(pheromones[i][j], 100000.0);
                pheromones[j][i] = pheromones[i][j];
            }
        }
    }

    private boolean edgeInRoute(int i, int j, int[] route) {
        int firstCityIndex = -1;
        for (int k = 0; k < route.length; k++) {
            if(route[k]==i) {
                firstCityIndex = k;
            }
        }
        return route[(firstCityIndex + 1) % route.length] == j || route[(route.length + firstCityIndex - 1) % route.length] == j;
    }
}
