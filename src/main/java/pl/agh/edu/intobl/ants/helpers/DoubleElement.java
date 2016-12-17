package pl.agh.edu.intobl.ants.helpers;

public class DoubleElement implements ParetoElement<Double> {
    @Override
    public Double getFirstCriterium() {
        return null;
    }

    @Override
    public Double getSecondCriterium() {
        return null;
    }

    @Override
    public int cityIndex() {
        return 0;
    }

    @Override
    public boolean isDominating(ParetoElement<Double> element) {
        return false;
    }

    @Override
    public boolean isDominatedBy(ParetoElement<Double> element) {
        return false;
    }
}
