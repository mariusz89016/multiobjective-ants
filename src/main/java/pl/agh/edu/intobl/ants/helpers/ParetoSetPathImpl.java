package pl.agh.edu.intobl.ants.helpers;

import java.util.HashSet;
import java.util.Set;

public class ParetoSetPathImpl implements ParetoSet<Path> {
    private Set<ParetoElement<Path>> set = new HashSet<>();


    @Override
    public boolean addParetoElementToSet(ParetoElement<Path> element) {
        final boolean isDominated = set.stream().anyMatch(element::isDominatedBy);
        if (isDominated) {
            return false;
        }
        removeDominatedElementsFromSet(element);
        set.add(element);

        return true;
    }

    private void removeDominatedElementsFromSet(ParetoElement<Path> elementToAdd) {
        set.removeIf(elementToAdd::isDominating);
    }

    @Override
    public Set<ParetoElement<Path>> getNonDominatedSet() {
        return set;
    }
}
