package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.boneless.Main.settingsFile;
import static com.boneless.util.GeneralUtils.adjustColor;

public class ScrollGridPanel extends JPanel {
    public Timer timer;
    private double offsetX = 0;
    private double offsetY = 0;
    private final int squareSize = 80;
    private final Color squareColor = Color.blue;
    private double angle = 225;
    private double speed = 50; //Pixels Per Second
    private long lastUpdateTime;
    private BufferedImage buffer;

    public ScrollGridPanel() {
        setLayout(null);
        setDoubleBuffered(true); //enable double buffer
        timer = new Timer(10, e -> updateOffsets());

        timer.start();

        lastUpdateTime = System.currentTimeMillis();
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
    }

    public void setSpeed(double speed) {
        this.speed = speed; //prob not needed, but it allows move dir change
    }

    private void updateOffsets() {
        if(!Boolean.parseBoolean(JsonFile.read(settingsFile, "misc", "play_background_ani"))){
            return;
        }
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - lastUpdateTime) / 1000.0; //in ms
        lastUpdateTime = currentTime;

        double radians = Math.toRadians(angle);

        offsetX = (offsetX + speed * elapsedTime * Math.cos(radians)) % squareSize;
        offsetY = (offsetY + speed * elapsedTime * Math.sin(radians)) % squareSize;

        if (offsetX < 0) offsetX += squareSize;
        if (offsetY < 0) offsetY += squareSize;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = buffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //calc + padding
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

        g.drawImage(buffer, 0, 0, null);
        g2d.dispose();
    }
}
