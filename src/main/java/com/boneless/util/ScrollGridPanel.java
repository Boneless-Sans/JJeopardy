package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.boneless.Main.fileName;

public class ScrollGridPanel extends JPanel {
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    private final ArrayList<GradientSquare> squareList = new ArrayList<>();
    public static final int GAP = 1;
    public static final int SQUARE_SIZE = 70;
    public Timer timer;
    private Color color1 = new Color(0,0,150);
    private Color color2 = new Color(20,20,255);

    public ScrollGridPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initializeSquares();
        setBackground(Color.black);

        timer = new Timer(20, e -> {
            moveSquares();
            repaint();
        });

        timer.start();
    }

    protected void changeColor(Color color) {
        color1 = color;
        color2 = adjustColor(color);
        for (GradientSquare gradientSquare : squareList) {
            gradientSquare.changeColors(color1, color2);
        }
    }

    public static Color adjustColor(Color color) {
        int totalRGB = color.getRed() + color.getGreen() + color.getBlue();
        int adjustment = totalRGB > 180 ? -100 : 100;
        int r = clamp(color.getRed() + adjustment);
        int g = clamp(color.getGreen() + adjustment);
        int b = clamp(color.getBlue() + adjustment);
        return new Color(r, g, b);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private void initializeSquares() {
        for (int y = -SQUARE_SIZE; y < HEIGHT + SQUARE_SIZE; y += (SQUARE_SIZE + GAP)) {
            for (int x = -SQUARE_SIZE; x < WIDTH + SQUARE_SIZE; x += (SQUARE_SIZE + GAP)) {
                squareList.add(new GradientSquare(x, y, SQUARE_SIZE, color1, color2));
            }
        }
    }
    private void moveSquares() {
        for (GradientSquare square : squareList) {
            square.move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y, WIDTH, HEIGHT); //pause to optimize
        }
    }
    private void drawSquares(Graphics2D g2d) {
        for (GradientSquare square : squareList) {
            square.draw(g2d);
        }
    }
    public static BufferedImage createGradientImage(int size, Color color1, Color color2) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        GradientPaint gradient = new GradientPaint(0, 0, color1, size, size, color2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();
        return image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawSquares(g2d);
    }
    private static class GradientSquare {
        private int x;
        private int y;
        private final int size;
        private BufferedImage image;

        public GradientSquare(int x, int y, int size, Color color1, Color color2) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.image = createGradientImage(size, color1, color2);
        }
        public void changeColors(Color color1, Color color2){
            image = createGradientImage(size, color1, color2);
        }
        public void move(int speedX, int speedY, int width, int height) {
            x += speedX;
            y += speedY;

            if (x > width) {
                x = -size;
            }

            if (y > height) {
                y = -size;
            }
        }

        public void draw(Graphics2D g2d) {
            g2d.drawImage(image, x, y, null);
        }
    }
}