package pl.agh.edu.intobl.ants.c_singlePheromone;

import pl.agh.edu.intobl.ants.a_normalAnts.AntsColony;
import pl.agh.edu.intobl.ants.loaders.BiobjectiveTSPLoader;

import java.io.IOException;
import java.util.Arrays;

import static pl.agh.edu.intobl.ants.Utils.evaluate;

//based on: https://sourceforge.net/p/jmetal/git/ci/54ef77662242e4eab6ae58755ad11a49bc19f3fc/tree/
public class AntsColonyTSP3Runner {
    private static double p1 = 0.0;
    private static double p2 = 1.0;

    public static void main(String[] args) throws IOException {
        String distanceFile = "/tspInstances/eil51.tsp";
        BiobjectiveTSPLoader tspLoader = new BiobjectiveTSPLoader(distanceFile, distanceFile);

        double alpha = 0.1;
        double beta = 0.5;
        double rho = 0.5;
        double q0 = 0.9;
        int numAnts = 10;
        int maxTime = 100000;

        AntsColony2 antsColony = new AntsColony2(alpha, beta, rho, q0, 0, numAnts, p1, p2);
        int numCities = tspLoader.getNumberOfCities();

        final int[][] ants = antsColony.initializeAnts(numCities);
        final double[][] pheromones = antsColony.initializePheromones(numCities);
        final double[][] distances = tspLoader.getFirstCriterium();
        final double[][] costs = tspLoader.getSecondCriterium();

        int time = 0;
        double best = Double.MAX_VALUE;
        int[] bestRoute = null;
        while (time < maxTime) {
            antsColony.updateAnts(ants, pheromones, distances, costs);
            antsColony.updatePheromones(pheromones, ants, distances, costs);
            for (int[] ant : ants) {
                final double lengthOfNewRoute = evaluate(distances, ant, numCities);
                final double costOfNewRoute = evaluate(distances, ant, numCities);
                final double weightedFitness = p1 * lengthOfNewRoute + p2 * costOfNewRoute;
                if (weightedFitness < best) {
                    best = weightedFitness;
                    bestRoute = ant;
                }
            }
            time++;
            if (time % 20 == 0) {
                System.out.println(time + " -> best:, " + best);
            }
        }
        System.out.println("Length: " + best);
        System.out.println("Route: " + Arrays.toString(bestRoute));
    }
}
