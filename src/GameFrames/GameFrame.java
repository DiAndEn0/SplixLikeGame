package GameFrames;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

    StartFrame startFrame;
    GameFrame(int highscore, StartFrame startFrame){

        this.startFrame = startFrame;
        this.add(new GamePanel(highscore, startFrame));
        this.setTitle("Splix");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }
}