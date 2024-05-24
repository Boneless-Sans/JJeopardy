package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class IconRenderer extends JComponent {

    private final Color colorGradient1;
    private final Color colorGradient2;
    private final Color textColor;
    private final Color textBorderColor;
    private final String text;
    private final Font font;

    public IconRenderer(Color colorGradient1, Color colorGradient2, Color textColor, Color textBorderColor, String text, Font font) {
        this.colorGradient1 = colorGradient1;
        this.colorGradient2 = colorGradient2;
        this.textColor = textColor;
        this.textBorderColor = textBorderColor;
        this.text = text;
        this.font = font;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Get component size
        int width = getWidth();
        int height = getHeight();

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(0, 0, colorGradient1, width, height, colorGradient2);
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, 20, 20));

        // Draw text
        g2d.setColor(textColor);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);

        // Cartoon-like text border
        g2d.setColor(textBorderColor);
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    g2d.drawString(text, x + i, y + j);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        // Create IconRenderer instance
        IconRenderer iconRenderer = new IconRenderer(Color.WHITE, Color.BLUE,Color.white,Color.black,"Jeopardy!",GeneralUtils.generateFont(50));

        // Add IconRenderer to the frame
        frame.add(iconRenderer, BorderLayout.CENTER);

        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
