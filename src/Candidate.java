public class Candidate {
    private Point point;
    private Link involvedLink;

    public Candidate (Point point, Link involvedLink){
        this.point = point;
        this.involvedLink = involvedLink;
    }

    public Point getPoint() {
        return point;
    }

    public Link getInvolvedLink(){
        return involvedLink;
    }
}
