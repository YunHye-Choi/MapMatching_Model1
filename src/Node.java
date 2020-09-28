public class Node {
    private int nodeID; // nodeID
    private Point coordinate; // Node의 좌표

    // ID를 String형으로 받는 Node 생성자
    public Node (String nodeID, Point coordinate) {
        this.nodeID = Integer.parseInt(nodeID);
        this.coordinate = coordinate;
    }

    // ID를 int형으로 받는 Node 생성자
    public Node (int nodeID, Point coordinate) {
        this.nodeID = nodeID;
        this.coordinate = coordinate;
    }

    // Getters and Setters
    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public String toString() {
        return "[" + nodeID + "]\t" + "(" +coordinate.getX().toString() +", "
                + coordinate.getY().toString()+")";
    }
}
