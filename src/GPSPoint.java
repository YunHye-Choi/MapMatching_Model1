import java.util.Random;

public class GPSPoint {
    private Point coordinate;
    private int timeStamp; // 일단 쉽게 짜려고 int형으로 생성. 필요시 수정 가능 (String형 혹은 다른 Time관련 클래스로)

    GPSPoint (int timeStamp, Point orgCoordinate) { // org means origin
        this.timeStamp = timeStamp;
        double gps_x, gps_y;
        Random random = new Random();
        gps_x = random.nextGaussian()+orgCoordinate.getX();
        gps_y = random.nextGaussian()+orgCoordinate.getY();
        coordinate = new Point (gps_x, gps_y);
    }

    public String toString() {
        return "[" + timeStamp+ "] " + coordinate;
    }
}
