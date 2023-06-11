package Players;

import GameFrames.GamePanel;
import MatrixObjects.AStarNode;
import MatrixObjects.Tile;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Algorithms.AstarAlgorithm.getAStarPath;
import static Algorithms.AstarAlgorithm.manhattanDistance;

public class Bot extends Player {

    public char direction;

    public List<Tile> moveStack;

    public static Color botColor = new Color(57, 32, 50);

    Random ran = new Random();

    public int fight;

    public int time;


    public Bot(int playerX, int playerY) {
        super(playerX, playerY);
        int num = ran.nextInt(4);
        switch (num){
            case 0:
                direction = 'U';
                break;
            case 1:
                direction = 'D';
                break;
            case 2:
                direction = 'R';
                break;
            case 3:
                direction = 'L';
                break;
        }
        score = 0;
        trailMode = false;
        fight =0;
        time =0;
        tileStack = new Stack<>();
        moveStack = new Stack<>();
        playerTiles = new Tile[GamePanel.SCREEN_WIDTH / GamePanel.UNIT_SIZE][GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE];
        for (int i = 0; i < GamePanel.SCREEN_WIDTH / GamePanel.UNIT_SIZE; i++) {
            for (int j = 0; j < GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE; j++) {
                playerTiles[i][j] = new Tile(i, j);
            }
        }

    }

    public Tile getRandomTileInRange(Tile[][] board, Tile tile, int minDistance, int maxDistance, char direction, List<Tile> notGoodTiles) {
        Random rand = new Random();
        int range = maxDistance - minDistance + 1;
        int distance = minDistance + rand.nextInt(range);
        int row = tile.getPositionX();
        int col = tile.getPositionY();
        int numRows = board.length;
        int numCols = board[0].length;

        int maxDeltaRow = Math.min(distance, numRows - row - 1);
        int minDeltaRow = Math.max(-distance, -row);
        int deltaRow = minDeltaRow + rand.nextInt(maxDeltaRow - minDeltaRow + 1);
        int maxDeltaCol = Math.min(distance - Math.abs(deltaRow), numCols - col - 1);
        int minDeltaCol = Math.max(-distance + Math.abs(deltaRow), -col);
        int deltaCol = minDeltaCol + rand.nextInt(maxDeltaCol - minDeltaCol + 1);

        int newRow = row + deltaRow;
        int newCol = col + deltaCol;

        // check if the new row or column is outside the bounds of the board
        if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
            newRow = row;
            newCol = col;
        }

        // adjust the generated tile based on the current direction of the bot
        switch (direction) {
            case 'U':
                while (newRow > row) {
                    newRow--;
                }
                break;
            case 'D':
                while (newRow < row) {
                    newRow++;
                }
                break;
            case 'L':
                while (newCol > col) {
                    newCol--;
                }
                break;
            case 'R':
                while (newCol < col) {
                    newCol++;
                }
                break;
        }

