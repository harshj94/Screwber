package Model;

public class Estimate implements Comparable<Estimate> {

    private Integer averageCost;
    private Integer estimatedTime;

    public Estimate() {
    }

    public Estimate(Integer averageCost, Integer estimatedTime) {
        this.averageCost = averageCost;
        this.estimatedTime = estimatedTime;
    }

    public Integer getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Integer averageCost) {
        this.averageCost = averageCost;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Override
    public int compareTo(Estimate o) {
        return this.getAverageCost().compareTo(o.getAverageCost());
    }

    public Estimate2 clone (Estimate estimate){
        Estimate2 estimate2 = new Estimate2();
        estimate2.setAverageCost(estimate.getAverageCost());
        estimate2.setEstimatedTime(estimate.getAverageCost());
        estimate2.setPosition(null);
        return estimate2;
    }
}
