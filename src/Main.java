//sejung check
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("===== [YSY] Map Matching Model 1 ====");

        // 파일이 저장된 path name
        // ㄱ자형: 1, ㅁ자형: 2, ㄹ자형: 3
        String directoryName = "simple_1"; // 이거 숫자만 변경하면됨!

        // 데이터를 보관할 ArrayList들
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        ArrayList<Link> linkArrayList = new ArrayList<>();
        ArrayList<GPSPoint> gpsPointArrayList = new ArrayList<>();
        ArrayList<Point> routePointArrayList = new ArrayList<>(); // 실제 경로의 points!

        /*=======Node.txt 파일읽어오기 작업========*/
        //파일 객체 생성
        File file1 = new File("./" + directoryName + "/Node.txt");
        //입력 스트림 생성
        FileReader fileReader1 = new FileReader(file1);
        //BufferedReader 클래스 이용하여 파일 읽어오기
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
        System.out.println("======== Node 정보 =======");
        while (bufferedReader1.ready()) {
            String line = bufferedReader1.readLine();
            String[] lineArray = line.split("\t");
            Point coordinate = new Point(lineArray[1], lineArray[2]);
            Node node = new Node(lineArray[0], coordinate); // 노드생성
            nodeArrayList.add(node); // nodeArrayList에 생성한 노드 추가
            System.out.println(node);
        }
        // close the bufferedReader
        bufferedReader1.close();

        /*=======Link.txt 파일읽어오기 작업========*/
        //파일 객체 생성
        File file2 = new File("./" + directoryName + "/Link.txt");
        //입력 스트림 생성
        FileReader fileReader2 = new FileReader(file2);
        //BufferedReader 클래스 이용하여 파일 읽어오기
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        System.out.println("======== Link 정보 =======");
        while (bufferedReader2.ready()) {
            String line = bufferedReader2.readLine();
            String[] lineArray = line.split("\t");
            //Point coordinate = new Point (lineArray[1], lineArray[2]);
            // weight 구하기 - 피타고라스법칙 적용
            // a=밑변 b=높이 weight=(a제곱+b제곱)의 제곱근의 반올림값
            Double a = nodeArrayList.get(Integer.parseInt(lineArray[1])).getCoordinate().getX()
                    - nodeArrayList.get(Integer.parseInt(lineArray[2])).getCoordinate().getX();
            Double b = nodeArrayList.get(Integer.parseInt(lineArray[1])).getCoordinate().getY()
                    - nodeArrayList.get(Integer.parseInt(lineArray[2])).getCoordinate().getY();
            Double weight = (double) Math.round(Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));

            // link 생성
            Link link = new Link(lineArray[0], lineArray[1], lineArray[2], weight);

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
                for (int x_cord = (int) xs; x_cord <= (int) xe; x_cord++) {
                    // xs가 5.1등과 같이 (int)5.1 즉 5보다 큰 경우 (int)5.1 즉 5는 involvingPoint가 될수없음
                    // 5.0등인 case는 else이하 로직을 수행할 수 있도록 함
                    if (x_cord < xs) continue;

                        // involvingPointList에 Point 추가
                    else {
                        involvingPointList.add(new Point((double) x_cord, ys));
                    }
                }

            }
            // link 기울기가 무한인 경우 : |
            else if (xs == xe) {
                // y값이 정수인 경우만 involvingPoint에 추가 (int의 ++연산)
                for (int y_cord = (int) ys; y_cord <= (int) ye; y_cord++) {
                    // ys가 5.1등과 같이 (int)5.1 즉 5보다 큰 경우 (int)5.1 즉 5는 involvingPoint가 될수없음
                    // 5.0등인 case는 else이하 로직을 수행할 수 있도록 함
                    if (y_cord < ys) continue;

                        // involvingPointList에 Point 추가
                    else {
                        involvingPointList.add(new Point(xs, (double) y_cord));
                    }
                }
            }

            link.setInvolvingPointList(involvingPointList);

            /* 아직 필요없어보여서 안짬 - 필요시 추가 예정
            // 기울기가 무한아닌 양수인 경우 : /

            // 기울기가 음수인 경우 : \
            */

            linkArrayList.add(link); // linkArrayList에 생성한 노드 추가
            System.out.println(link);
            System.out.print("involving points:");
            System.out.println(link.getInvolvingPointList());
        }
        // close the bufferedReader
        bufferedReader2.close();

        // 유네 깃허브연습 세번째~ 커밋푸시만 하는중~ 더 많은
        // pull request나 fetch등 기능 알아야할듯!
        // 그리고 push랑 commit밖에 안된다! 누군가 바꿔줘야 내가 
        // pull할 수 있는듯...? 내가 못하는것같기도 하다..흐윽

        // 20.10.06. GPS포인트 생성 구현

        GenerateGPSPoint(linkArrayList, gpsPointArrayList, routePointArrayList);

        // origin route points와 랜덤하게 생성된 GPS points 500ms에 한번씩 출력하기
        for (int i = 0; i < gpsPointArrayList.size(); i++) {
            System.out.println(routePointArrayList.get(i));
            System.out.println(gpsPointArrayList.get(i));
            Thread.sleep(500); // 500ms 마다 출력
        }
    }
    // GPS 포인트 1초에 1개씩 생성하는 함수
    private static void GenerateGPSPoint(ArrayList<Link> linkArrayList, ArrayList<GPSPoint> gpsPointArrayList, ArrayList<Point> routePointArrayList) {
        /////////// ㄱ, ㅁ, ㄹ에만 적용되는 코드임//////////
        System.out.println("======GPS Data=======");
        boolean isThereEdgeLink;
        Link startLink = new Link(0, 0, 0, 0.0);
        ArrayList<Integer> overlappedNodeIDs = new ArrayList<>();
        ArrayList<Integer> nodeIDs = new ArrayList<>();
        ArrayList<Integer> linkIDs = new ArrayList<>();
        // nodeID 중복허용 모두 저장
        for (Link link : linkArrayList) {
            overlappedNodeIDs.add(link.getStartNodeID());
            overlappedNodeIDs.add(link.getEndNodeID());
        }
        // 중복되지 않는 노드만 저장
        for (int index1 = 0; index1 < overlappedNodeIDs.size(); index1++) {
            for (int index2 = 0; index2 < overlappedNodeIDs.size(); index2++) {
                if (index1 == index2) continue;
                else if (overlappedNodeIDs.get(index1) == overlappedNodeIDs.get(index2)) {
                    break;
                }
                if (index2 == overlappedNodeIDs.size() - 1) {
                    nodeIDs.add(overlappedNodeIDs.get(index1));
                }
            }
        }

        // 만약 노드 중 end이기만 하거나 start이기만 한 node (끝 노드) 가 있다면
        // nodeID가 작은 node를 end/start로 가지는 link가 시작링크
        // end이기만 하거나 start이기만 한 link 가 없다면 0을 startNodeID로 갖는 link가 시작링크
        int minNodeID = 0;
        if (nodeIDs.size() == 2) { // 끝노드 2개인 경우
            minNodeID = (nodeIDs.get(0) < nodeIDs.get(1)) ? nodeIDs.get(0) : nodeIDs.get(1);
        } else if (nodeIDs.size() == 1) { // 끝노드 1개인 경우
            minNodeID = nodeIDs.get(0);
        }
        for (Link link : linkArrayList) {
            if (link.getStartNodeID() == minNodeID || link.getEndNodeID() == minNodeID) {
                startLink = link;
                break;
            }
        }
        // currNodeID를 startNodeID 혹은 endNodeID 로 가지는 Link 중복체크하면서 하나씩 불러오기 (반복)
        int count = 0;
        int currNodeID = startLink.getEndNodeID();
        int timeStamp = 0; // 추후 String으로 변경
        linkIDs.add(startLink.getLinkID());
        while (count < linkArrayList.size()) {
            if (count == linkArrayList.size()) break;

            // startLink의 involvingPoints는 무조건 이용!
            if (count == 0) {
                ArrayList<Point> involvingPoints = startLink.getInvolvingPointList();
                for (Point point : involvingPoints) {
                    routePointArrayList.add(point); // routePointArrayList에 추가
                    GPSPoint gpsPoint = new GPSPoint(timeStamp, point); // GPS 포인트 생성!
                    gpsPointArrayList.add(gpsPoint); // gpsPointArrayList에 추가
                    timeStamp++;
                }
            }
            for (Link link : linkArrayList) {
                // currNodeID를 startNodeID로 가지는 Link 불러오기
                if (link.getStartNodeID() == currNodeID && !linkIDs.contains(link.getLinkID())) {
                    // GPS 생성을 위한 link의 involvingPoints출력
                    linkIDs.add(link.getLinkID()); // 중복검색 방지
                    ArrayList<Point> involvingPoints = link.getInvolvingPointList();
                    for (Point point : involvingPoints) {
                        routePointArrayList.add(point); // routePointArrayList에 추가
                        GPSPoint gpsPoint = new GPSPoint(timeStamp, point); // GPS 포인트 생성!
                        gpsPointArrayList.add(gpsPoint); // gpsPointArrayList에 추가
                        timeStamp++;
                    }
                    currNodeID = link.getEndNodeID();
                }
                // currNodeID를 endNodeID로 가지는 Link 불러오기
                else if (link.getEndNodeID() == currNodeID && !linkIDs.contains(link.getLinkID())) {
                    // GPS 생성을 위한 link의 involvingPoints출력
                    linkIDs.add(link.getLinkID()); // 중복검색 방지
                    ArrayList<Point> involvingPoints = link.getInvolvingPointList();
                    for (int i = involvingPoints.size() - 1; i >= 0; i--) {
                        routePointArrayList.add(involvingPoints.get(i)); // routePointArrayList에 추가
                        GPSPoint gpsPoint = new GPSPoint(timeStamp, involvingPoints.get(i)); // GPS 포인트 생성!
                        gpsPointArrayList.add(gpsPoint); // gpsPointArrayList에 추가
                        timeStamp++;
                    }
                    // 다음 currNode지정: start인 경우에는 그 링크의 endNode를, 반대의 경우에는 startNode를 currNode로 지정
                    currNodeID = link.getStartNodeID();
                }
            }
            count++;
        }
    }
}

