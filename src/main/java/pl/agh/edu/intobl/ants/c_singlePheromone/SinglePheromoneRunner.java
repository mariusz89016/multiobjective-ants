package pl.agh.edu.intobl.ants.c_singlePheromone;

import pl.agh.edu.intobl.ants.helpers.*;
import pl.agh.edu.intobl.ants.loaders.BiobjectiveTSPLoader;

import java.io.IOException;

public class SinglePheromoneRunner {
    public static void main(String[] args) throws IOException {
        String distanceFile = "/tspInstances/kroA100.tsp";
        String costFile = "/tspInstances/kroB100.tsp";
        BiobjectiveTSPLoader tspLoader = new BiobjectiveTSPLoader(distanceFile, costFile);

        double alpha = 0.1;
        double beta = 0.5;
        double rho = 0.5;
        double q0 = 0.9;
        int numAnts = 10;
        int maxTime = 160;
        int numCities = tspLoader.getNumberOfCities();

        AntsColony3 antsColony = new AntsColony3(alpha, beta, numAnts, numCities, rho, q0, 0.0, 0.0, 0.0, 1.0);
        ParetoSet<Path> front = new ParetoSetPathImpl();

        final double[][] pheromones = antsColony.initializePheromones(numCities);
        final int[][] distances = tspLoader.getFirstCriterium();
        final int[][] costs = tspLoader.getSecondCriterium();

        int time = 0;
        while (time < maxTime) {
            antsColony.updateAnts(pheromones, distances, costs);
            antsColony.updatePheromones(pheromones, distances, costs);
            for (Ants3 ants3 : antsColony.getAnts()) {
                for(Path p : ants3.getFrontPareto().getNonDominatedSet()) {
                    front.addParetoElementToSet(p);
                }
            }
            time++;
            if (time % 20 == 0) {
                System.out.println(time + " -> size: " + front.getNonDominatedSet().size());
            }
        }

        for (Path path : front.getNonDominatedSet()) {
            System.out.println(path.getFirstCriterium() + "\t" + path.getSecondCriterium());
        }
    }
}
