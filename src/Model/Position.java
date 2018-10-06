package Model;

public class Position {
    private Double x;
    private Double y;

    public Position(Double x,Double y){
        this.x=x;
        this.y=y;
    }

    public Position(){
        this.x=0.0d;
        this.y=0.0d;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }



}