        Tile newTile = board[newRow][newCol];
        while (newTile.getPositionX() == tile.getPositionX() || newTile.getPositionY() == tile.getPositionY() && !notGoodTiles.contains(newTile)) {
            // generate new random tile until one is found that doesn't have the same x or y coordinate
            maxDeltaRow = Math.min(distance, numRows - row - 1);
            minDeltaRow = Math.max(-distance, -row);
            deltaRow = minDeltaRow + rand.nextInt(maxDeltaRow - minDeltaRow + 1);
            maxDeltaCol = Math.min(distance - Math.abs(deltaRow), numCols - col - 1);
            minDeltaCol = Math.max(-distance + Math.abs(deltaRow), -col);
            deltaCol = minDeltaCol + rand.nextInt(maxDeltaCol - minDeltaCol + 1);

            newRow = row + deltaRow;
            newCol = col + deltaCol;

            // check if the new row or column is outside the bounds of the board
            if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
                // if the new row or column is outside the bounds of the board, generate a new tile
                newRow = row;
                newCol = col;
            }
            newTile = board[newRow][newCol];
        }
        return newTile;
    }


    public Tile checkShortestPath(Tile[][] board, Player player, Bot bot)
    {
        int shortDistance = 999;
        Iterator<Tile> itr = player.tileStack.iterator();
        Tile shortest = null;
        while (itr.hasNext())
        {
            Tile tile = itr.next();
            if (manhattanDistance(board[bot.playerX][bot.playerY], tile) <shortDistance)
            {
                shortDistance = manhattanDistance(board[bot.playerX][bot.playerY], tile);
                shortest = tile;
            }
        }
        return shortest;
    }
    public static boolean isValidPath(List<Tile> path, Stack<Tile> tileStack) {
        if (path == null) {
            return false;
        }

        for (Tile tile : path) {
            if (tileStack.contains(tile)) {
                return false;
            }
        }
        return true;
    }

    public static List<Tile> getPath(Tile[][] board, Tile startTile, Tile endTile, List<Tile> visitedTiles, Bot bot) {
        if (startTile == null || endTile == null) {
            return null;
        }

        List<Tile> path = getAStarPath(board, startTile, endTile, visitedTiles, bot);

        if (path == null || path.isEmpty()) {
            return null;
        }

        return path;
    }

    public void act(List<Tile> movesStack, Bot bot){

        Tile tile = movesStack.get(0);
        if (playerX < tile.positionX )
        {
            direction = 'R';
        }

        if (playerX > tile.positionX )
        {
            direction = 'L';
        }

        if (playerY > tile.positionY )
        {
            direction = 'U';
        }

        if (playerY < tile.positionY )
        {
            direction = 'D';
        }

        bot.playerX = tile.positionX;
        bot.playerY = tile.positionY;
        movesStack.remove(0);
    }


    public static List<Tile> getTilesOfLargestEnclosedArea(Tile[][] board) {
        List<Tile> tiles= new ArrayList<>();

        for (int i = 0; i< GamePanel.SCREEN_WIDTH/ GamePanel.UNIT_SIZE; i++)
        {
            for (int j=0; j< GamePanel.SCREEN_HEIGHT/ GamePanel.UNIT_SIZE; j++)
            {
                if (board[i][j].ownerId ==2)
                {
                    tiles.add(board[i][j]);
                }
            }
        }

        return  tiles;
    }


    public static Tile getBestTargetTile(Tile[][] board, Tile botTile, List<Tile> enclosedAreaTiles) {
        Tile bestTargetTile = null;
        int minDistance = Integer.MAX_VALUE;

        for (Tile enclosedAreaTile : enclosedAreaTiles) {
            int distance = manhattanDistance(botTile, enclosedAreaTile);
            if (distance < minDistance) {
                minDistance = distance;
                bestTargetTile = enclosedAreaTile;
            }
        }

        return bestTargetTile;
    }

    // Thaat's the "brain" of the bot, which consists of a lot of cases with a specific order in which each of the checks is executed
    public void Ai(Bot bot, Tile[][] tiles, Player player){
        // This check sees if the bot is inside of his area, meaning if he just finished his own trail by enclosing a closed shape
        if (!bot.trailMode && Objects.requireNonNull(bot.moveStack).isEmpty()) {
            // It finds a tile on the board within the bounds of variables provided by the function that is not part of the bot's area.
            Tile newR = bot.getRandomTileInRange(tiles, tiles[bot.playerX][bot.playerY], 30, 50, direction, getTilesOfLargestEnclosedArea(tiles));
            // Then it provides the bot with a path using A* Algorithm
            bot.moveStack.addAll(Objects.requireNonNull(getPath(tiles, tiles[bot.playerX][bot.playerY], newR, bot.tileStack, bot)));
        } else {
            // This check looks if the bot is already building a trail, ensuring that it will find its way back to its area
            if ((bot.moveStack.isEmpty())&& tiles[bot.playerX][bot.playerY].ownerId != 2 && bot.tileStack.size() != 1)
            {
                // This gets the bot its path back by giving it a tile in the area
                bot.moveStack =  Bot.getPath(tiles, tiles[bot.playerX][bot.playerY], Bot.getBestTargetTile(tiles, tiles[bot.playerX][bot.playerY], Bot.getTilesOfLargestEnclosedArea(tiles) ),  bot.tileStack, bot);
            }
            // This ensures the bot is starting to build a path after exiting its area
            else if (tiles[bot.playerX][bot.playerY].ownerId != 2 && bot.moveStack.size() < 10) {
                // Same function that retrieves a path for the bot
                bot.getMode(tiles);
            }
            assert bot.moveStack != null;
            // Here the bot uses many check in order to see if he wants to fight the player
            if (player.trailMode)
            {
                // I'm using a delay both to make the code better performing and also to ensure the bot to not change his directions too often
                bot.time ++;
                if (bot.time >5)
                {
                    bot.time =  0;
                    // Here the bot checks if the conditions for the fight are right, meaning if he is close enough, if it's better to engage in a fight rather than complete a path and if the player is build a very big path
                    if (bot.moveStack.size() > manhattanDistance(tiles[bot.playerX][bot.playerY], bot.checkShortestPath(tiles, player, bot)) || manhattanDistance(tiles[bot.playerX][bot.playerY], bot.checkShortestPath(tiles, player, bot)) < 15 || player.tileStack.size() > 70)
                    {
                        // Another check that checks if the bot is ready to fight and if he can actually build a path
                        if ((bot.fight ==0 ||  manhattanDistance(tiles[bot.playerX][bot.playerY], tiles[player.playerX][player.playerY]) < bot.moveStack.size()) && getPath(tiles, tiles[bot.playerX][bot.playerY], tiles[player.playerX][player.playerY], bot.tileStack, bot)!= null)
                        {
                            // Bot is engaging in a fight
                            bot.fight = 1;
                            bot.moveStack.clear();
                            // Builds a path to the last location of the bot
                            bot.moveStack.addAll(Objects.requireNonNull(Bot.getPath(tiles, tiles[bot.playerX][bot.playerY], tiles[player.playerX][player.playerY], bot.tileStack, bot)));
                        }
                        // Another check to ensure that there is always a path that the bot follows
                        if (moveStack.isEmpty())
                        {
                            bot.getMode(tiles);
                        }
                    }
                }
            }
            // The bot uses the move stack he got and goes to a function that moves the bot accordingly
            if (!bot.moveStack.isEmpty())
            {
                bot.act(bot.moveStack, bot);
            }
        }
    }

    public void getMode(Tile[][] tiles)
    {
        if (moveStack.isEmpty())
        {
            List<Tile> path;
            int attempts = 0;
            int maxAttempts = 100;
            do {
                path = getPath(tiles, tiles[playerX][playerY], getRandomTileInRange(tiles, tiles[playerX][playerY], 30, 50, direction, getTilesOfLargestEnclosedArea(tiles)),  tileStack, Bot.this);
                attempts++;
            } while (!isValidPath(path, tileStack) && attempts < maxAttempts);
            if (attempts < maxAttempts) {
                moveStack = path;
            }
        }
        }





}




