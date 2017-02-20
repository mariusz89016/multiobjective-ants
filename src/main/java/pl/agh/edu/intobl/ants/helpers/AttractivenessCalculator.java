package pl.agh.edu.intobl.ants.helpers;

import pl.agh.edu.intobl.ants.helpers.configs.ConfigurationReader;
import pl.agh.edu.intobl.ants.helpers.configs.Factors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AttractivenessCalculator {
    private double firstCriteriumWeight;
    private double secondCriteriumWeight;
    private final ConfigurationReader configurationReader;

    public AttractivenessCalculator(ConfigurationReader configurationReader, double firstCriteriumWeight, double secondCriteriumWeight) {
        this.configurationReader = configurationReader;
        this.firstCriteriumWeight = firstCriteriumWeight;
        this.secondCriteriumWeight = secondCriteriumWeight;
    }

    public double[] movesProbability(int actualCity, boolean[] visited, double[][] pheromones1, double[][] pheromones2, int[][] firstCriterium, int[][] secondCriterium, List<Path> historicalPaths, double alpha, double beta, int iter) {
        double[] probability = new double[visited.length];
        double[] dominance = new double[visited.length];

        final Factors factors = configurationReader.getConfiguration(iter);

        if(factors.getDominance() != 0.0) {
            dominance = dominance(actualCity, visited, firstCriterium, secondCriterium);
        }
        double[] taboo = new double[visited.length];
        if(factors.getTaboo() != 0.0) {
            taboo = taboo(actualCity, visited, historicalPaths);
        }

        double[] bestOf = new double[visited.length];
        if(factors.getBestOf() != 0.0) {
            bestOf = bestOf(actualCity, visited, historicalPaths);
        }

        double[] pheromones = new double[visited.length];
        if(factors.getPheromone() != 0.0) {
            pheromones = pheromones(actualCity, visited, pheromones1, pheromones2, alpha, beta, firstCriterium, secondCriterium);
        }

        for (int i = 0; i < probability.length; i++) {
            probability[i] = factors.getDominance() * dominance[i] +
                                factors.getTaboo() * taboo[i] +
                                factors.getPheromone() * pheromones[i] +
                                factors.getBestOf() * bestOf[i];
        }

        return normalize(probability);
    }

    private double[] normalize(double[] probability) {
        double sum = 0.0;
        for (double aProbability : probability) {
            sum += aProbability;
        }
        for (int i = 0; i < probability.length; i++) {
            probability[i] /= sum;
        }
        return probability;
    }

    private double[] dominance(int actualCity, boolean[] visited, int[][] firstCriterium, int[][] secondCriterium) {
        ParetoSet<ParetoElement<Integer>> paretoSet = new ParetoSetImpl();
        int nonVisitedSize = 0;
        for (int i = 0; i < firstCriterium[actualCity].length; i++) {
            if (!visited[i]) {
                final IntegerElement element = new IntegerElement(firstCriterium[actualCity][i], secondCriterium[actualCity][i], i);
                paretoSet.addParetoElementToSet(element);
                nonVisitedSize++;
            }
        }
        final Set<ParetoElement<Integer>> nonDominatedSet = paretoSet.getNonDominatedSet();

        double nonDominatedProbabilityAmount = 1.0 / (nonDominatedSet.size() + 1);
        double dominatedProbabilityAmount = nonDominatedProbabilityAmount / (nonVisitedSize - nonDominatedSet.size());

        double[] probabilities = new double[visited.length];
        final Set<Integer> nonDominatedCities = nonDominatedSet.stream().map(ParetoElement::cityIndex).collect(Collectors.toSet());
        for (int i = 0; i < firstCriterium[actualCity].length; i++) {
            if(!visited[i]) {
                if(nonDominatedCities.contains(i)) {
                    probabilities[i] = nonDominatedProbabilityAmount;
                }
                else {
                    probabilities[i] = dominatedProbabilityAmount;
                }
            }
        }

        return probabilities;
    }

    private double[] taboo(int actualCity, boolean[] visited, List<Path> historicalPaths) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                set.add(i);
            }
        }

        Set<Integer> historySet = new HashSet<>();
        for (Path path : historicalPaths) {
            int[] cityPath = path.getCityPath();
            for (int i = 0; i < cityPath.length - 1; i++) {
                if (set.contains(cityPath[i]) && cityPath[i + 1] == actualCity) {
                    historySet.add(cityPath[i]);
                } else if (set.contains(cityPath[i + 1]) && cityPath[i] == actualCity) {
                    historySet.add(cityPath[i + 1]);
                }
            }
        }

        //watch out! - inverse
        int amount = set.size() - historySet.size();
        double attractProbabilityAmount = 1.0 / (amount + 1); //for cities not in best-of
        double repelProbabilityAmount = attractProbabilityAmount / historySet.size(); //for best-of cities
        double[] probabilities = new double[visited.length];
        for (int i = 0; i < visited.length; i++) {
            if(!historySet.contains(i)) {
                probabilities[i] = attractProbabilityAmount;
            }
            else {
                probabilities[i] = repelProbabilityAmount;
            }
        }
        return probabilities;
    }

    private double[] bestOf(int actualCity, boolean[] visited, List<Path> historicalPaths) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                set.add(i);
            }
        }

        Set<Integer> historySet = new HashSet<>();
        for (Path path : historicalPaths) {
            int[] cityPath = path.getCityPath();
            for (int i = 0; i < cityPath.length - 1; i++) {
                if (set.contains(cityPath[i]) && cityPath[i + 1] == actualCity) {
                    historySet.add(cityPath[i]);
                } else if (set.contains(cityPath[i + 1]) && cityPath[i] == actualCity) {
                    historySet.add(cityPath[i + 1]);
                }
            }
        }

        double probabilityAmount = 1.0 / historySet.size();
        double[] probabilities = new double[visited.length];
        for (Integer cityIndex : historySet) {
            probabilities[cityIndex] = probabilityAmount;
        }

        return probabilities;
    }

    private double[] pheromones(int actualCity, boolean[] visited, double[][] pheromones1, double[][] pheromones2, double alpha, double beta, int[][] firstCriterium, int[][] secondCriterium) {
        int numCities = pheromones1.length;
        double[] taueta = new double[numCities];
        double sum = 0;

        for (int j = 0; j < taueta.length; j++) {
            if(j==actualCity || visited[j]) {
                taueta[j] = 0;
            } else {
                taueta[j] = firstCriteriumWeight * (Math.pow(pheromones1[actualCity][j], alpha) * Math.pow(1.0/(firstCriterium[actualCity][j]), beta))
                        + secondCriteriumWeight * Math.pow(pheromones2[actualCity][j], alpha) * Math.pow(1.0/(secondCriterium[actualCity][j]), beta);
                taueta[j] = Math.max(taueta[j], 0.0001);
                taueta[j] = Math.min(taueta[j], Double.MAX_VALUE/(numCities*100));
            }
            sum += taueta[j];
        }

        double[] probabilites = new double[numCities];
        for (int j = 0; j < probabilites.length; j++) {
            probabilites[j] = taueta[j]/sum;
        }
        return probabilites;
    }
}