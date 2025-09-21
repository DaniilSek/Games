package com.flappybird;

import javax.swing.*;

/**
 * Создание и настройка игрового окна
 */
public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.startGame();
    }
}
