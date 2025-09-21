package com.flappybird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Основная игровая логика, рендеринг, обработка input'а
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND_HEIGHT = 100;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -15;
    private static final int PIPE_SPEED = 5;
    private static final int PIPE_SPACING = 200;
    private static final int PIPE_GAP = 150;

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private Timer gameTimer;
    private int score;
    private boolean gameOver;
    private boolean gameStarted;
    private Random random;

    private BufferedImage backgroundImage;
    private BufferedImage groundImage;
    private BufferedImage birdImage;
    private BufferedImage pipeImage;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        random = new Random();
        bird = new Bird(100, HEIGHT / 2);
        pipes = new ArrayList<>();
        score = 0;
        gameOver = false;
        gameStarted = false;

        // Загрузка изображений (заглушки)
        //background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //ground = new BufferedImage(WIDTH, GROUND_HEIGHT, BufferedImage.TYPE_INT_RGB);
        loadImages();
        createGroundImage();

        gameTimer = new Timer(16, this); // ~60 FPS
    }

    private void loadImages() {
        birdImage = ImageLoader.loadImage("src/main/resources/images/bird.png");
        pipeImage = ImageLoader.loadImage("src/main/resources/images/pipe.png");
        backgroundImage = ImageLoader.loadImage("src/main/resources/images/background.png");
    }

    private void createGroundImage() {
        groundImage = new BufferedImage(WIDTH, GROUND_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = groundImage.createGraphics();

        // Земля
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(0, 0, WIDTH, GROUND_HEIGHT);

        // Трава
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, 0, WIDTH, 20);

        // Текстура травы
        g2d.setColor(new Color(20, 120, 20));
        for (int i = 0; i < WIDTH; i += 10) {
            g2d.drawLine(i, 5, i + 5, 0);
            g2d.drawLine(i + 5, 0, i + 10, 5);
        }

        g2d.dispose();
    }

    public void startGame() {
        gameTimer.start();
    }

    private void update() {
        if (!gameStarted || gameOver) return;

        bird.update();

        // Генерация новых труб
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < WIDTH - PIPE_SPACING) {
            int pipeHeight = random.nextInt(200) + 100;
            pipes.add(new Pipe(WIDTH, pipeHeight, pipeImage));
        }

        // Обновление труб и проверка столкновений
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.update();

            // Проверка столкновений
            if (pipe.collidesWith(bird)) {
                gameOver = true;
                return;
            }

            // Увеличение счета
            if (!pipe.isPassed() && pipe.getX() + pipe.getWidth() < bird.getX()) {
                pipe.setPassed(true);
                score++;
            }

            // Удаление труб за экраном
            if (pipe.getX() + pipe.getWidth() < 0) {
                pipes.remove(i);
                i--;
            }
        }

        // Проверка столкновения с землей
        if (bird.getY() + bird.getHeight() > HEIGHT - GROUND_HEIGHT) {
            gameOver = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Отрисовка фона
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT - GROUND_HEIGHT, null);
        } else {
            g2d.setColor(new Color(135, 206, 235));
            g2d.fillRect(0, 0, WIDTH, HEIGHT - GROUND_HEIGHT);
        }

        // Отрисовка труб
        for (Pipe pipe : pipes) {
            pipe.draw(g2d);
        }

        // Отрисовка земли
        if (groundImage != null) {
            g2d.drawImage(groundImage, 0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT, null);
        } else {
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT);
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, 20);
        }

        // Отрисовка птицы
        if (birdImage != null) {
            // Небольшая анимация при прыжке/падении
            double rotation = Math.toRadians(bird.getVelocity() * 2);
            if (rotation > Math.toRadians(45)) rotation = Math.toRadians(45);
            if (rotation < Math.toRadians(-45)) rotation = Math.toRadians(-45);

            g2d.rotate(rotation, bird.getX() + bird.getWidth() / 2, bird.getY() + bird.getHeight() / 2);
            g2d.drawImage(birdImage, bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);
            g2d.rotate(-rotation, bird.getX() + bird.getWidth() / 2, bird.getY() + bird.getHeight() / 2);
        } else {
            bird.draw(g2d);
        }

        // Отрисовка счета с тенью
        g2d.setFont(new Font("Arial", Font.BOLD, 36));

        // Тень
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 22, 42);

        // Основной текст
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + score, 20, 40);

        // Отрисовка сообщений
        if (!gameStarted) {
            drawCenteredString(g2d, "Press SPACE to start", new Font("Arial", Font.BOLD, 48),
                    Color.BLACK, WIDTH / 2, HEIGHT / 2);
        }

        if (gameOver) {
            drawCenteredString(g2d, "Game Over!", new Font("Arial", Font.BOLD, 48),
                    Color.RED, WIDTH / 2, HEIGHT / 2 - 50);
            drawCenteredString(g2d, "Final Score: " + score, new Font("Arial", Font.BOLD, 36),
                    Color.WHITE, WIDTH / 2, HEIGHT / 2);
            drawCenteredString(g2d, "Press R to restart", new Font("Arial", Font.BOLD, 36),
                    Color.WHITE, WIDTH / 2, HEIGHT / 2 + 50);
        }
    }

    private void drawCenteredString(Graphics2D g2d, String text, Font font, Color color, int x, int y) {
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        g2d.setColor(color);
        g2d.drawString(text, x - textWidth / 2, y);
    }

    private void restartGame() {
        bird = new Bird(100, HEIGHT / 2);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                gameStarted = true;
            }
            if (!gameOver) {
                bird.jump();
            }
        }

        if (key == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
