package pl.agh.edu.intobl.ants.helpers;

import java.util.Set;

public interface ParetoSet<T> {
    boolean addParetoElementToSet(T element);
    Set<T> getNonDominatedSet();
}