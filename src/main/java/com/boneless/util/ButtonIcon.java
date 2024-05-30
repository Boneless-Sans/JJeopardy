package com.boneless.util;

import javax.swing.*;
import java.awt.*;

import static com.boneless.util.GeneralUtils.generateFont;

public class ButtonIcon extends JButton {
    private final int iconID;
    private final Color color;
    private final Color color2;

    public ButtonIcon(int iconID, Color color, Color color2) {
        this.iconID = iconID;
        this.color = color;
        this.color2 = color2;
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

            g2d.drawLine(checkmarkStartX, checkmarkStartY, checkmarkMiddleX, checkmarkMiddleY);
            g2d.drawLine(checkmarkMiddleX, checkmarkMiddleY, checkmarkEndX, checkmarkEndY);
        } else if(iconID == 1) { //cross

        } else if(iconID == 2) { //plus
            g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength); // |
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY); // -
        } else if(iconID == 3) { //minus
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
        } else {
            System.err.println("Unknown icon ID: " + iconID);

            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int size = 2;
            int textWidth = fm.stringWidth("?");
            int textHeight = fm.getHeight();
            int x = (size - textWidth) / 2;
            int y = (size - textHeight) / 2 + fm.getAscent();

            g2d.drawString("?", x, y);
        }
        g2d.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        //disable
    }
}
