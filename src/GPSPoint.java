import java.util.Random;

public class GPSPoint {
    private Point coordinate;
    private int timeStamp; // 일단 쉽게 짜려고 int형으로 생성. 필요시 수정 가능 (String형 혹은 다른 Time관련 클래스로)

    GPSPoint (int timeStamp, Point orgCoordinate) { // org means origin
        this.timeStamp = timeStamp;
        double gps_x, gps_y;
        Random random = new Random();
        gps_x = random.nextGaussian()*0.1+orgCoordinate.getX();
        gps_y = random.nextGaussian()*0.1+orgCoordinate.getY();
        coordinate = new Point (gps_x, gps_y);
    }

    public Point getPoint() {
        return coordinate;
    }

    //////sj////
    public Double getX(){return coordinate.getX();}
    public Double getY(){return coordinate.getY();}
    //////////

    public String toString() {
        return "[" + timeStamp+ "] " + coordinate;
    }
}
