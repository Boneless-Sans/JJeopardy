package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static com.boneless.GameBoard.fontColor;
import static com.boneless.GameBoard.mainColor;
import static com.boneless.Main.fileName;

public class GeneralUtils {
    public static final GridBagConstraints gbc = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        fill = 0;
    }};

    public static void renderIcon(){
        int size = 103;
        int posX = 12;
        int posY = 12;
        int arc = 40;
        int fontSize = 18;
        String jeopardy;
        Color color;
        Color fontColor;
        Font font;

        if(fileName == null){
            jeopardy = "Jeopardy!";
            color = new Color(20,20,255);
            fontColor = Color.white;
            font = new Font("New Roman Times", Font.PLAIN, fontSize);
        } else {
            jeopardy = JsonFile.read(fileName,"data","icon_text");
            color = parseColor(JsonFile.read(fileName, "data", "global_color"));
            fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
            font = generateFont(fontSize);
        }

        BufferedImage image = new BufferedImage(128,128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setPaint(new GradientPaint(0,0,color,128,128,ScrollGridPanel.adjustColor(color)));
        g2d.fillRoundRect(posX,posY,size,size,arc,arc);

        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(jeopardy);
        int textHeight = fm.getHeight();

        int x = posX + (size - textWidth) / 2;
        int y = posY + (size - textHeight) / 2 + fm.getAscent();

        g2d.setColor(fontColor);
        g2d.drawString(jeopardy, x, y);

        Taskbar.getTaskbar().setIconImage(image);
    }

    public static Color parseColor(String color){
        String[] split = color.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }

    public static Color parseColorFade(String color, int alpha){
        String[] split = color.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue,alpha);
    }

    public static Font generateFont(int fontSize){
        try {
            if(fileName != null && !fileName.isEmpty()) {
                return new Font(
                        JsonFile.read(fileName, "data", "font"),
                        Font.PLAIN,
                        fontSize
                );
            }
        } catch (NullPointerException e){
            return new Font("Arial", Font.PLAIN, fontSize);
        }

        return new Font("Arial", Font.PLAIN, fontSize);
    }

    public static void changeCurrentPanel(JPanel panelToSet, JComponent self) {
        Container parent = self.getParent();

        if(parent == null){
            System.err.println("Warning: Parent is null! Panels will not change!");
            return;
        }
        parent.remove(self);
        parent.add(panelToSet);

        parent.revalidate();
        parent.repaint();
    }

    public static JPanel createGap(int size, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(size, size));
        return panel;
    }
}
