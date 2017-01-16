package pl.agh.edu.intobl.ants.helpers;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AttractivenessCalculatorTest {
    @Test
    public void shouldCalculateProperMoveProbabilities() throws Exception {
//        *1       *2        *3
//          \       |       /
//        (2, 3) (1, 2)  (2, 1)
//             \    |    /
//                  *0

        int actualCity = 0;
        boolean[] visited = new boolean[] {true, false, false, false};
        int[][] firstCriterium =  new int[][] {{0, 2, 1, 2},
                                               {2, 2, 1, 1},
                                               {1, 1, 0, 1},
                                               {2, 1, 1, 0}};
        int[][] secondCriterium = new int[][] {{0, 3, 2, 1},
                                               {3, 0, 0, 0},
                                               {2, 0, 0, 0},
                                               {1, 0, 0, 0}};
        final AttractivenessCalculator attractivenessCalculator = new AttractivenessCalculator(1.0, 0.0, 0.0, 0.0, 0.0);
        double[][] array = new double[0][0];
//        final double[] probabilities = attractivenessCalculator.movesProbability(actualCity, visited, array, firstCriterium, secondCriterium, null, 0.0, 0.0);

//        assertTrue(probabilities[0] == 0.0);
//        assertTrue(probabilities[1] == 0.0);
//        assertTrue(probabilities[2] == 0.5);
//        assertTrue(probabilities[3] == 0.5);
    }
}