package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class ButtonIcon extends JButton {
    public static final int CHECKMARK = 0;
    public static final int CROSS = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int START = 4;

    public static final Color GREEN = new Color(64,155,76);
    public static final Color RED = new Color(201, 0, 0);

    private final Color color2 = Color.white;

    private int iconID;
    private Color color;
    private boolean isChecked;

    public ButtonIcon(int size, boolean startChecked) {
        setPreferredSize(new Dimension(size, size));
        setFocusable(false);
        color = startChecked ? GREEN : RED;
        iconID = startChecked ? CHECKMARK : CROSS;
        isChecked = startChecked;
        revalidate();
        repaint();
    }
    public ButtonIcon(int size, int iconID, Color color) {
        this.iconID = iconID;
        this.color = color;
        setPreferredSize(new Dimension(size, size));
        setFocusable(false);
    }

    public void setIconID(int iconID){
        this.iconID = iconID;
    }

    public void toggleIcon(){
        if(isChecked){
            color = RED;
            isChecked = false;
            setIconID(1);
        } else {
            color = GREEN;
            isChecked = true;
            setIconID(0);
        }
    }

    public boolean isChecked() {return isChecked;}

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
        if(iconID == CHECKMARK) { //check
            int checkmarkSize = ovalDiameter / 3; //size

            int checkmarkStartX = centerX - checkmarkSize / 2;
            int checkmarkStartY = centerY + checkmarkSize / 4;
            int checkmarkMiddleX = centerX - checkmarkSize / 8;
            int checkmarkMiddleY = centerY + checkmarkSize / 2;
            int checkmarkEndX = centerX + checkmarkSize / 2;
            int checkmarkEndY = centerY - checkmarkSize / 2;

            g2d.drawLine(checkmarkStartX, checkmarkStartY, checkmarkMiddleX, checkmarkMiddleY); // .
            g2d.drawLine(checkmarkMiddleX, checkmarkMiddleY, checkmarkEndX, checkmarkEndY); // /
        } else if(iconID == CROSS) { //cross
            int margin = 4;
            g2d.drawLine(centerX - (lineLength - margin), centerY - (lineLength - margin), centerX + (lineLength - margin), centerY + (lineLength - margin)); // \
            g2d.drawLine(centerX + (lineLength - margin), centerY - (lineLength - margin), centerX - (lineLength - margin), centerY + (lineLength - margin)); // /
        } else if(iconID == PLUS) { //plus
            g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength); // |
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY); // -
        } else if(iconID == MINUS) { //minus
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
        } else if(iconID == START){ //start
            int numPoints = 3;
            double outerRadius = (double) ovalDiameter / 3;
            double innerRadius = outerRadius / 2;

            //array for cords
            int[] xPoints = new int[2 * numPoints];
            int[] yPoints = new int[2 * numPoints];

            //calc cords
            for (int i = 0; i < 2 * numPoints; i++) {
                double angle = Math.PI / numPoints * i;
                double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                xPoints[i] = (int) Math.round(centerX + radius * Math.cos(angle));
                yPoints[i] = (int) Math.round(centerY + radius * Math.sin(angle));
            }

            //smooth
            GeneralPath star = new GeneralPath();
            star.moveTo(xPoints[0], yPoints[0]);
            for (int i = 1; i < xPoints.length; i++) {
                star.lineTo(xPoints[i], yPoints[i]);
            }
            star.closePath();

            //render
            g2d.fill(star);
        }
        else {
            System.err.println("Unknown icon ID: " + iconID);

            g2d.drawString(String.valueOf(iconID), centerX, centerY);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        //disable
    }
}
