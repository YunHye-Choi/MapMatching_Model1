import java.util.ArrayList;

public class Point {
    private Double x; // x좌표
    private Double y; // y좌표

    // String을 파라미터로 가지는 생성자
    public Point (String x, String y){
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
    }

    // Double형을 파라미터로 가지는 생성자
    public Point (Double x, Double y){
        this.x = x;
        this.y = y;
    }

    // 출력~
    public String toString() {
        return "(" + x + ", "+ y +")";
    }

    public Double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ArrayList<Integer> findRadiusLink(ArrayList<Link> linkArrayList){
        int Radiusnum = 2;
        ArrayList<Integer> RadiusLinkID = new ArrayList<>();
        return RadiusLinkID;
    }//반경 내에 존재하는 link ID구하기

    public Double coordDistance(Point a){
        return Math.sqrt(Math.pow(a.getX()-this.getX(),2)+Math.pow(a.getY()-this.getY(),2));
    }//점과 점 사이의 거리 구하기
}
