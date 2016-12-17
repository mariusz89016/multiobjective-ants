package pl.agh.edu.intobl.ants.loaders;

import java.io.*;

public class BiobjectiveTSPLoader {
    private final double[][] firstCriterium;
    private final double[][] secondCriterium;

    public BiobjectiveTSPLoader(String file1, String file2) throws IOException {
        this.firstCriterium = new TSPLoader(file1).getMatrix();
        this.secondCriterium = new TSPLoader(file2).getMatrix();
    }

    public int getNumberOfCities() {
        return firstCriterium.length;
    }

    public int[][] getSecondCriterium() {
        int[][] results = new int[secondCriterium.length][secondCriterium.length];
        for (int i = 0; i < secondCriterium.length; i++) {
            for (int j = 0; j < secondCriterium.length; j++) {
                results[i][j] = (int) secondCriterium[i][j];
            }
        }
        return results;
    }

    public int[][] getFirstCriterium() {
        int[][] results = new int[firstCriterium.length][firstCriterium.length];
        for (int i = 0; i < firstCriterium.length; i++) {
            for (int j = 0; j < firstCriterium.length; j++) {
                results[i][j] = (int) firstCriterium[i][j];
            }
        }
        return results;
    }
}
