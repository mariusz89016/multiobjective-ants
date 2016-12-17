package pl.agh.edu.intobl.ants.helpers;

import java.util.HashSet;
import java.util.Set;

public class ParetoSetImpl implements ParetoSet<Integer> {
    private Set<ParetoElement<Integer>> set = new HashSet<>();

    @Override
    public boolean addParetoElementToSet(ParetoElement<Integer> elementToAdd) {
        final boolean isDominated = set.stream().anyMatch(elementToAdd::isDominatedBy);
        if (isDominated) {
            return false;
        }
        removeDominatedElementsFromSet(elementToAdd);
        set.add(elementToAdd);

        return true;
    }

    private void removeDominatedElementsFromSet(ParetoElement<Integer> elementToAdd) {
        set.removeIf(elementToAdd::isDominating);
    }

    @Override
    public Set<ParetoElement<Integer>> getNonDominatedSet() {
        return set;
    }
}

