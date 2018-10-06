import Model.Estimate;
import Model.Estimate2;
import Model.Position;

public class WorkerThread implements Runnable {

    private Estimate2 estimate2;
    private Position position;
    private Double[] latLong;
    private String type;

    public WorkerThread(Position position, Double[] latLong, String type) {
        estimate2 = new Estimate2();
        this.position = position;
        this.latLong = latLong;
        this.type = type;
    }

    @Override
    public void run() {
        Main main = new Main();
        Estimate srcToTemp = main.calculateFareAndTime(
                latLong[0].floatValue(),
                latLong[1].floatValue(),
                position.getX().floatValue(),
                position.getY().floatValue(),
                type);
        Estimate tempToDest = main.calculateFareAndTime(
                position.getX().floatValue(),
                position.getY().floatValue(),
                latLong[2].floatValue(),
                latLong[3].floatValue(),
                type);
        Estimate2 total = new Estimate2();
        if (srcToTemp != null && tempToDest != null) {
            total.setAverageCost(srcToTemp.getAverageCost() + tempToDest.getAverageCost());
            total.setEstimatedTime(srcToTemp.getEstimatedTime() + tempToDest.getEstimatedTime());
            total.setPosition(position);
            ScrewberResource2.estimates.add(total);
        }
    }

    public Estimate2 getEstimate2() {
        return estimate2;
    }

    public void setEstimate2(Estimate2 estimate2) {
        this.estimate2 = estimate2;
    }
}
