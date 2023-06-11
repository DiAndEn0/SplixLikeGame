package GameFrames;
import Algorithms.FloodFillAlgorithm;
import MatrixObjects.Tile;
import Players.Bot;
import Players.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener{



    public static final int SCREEN_WIDTH = 1300;
    public static final int SCREEN_HEIGHT = 750;
    public static final int UNIT_SIZE = 10;
    public static final int DELAY = 75;
    public final Tile[][] tiles;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Timer gameTimer;
    Random random;
    Player player;
    Bot bot;
    int timeLeft = 90;
    int showScore=0;
    int playerScore= 0;
    int botScore =0;
    int playerKills =0;
    int botKills =0;

    int lives = 3;

    int highscore;

    StartFrame startFrame;

    GamePanel(int highscore, StartFrame startFrame){

        this.highscore = highscore;
        this.startFrame = startFrame;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        tiles = new Tile[SCREEN_WIDTH/UNIT_SIZE][SCREEN_HEIGHT/UNIT_SIZE];

        for (int i = 0; i< SCREEN_WIDTH/UNIT_SIZE; i++ )
        {
            for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE ; j++)
            {
                tiles[i][j] = new Tile(i,j);
            }
        }

        player = new Player(random.nextInt(SCREEN_WIDTH/UNIT_SIZE), random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) );

        while (player.playerX <5||player.playerY < 5 ||player.playerX > SCREEN_WIDTH/UNIT_SIZE -5  || player.playerY > SCREEN_HEIGHT/UNIT_SIZE -5 )
        {
            player = new Player(random.nextInt(SCREEN_WIDTH/UNIT_SIZE), random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) );
        }
        player.startingTiles(player.playerX, player.playerY, tiles, 1);

        bot = new Bot(random.nextInt(SCREEN_WIDTH/UNIT_SIZE), random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) );

        while (bot.playerX <5||bot.playerY < 5 || bot.playerX > SCREEN_WIDTH/UNIT_SIZE -5  || bot.playerY > SCREEN_HEIGHT/UNIT_SIZE -5 )
        {
            bot = new Bot(random.nextInt(SCREEN_WIDTH/UNIT_SIZE), random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) );
        }
        bot.startingTiles(bot.playerX, bot.playerY, tiles, 2);

        startGame();
    }
    public void startGame() {
        ActionListener timer2Action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GamePanel.this.timeLeft --;
                if (GamePanel.this.timeLeft == 0)
                {
                    running =false;
                }
            }
        };
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        gameTimer = new Timer(1000, timer2Action);
        gameTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        if (showScore == 1)
        {
            displayScores(g, playerScore, botScore, lives, timeLeft);
        }
    }
    public void draw(Graphics g) {

        if(running) {
            playerScore = playerKills * 100;
            botScore = botKills * 250;
            for (int i = 0; i< SCREEN_WIDTH/UNIT_SIZE; i++ )
            {
                for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE ; j++)
                {
                    switch (tiles[i][j].ownerId)
                    {
                        case 0:
                            g.setColor(Color.GRAY);
                        break;
                        case 1:
                            g.setColor(Player.playerColor);
                            playerScore ++;
                            break;
                        case 2:
                            g.setColor(Bot.botColor);
                            botScore++;
                            break;
                    }
                    g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }

            Iterator<Tile> itr = player.tileStack.iterator();

            Iterator<Tile> botItr = bot.tileStack.iterator();

            if (player.trailMode) {
                while (itr.hasNext()) {
                    Tile tile = itr.next();
                    g.setColor(tile.color);
                    g.fillRect(tiles[tile.positionX][tile.positionY].positionX * UNIT_SIZE, tiles[tile.positionX][tile.positionY].positionY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }

            if (bot.trailMode) {
                while (botItr.hasNext()) {
                    Tile tile = botItr.next();
                    g.setColor(tile.color);
                    g.fillRect(tiles[tile.positionX][tile.positionY].positionX * UNIT_SIZE, tiles[tile.positionX][tile.positionY].positionY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE || i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                for (int j = 0; j < SCREEN_WIDTH / UNIT_SIZE; j++) {
                    g.setColor(Color.BLACK);
                    g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                }
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }


            g.setColor(Player.playerColor);
            g.fillRect(tiles[player.playerX][player.playerY].positionX*UNIT_SIZE, tiles[player.playerX][player.playerY].positionY*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.RED);
            g.drawRect(tiles[player.playerX][player.playerY].positionX*UNIT_SIZE , tiles[player.playerX][player.playerY].positionY*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Bot.botColor);
            g.fillRect(tiles[bot.playerX][bot.playerY].positionX*UNIT_SIZE, tiles[bot.playerX][bot.playerY].positionY*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.BLUE);
            g.drawRect(tiles[bot.playerX][bot.playerY].positionX*UNIT_SIZE , tiles[bot.playerX][bot.playerY].positionY*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);



        }
        else {
            gameOver(g);
        }

    }

    public void move() {
        switch (direction) {
            case 'U':
                player.playerY = player.playerY - 1;
                break;
            case 'D':
                player.playerY = player.playerY + 1;
                break;
            case 'L':
                player.playerX = player.playerX - 1;
                break;
            case 'R':
                player.playerX = player.playerX + 1;
                break;
        }


        bot.Ai(bot, tiles, player);
    }

    // Collisions of the Players.Player.
    // Has most of the cases for any collisions or other logic and responses to them
    public void checkCollisions() {

        //check if head touches left border

        if (player.playerX <= 0 && direction == 'L')
        {
            respawnFromBounds(1);
        }

        //check if head touches right border

        if (player.playerX >= SCREEN_WIDTH/UNIT_SIZE && direction == 'R')
        {
            respawnFromBounds(1);
        }

        //check if head touches top border

        if (player.playerY >= SCREEN_HEIGHT/UNIT_SIZE)
        {
            respawnFromBounds(1);
        }

        //check if head touches bottom border

        if (player.playerY <= 0)
        {
            respawnFromBounds(1);
        }

        if(!running) {
            timer.stop();
        } else
        {
            // checks if the player is not in his tile

            if (!player.trailMode && !player.tileExistsInStack(player.playerX, player.playerY) && tiles[player.playerX][player.playerY].ownerId !=1)
            {
                //Players.Player starts his trail
                player.trailMode = true;
            }

            // If the tile is not the player's and the character is building a trail

            if (tiles[player.playerX][player.playerY].ownerId != 1 && player.trailMode) {
                if (tiles[player.playerX][player.playerY].isTrail && tiles[player.playerX][player.playerY].trailId ==1 ) {
                    respawnFromBounds(1);
                    if (lives <=0)
                    {
                        running = false;
                    }
                } else {
                    player.addTileStack(player.playerX, player.playerY, 1);
                    tiles[player.playerX][player.playerY].trailId = 1;
                    tiles[player.playerX][player.playerY].isTrail = true;

                }
            }



            // if  the tile is already the player's and he is in trail mode
            if (tiles[player.playerX][player.playerY].ownerId == 1 && player.trailMode && !player.tileStack.isEmpty() ) {
                // Disables trail
                player.trailMode = false;
                // Fills the trail first



                player.trailFill(tiles, player.tileStack);


                // Calls floodfill method
                fillCall(1);

                // Clears the trail stack
                player.tileStack.clear();
            }


            if (bot.tileExistsInStack(player.playerX, player.playerY))
            {

                Iterator<Tile> itr = bot.tileStack.iterator();
                Tile tile;
                while (itr.hasNext())
                {
                    tile = itr.next();
                    tiles[tile.positionX][tile.positionY].isTrail =false;
                    tiles[tile.positionX][tile.positionY].trailId = 0;
                }

                bot.tileStack.clear();
                bot.moveStack.clear();
                bot.respawn(tiles, 2);
                bot.playerX = bot.startStartingTile.positionX;
                bot.playerY = bot.startStartingTile.positionY;
                bot.trailMode = false;

                playerKills++;
            }
        }


    }

    public void checkBotCollisions() {

        //check if head touches left border

        if (bot.playerX < 0)
        {
            respawnFromBounds(2);
        }

        //check if head touches right border

        if (bot.playerX > SCREEN_WIDTH/UNIT_SIZE)
        {
            respawnFromBounds(2);
        }

        //check if head touches top border

        if (bot.playerY > SCREEN_HEIGHT/UNIT_SIZE)
        {
            respawnFromBounds(2);
        }

        //check if head touches bottom border

        if (bot.playerY < 0)
        {
            respawnFromBounds(2);
        }

        if(!running) {
            timer.stop();
        } else
        {

            if (!player.trailMode && bot.fight ==1)
            {
                bot.fight = 0;
                bot.moveStack.clear();
            }
            // checks if the bot is not in his tile

            if (!bot.trailMode && !bot.tileExistsInStack(bot.playerX, bot.playerY) && tiles[bot.playerX][bot.playerY].ownerId != 2)
            {
                //bot starts his trail
                bot.trailMode = true;
            }

            // If the tile is not the bot's and the character is building a trail

            if (tiles[bot.playerX][bot.playerY].ownerId != 2 && bot.trailMode) {
                if (tiles[bot.playerX][bot.playerY].isTrail && tiles[bot.playerX][bot.playerY].trailId == 2 && bot.tileStack.peek() ==  tiles[bot.playerX][bot.playerY]) {
                    respawnFromBounds( 2);
                } else {
                    if (bot.tileStack.isEmpty()) bot.startingTile = tiles[bot.playerX][bot.playerY];
                    bot.addTileStack(bot.playerX, bot.playerY, 2);
                    tiles[bot.playerX][bot.playerY].trailId = 2;
                    tiles[bot.playerX][bot.playerY].isTrail = true;
                }
            }



            // if  the tile is already the bot's and he is in trail mode
          if (tiles[bot.playerX][bot.playerY].ownerId == 2 && bot.trailMode && !bot.tileStack.isEmpty() ) {
                // Disables trail
                bot.trailMode = false;
                // Fills the trail first

                bot.trailFill(tiles, bot.tileStack);


                // Calls floodfill method
                fillCall(2);

                // Clears the trail stack
                bot.tileStack.clear();
                bot.startingTile = null;
                if (bot.fight == 0)
                {
                    bot.moveStack.clear();
                }
            }

            if (player.tileExistsInStack(bot.playerX, bot.playerY))
            {

                Iterator<Tile> itr = player.tileStack.iterator();
                Tile tile;
                while (itr.hasNext())
                {
                    tile = itr.next();
                    tiles[tile.positionX][tile.positionY].isTrail =false;
                    tiles[tile.positionX][tile.positionY].trailId = 0;
                }

                player.tileStack.clear();
                player.respawn(tiles, 1);
                player.playerX = player.startStartingTile.positionX;
                player.playerY = player.startStartingTile.positionY;
                player.trailMode = false;

                botKills++;
                lives--;

                if (lives == 0)
                {
                    running = false;
                }
            }
        }

    }

    public void fillCall(int ownerId)
    {
        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++)
        {
            for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE; j++)
            {
                if (inside(tiles, tiles[i][j].getPositionX(),tiles[i][j].getPositionY() ,ownerId ) == 1)
                {
                    Tile[][] copy = copyMatrix(tiles);
                    FloodFillAlgorithm.floodFill(copy, copy[i][j].getPositionX(), copy[i][j].getPositionY(), ownerId);

                    for (int h = 0; h < SCREEN_WIDTH/UNIT_SIZE; h++ )
                    {
                        for (int v = 0; v < SCREEN_HEIGHT/UNIT_SIZE; v++)
                        {
                            if (copy[h][v].ownerId !=ownerId)
                            {
                                tiles[h][v].ownerId =ownerId;
                            }
                        }
                    }

                    break;
                }
            }
        }
    }


    public void respawnFromBounds(int ownerId)
    {
        if (ownerId == 1)
        {
            player.resetTiles(tiles, player.tileStack);
            player.tileStack.clear();
            player.respawn(tiles, 1);
            player.playerX = player.startStartingTile.positionX;
            player.playerY = player.startStartingTile.positionY;
            player.trailMode = false;
            lives--;

            if (lives <=0)
            {
                running= false;
            }
        }
        else
        {
            bot.resetTiles(tiles, bot.tileStack);
            bot.tileStack.clear();
            bot.moveStack.clear();
            bot.respawn(tiles, 2);
            bot.playerX = bot.startStartingTile.positionX;
            bot.playerY = bot.startStartingTile.positionY;
            bot.trailMode = false;
        }
    }


    public void gameOver(Graphics g) {
        Font font = new Font("Arial", Font.BOLD, 48);
        g.setFont(font);
        g.setColor(Color.WHITE);

        // Draw the string in the center of the panel
        FontMetrics fm = g.getFontMetrics();
        String text = "Your Score is: " + playerScore;
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        JButton button = new JButton("Return");
        button.setBounds(x+ text.length()*20/2, y + fm.getHeight() + 10, 80, 30);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GamePanel.this.highscore < playerScore)
                {
                    startFrame.dispose();
                    new StartFrame(playerScore);
                }
                else {
                    startFrame.setVisible(true);
                }
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(button);
                frame.dispose();
            }
        });
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkCollisions();
            checkBotCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (showScore ==0)
                    {
                        showScore = 1;
                    } else showScore =0;
                    break;
                case KeyEvent.VK_ESCAPE:
                    running = false;
                    break;
            }
        }
    }

    public static void displayScores(Graphics g, int playerScore, int botScore, int lives, int time) {
        // Set the font and color for the text
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);

        // Determine the size of the text to be displayed
        String scoreText = "Player: " + playerScore + " | Bot: " + botScore + " | Lives: " +lives + " | Time Left: " + time;
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        int textHeight = fm.getHeight();

        // Determine the location to display the text
        int x = g.getClipBounds().width - textWidth - 10;
        int y = textHeight + 10;

        // Draw the text at the determined location
        g.drawString(scoreText, x, y);
    }


    public int inside(Tile[][] tiles, int x, int y, int ownerId) {
        int currentlyLine = 0;
        int x_count = 0;
        int startX = -1; // Track the starting position of an enclosed shape

        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            if (tiles[i][y].ownerId == ownerId && currentlyLine == 0) {
                x_count++;
                currentlyLine = 1;
                startX = i; // Set the starting position of an enclosed shape
            } else if (currentlyLine == 1 && tiles[i][y].ownerId != ownerId) {
                currentlyLine = 0;
            }
        }

        // Check if the specified position is outside of any enclosed shape
        if (x < startX || x >= startX + x_count) {
            return 1; // Outside of an enclosed shape
        } else {
            return 0; // Inside an enclosed shape
        }
    }



    public static Tile[][] copyMatrix(Tile[][] tiles) {
        Tile[][] tile = new Tile[SCREEN_WIDTH/UNIT_SIZE][SCREEN_HEIGHT/UNIT_SIZE];
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            for (int j = 0; j < SCREEN_HEIGHT / UNIT_SIZE; j++) {
                Tile tile1 = new Tile(tiles[i][j]);
                tile[i][j] = tile1;
            }
        }
        return tile;
    }
}

//*****************************************