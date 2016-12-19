package pl.agh.edu.intobl.ants.a_normalAnts;

import pl.agh.edu.intobl.ants.loaders.TSPLoader;
import java.io.IOException;
import java.util.Arrays;

import static pl.agh.edu.intobl.ants.Utils.evaluate;

//based on: https://sourceforge.net/p/jmetal/git/ci/54ef77662242e4eab6ae58755ad11a49bc19f3fc/tree/
public class AntsColonyTSPRunner {
    public static void main(String[] args) throws IOException {
        TSPLoader tspLoader = new TSPLoader("/tspInstances/eil51.tsp");

        double alpha = 0.1;
        double beta = 2;
        double rho = 0.3;
        double q0 = 1;
        int numAnts = 30;
        int maxTime = 100000;

        AntsColony antsColony = new AntsColony(alpha, beta, rho, q0, 0.0001, numAnts);
        int numCities = tspLoader.getNumberOfCities();

        final int[][] ants = antsColony.initializeAnts(numCities);
        final double[][] pheromones = antsColony.initializePheromones(numCities);
        final double[][] distances = tspLoader.getMatrix();

        int time = 0;
        double best = Double.MAX_VALUE;
        int[] bestRoute = null;
        while (time < maxTime) {
            antsColony.updateAnts(ants, pheromones, distances);
            antsColony.updatePheromones(pheromones, ants, distances);
            for (int[] ant : ants) {
                final double lengthOfNewRoute = evaluate(distances, ant, numCities);
                if (lengthOfNewRoute < best) {
                    best = lengthOfNewRoute;
                    bestRoute = ant;
                }
            }
            time++;
            if (time % 20 == 0) {
                System.out.println(time + " -> best: " + best);
            }
        }
        System.out.println("Length: " + best);
        System.out.println("Route: " + Arrays.toString(bestRoute));
    }
}
