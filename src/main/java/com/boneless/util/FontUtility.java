package com.boneless.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class FontUtility {
    public static Font getScaledFont(String text, int fontStyle, JPanel panel, float maxWidthRatio, float maxHeightRatio) {
        Font currentFont = new Font(Font.SANS_SERIF, fontStyle, 12); // Start with a small initial font size
        FontMetrics fontMetrics = getFontMetrics(currentFont);
        int fontSize = currentFont.getSize();
        int maxWidth = (int) (panel.getWidth() * maxWidthRatio);
        int maxHeight = (int) (panel.getHeight() * maxHeightRatio);

        while (fontMetrics.stringWidth(text) > maxWidth || fontMetrics.getHeight() > maxHeight) {
            fontSize--;
            currentFont = currentFont.deriveFont((float) fontSize);
            fontMetrics = getFontMetrics(currentFont);
        }

        return currentFont;
    }

    private static FontMetrics getFontMetrics(Font font) {
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }
}