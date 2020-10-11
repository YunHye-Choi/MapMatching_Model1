import java.util.ArrayList;

public class Link {
    private int linkID; // Link ID
    private int startNodeID; // Link의 Start Node
    private int endNodeID; // Link의 End Node
    private Double weight; // Link의 weight (길이)
    // Link가 포함하고 있는 node List
    private ArrayList<Point> involvingPointList = new ArrayList<>();

    // int로 ID 파라미터 받음
    public Link (int linkID, int startNodeID, int endNodeID, Double weight){
        this.linkID = linkID;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.weight = weight;
    }

    // String으로 ID 파라미터 받음
    public Link(String linkID, String startNodeID, String endNodeID, Double weight) {
        this.linkID = Integer.parseInt(linkID);
        this.startNodeID = Integer.parseInt(startNodeID);
        this.endNodeID = Integer.parseInt(endNodeID);
        this.weight = weight;
    }

    public String toString() {
        return "[" + linkID + "]\t" + "(" + startNodeID +", "
                + endNodeID+")" + "\t" + "weight: " + weight;
    }

    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }

    public int getStartNodeID() {
        return startNodeID;
    }

    public void setStartNodeID(int startNodeID) {
        this.startNodeID = startNodeID;
    }

    public int getEndNodeID() {
        return endNodeID;
    }

    public void setEndNodeID(int endNodeID) {
        this.endNodeID = endNodeID;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public ArrayList<Point> getInvolvingPointList() {
        return involvingPointList;
    }

    public void setInvolvingPointList(ArrayList<Point> involvingPointList) {
        this.involvingPointList = involvingPointList;
    }

}
