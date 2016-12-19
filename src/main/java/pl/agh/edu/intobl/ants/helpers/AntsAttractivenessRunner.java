package pl.agh.edu.intobl.ants.helpers;

import pl.agh.edu.intobl.ants.loaders.BiobjectiveTSPLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class AntsAttractivenessRunner {
    public static void main(String[] args) throws IOException {
        String distanceFile = "/tspInstances/kroA100.tsp";
        String costFile = "/tspInstances/kroB100.tsp";
        BiobjectiveTSPLoader tspLoader = new BiobjectiveTSPLoader(distanceFile, costFile);

        double dominanceFactor = 0.33;
        double historyFactor = 0.33;
        double pheromoneFactor = 0.34;

        double alpha = 0.1;
        double beta = 2;
        double rho = 1;
        double q0 = 1;
        int numAnts = 40;
        int maxTime = 500;
        int numCities = tspLoader.getNumberOfCities();

        AntsColony3 antsColony = new AntsColony3(alpha, beta, numAnts, numCities, rho, q0, 0.0001, dominanceFactor, historyFactor, pheromoneFactor);
        ParetoSet<Path> front = new ParetoSetPathImpl();

        final double[][] pheromones = antsColony.initializePheromones(numCities);
        final int[][] distances = tspLoader.getFirstCriterium();
        final int[][] costs = tspLoader.getSecondCriterium();


        long startTime = System.currentTimeMillis();
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
        final long durationInMillis = System.currentTimeMillis() - startTime;
        final Duration duration = Duration.ofMillis(durationInMillis);
        System.out.println(duration + "(" + duration.getSeconds() + ")");


        final String filename = "attractiveness_" +
                numAnts + "_" +
                maxTime + "_" +
                dominanceFactor + "_" +
                historyFactor + "_" +
                pheromoneFactor + ".out";
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(maxTime + "\n");
        fileWriter.write(dominanceFactor + "\n");
        fileWriter.write(historyFactor + "\n");
        fileWriter.write(pheromoneFactor + "\n");

        for (Path path : front.getNonDominatedSet()) {
            fileWriter.write(path.getFirstCriterium() + "\t" + path.getSecondCriterium() + "\n");
        }
        fileWriter.close();
    }
}
//ustalic ilosc mrowek
//20 wywolan (po 10):

//dominanceFactor:
//historyFactor:
//pheromoneFactor:

