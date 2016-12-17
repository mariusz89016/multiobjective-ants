package pl.agh.edu.intobl.ants.loaders;

import java.io.*;

public class TSPLoader {
    private int numberOfCities;
    private final double[][] matrix;
    private final String file;

    public TSPLoader(String file) throws IOException {
        this.file = file;
        this.matrix = readProblem();
    }

    public double[][] readProblem() throws IOException {
        InputStream in = getClass().getResourceAsStream(file);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StreamTokenizer token = new StreamTokenizer(br);

        skipToDimensionSection(token);
        numberOfCities = (int) token.nval;
        skipToEdgesWeight(token);

        return readEdgesWeight(token, numberOfCities);
    }

    private double[][] readEdgesWeight(StreamTokenizer token, int numberOfCities) throws IOException {
        double[][] matrix = new double[numberOfCities][numberOfCities];
        double[] c = new double[2 * numberOfCities];

        for (int i = 0; i < numberOfCities; i++) {
            token.nextToken();
            int j = (int) token.nval;

            token.nextToken();
            c[2 * (j - 1)] = token.nval;
            token.nextToken();
            c[2 * (j - 1) + 1] = token.nval;
        }

        double dist;
        for (int k = 0; k < numberOfCities; k++) {
            matrix[k][k] = 0;
            for (int j = k + 1; j < numberOfCities; j++) {
                dist = Math.sqrt(Math.pow((c[k * 2] - c[j * 2]), 2.0) +
                        Math.pow((c[k * 2 + 1] - c[j * 2 + 1]), 2));
                dist = (int) (dist + .5);
                matrix[k][j] = dist;
                matrix[j][k] = dist;
            }
        }

        return matrix;
    }

    private void skipToEdgesWeight(StreamTokenizer token) throws IOException {
        boolean found;// Find the string SECTION
        found = false;
        do {
            token.nextToken();
            if ((token.sval != null) && ((token.sval.compareTo("SECTION") == 0))) {
                found = true;
            }
        } while (!found);
    }

    private void skipToDimensionSection(StreamTokenizer token) throws IOException {
        boolean found = false;
        do {
            token.nextToken();
            if ((token.sval != null) && ((token.sval.equals("DIMENSION")))) {
                found = true;
            }

        } while (!found);

        token.nextToken(); //skip 'DIMENSION'
        token.nextToken(); //skip ':'
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
