import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int SCALE = 10;
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final int SPEED = 100;

    private ArrayList<Point> snake;
    private Point food;
    private boolean isPlaying;
    private Direction direction;
    private Timer timer;

    public SnakeGame() {
        snake = new ArrayList<>();
        direction = Direction.RIGHT;
        isPlaying = true;
        timer = new Timer(SPEED, this);
        timer.start();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            }
        });

        initGame();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5)); // Initial snake position
        spawnFood();
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / SCALE);
        int y = random.nextInt(HEIGHT / SCALE);
        food = new Point(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying) {
            move();
            checkCollisions();
            repaint();
        }
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        if (head.x < 0 || head.y < 0 || head.x >= WIDTH / SCALE || head.y >= HEIGHT / SCALE) {
            gameOver();
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        isPlaying = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Snake Game", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isPlaying) {
            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * SCALE, point.y * SCALE, SCALE, SCALE);
            }

            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * SCALE, food.y * SCALE, SCALE, SCALE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
