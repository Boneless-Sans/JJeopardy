package com.boneless.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

class Square {
    private int x;
    private int y;
    private int size;
    private Image image;  // Image to represent the square

    public Square(int x, int y, int size, String imagePath) {
        this.x = x;
        this.y = y;
        this.size = size;

        // Load the image
        try {
            URL imageURL = getClass().getClassLoader().getResource(imagePath);
            if (imageURL != null) {
                this.image = ImageIO.read(imageURL);
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(int speedX, int speedY) {
        x += speedX;
        y += speedY;

        // Check if square is beyond the right edge
        if (x > ScrollGridPanel.WIDTH) {
            x = -size;
        }

        // Check if square is beyond the bottom edge
        if (y > ScrollGridPanel.HEIGHT) {
            y = -size;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}

public class ScrollGridPanel extends JPanel {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int ROW_COUNT = 9;
    public static final int COL_COUNT = 9;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    public static final int GAP = 1;
    public static final int SQUARE_SIZE = 70;

    private Square[][] squares = new Square[ROW_COUNT][COL_COUNT];

    public ScrollGridPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initializeSquares();
        setBackground(Color.black);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSquares();
                repaint();
            }
        });

        timer.start();
    }

    private void initializeSquares() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                squares[i][j] = new Square(
                        j * (SQUARE_SIZE + GAP),
                        i * (SQUARE_SIZE + GAP),
                        SQUARE_SIZE,
                        "assets/textures/background_tile.png"  // Replace with the actual image path
                );
            }
        }
    }

    private void moveSquares() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                squares[i][j].move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSquares(g);
    }

    private void drawSquares(Graphics g) {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                squares[i][j].draw(g);
            }
        }
    }

    public static JPanel createScrollGridPanel() {
        return new ScrollGridPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(WIDTH, HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(createScrollGridPanel());
                frame.setVisible(true);
            }
        });
    }
}
