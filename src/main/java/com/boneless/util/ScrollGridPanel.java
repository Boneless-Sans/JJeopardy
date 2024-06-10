package com.boneless.util;

import javax.swing.*;
import java.awt.*;

import static com.boneless.util.GeneralUtils.*;

public class ScrollGridPanel extends JPanel {
    public Timer timer;
    private double offsetX = 0; //track offset
    private double offsetY = 0;
    private final int squareSize = 80;
    private final Color squareColor = Color.blue;
    private double angle = 225; //direction of movement, in degrees, flipped

    public ScrollGridPanel() {
        setLayout(null);

        timer = new Timer(16, e -> updateOffsets());
        timer.start();
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
    }

    private void updateOffsets() {
        //convert angle to radians?? thanks stackoverflow
        double radians = Math.toRadians(angle);

        //speed controller
        double speed = 1;

        //calc direction with angle
        offsetX = (offsetX + speed * Math.cos(radians)) % squareSize;
        offsetY = (offsetY + speed * Math.sin(radians)) % squareSize;

        //offsets to remove gaps
        if (offsetX < 0) offsetX += squareSize;
        if (offsetY < 0) offsetY += squareSize;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //calc amount of squares needed plus a bit extra for wrapping
        int rows = (getHeight() / squareSize) + 2;
        int cols = (getWidth() / squareSize) + 2;

        //draw
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = (int) (col * squareSize - offsetX);
                int y = (int) (row * squareSize - offsetY);

                g2d.setPaint(new GradientPaint(x, y, squareColor, x + squareSize, y + squareSize, adjustColor(squareColor)));
                g2d.fillRect(x, y, squareSize, squareSize);
            }
        }
    }
}
