package com.boneless;

import com.boneless.util.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Dev extends JFrame {
    public static void main(String[] args){
        new Dev();
    }
    public Dev(){
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        init();
        setVisible(true);
    }
    private void init() {

        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();

                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = (Graphics2D) g;

                // enable antialiasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //part 1: rounded corner


                int centerX = width / 2;
                int centerY = height / 2;

                String text = "Jeopardy!";
                Font font = new Font("Comic Sans MS", Font.PLAIN, 95);

                //shadow props
                g2d.setColor(new Color(10, 20, 70, 200));
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics(font);
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int textX = centerX - (textWidth / 2);
                int textY = centerY + (textHeight / 4);

                //draw multiple shadows to create one nice even one
                int shadowOffset = 2;
                for (int x = -shadowOffset; x <= shadowOffset; x++) {
                    for (int y = -shadowOffset; y <= shadowOffset; y++) {
                        if (x != 0 || y != 0) {
                            g2d.drawString(text, textX + x, textY + y);
                        }
                    }
                }

                //main text
                g2d.setColor(Color.white);
                g2d.drawString(text, textX, textY);

                //
                // Apply rounded corners to the off-screen image
                Graphics2D g2dFinal = (Graphics2D) g;
                g2dFinal.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cornerRadius = 25; // Adjust the corner radius as needed
                BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2dRounded = roundedImage.createGraphics();
                g2dRounded.setComposite(AlphaComposite.Src);
                g2dRounded.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dRounded.setColor(new Color(0, 0, 0, 0)); // Transparent color
                g2dRounded.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
                g2dRounded.setComposite(AlphaComposite.SrcAtop);
                g2dRounded.drawImage(bufferedImage, 0, 0, null);
                g2dRounded.dispose();

                // Draw the final image with rounded corners on the component
                g2dFinal.drawImage(roundedImage, 0, 0, null);
            }
        };
        panel.setBackground(new Color(38,51,135));


        add(panel);
    }
}
