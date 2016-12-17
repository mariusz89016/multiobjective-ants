package pl.agh.edu.intobl.ants.helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttractivenessCalculator {
    private final double dominanceFactor;
    private final double historyFactor;
    private final double pheromoneFactor;
    private final double firstCriteriumWeight;
    private final double secondCriteriumWeight;

    public AttractivenessCalculator(double dominanceFactor, double historyFactor, double pheromoneFactor, double firstCriteriumWeight, double secondCriteriumWeight) {
        this.dominanceFactor = dominanceFactor;
        this.historyFactor = historyFactor;
        this.pheromoneFactor = pheromoneFactor;
        this.firstCriteriumWeight = firstCriteriumWeight;
        this.secondCriteriumWeight = secondCriteriumWeight;
    }

    public double[] movesProbability(int actualCity, boolean[] visited, double[][] initialPheromones, int[][] firstCriterium, int[][] secondCriterium, List<Path> historicalPaths, double alpha, double beta) {
        double[] probability = new double[visited.length];
        double[] dominance = dominance(actualCity, visited, firstCriterium, secondCriterium);
        double[] history = history(actualCity, visited, historicalPaths);
        double[] pheromones = pheromones(actualCity, visited, initialPheromones, alpha, beta, firstCriterium, secondCriterium);

        for (int i = 0; i < probability.length; i++) {
            probability[i] = dominanceFactor * dominance[i] + historyFactor * history[i] + pheromoneFactor * pheromones[i];
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
        ParetoSet<Integer> paretoSet = new ParetoSetImpl();
        for (int i = 0; i < firstCriterium[actualCity].length; i++) {
            if (!visited[i]) {
                final IntegerElement element = new IntegerElement(firstCriterium[actualCity][i], secondCriterium[actualCity][i], i);
                paretoSet.addParetoElementToSet(element);
            }
        }
        final Set<ParetoElement<Integer>> nonDominatedSet = paretoSet.getNonDominatedSet();

        double probabilityAmount = 1.0 / nonDominatedSet.size();
        double[] probabilities = new double[visited.length];
        for (ParetoElement<Integer> element : nonDominatedSet) {
            probabilities[element.cityIndex()] = probabilityAmount;
        }

        return probabilities;
    }

    private double[] history(int actualCity, boolean[] visited, List<Path> historicalPaths) {
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

    private double[] pheromones(int actualCity, boolean[] visited, double[][] initialPheromones, double alpha, double beta, int[][] firstCriterium, int[][] secondCriterium) {
        int numCities = initialPheromones.length;
        double[] taueta = new double[numCities];
        double sum = 0;

        for (int j = 0; j < taueta.length; j++) {
            if(j==actualCity || visited[j]) {
                taueta[j] = 0;
            } else {
                taueta[j] = Math.pow(initialPheromones[actualCity][j], alpha) *
                        Math.pow(1.0/(firstCriteriumWeight*firstCriterium[actualCity][j] + secondCriteriumWeight*secondCriterium[actualCity][j]), beta);
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