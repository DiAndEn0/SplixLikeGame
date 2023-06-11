package Algorithms;

import MatrixObjects.AStarNode;
import MatrixObjects.Tile;
import Players.Bot;

import java.util.*;

public class AstarAlgorithm {

    public static class AStarComparator implements Comparator<AStarNode> {
        public int compare(AStarNode a, AStarNode b) {
            return Double.compare(a.fScore, b.fScore);
        }
    }

    //Main pathfinding algorithm inside of the project. Works by iterating through nodes and applying scores to each node that it then coordinates inside of a priority Queue
    public static List<Tile> getAStarPath(Tile[][] board, Tile start, Tile end, List<Tile> tileList, Bot bot) {
        // Initializing variables as well as the noes we'd be working with
        int width = board.length;
        int height = board[0].length;

        // Uses the custom-built class to copy and convert all tiles to nodes
        AStarNode[][] nodes = new AStarNode[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y] = new AStarNode(board[x][y], null, Double.MAX_VALUE, Double.MAX_VALUE);
            }
        }

        // Creates the open and closed sets, one for dumping all of the visited tiles inside and the other for finding valid neighbors
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(new AStarComparator());
        Set<AStarNode> closedSet = new HashSet<>();

        //Initializes the first node which is the starting point
        nodes[start.getPositionX()][start.getPositionY()].gScore = 0;
        nodes[start.getPositionX()][start.getPositionY()].hScore = manhattanDistance(start, end);
        nodes[start.getPositionX()][start.getPositionY()].fScore = nodes[start.getPositionX()][start.getPositionY()].hScore;
        openSet.add(nodes[start.getPositionX()][start.getPositionY()]);

        // Goes into a loop that finds a path by iterating through neigbors of the first node and then doing the same for every other node until the end tile is found, after which the function calls another function to reconstruct this path
        while (!openSet.isEmpty())
        {
            // takes the head node with the lowest gScore(meaning that has the least amount of weight based on its position and the distance between the end and start) and sees if it is the end node
            AStarNode current = openSet.poll();
            if (current.tile.equals(end)) {
                //Reconstructs path using that node and the parent instance that points to all the other nodes
                return reconstructPath(current);
            }
            // Adds the node to the closed set and marks it as visited
            closedSet.add(current);
            // Gets all the valid neighbors of that node
            List<Tile> neighbors = getValidNeighboringTiles(board, current.tile, new HashSet<>(tileList));
            for (Tile neighborTile : neighbors) {
                //for each neighbor checks if they're already visited or are part of a tile stack which in general hoards all the 'obstacles' or tiles that i dont want the function to build a path with
                AStarNode neighbor = nodes[neighborTile.getPositionX()][neighborTile.getPositionY()];
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                boolean inTileStack = false;
                for (Tile t : tileList) {
                    if (t.getPositionX() == neighborTile.getPositionX() && t.getPositionY() == neighborTile.getPositionY()) {
                        inTileStack = true;
                        break;
                    }
                }
                if (inTileStack || neighborTile.getPositionX() == bot.playerX && neighborTile.getPositionY() == bot.playerY) {
                    continue;
                }

                // Here are two checks
                double tentativeGScore = current.gScore + 1;
                // Firstly, it checks if the node is already in the open set, if it is not then it wasn't explored and thus will be put in the open set
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
                // If it is, this check sees if there is already a better path explored and if it is necessary to update the neigbor for further exploration
                // If it has the same or higher cost of reaching its neighbor, then that means that the function found a better path to explore, so it skips over this node and continues onwards
                else if (tentativeGScore >= neighbor.gScore) {
                    continue;
                }
                //If it did not skip, it means that the node will be updated for further exploration and finding a shorter path
                neighbor.parent = current;
                neighbor.gScore = tentativeGScore;
                neighbor.hScore = manhattanDistance(neighbor.tile, end);
                neighbor.fScore = neighbor.gScore + neighbor.hScore;
            }
        }
        return null;
    }

    public static int manhattanDistance(Tile a, Tile b) {
        return Math.abs(a.getPositionX() - b.getPositionX()) + Math.abs(a.getPositionY() - b.getPositionY());
    }

    public static List<Tile> getValidNeighboringTiles(Tile[][] board, Tile currentTile, Set<Tile> visitedTiles) {
        List<Tile> validNeighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions) {
            int newX = currentTile.getPositionX() + direction[0];
            int newY = currentTile.getPositionY() + direction[1];

            if (newX >= 0 && newX < board.length && newY >= 0 && newY < board[0].length) {
                Tile neighborTile = board[newX][newY];
                if (!visitedTiles.contains(neighborTile) && !neighborTile.equals(currentTile)) {
                    validNeighbors.add(neighborTile);
                }
            }
        }
        return validNeighbors;
    }

    public static java.util.List<Tile> reconstructPath(AStarNode node) {
        java.util.List<Tile> path = new ArrayList<>();
        AStarNode current = node;
        while (current != null) {
            path.add(current.tile);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
