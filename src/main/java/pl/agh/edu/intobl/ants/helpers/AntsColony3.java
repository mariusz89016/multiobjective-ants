package pl.agh.edu.intobl.ants.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AntsColony3 {
    private final Random random;
    private final int numCities;
    private final double rho;
    private final double q0;
    private final double tau0;

    public AntsColony3(double alpha, double beta, int numAnts, int numCities, double rho, double q0, double tau0, double dominanceFactor, double historyFactor, double pheromoneFactor) {
        this.numCities = numCities;
        this.rho = rho;
        this.q0 = q0;
        this.tau0 = tau0;
        random = new Random();
        double step = 1.0 / numAnts;
        double current = 0.0;
        for (int i = 0; i < numAnts; i++) {
            Ants3 ant = new Ants3(dominanceFactor, historyFactor, pheromoneFactor, alpha, beta, current, 1.0 - current);
            current += step;
            ant.setPath(randomRoute(random.nextInt(numCities), numCities));
            ants.add(ant);
        }
    }

    public List<Ants3> getAnts() {
        return ants;
    }
    private List<Ants3> ants = new ArrayList<>();

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

    public void updateAnts(double[][] pheromones, int[][] distances, int[][] distances2) {
        int numCities = pheromones.length;
        for (int i = 0; i < ants.size(); i++) {
            final int start = random.nextInt(numCities);
            Ants3 currentAnt = ants.get(i);
            currentAnt.setPath(currentAnt.buildRoute(start, pheromones, distances, distances2));
        }
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

    public void updatePheromones(double[][] pheromones, int[][] distances, int[][] distances2) {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = i+1; j < pheromones[i].length; j++) {
                double decrease = 0;
                double increase = 0;
                for (int k = 0; k < ants.size(); k++) {
                    double length = length(ants.get(k).getPath(), distances); // TODO: 12/12/16 check - added!
                    double length2 = length(ants.get(k).getPath(), distances2);
                    decrease = (1.0 - rho)*pheromones[i][j];
                    increase = 0.0;
                    if(edgeInRoute(i, j, ants.get(k).getPath())) {
                        increase = q0 /(ants.get(k).getWeight1() *length + ants.get(k).getWeight2()*length2);
                    }
                }
                //todo split pheromonoes
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

    public double length(int[] route, int[][] distances) {
        double totalDistance = 0;
        for (int i = 0; i < route.length-1; i++) {
            totalDistance += distances[route[i]][route[i + 1]];
        }
        return totalDistance;
    }
}
