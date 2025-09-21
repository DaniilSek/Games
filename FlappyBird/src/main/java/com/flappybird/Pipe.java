package com.flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Логика труб (генерация, движение, коллизии)
 */
public class Pipe {

    private int x;
    private int width;
    private int topHeight;
    private int bottomY;
    private int gap;
    private boolean passed;
    private BufferedImage pipeImage;

    public Pipe(int x, int topHeight, BufferedImage pipeImage) {
        this.x = x;
        this.width = 70;
        this.topHeight = topHeight;
        this.gap = 150;
        this.bottomY = topHeight + gap;
        this.passed = false;
        this.pipeImage = pipeImage;
    }

    public void update() {
        x -= 5;
    }

    public void draw(Graphics g) {
        if (pipeImage != null) {
            // Верхняя труба (перевернутая)
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(1, -1);
            g2d.drawImage(pipeImage, x, -topHeight, width, topHeight, null);
            g2d.scale(1, -1);

            // Нижняя труба
            g2d.drawImage(pipeImage, x, bottomY, width, HEIGHT - bottomY, null);
        } else {
            // Резервная отрисовка
            g.setColor(Color.GREEN);
            g.fillRect(x, 0, width, topHeight);
            g.fillRect(x, bottomY, width, 600 - bottomY);

            g.setColor(new Color(0, 100, 0));
            g.fillRect(x - 5, topHeight - 20, width + 10, 20);
            g.fillRect(x - 5, bottomY, width + 10, 20);
        }
    }

    public boolean collidesWith(Bird bird) {
        Rectangle birdBounds = bird.getBounds();
        Rectangle topPipe = new Rectangle(x, 0, width, topHeight);
        Rectangle bottomPipe = new Rectangle(x, bottomY, width, 600 - bottomY);

        return birdBounds.intersects(topPipe) || birdBounds.intersects(bottomPipe);
    }

    public int getX() { return x; }
    public int getWidth() { return width; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    private static final int HEIGHT = 600;
}
