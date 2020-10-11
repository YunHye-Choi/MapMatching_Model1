//sejung check
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("===== [YSY] Map Matching Model 1 ====");

        // 데이터를 보관할 ArrayList들
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        ArrayList<Link> linkArrayList = new ArrayList<>();

        /*=======Node.txt 파일읽어오기 작업========*/
        //파일 객체 생성
        File file1 = new File("C:\\MapMatching\\Model1\\simple_3\\Node.txt");
        //입력 스트림 생성
        FileReader fileReader1 = new FileReader(file1);
        //BufferedReader 클래스 이용하여 파일 읽어오기
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
        while(bufferedReader1.ready()){
            String line = bufferedReader1.readLine();
            String [] lineArray = line.split("\t");
            Point coordinate = new Point (lineArray[1], lineArray[2]);
            Node node = new Node (lineArray[0], coordinate); // 노드생성
            nodeArrayList.add(node); // nodeArrayList에 생성한 노드 추가
             System.out.println(node);
        }
        // close the bufferedReader
        bufferedReader1.close();

        /*=======Link.txt 파일읽어오기 작업========*/
        //파일 객체 생성
        File file2 = new File("C:\\MapMatching\\Model1\\simple_3\\Link.txt");
        //입력 스트림 생성
        FileReader fileReader2 = new FileReader(file2);
        //BufferedReader 클래스 이용하여 파일 읽어오기
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        while(bufferedReader2.ready()){
            String line = bufferedReader2.readLine();
            String [] lineArray = line.split("\t");
            //Point coordinate = new Point (lineArray[1], lineArray[2]);
            // weight 구하기 - 피타고라스법칙 적용
            // a=밑변 b=높이 weight=(a제곱+b제곱)의 제곱근의 반올림값
            Double a = nodeArrayList.get(Integer.parseInt(lineArray[1])).getCoordinate().getX()
                    - nodeArrayList.get(Integer.parseInt(lineArray[2])).getCoordinate().getX();
            Double b = nodeArrayList.get(Integer.parseInt(lineArray[1])).getCoordinate().getY()
                    - nodeArrayList.get(Integer.parseInt(lineArray[2])).getCoordinate().getY();
            Double weight = (double) Math.round(Math.sqrt(Math.pow(a,2) + Math.pow(b,2)));

            // link 생성
            Link link  = new Link (lineArray[0], lineArray [1], lineArray [2], weight);

            //involving points 구하기

            // start point와 end point 좌표 지정
            double xs = nodeArrayList.get(link.getStartNodeID()).getCoordinate().getX();
            double ys = nodeArrayList.get(link.getStartNodeID()).getCoordinate().getY();
            double xe = nodeArrayList.get(link.getEndNodeID()).getCoordinate().getX();
            double ye = nodeArrayList.get(link.getEndNodeID()).getCoordinate().getY();

            ArrayList<Point> involvingPointList = new ArrayList<>();

            // link 기울기가 0인 경우 : ㅡ
            if (ys == ye) {
                // y값이 정수인 경우만 involvingPoint에 추가 (int의 ++연산)
                for (int x_cord= (int)xs ; x_cord <= (int)xe; x_cord++) {
                    // xs가 5.1등과 같이 (int)5.1 즉 5보다 큰 경우 (int)5.1 즉 5는 involvingPoint가 될수없음
                    // 5.0등인 case는 else이하 로직을 수행할 수 있도록 함
                    if (x_cord < xs) continue;

                    // involvingPointList에 Point 추가
                    else {
                        involvingPointList.add(new Point((double)x_cord,ys));
                    }
                }

            }
            // link 기울기가 무한인 경우 : |
            else if (xs == xe) {
                // y값이 정수인 경우만 involvingPoint에 추가 (int의 ++연산)
                for (int y_cord= (int)ys ; y_cord <= (int)ye; y_cord++) {
                    // ys가 5.1등과 같이 (int)5.1 즉 5보다 큰 경우 (int)5.1 즉 5는 involvingPoint가 될수없음
                    // 5.0등인 case는 else이하 로직을 수행할 수 있도록 함
                    if (y_cord < ys) continue;

                    // involvingPointList에 Point 추가
                    else {
                        involvingPointList.add(new Point(xs,(double)y_cord));
                    }
                }
            }

            link.setInvolvingPointList(involvingPointList);

            /* 아직 필요없어보여서 안짬 - 필요시 추가 예정
            // 기울기가 무한아닌 양수인 경우
            else {

            }

            // 기울기가 음수인 경우
            */

            linkArrayList.add(link); // linkArrayList에 생성한 노드 추가
            System.out.println(link);
            System.out.print("involving points:");
            System.out.println(link.getInvolvingPointList());
        }
        // close the bufferedReader
        bufferedReader2.close();

        Point gpsPoint = new Point(1.0,2.0);
        ArrayList<Link> candidateLink = new ArrayList<>();
        candidateLink = gpsPoint.findRadiusLink(linkArrayList,nodeArrayList);
        ArrayList<Point> candidate = new ArrayList<>();
        for(int i=0;i<candidateLink.size();i++)//모든 candidate Link 순회 하며, involving node들만 모아서 'candidate'에 저장
            candidate.addAll(findRadiusPoint(gpsPoint,candidateLink.get(i),2));
    }

    public static Double coordDistanceofPoints(Point a, Point b){
        return Math.sqrt(Math.pow(a.getX()-b.getX(),2)+Math.pow(a.getY()-b.getY(),2));
    }//유클리드 거리 구하기

    public static ArrayList<Point> findRadiusPoint(Point center, Link link, Integer Radius){//Link 안, 반경 내 involving node들만 반환
        ArrayList<Point> allInvolvingPoint =link.getInvolvingPointList();
        ArrayList<Point> resultPoint = new ArrayList<>();
        for(int i=0;i<allInvolvingPoint.size();i++){
            if(coordDistanceofPoints(center,allInvolvingPoint.get(i))<=Radius)
                resultPoint.add(allInvolvingPoint.get(i));
        }
        return resultPoint;
    }
}
