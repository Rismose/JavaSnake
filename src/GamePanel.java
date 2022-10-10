import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {




    static final int S_WIDTH = 800;
    static final int S_HEIGHT = 600;
    static final int U_SIZE = 25;
    static final int G_UNITS = (S_WIDTH * S_HEIGHT) / U_SIZE;

    static int DELAY = 50;

    int[] x = new int[G_UNITS];
    int[] y = new int[G_UNITS];

    int bodyParts = 5;
    int pointsTaken;
    int highScorePoints;
    int pointX;
    int pointY;

    char direction = 'R';

    boolean running = false;

    Timer timer;

    Random random;

    Color backgroundColor = new Color(35,36, 40);
    Color snakeHeadColor = new Color(116, 137, 241);
    Color SnakeColor = new Color(149, 202, 245);
    Color pointColor = new Color(253, 24, 87);

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
        this.setBackground(backgroundColor);
        this.setFocusable(true);
        this.addKeyListener(new KeyInput());
        timer = new Timer(DELAY, this);
        Start();
    }

    public void Start() {
        x = new int[G_UNITS];
        y = new int[G_UNITS];
        bodyParts = 5;
        pointsTaken = 0;
        direction = 'R';
        newPoint();
        running = true;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < S_WIDTH / U_SIZE; i++) {
                g.drawLine(i * U_SIZE, 0, i * U_SIZE, S_HEIGHT);
                g.drawLine(0, i * U_SIZE, S_WIDTH, i * U_SIZE);
            }
            g.setColor(pointColor);
            g.fillOval(pointX, pointY, U_SIZE, U_SIZE);

            drawSnake(g);
            g.setColor(Color.white);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(String.valueOf(pointsTaken), (S_WIDTH - metrics.stringWidth(String.valueOf(pointsTaken))) / 2, g.getFont().getSize());
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            g.drawString("Rismose JavaSnake", (S_WIDTH - metrics.stringWidth("Rismose JavaSnake")) + 225, 590);
        } else {
            for (int i = 0; i < S_WIDTH / U_SIZE; i++) {
                g.drawLine(i * U_SIZE, 0, i * U_SIZE, S_HEIGHT);
                g.drawLine(0, i * U_SIZE, S_WIDTH, i * U_SIZE);
            }
            gameOver(g);
        }

        drawSnake(g);
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(snakeHeadColor);
                g.fillRect(x[i], y[i], U_SIZE, U_SIZE);
            } else {
                g.setColor(SnakeColor);
                g.fillRect(x[i], y[i], U_SIZE, U_SIZE);
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - U_SIZE;
            case 'D' -> y[0] = y[0] + U_SIZE;
            case 'L' -> x[0] = x[0] - U_SIZE;
            case 'R' -> x[0] = x[0] + U_SIZE;
        }
    }

    public void newPoint() {
        pointX = random.nextInt((int) (S_WIDTH / U_SIZE)) * U_SIZE;
        pointY = random.nextInt((int) (S_HEIGHT / U_SIZE)) * U_SIZE;
    }

    public void checkPoint() {
        if ((x[0] == pointX) && (y[0] == pointY)) {
            bodyParts++;
            pointsTaken++;
            newPoint();
        }
        if (pointsTaken > highScorePoints) {
            highScorePoints = pointsTaken;
        }  // nothing
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > S_WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > S_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        bodyParts = 0;
        g.setColor(Color.white);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("High Score: " + highScorePoints, (S_WIDTH - metrics1.stringWidth("High Score: " + highScorePoints)) / 2, g.getFont().getSize());
        g.setColor(Color.white);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (S_WIDTH - metrics2.stringWidth("Game Over")) / 2, S_HEIGHT / 2 - 50);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("Press Space To Restart", (S_WIDTH - metrics2.stringWidth("Press Space To Restart")) / 2 + 315, S_HEIGHT / 2 + 200);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        g.drawString("Score: " + pointsTaken, (S_WIDTH - metrics1.stringWidth("Score: " + pointsTaken)) / 2 - 25, S_HEIGHT / 2 + 50);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        g.drawString("Rismose JavaSnake", (S_WIDTH - metrics1.stringWidth("Rismose JavaSnake")) + 225, 590);

    }

    public void restart() {
        Start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkPoint();
            checkCollision();
        }
        repaint();
    }

    public class KeyInput implements KeyListener {


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
                case KeyEvent.VK_SPACE -> {
                    if (!running) {
                        restart();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
