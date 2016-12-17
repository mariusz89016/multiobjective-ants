package pl.agh.edu.intobl.ants.helpers;

public class IntegerElement implements ParetoElement<Integer> {
    private final Integer firstCriterium;
    private final Integer secondCriterium;
    private final int cityIndex;

    public IntegerElement(Integer firstCriterium, Integer secondCriterium, int cityIndex) {
        this.firstCriterium = firstCriterium;
        this.secondCriterium = secondCriterium;
        this.cityIndex = cityIndex;
    }

    @Override
    public Integer getFirstCriterium() {
        return firstCriterium;
    }

    @Override
    public Integer getSecondCriterium() {
        return secondCriterium;
    }

    @Override
    public int cityIndex() {
        return cityIndex;
    }

    @Override
    public boolean isDominating(ParetoElement<Integer> element) {
//        A > B
//                (a1, a2) (b1, b2)
//        a1 <= b1 && a2 <= b2
//                &&
//        a1 < b1 || a2 < b2
        final Integer firstCriterium = element.getFirstCriterium();
        final Integer secondCriterium = element.getSecondCriterium();

        return (this.getFirstCriterium() <= firstCriterium && this.getSecondCriterium() <= secondCriterium) &&
                (this.getFirstCriterium() < firstCriterium || this.getSecondCriterium() < secondCriterium);
    }

    @Override
    public boolean isDominatedBy(ParetoElement<Integer> element) {
        return element.isDominating(this);
    }

    @Override
    public String toString() {
        return "IntegerElement{" +
                "firstCriterium=" + firstCriterium +
                ", secondCriterium=" + secondCriterium +
                ", cityIndex=" + cityIndex +
                '}';
    }
}
