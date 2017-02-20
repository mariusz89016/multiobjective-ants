package pl.agh.edu.intobl.ants.helpers;

import pl.agh.edu.intobl.ants.helpers.configs.ConfigurationReader;
import pl.agh.edu.intobl.ants.helpers.configs.ConfigurationReaderImpl;
import pl.agh.edu.intobl.ants.loaders.BiobjectiveTSPLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Random;

public class AntsAttractivenessRunner {
    public static void main(String[] args) throws IOException {
        String distanceFile = "/tspInstances/kroA100.tsp";
        String costFile = "/tspInstances/kroB100.tsp";
        BiobjectiveTSPLoader tspLoader = new BiobjectiveTSPLoader(distanceFile, costFile);

        double alpha = 0.1;
        double beta = 2;
        double rho = 1;
        double q0 = 1;
        int numAnts = 60;
        int numCities = tspLoader.getNumberOfCities();

        ConfigurationReader configurationReader = new ConfigurationReaderImpl("configuration-input");
        int maxTime = configurationReader.getNumberOfIteration();

        AntsColony3 antsColony = new AntsColony3(alpha, beta, numAnts, numCities, rho, q0, 0.0001, configurationReader);
        ParetoSet<Path> front = new ParetoSetPathImpl();

        final double[][] pheromonesDistances = antsColony.initializePheromones(numCities);
        final double[][] pheromonesCosts = antsColony.initializePheromones(numCities);
        final int[][] distances = tspLoader.getFirstCriterium();
        final int[][] costs = tspLoader.getSecondCriterium();


        long startTime = System.currentTimeMillis();
        int time = 0;
        while (time < maxTime) {
            antsColony.updateAnts(pheromonesDistances, pheromonesCosts, distances, costs, time);
            antsColony.updatePheromones(pheromonesDistances, pheromonesCosts, distances, costs);
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


        String dirName = "4-factors-new";
        try {
            Files.createDirectory(Paths.get(dirName));
        } catch (Exception e) {
            System.out.println("directory exists");
        }
        final int randomInt = new Random().nextInt(1000);
        final String filename = dirName + "/attractiveness_" +
                numAnts + "_" +
                maxTime + "_" +
                randomInt + ".out";

        configurationReader.saveCopy(filename + "_config.txt");

        FileWriter fileWriter = new FileWriter(filename);

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

