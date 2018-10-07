package Model;

public class Estimate2 implements Comparable<Estimate2> {

    private Integer averageCost;
    private Integer estimatedTime;
    private Position position;

    public Estimate2() {
    }

    public Estimate2(Integer averageCost, Integer estimatedTime, Position position) {
        this.averageCost = averageCost;
        this.estimatedTime = estimatedTime;
        this.position = position;
    }

    public Estimate2(Estimate estimate, Position position){
        this.averageCost = estimate.getAverageCost();
        this.estimatedTime = estimate.getEstimatedTime();
        this.position = position;
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
    public int compareTo(Estimate2 o) {
        if(o!=null) {
            return this.getAverageCost().compareTo(o.getAverageCost());
        }else {
            return -1;
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Estimate2 clone (Estimate estimate){
        Estimate2 estimate2 = new Estimate2();
        estimate2.setAverageCost(estimate.getAverageCost());
        estimate2.setEstimatedTime(estimate.getAverageCost());
        estimate2.setPosition(null);
        return estimate2;
    }
}
