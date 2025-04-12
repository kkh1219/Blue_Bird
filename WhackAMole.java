package minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WhackAMole extends JFrame {
    private JButton[] buttons = new JButton[16];  // 4x4 배열 (총 16개)
    private int score = 0;
    private JLabel scoreLabel;
    private Random random = new Random();
    private Timer moleTimer;  // 두더지 타이머
    private Timer gameTimer;  // 게임 종료 타이머
    private int timeLeft = 30;  // 게임 제한 시간 (초)
    private JLabel timeLabel;  // 남은 시간 표시
    private int activeMole = -1;  // 현재 두더지가 있는 위치

    public WhackAMole() {
        super("먹이 잡기 게임");
        setSize(600, 700);  // 창 크기 변경
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 점수 & 시간 표시
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        scoreLabel = new JLabel("점수: 0", SwingConstants.CENTER);
        timeLabel = new JLabel("남은 시간: 30초", SwingConstants.CENTER);
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        // 버튼 패널 (4x4)
        JPanel panel = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < 16; i++) {
            final int index = i;  // 버튼 번호 저장
            buttons[i] = new JButton();
            buttons[i].setEnabled(false);  // 처음엔 비활성화
            buttons[i].addActionListener(new ActionListener() {  // 버튼 클릭 이벤트
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (index == activeMole) {  // 두더지가 있을 때만 점수 증가
                        score=score+10;
                        scoreLabel.setText("점수: " + score);
                        buttons[index].setText("");  // 두더지 사라짐
                        buttons[index].setEnabled(false);  // 다시 클릭 불가
                        activeMole = -1;  // 두더지 위치 초기화
                    }
                }
            });
            panel.add(buttons[i]);
        }
        add(panel, BorderLayout.CENTER);

        // 게임 시작 버튼
        JButton startButton = new JButton("게임 시작");
        add(startButton, BorderLayout.SOUTH);
        
        // 두더지 타이머 (770ms마다 랜덤 위치에 두더지 등장)
        moleTimer = new Timer(850, new ActionListener() {  // ✅ 속도 1.3배 증가
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeMole != -1) {
                    buttons[activeMole].setText("");  // 이전 두더지 제거
                    buttons[activeMole].setEnabled(false); // 클릭 비활성화
                }
                activeMole = random.nextInt(16);  // ✅ 4x4 배열에서 랜덤 위치 선택
                buttons[activeMole].setText("꿈틀!");
                buttons[activeMole].setEnabled(true);  // 클릭 가능하게 설정
            }
        });

        // 30초 타이머 (게임 종료)
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;  // 남은 시간 감소
                timeLabel.setText("남은 시간: " + timeLeft + "초");

                if (timeLeft == 0) {
                    moleTimer.stop();  // 두더지 스폰 중단
                    gameTimer.stop();  // 게임 타이머 중단
                    JOptionPane.showMessageDialog(null, "게임 종료!\n최종 점수: " + score);
                }
            }
        });

        // 시작 버튼 이벤트
        startButton.addActionListener(e -> {
            score = 0;
            scoreLabel.setText("점수: 0");
            timeLeft = 30;  // 남은 시간 초기화
            timeLabel.setText("남은 시간: 30초");
            activeMole = -1;  // 두더지 초기화

            moleTimer.start();
            gameTimer.start();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new WhackAMole();
    }
}
