package minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WhackAMole extends JFrame {
    private JButton[] buttons = new JButton[16];
    private int score = 0;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private int timeLeft = 30;
    private int activeMole = -1;
    private Random random = new Random();
    private Timer moleTimer;
    private Timer gameTimer;
    private ImageIcon wormIcon;

    public WhackAMole() {
        super("먹이 잡기 게임");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        wormIcon = new ImageIcon(getClass().getResource("/minigame/Snack.png"));
        Image scaledImage = wormIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        wormIcon = new ImageIcon(scaledImage);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        scoreLabel = new JLabel("점수: 0", SwingConstants.CENTER);
        timeLabel = new JLabel("남은 시간: 30초", SwingConstants.CENTER);
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < 16; i++) {
            final int index = i;
            buttons[i] = new JButton();
            buttons[i].setEnabled(false);
            buttons[i].addActionListener(e -> {
                if (index == activeMole) {
                    score += 10;
                    scoreLabel.setText("점수: " + score);
                    buttons[index].setIcon(null);
                    buttons[index].setText("");
                    buttons[index].setEnabled(false);
                    activeMole = -1;
                }
            });
            panel.add(buttons[i]);
        }
        add(panel, BorderLayout.CENTER);

        JButton startButton = new JButton("게임 시작");
        add(startButton, BorderLayout.SOUTH);

        moleTimer = new Timer(850, e -> {
            if (activeMole != -1) {
                buttons[activeMole].setIcon(null);
                buttons[activeMole].setText("");
                buttons[activeMole].setEnabled(false);
            }
            activeMole = random.nextInt(16);
            buttons[activeMole].setIcon(wormIcon);
            buttons[activeMole].setText("");
            buttons[activeMole].setEnabled(true);
        });

        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("남은 시간: " + timeLeft + "초");

            if (timeLeft == 0) {
                moleTimer.stop();
                gameTimer.stop();
                JOptionPane.showMessageDialog(null, "게임 종료!\n최종 점수: " + score);
            }
        });

        startButton.addActionListener(e -> {
            score = 0;
            timeLeft = 30;
            activeMole = -1;
            scoreLabel.setText("점수: 0");
            timeLabel.setText("남은 시간: 30초");

            for (JButton button : buttons) {
                button.setIcon(null);
                button.setText("");
                button.setEnabled(false);
            }

            moleTimer.start();
            gameTimer.start();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new WhackAMole();
    }
}
