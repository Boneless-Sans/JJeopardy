package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class GradientSquare {
    private int x;
    private int y;
    private int size;
    private Color color1; // Starting color of the gradient
    private Color color2; // Ending color of the gradient

    public GradientSquare(int x, int y, int size, Color color1, Color color2) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color1 = color1;
        this.color2 = color2;
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
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(x, y, color1, x + size, y + size, color2);
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, size, size);
    }
}

public class ScrollGridPanel extends JPanel {

    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    private final ArrayList<GradientSquare> squareList = new ArrayList<>();
    public static final int GAP = 1;
    public static final int SQUARE_SIZE = 70;

    public ScrollGridPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initializeSquares();
        setBackground(Color.black);

        Timer timer = new Timer(20, e -> {
            moveSquares();
            repaint();
        });

        timer.start();
    }

    private void initializeSquares() {
        int squareCount = (HEIGHT / SQUARE_SIZE) * (WIDTH / SQUARE_SIZE) * 2; // Double the number of squares to handle wraparound

        for (int i = 0; i < squareCount; i++) {
            int x = (i % (WIDTH / SQUARE_SIZE)) * (SQUARE_SIZE + GAP);
            int y = (i / (WIDTH / SQUARE_SIZE)) * (SQUARE_SIZE + GAP);
            squareList.add(new GradientSquare(x, y, SQUARE_SIZE, Color.BLUE, Color.RED));
        }
    }

    private void moveSquares() {
        for (GradientSquare square : squareList) {
            square.move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y);
        }
    }

    private void drawSquares(Graphics g) {
        for (GradientSquare square : squareList) {
            square.draw(g);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSquares(g);
    }

    public static JPanel createScrollGridPanel() {
        return new ScrollGridPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(WIDTH, HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(createScrollGridPanel());
            frame.setVisible(true);
        });
    }
}
