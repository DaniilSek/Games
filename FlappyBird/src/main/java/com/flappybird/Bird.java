package com.flappybird;

import java.awt.*;

/**
 * Логика птицы (движение, прыжки, коллизии)
 */
public class Bird {

    private int x, y;
    private int width, height;
    private int velocity;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 30;
        this.velocity = 0;
    }

    public void update() {
        velocity += 1; // Гравитация
        y += velocity;

        // Ограничение скорости
        if (velocity > 15) velocity = 15;
    }

    public void jump() {
        velocity = -15;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);

        // Глаз
        g.setColor(Color.BLACK);
        g.fillOval(x + width - 10, y + 10, 8, 8);

        // Клюв
        g.setColor(Color.ORANGE);
        g.fillPolygon(
                new int[]{x + width, x + width + 15, x + width},
                new int[]{y + height / 2 - 5, y + height / 2, y + height / 2 + 5},
                3
        );
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getVelocity() { return velocity; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
