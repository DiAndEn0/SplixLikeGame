package MatrixObjects;

public class Node {
    private Tile tile;
    private Node parent;

    public Node(Tile tile, Node parent) {
        this.tile = tile;
        this.parent = parent;
    }

    public Tile getTile() {
        return tile;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return tile.equals(node.tile);
    }

}
