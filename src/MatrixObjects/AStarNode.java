package MatrixObjects;

public class AStarNode {
    public Tile tile;
    public AStarNode parent;
    public double gScore;
    public double hScore;
    public double fScore;

    public AStarNode(Tile tile, AStarNode parent, double gScore, double hScore) {
        this.tile = tile;
        this.parent = parent;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = gScore + hScore;
    }
}