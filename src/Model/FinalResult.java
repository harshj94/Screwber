package Model;

public class FinalResult {

    private String breakPoint;
    private String finalSource;
    private String finalDest;
    private int netSavings;
    private int finalTime;
    private int initialTime;
    private int initialCost;
    private int finalCost;

    public FinalResult(String breakPoint, String finalSource, String finalDest, int netSavings, int finalTime, int initialTime, int initialCost, int finalCost) {
        this.breakPoint = breakPoint;
        this.finalSource = finalSource;
        this.finalDest = finalDest;
        this.netSavings = netSavings;
        this.finalTime = finalTime;
        this.initialTime = initialTime;
        this.initialCost = initialCost;
        this.finalCost = finalCost;
    }

    public String getBreakPoint() {
        return breakPoint;
    }

    public void setBreakPoint(String breakPoint) {
        this.breakPoint = breakPoint;
    }

    public String getFinalSource() {
        return finalSource;
    }

    public void setFinalSource(String finalSource) {
        this.finalSource = finalSource;
    }

    public String getFinalDest() {
        return finalDest;
    }

    public void setFinalDest(String finalDest) {
        this.finalDest = finalDest;
    }

    public int getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(int netSavings) {
        this.netSavings = netSavings;
    }

    public int getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(int finalTime) {
        this.finalTime = finalTime;
    }

    public int getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(int initialTime) {
        this.initialTime = initialTime;
    }

    public int getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(int initialCost) {
        this.initialCost = initialCost;
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }
}
