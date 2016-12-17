package pl.agh.edu.intobl.ants.helpers;

public class Path implements ParetoElement<Integer> {
    private final int[] cityPath;
    private final int[][] firstCriterium;
    private final int[][] secondCriterium;

    public int[] getCityPath() {
        return cityPath;
    }

    private int firstCriteriumCalculated = -1;
    private int secondCriteriumCalculated = -1;

    public Path(int[] cityPath, int[][] firstCriterium, int[][] secondCriterium) {
        this.cityPath = cityPath;
        this.firstCriterium = firstCriterium;
        this.secondCriterium = secondCriterium;
    }

    @Override
    public Integer getFirstCriterium() {
        if(firstCriteriumCalculated == -1) {
            int sum = 0;
            for (int i = 0; i < cityPath.length - 1; i++) {
                int x = cityPath[i];
                int y = cityPath[i+1];
                sum += firstCriterium[x][y];
            }

            int x = cityPath[cityPath.length-1];
            int y = cityPath[0];
            sum += firstCriterium[x][y];
            firstCriteriumCalculated = sum;
        }
        return firstCriteriumCalculated;
    }

    @Override
    public Integer getSecondCriterium() {
        if(secondCriteriumCalculated == -1) {
            int sum = 0;
            for (int i = 0; i < cityPath.length - 1; i++) {
                int x = cityPath[i];
                int y = cityPath[i+1];
                sum += secondCriterium[x][y];
            }

            int x = cityPath[cityPath.length-1];
            int y = cityPath[0];
            sum += secondCriterium[x][y];
            secondCriteriumCalculated = sum;
        }
        return secondCriteriumCalculated;
    }


    @Override
    public int cityIndex() {
        return 0;
    }

    @Override
    public boolean isDominating(ParetoElement<Integer> element) {
        final Integer firstCriterium = element.getFirstCriterium();
        final Integer secondCriterium = element.getSecondCriterium();

        return (this.getFirstCriterium() <= firstCriterium && this.getSecondCriterium() <= secondCriterium) &&
                (this.getFirstCriterium() < firstCriterium || this.getSecondCriterium() < secondCriterium);
    }

    @Override
    public boolean isDominatedBy(ParetoElement<Integer> element) {
        return element.isDominating(this);
    }

}
