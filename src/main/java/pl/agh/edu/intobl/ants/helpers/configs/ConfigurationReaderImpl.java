package pl.agh.edu.intobl.ants.helpers.configs;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private final SortedMap<Integer, Factors> factorsMap = new TreeMap<>();
    private final int numberOfIteration;
    private final List<String> content;

    public ConfigurationReaderImpl(String filename) throws IOException {
        content = Files.readAllLines(Paths.get(filename));
        numberOfIteration = Integer.parseInt(content.get(0));

        for (int i = 1; i < content.size(); i += 2) {
            int iteration = Integer.parseInt(content.get(i));
            final String factorsLine = content.get(i + 1);
            factorsMap.put(iteration, Factors.parseFrom(factorsLine));
        }
    }

    @Override
    public Factors getConfiguration(int iteration) {
        int last = factorsMap.firstKey();
        for (Integer integer : factorsMap.keySet()) {
            if(integer > iteration) {
                break;
            }
            last = integer;
        }
        return factorsMap.get(last);
    }

    @Override
    public int getNumberOfIteration() {
        return numberOfIteration;
    }

    @Override
    public void saveCopy(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (String line : content) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
