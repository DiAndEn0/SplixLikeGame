package GameFrames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame {
    public StartFrame(int highscore) {
        // Set up the window properties
        setTitle("Splix");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel for the content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(67, 79, 108));

        // Add the title label to the panel
        JLabel titleLabel = new JLabel("Splix");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.CENTER);

        // Add the high score label to the panel
        JLabel highScoreLabel = new JLabel("High Score: " + highscore);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(highScoreLabel, BorderLayout.NORTH);

        // Add the play button to the panel
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 24));
        playButton.setBackground(new Color(119, 139, 180));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setOpaque(true);
        playButton.setPreferredSize(new Dimension(200, 50));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameFrame(highscore, StartFrame.this);
                setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.setBackground(new Color(67, 79, 108));
        buttonPanel.add(playButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the content panel to the frame
        add(contentPanel);
        setVisible(true);
    }
}
