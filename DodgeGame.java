package minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class DodgeGame extends JPanel implements ActionListener, KeyListener {
    private int playerX = 250, playerY = 450;
    private final int PLAYER_SIZE = 50;
    private final int BALL_SIZE = 20;
    private int score = 0;
    private boolean gameOver = false;

    private ArrayList<Point> balls = new ArrayList<>();
    private Timer gameTimer;
    private Random random = new Random();

    private Image hamsterImage;

    public DodgeGame() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        hamsterImage = new ImageIcon(getClass().getResource("/minigame/Hamster_young01.png"))
                .getImage().getScaledInstance(PLAYER_SIZE, PLAYER_SIZE, Image.SCALE_SMOOTH);

        gameTimer = new Timer(30, this);
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(hamsterImage, playerX, playerY, PLAYER_SIZE, PLAYER_SIZE, this);

        g.setColor(Color.RED);
        for (Point ball : balls) {
            g.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("점수: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("게임 오버!", 180, 250);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // 공 생성 확률 조정
            if (random.nextInt(10) > 8) {
                balls.add(new Point(random.nextInt(480), 0));
            }

            // 공 이동 및 충돌 확인
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).y += 5 + (score / 50);

                if (new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)
                        .intersects(new Rectangle(balls.get(i).x, balls.get(i).y, BALL_SIZE, BALL_SIZE))) {
                    gameOver = true;
                    gameTimer.stop();
                }
            }

            // 화면 밖 공 제거 및 점수 증가
            int before = balls.size();
            balls.removeIf(ball -> ball.y > 500);
            score += (before - balls.size()) * 10;

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= 20;
            }
            if (key == KeyEvent.VK_RIGHT && playerX < getWidth() - PLAYER_SIZE) {
                playerX += 20;
            }
            repaint();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("공 피하기 게임");
        DodgeGame game = new DodgeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
