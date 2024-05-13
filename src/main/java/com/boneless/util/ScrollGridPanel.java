package com.boneless.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

@SuppressWarnings("CallToPrintStackTrace")
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

    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final double SCROLL_SPEED_X = 1;
    public static final double SCROLL_SPEED_Y = 1;
    private List<Square> squareList = new ArrayList<>();
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
            squareList.add(new Square(x, y, SQUARE_SIZE, "textures/background_tile.png"));
        }
    }

    private void moveSquares() {
        for (Square square : squareList) {
            square.move((int) SCROLL_SPEED_X, (int) SCROLL_SPEED_Y);
        }
    }

    private void drawSquares(Graphics g) {
        for (Square square : squareList) {
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
