package com.boneless.util;

import javax.swing.*;
import java.awt.*;

public class IconResize {
    private ImageIcon imageIcon;
    private String icon;
    private int width;
    private int height;

    private static final String DEFAULT_PATH = "src/main/resources/assets/textures/";

    public IconResize() {
        icon = DEFAULT_PATH + "default.png";
        width = 100;
        height = 100;
        loadImage();
    }

    public IconResize(String icon, int width, int height) {
        this.icon = icon.startsWith(DEFAULT_PATH) ? icon : DEFAULT_PATH + icon;
        this.width = width;
        this.height = height;
        loadImage();
    }

    public IconResize(String icon) {
        this.icon = icon.startsWith(DEFAULT_PATH) ? icon : DEFAULT_PATH + icon;
        width = 50;
        height = 50;
        loadImage();
    }

    private void loadImage() {
        // Must run before being called!!
        ImageIcon originalIcon = new ImageIcon(icon);
        Image image = originalIcon.getImage();
        Image newImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImg);
    }

    public ImageIcon getImage() {
        return imageIcon;
    }
}
