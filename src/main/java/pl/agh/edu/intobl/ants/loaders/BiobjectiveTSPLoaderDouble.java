package pl.agh.edu.intobl.ants.loaders;

import java.io.IOException;

public class BiobjectiveTSPLoaderDouble {
    private final double[][] firstCriterium;
    private final double[][] secondCriterium;

    public BiobjectiveTSPLoaderDouble(String file1, String file2) throws IOException {
        this.firstCriterium = new TSPLoader(file1).getMatrix();
        this.secondCriterium = new TSPLoader(file2).getMatrix();
    }

    public int getNumberOfCities() {
        return firstCriterium.length;
    }

    public double[][] getSecondCriterium() {
        return secondCriterium;
    }

    public double[][] getFirstCriterium() {
        return firstCriterium;
    }
}
