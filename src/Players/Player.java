package Players;

import GameFrames.GamePanel;
import MatrixObjects.Tile;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Player {
    public int playerX;
    public int playerY;
    public int score;
    public boolean trailMode;
    public Tile[][] playerTiles;
    public Stack<Tile> tileStack;

    public Tile startingTile;

    public Tile startStartingTile;

    public static final Color playerColor = Color.GREEN;

    public static final Color playerTrailColor = Color.getHSBColor(140, 100,75);

    public Player(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
        score = 0;
        trailMode = false;
        tileStack = new Stack<>();
//      allTiles = new Stack<>();
        playerTiles = new Tile[GamePanel.SCREEN_WIDTH / GamePanel.UNIT_SIZE][GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE];
        for (int i = 0; i < GamePanel.SCREEN_WIDTH / GamePanel.UNIT_SIZE; i++) {
            for (int j = 0; j < GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE; j++) {
                playerTiles[i][j] = new Tile(i, j);
            }
        }
    }

    public Player() {
    }

    public void addTileStack(int x, int y, int ownerId){
        Tile tempTile = new Tile(x,y);
        tempTile.ownerId = ownerId;
        tempTile.color = playerTrailColor;
        tileStack.push(tempTile);
    }

    public  boolean tileExistsInStack(int x, int y)
    {
        Iterator<Tile> itr = tileStack.iterator();
        Tile tile;
        while (itr.hasNext())
        {
            tile = itr.next();
                if (tile.positionX == x && tile.positionY == y)
                {
                    return true;
                }
        }
        return false;
    }


    public void startingTiles(int x, int y, Tile[][] tiles, int ownerID)
    {
        tiles[x][y].setColor(playerColor);
        tiles[x+1][y].setColor(playerColor);
        tiles[x-1][y].setColor(playerColor);
        tiles[x][y+1].setColor(playerColor);
        tiles[x][y-1].setColor(playerColor);
        tiles[x+1][y+1].setColor(playerColor);
        tiles[x-1][y-1].setColor(playerColor);
        tiles[x-1][y+1].setColor(playerColor);
        tiles[x+1][y-1].setColor(playerColor);
        tiles[x][y].setOwnerId(ownerID);
        tiles[x+1][y].setOwnerId(ownerID);
        tiles[x-1][y].setOwnerId(ownerID);
        tiles[x][y+1].setOwnerId(ownerID);
        tiles[x][y-1].setOwnerId(ownerID);
        tiles[x+1][y+1].setOwnerId(ownerID);
        tiles[x-1][y-1].setOwnerId(ownerID);
        tiles[x-1][y+1].setOwnerId(ownerID);
        tiles[x+1][y-1].setOwnerId(ownerID);

        startingTile = tiles[x][y];
        startStartingTile =tiles[x][y];
   }

    public void trailFill(Tile[][] tiles, Stack<Tile> playerStack)
    {
        Iterator<Tile> itr = playerStack.iterator();
        Tile tile;
        while (itr.hasNext())
        {
            tile = itr.next();
            tiles[tile.positionX][tile.positionY] = tile;
            tiles[tile.positionX][tile.positionY].isTrail =false;
            tiles[tile.positionX][tile.positionY].trailId = 0;
        }
    }

    public void resetTiles(Tile[][] tiles, Stack<Tile> playerStack)
    {
        Iterator<Tile> itr = playerStack.iterator();
        Tile tile;
        while (itr.hasNext())
        {
            tile = itr.next();
            tiles[tile.positionX][tile.positionY].isTrail =false;
            tiles[tile.positionX][tile.positionY].trailId = 0;
        }
    }

    public void respawn(Tile[][] tiles, int ownerId)
    {
        List<Tile> tileList = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i< GamePanel.SCREEN_WIDTH/ GamePanel.UNIT_SIZE; i++ )
        {
            for (int j = 0; j < GamePanel.SCREEN_HEIGHT/ GamePanel.UNIT_SIZE ; j++)
            {
                if (tiles[i][j].ownerId == ownerId)
                {
                    tileList.add(tiles[i][j]);
                }
            }
        }

        int i = r.nextInt(tileList.size());
        startStartingTile = tileList.get(i);
    }
}
