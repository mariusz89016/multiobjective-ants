package pl.agh.edu.intobl.ants.helpers.configs;

public class Factors {
    private final double pheromone;
    private final double dominance;
    private final double bestOf;
    private final double taboo;

    private Factors(double pheromone, double dominance, double bestOf, double taboo) {
        this.pheromone = pheromone;
        this.dominance = dominance;
        this.bestOf = bestOf;
        this.taboo = taboo;
    }

    public static Factors parseFrom(String line) {
        String[] arr = line.split(",");
        double pheromone = Double.parseDouble(arr[0]);
        double dominance = Double.parseDouble(arr[1]);
        double bestOf = Double.parseDouble(arr[2]);
        double tabu = Double.parseDouble(arr[3]);
        return new Factors(pheromone, dominance, bestOf, tabu);
    }

    @Override
    public String toString() {
        return "Factors{" +
                "pheromone=" + pheromone +
                ", dominance=" + dominance +
                ", bestOf=" + bestOf +
                ", taboo=" + taboo +
                '}';
    }

    public double getPheromone() {
        return pheromone;
    }

    public double getDominance() {
        return dominance;
    }

    public double getBestOf() {
        return bestOf;
    }

    public double getTaboo() {
        return taboo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factors factors = (Factors) o;

        if (Double.compare(factors.pheromone, pheromone) != 0) return false;
        if (Double.compare(factors.dominance, dominance) != 0) return false;
        if (Double.compare(factors.bestOf, bestOf) != 0) return false;
        return Double.compare(factors.taboo, taboo) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(pheromone);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dominance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bestOf);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(taboo);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}