package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class ScrollGridPanel extends JPanel {
    protected Timer timer;

    private final ArrayList<Square> squareArrayList = new ArrayList<>();

    public ScrollGridPanel() {
        setLayout(null);

        int squareSize = 64;
        int squareX = 0;
        int squareY = 0;

        for(int i = 0; i < screenHeight / squareSize; i++) {
            for(int j = 0;j < screenWidth / squareSize; j++) {
                Square square = new Square(64, Color.blue);
                square.setBounds(squareX, squareY, squareSize, squareSize);
                add(square);
                squareArrayList.add(square);
                squareX += squareSize;
            }
            squareX = 0;
            squareY += squareSize;
        }

        timer = new Timer(16, e -> updateSquares(squareSize));

        timer.start();
    }

    private void updateSquares(int squareSize) {
        for (Square square : squareArrayList) {
            int newX = square.getX() + 1;
            int newY = square.getY();

            // Check if the square goes out of the screen bounds horizontally
            if (newX >= screenWidth) {
                newX = 0;
                newY += squareSize;
            }

            // Check if the square goes out of the screen bounds vertically
            if (newY >= screenHeight) {
                newY = 0;
            }

            square.setBounds(newX, newY, square.getWidth(), square.getHeight());
        }
    }

    private static class Square extends JPanel {
        private final int size;
        private Color color;

        public Square(int size, Color color){
            this.size = size;
            this.color = color;
        }

        public void changeColor(Color color){
            this.color = color;
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(new GradientPaint(0,0, color, size, size, adjustColor(color)));
            g2d.fillRect(0, 0, screenWidth, screenHeight);
        }
    }
}