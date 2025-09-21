package com.flappybird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Загрузка и обработка изображений
 */
public class ImageLoader {

    public static BufferedImage loadImage(String path) {
        try {
            InputStream inputStream = ImageLoader.class.getResourceAsStream(path);
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            } else {
                System.out.println("Изображение не найдено: " + path);
                return createFallbackImage(path);
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки изображения: " + path);
            return createFallbackImage(path);
        }
    }

    private static BufferedImage createFallbackImage(String path) {
        if (path.contains("bird")) {
            return createBirdImage();
        } else if (path.contains("pipe")) {
            return createPipeImage();
        } else if (path.contains("background")) {
            return createBackgroundImage();
        } else {
            return createDefaultImage();
        }
    }

    private static BufferedImage createBirdImage() {
        BufferedImage image = new BufferedImage(40, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Прозрачный фон
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, 40, 30);
        g2d.setComposite(AlphaComposite.SrcOver);

        // Тело птицы
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(5, 5, 30, 20);

        // Глаз
        g2d.setColor(Color.BLACK);
        g2d.fillOval(30, 10, 8, 8);

        // Клюв
        g2d.setColor(Color.ORANGE);
        g2d.fillPolygon(
                new int[]{35, 40, 35},
                new int[]{12, 15, 18},
                3
        );

        g2d.dispose();
        return image;
    }

    private static BufferedImage createPipeImage() {
        BufferedImage image = new BufferedImage(70, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Прозрачный фон
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, 70, 600);
        g2d.setComposite(AlphaComposite.SrcOver);

        // Основная часть трубы
        g2d.setColor(new Color(0, 150, 0));
        g2d.fillRect(0, 0, 70, 600);

        // "Шляпка" трубы
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(-5, 0, 80, 20);
        g2d.fillRect(-5, 580, 80, 20);

        // Световые блики
        g2d.setColor(new Color(0, 200, 0, 100));
        g2d.fillRect(5, 0, 10, 600);

        g2d.dispose();
        return image;
    }

    private static BufferedImage createBackgroundImage() {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Градиентное небо
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(135, 206, 235),
                0, 600, new Color(176, 226, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 800, 600);

        // Облака
        g2d.setColor(new Color(255, 255, 255, 200));
        drawCloud(g2d, 100, 100);
        drawCloud(g2d, 300, 150);
        drawCloud(g2d, 500, 80);
        drawCloud(g2d, 700, 120);

        g2d.dispose();
        return image;
    }

    private static BufferedImage createDefaultImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.MAGENTA);
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(Color.BLACK);
        g2d.drawString("?", 20, 25);
        g2d.dispose();
        return image;
    }

    private static void drawCloud(Graphics2D g2d, int x, int y) {
        g2d.fillOval(x, y, 60, 40);
        g2d.fillOval(x + 20, y - 10, 70, 50);
        g2d.fillOval(x + 50, y, 60, 40);
    }

}
