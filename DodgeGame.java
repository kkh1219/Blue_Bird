package minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class DodgeGame extends JPanel implements ActionListener, KeyListener {
    private int playerX = 250, playerY = 450;  // 플레이어 위치
    private final int PLAYER_SIZE = 30;
    private final int BALL_SIZE = 20;
    private int score = 0;
    private boolean gameOver = false;
    
    private ArrayList<Point> balls = new ArrayList<>();
    private Timer gameTimer;
    private Random random = new Random();
    
    public DodgeGame() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        
        gameTimer = new Timer(30, this);  // 30ms마다 화면 업데이트
        gameTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 플레이어 그리기
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        
        // 공 그리기
        g.setColor(Color.RED);
        for (Point ball : balls) {
            g.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE);
        }
        
        // 점수 표시
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("점수: " + score, 10, 20);
        
        // 게임 오버 메시지
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("게임 오버!", 180, 250);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // 공 추가 (30% 감소: 기존보다 덜 생성)
            if (random.nextInt(10) > 8) { // ✅ 공이 생성될 확률을 낮춤
                balls.add(new Point(random.nextInt(480), 0));
            }
            
            // 공 이동
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).y += 5 + (score / 50);  // ✅ 점수 높을수록 속도 증가
                
                // 충돌 체크
                if (new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)
                        .intersects(new Rectangle(balls.get(i).x, balls.get(i).y, BALL_SIZE, BALL_SIZE))) {
                    gameOver = true;
                    gameTimer.stop();
                }
            }
            
            // 화면 밖으로 나간 공 처리
            int initialSize = balls.size(); // 기존 공 개수 저장
            balls.removeIf(ball -> ball.y > 500);
            int removedBalls = initialSize - balls.size(); // 제거된 공 개수 계산
            
            // 제거된 공 개수만큼 점수 증가
            score += removedBalls * 10; // ✅ 공 하나 피할 때마다 10점 증가
            
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
            if (key == KeyEvent.VK_RIGHT && playerX < 470) {
                playerX += 20;
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("공 피하기 게임");
        DodgeGame game = new DodgeGame();
        
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
