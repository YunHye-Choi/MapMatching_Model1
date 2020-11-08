//sejung check
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
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
        ArrayList<Point> matching_success = new ArrayList<>();

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

		// 유네가 쓴 머지한 코드 시작///////////////////////////

        // 유네 깃허브연습 세번째~ 커밋푸시만 하는중~ 더 많은
        // pull request나 fetch등 기능 알아야할듯!
        // 그리고 push랑 commit밖에 안된다! 누군가 바꿔줘야 내가 
        // pull할 수 있는듯...? 내가 못하는것같기도 하다..흐윽

        // 20.10.06. GPS포인트 생성 구현

        GenerateGPSPoint(linkArrayList, gpsPointArrayList, routePointArrayList);
        // origin route points와 랜덤하게 생성된 GPS points 500ms에 한번씩 출력하기
        for(int i=0; i<gpsPointArrayList.size(); i++){
            Emission_Median(gpsPointArrayList.get(i), routePointArrayList.get(i));
            if(i>0){
                Transition_Median(gpsPointArrayList.get(i-1), gpsPointArrayList.get(i),routePointArrayList.get(i-1), routePointArrayList.get(i));
            }//매칭된 point로 해야하나.. 실제 point로 해야하나.. 의문?
            //중앙값 저장
        }

        for (int i = 0; i < gpsPointArrayList.size(); i++) {
            ArrayList<Link> candidateLink = new ArrayList<>();
            System.out.println(routePointArrayList.get(i));
            System.out.println(gpsPointArrayList.get(i));
            candidateLink.addAll(gpsPointArrayList.get(i).getPoint().findRadiusLink(linkArrayList,nodeArrayList));
            System.out.println("Link : "+candidateLink);
            ArrayList<Point> candidates= new ArrayList<>();
            for(int j=0;j<candidateLink.size();j++) {
                candidates.addAll(findRadiusPoint(gpsPointArrayList.get(i).getPoint(), candidateLink.get(j), 3));
            }
            System.out.println("candidate : "+candidates);
            //Thread.sleep(500); // 500ms 마다 출력
            matching_success.add(Matching(candidates, gpsPointArrayList, routePointArrayList, matching_success, i+1)); //size 1부터 시작
            System.out.print("matching: ");
            System.out.println(matching_success.get(i)); //매칭된 point 출력
            System.out.print("\n");
        }

        for(int i=0; i< matching_success.size(); i++)
        {
            System.out.println(matching_success.get(i));
        } //매칭된 결과값 출력


        // 유네가 쓴 머지한 코드 끝///////////////////

        //유림이가 썼던 코드
        Point gpsPoint = new Point(1.0,2.0);
        ArrayList<Link> candidateLink = new ArrayList<>();
        candidateLink = gpsPoint.findRadiusLink(linkArrayList,nodeArrayList);
        ArrayList<Point> candidate = new ArrayList<>();
        for(int i=0;i<candidateLink.size();i++)//모든 candidate Link 순회 하며, involving node들만 모아서 'candidate'에 저장
        {
            candidate.addAll(findRadiusPoint(gpsPoint, candidateLink.get(i), 2));
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


    //////////////////세정/////////////
    static ArrayList<Double> tp_median;
    static ArrayList<Double> ep_median; //실제 위치값과 gps값 차이. arraylist로 저장

    static {
        tp_median = new ArrayList<Double>();
        ep_median = new ArrayList<Double>();
    }
    // tp, ep 전역변수 선언, static:클래스에서도 호출 가능

    public static double Emission(GPSPoint gps, Point candidate, int size) {
        double ep_distance = 0;

        Point gpspoint = new Point(0.0, 0.0);
        gpspoint.setX(gps.getX());
        gpspoint.setY(gps.getY());

        ep_distance = coordDistanceofPoints(candidate, gpspoint); //후보point와 gps point의 유클리드 직선 거리

        if(size==1 || size == 2) {
            return ep_distance;
        } //size: gps배열 사이즈

        double ep = 0;
        double sigma=0;
        System.out.print("ep_median : ");
        System.out.println(ep_median.get(ep_median.size()/2));
        sigma = (1.4826) * ep_median.get((ep_median.size()/2));

        ep = Math.exp(Math.pow(ep_distance / sigma,2) * (-0.5)) / (Math.sqrt(2) * Math.PI * sigma);

        System.out.println(candidate);
        System.out.print("ep : ");
        System.out.println(ep);
        return ep;
    } //GPS와 후보의 거리 구하기, 중앙값 배열에 저장

    public static void Emission_Median(GPSPoint gps, Point matching){
        double ep_distance = 0;

        Point gpspoint = new Point(0.0, 0.0);
        gpspoint.setX(gps.getX());
        gpspoint.setY(gps.getY());

        ep_distance = coordDistanceofPoints(matching, gpspoint); //매칭된 포인트와 gps point의 유클리드 직선거리

        if(ep_median.size() == 0)
            ep_median.add(ep_distance);

        else {
            for (int i = 0; i < ep_median.size(); i++) {
                if (ep_median.get(i) > ep_distance) {
                    ep_median.add(i, ep_distance);
                    break;
                }
                if(i == ep_median.size()-1){
                    ep_median.add(ep_distance);
                    break;
                }
            }//위치 찾고 삽입
        }
    }//중앙값 저장

    public static double Transition(GPSPoint gps_pre, GPSPoint gps, Point matching_pre, Point candidate) {

        double tp_gps_distance, tp_route_distance;
        double dt=0;

        Point gpspoint_pre = new Point(0.0, 0.0);
        gpspoint_pre.setX(gps_pre.getX());
        gpspoint_pre.setY(gps_pre.getY());

        Point gpspoint = new Point(0.0, 0.0);
        gpspoint.setX(gps.getX());
        gpspoint.setY(gps.getY());
        tp_gps_distance = coordDistanceofPoints(gpspoint_pre, gpspoint); //이전gps_point 와 gps_point의 유클리드 직선거리

        tp_route_distance = coordDistanceofPoints(matching_pre, candidate); //이전 매칭된point와 후보의 유클리드 직선거리
        //실제 tp는 직선거리가 아니고 경로상의 거리여야함!!

        dt = Math.abs(tp_gps_distance-tp_route_distance); //gps와 경로 거리 차이 절대값
        double tp=0;
        double beta=0;

        beta = tp_median.get(tp_median.size()/2) / (Math.log(2));
        tp = Math.exp((dt * -1) / beta) / beta;

        System.out.print("tp : ");
        System.out.println(tp);
        return tp;
    }

    public static void Transition_Median(GPSPoint gps_pre, GPSPoint gps, Point matching_pre, Point matching){

        double tp_gps_distance, tp_route_distance;
        double dt=0;

        Point gpspoint_pre = new Point(0.0, 0.0);
        gpspoint_pre.setX(gps_pre.getX());
        gpspoint_pre.setY(gps_pre.getY());

        Point gpspoint = new Point(0.0, 0.0);
        gpspoint.setX(gps.getX());
        gpspoint.setY(gps.getY());
        tp_gps_distance = coordDistanceofPoints(gpspoint_pre, gpspoint); //이전gps_point 와 gps_point의 유클리드 직선거리

        tp_route_distance = coordDistanceofPoints(matching_pre, matching); //이전 실제 point와 실제 point의 유클리드 직선거리
        //실제 tp는 직선거리가 아니고 경로상의 거리여야함!!

        dt = Math.abs(tp_gps_distance-tp_route_distance); //gps와 경로 거리 차이 절대값

        if(tp_median.size() == 0)
            tp_median.add(dt);

        else {
            for (int i = 0; i < tp_median.size(); i++) {
                if (tp_median.get(i) > dt) {
                    tp_median.add(i, dt);
                    break;
                }
                if(i == tp_median.size()-1){
                    tp_median.add(dt);
                    break;
                }
            }//위치 찾고 삽입
        }
    }//중앙값 저장

    public static Point Matching(ArrayList<Point> candidates, ArrayList<GPSPoint> gpsPointArrayList, ArrayList<Point> routePointArrayList, ArrayList<Point> matching_success, int size){
        Point matching = new Point(0.0, 0.0);

        double maximun_tpep=0;

        if(size==1 || size==2){
            double min_ep=0;
            for(int i =0; i< candidates.size(); i++){
                if(i==0) {
                    min_ep = Emission(gpsPointArrayList.get(size-1), candidates.get(i), size); //gpspoint
                    matching = candidates.get(i);
                    System.out.println(min_ep);
                }
                else if(min_ep > Emission(gpsPointArrayList.get(size-1), candidates.get(i), size) ) {
                    min_ep = Emission(gpsPointArrayList.get(size-1), candidates.get(i), size);
                    matching = candidates.get(i);
                    System.out.println(min_ep);
                }
            }
            return matching;
        }//첫번째 포인트와 두번째 포인트는 ep만 확인

        maximun_tpep=0;
        for(int i =0; i< candidates.size(); i++){
            double tpep=0;
            tpep = Emission(gpsPointArrayList.get(size-1), candidates.get(i), size) * Transition(gpsPointArrayList.get(size-2), gpsPointArrayList.get(size-1), routePointArrayList.get(size-2), candidates.get(i));
            System.out.print("tpep : ");
            System.out.println(tpep);
            if(maximun_tpep < tpep){
                maximun_tpep = tpep;
                matching = candidates.get(i);
            }
        }

        System.out.println(ep_median.get(ep_median.size()/2));
        System.out.println(tp_median.get(tp_median.size()/2));

        return matching;
    }

    ////////////////////

}
