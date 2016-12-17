package pl.agh.edu.intobl.ants.helpers;

public interface ParetoElement<T> {
    T getFirstCriterium();
    T getSecondCriterium();
    int cityIndex();
    boolean isDominating(ParetoElement<T> element);
    boolean isDominatedBy(ParetoElement<T> element);
}
