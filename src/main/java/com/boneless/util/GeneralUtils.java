package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static com.boneless.GameBoard.mainColor;
import static com.boneless.Main.fileName;

public class GeneralUtils {
    public static final GridBagConstraints gbc = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        fill = 0;
    }};

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

    public static Color parseColorFadeComplete() {
        return parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
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
