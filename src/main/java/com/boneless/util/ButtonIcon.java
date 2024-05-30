package com.boneless.util;

import javax.swing.*;
import java.awt.*;

public class ButtonIcon extends JButton {
    public static final int CHECKMARK = 0;
    public static final int CROSS = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int START = 4;


    private final Color color;
    private final Color color2 = Color.white;

    private int iconID;
    private boolean isChecked = false;

    public ButtonIcon(boolean startChecked) {
        if(startChecked) {
            color = new Color(64,155,76);
        } else {
            color = new Color(201, 0, 0);
        }
        addActionListener(e -> {
            if(isChecked){
                setIconID(1);
                setBackground(color);
                isChecked = false;
            } else {
                setIconID(0);
                isChecked = true;
            }
        });
    }
    public ButtonIcon(int iconID, Color color, boolean isCheckBox) {
        this.iconID = iconID;
        this.color = color;
    }

    public void setIconID(int iconID){
        this.iconID = iconID;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        int lineThickness = 4;

        int lineLength = Math.min(width, height) / 4; //sets line length, higher is smaller

        int ovalDiameter = (Math.min(width, height) / 2) + 10;
        int ovalX = centerX - ovalDiameter / 2;
        int ovalY = centerY - ovalDiameter / 2;

        g2d.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        //draw background circle
        g2d.setColor(color);
        g2d.fillOval(ovalX, ovalY, ovalDiameter, ovalDiameter);

        g2d.setColor(color2);
        if(iconID == 0) { //check
            int checkmarkSize = ovalDiameter / 3; //size

            int checkmarkStartX = centerX - checkmarkSize / 2;
            int checkmarkStartY = centerY + checkmarkSize / 4;
            int checkmarkMiddleX = centerX - checkmarkSize / 8;
            int checkmarkMiddleY = centerY + checkmarkSize / 2;
            int checkmarkEndX = centerX + checkmarkSize / 2;
            int checkmarkEndY = centerY - checkmarkSize / 2;

            g2d.drawLine(checkmarkStartX, checkmarkStartY, checkmarkMiddleX, checkmarkMiddleY); // .
            g2d.drawLine(checkmarkMiddleX, checkmarkMiddleY, checkmarkEndX, checkmarkEndY); // /
        } else if(iconID == 1) { //cross
            int margin = 4;
            g2d.drawLine(centerX - (lineLength - margin), centerY - (lineLength - margin), centerX + (lineLength - margin), centerY + (lineLength - margin)); // \
            g2d.drawLine(centerX + (lineLength - margin), centerY - (lineLength - margin), centerX - (lineLength - margin), centerY + (lineLength - margin)); // /
        } else if(iconID == 2) { //plus
            g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength); // |
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY); // -
        } else if(iconID == 3) { //minus
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
        } else if(iconID == 4){ //start
            int numPoints = 3;
            double outerRadius = (double) ovalDiameter / 2;
            double innerRadius = outerRadius / 2;

            //hell :(
            int[] xPoints = new int[2 * numPoints];
            int[] yPoints = new int[2 * numPoints];

            // calc cords
            for (int i = 0; i < 2 * numPoints; i++) {
                double angle = Math.PI / numPoints * i;
                double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                xPoints[i] = (int) (centerX + radius * Math.cos(angle));
                yPoints[i] = (int) (centerY + radius * Math.sin(angle));
            }

            //finally, draw
            g2d.fillPolygon(xPoints, yPoints, 2 * numPoints);
        }
        else {
            System.err.println("Unknown icon ID: " + iconID);

            g2d.drawString(String.valueOf(iconID), centerX, centerY);
        }
        g2d.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        //disable
    }
}
