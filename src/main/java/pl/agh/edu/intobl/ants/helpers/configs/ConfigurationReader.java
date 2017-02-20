package pl.agh.edu.intobl.ants.helpers.configs;

import java.io.IOException;

public interface ConfigurationReader {
    Factors getConfiguration(int iteration);
    int getNumberOfIteration();
    void saveCopy(String filename) throws IOException;
}
