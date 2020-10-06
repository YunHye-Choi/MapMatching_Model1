import java.util.ArrayList;
import java.util.Vector;

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

    public ArrayList<Link> findRadiusLink(ArrayList<Link> linkArrayList, ArrayList<Node> nodeArrayList){
        int Radiusnum = 2;
        ArrayList<Link> RadiusLinkID = new ArrayList<>();
        for(int i=0;i<linkArrayList.size();i++){//원 안에 시작,끝점이 있는 경우
            if(this.coordDistance(nodeArrayList.get(linkArrayList.get(i).getStartNodeID()).getCoordinate())<Radiusnum||
                    this.coordDistance(nodeArrayList.get(linkArrayList.get(i).getEndNodeID()).getCoordinate())<Radiusnum)
                RadiusLinkID.add(linkArrayList.get(i));
            else{// 원 안에 시작, 끝점이 없는 경우
                double startX = nodeArrayList.get(linkArrayList.get(i).getStartNodeID()).getCoordinate().getX();
                double startY = nodeArrayList.get(linkArrayList.get(i).getStartNodeID()).getCoordinate().getY();
                double endX = nodeArrayList.get(linkArrayList.get(i).getEndNodeID()).getCoordinate().getX();
                double endY = nodeArrayList.get(linkArrayList.get(i).getEndNodeID()).getCoordinate().getY();

                double inclination = (startY-endY)/(startX-endX);
                double Y_Intercept = -(inclination*startX)+startY;

                //원의 중심 부터 직선 사이의 거리가 반지름보다 작은 경우
                if(Math.abs(inclination*this.getX()-this.getY()+Y_Intercept)/Math.sqrt(Math.pow(inclination,2)+1)<Radiusnum) {
                    //원의 중심 부터 직선까지의 거리가 r보다 작다면 다음 식에 들어오게 된다.
                    Vector2D vectorFromCircleToLine1 = new Vector2D(endX-this.getX(),endY-this.getY());
                    Vector2D vectorFromCircleToLine2 = new Vector2D(startX-this.getX(),startY-this.getY());
                    Vector2D vectorLine1 = new Vector2D(startX-endX,startY-endY);
                    Vector2D vectorLine2 = new Vector2D(endX-startX,endY-startY);
                    if(Math.acos(vectorFromCircleToLine1.dot(vectorLine1))>=90&&Math.acos(vectorFromCircleToLine2.dot(vectorLine2))>=90)
                        RadiusLinkID.add(linkArrayList.get(i));
                }
            }
        }
        return RadiusLinkID;
    }//반경 내에 존재하는 link ID구하기

    public Double coordDistance(Point a){
        return Math.sqrt(Math.pow(a.getX()-this.getX(),2)+Math.pow(a.getY()-this.getY(),2));
    }//점과 점 사이의 거리 구하기
}
