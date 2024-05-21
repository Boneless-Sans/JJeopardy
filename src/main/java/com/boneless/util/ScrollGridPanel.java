package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ScrollGridPanel extends JPanel {
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    private final ArrayList<GradientSquare> squareList = new ArrayList<>();
    private final int squareSize;
    private final int gap = 1; // Single-pixel gap
    private Color color1 = new Color(0, 0, 150);
    private Color color2 = new Color(20, 20, 255);

    public ScrollGridPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        int maxSquaresHorizontal = (WIDTH + gap) / (100 + gap); // Assuming a minimum square size of 100
        int maxSquaresVertical = (HEIGHT + gap) / (100 + gap);
        squareSize = Math.min(WIDTH / maxSquaresHorizontal, HEIGHT / maxSquaresVertical);
        initializeSquares();
        setBackground(Color.black);

        Timer timer = new Timer(20, e -> {
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

    private Color adjustColor(Color color) {
        int adjustment = -100;
        int r = clamp(color.getRed() + adjustment);
        int g = clamp(color.getGreen() + adjustment);
        int b = clamp(color.getBlue() + adjustment);
        return new Color(r, g, b);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private void initializeSquares() {
        for (int y = 0; y < HEIGHT; y += (squareSize + gap)) {
            for (int x = 0; x < WIDTH; x += (squareSize + gap)) {
                squareList.add(new GradientSquare(x, y, squareSize, color1, color2));
            }
        }
    }

    private void moveSquares() {
        GradientSquare lastSquare = null;
        for (GradientSquare square : squareList) {
            square.move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y, WIDTH, HEIGHT);
            if (isOutsideScreen(square, WIDTH, HEIGHT)) {
                if (lastSquare != null) {
                    square.snapToLastSquare(lastSquare);
                }
            } else {
                lastSquare = square;
            }
        }
    }

    private boolean isOutsideScreen(GradientSquare square, int width, int height) {
        int x = square.getX();
        int y = square.getY();
        int size = square.getSize();
        return x < 0 - size || x > width || y < 0 - size || y > height;
    }

    private void drawSquares(Graphics2D g2d) {
        for (GradientSquare square : squareList) {
            square.draw(g2d);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawSquares(g2d);
    }

    private class GradientSquare {
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

        public void changeColors(Color color1, Color color2) {
            image = createGradientImage(size, color1, color2);
        }

        private BufferedImage createGradientImage(int size, Color color1, Color color2) {
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            GradientPaint gradient = new GradientPaint(0, 0, color1, size, size, color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, size, size);
            g2d.dispose();
            return image;
        }

        public void move(int speedX, int speedY, int width, int height) {
            x += speedX;
            y += speedY;
        }

        public void snapToLastSquare(GradientSquare lastSquare) {
            x = lastSquare.getX() + size + gap;
            y = lastSquare.getY();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }

        public void draw(Graphics2D g2d) {
            g2d.drawImage(image, x, y, null);
        }
    }
}