package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class GradientSquare {
    private int x;
    private int y;
    private final int size;
    private final BufferedImage image;

    public GradientSquare(int x, int y, int size, Color color1, Color color2) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.image = createGradientImage(size, color1, color2);
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

public class ScrollGridPanel extends JPanel {
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    private final List<GradientSquare> squareList = new ArrayList<>();
    public static final int GAP = 1;
    public static final int SQUARE_SIZE = 70;
    private static Color color1 = Color.blue;
    private static Color color2 = Color.gray;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

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
        int columns = (WIDTH / (SQUARE_SIZE + GAP)) + 2; // Adding buffer columns
        int rows = (HEIGHT / (SQUARE_SIZE + GAP)) + 2; // Adding buffer rows

        for (int y = -SQUARE_SIZE; y < HEIGHT + SQUARE_SIZE; y += (SQUARE_SIZE + GAP)) {
            for (int x = -SQUARE_SIZE; x < WIDTH + SQUARE_SIZE; x += (SQUARE_SIZE + GAP)) {
                squareList.add(new GradientSquare(x, y, SQUARE_SIZE, color1, color2));
            }
        }
    }

    private void moveSquares() {
        List<List<GradientSquare>> clusters = createClusters();

        for (List<GradientSquare> cluster : clusters) {
            executor.submit(() -> {
                for (GradientSquare square : cluster) {
                    square.move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y, WIDTH, HEIGHT);
                }
            });
        }

        // Wait for all tasks to complete
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        executor = Executors.newFixedThreadPool(NUM_THREADS); // Reinitialize the executor for the next call
    }

    private List<List<GradientSquare>> createClusters() {
        int clusterSize = (int) Math.ceil((double) squareList.size() / NUM_THREADS);
        List<List<GradientSquare>> clusters = new ArrayList<>();

        for (int i = 0; i < squareList.size(); i += clusterSize) {
            clusters.add(new ArrayList<>(squareList.subList(i, Math.min(i + clusterSize, squareList.size()))));
        }

        return clusters;
    }

    private void drawSquares(Graphics2D g2d) {
        List<List<GradientSquare>> clusters = createClusters();

        for (List<GradientSquare> cluster : clusters) {
            executor.submit(() -> {
                for (GradientSquare square : cluster) {
                    synchronized (g2d) {
                        square.draw(g2d);
                    }
                }
            });
        }

        // Wait for all tasks to complete
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        executor = Executors.newFixedThreadPool(NUM_THREADS); // Reinitialize the executor for the next call
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawSquares(g2d);
    }

    public static JPanel createScrollGridPanel() {
        return new ScrollGridPanel();
    }
}
