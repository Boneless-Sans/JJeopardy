package com.boneless.util;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class FontUtility {
    private static final int MIN_FONT_SIZE = 50;

    public static Font getScaledFont(String text, int fontStyle, JComponent component) {
        if (component instanceof JButton) {
            return getScaledFontForButton(text, fontStyle, (JButton) component);
        } else if (component instanceof JPanel) {
            return getScaledFontForPanel(text, fontStyle, (JPanel) component);
        } else {
            throw new IllegalArgumentException("Unsupported component type");
        }
    }

    private static Font getScaledFontForButton(String text, int fontStyle, JButton button) {
        Font currentFont = new Font(Font.SANS_SERIF, fontStyle, MIN_FONT_SIZE); // Start with the minimum font size
        FontMetrics fontMetrics = button.getFontMetrics(currentFont);
        int maxWidth = button.getWidth() - button.getInsets().left - button.getInsets().right;
        int maxHeight = button.getHeight() - button.getInsets().top - button.getInsets().bottom;

        if (fontMetrics.stringWidth(text) <= maxWidth && fontMetrics.getHeight() <= maxHeight) {
            return currentFont;
        } else {
            StringBuilder builder = new StringBuilder("<html>");
            int start = 0;
            int end = 0;
            int lineWidth = 0;

            while (end < text.length()) {
                lineWidth = fontMetrics.stringWidth(text.substring(start, end + 1));
                if (lineWidth > maxWidth) {
                    builder.append(text, start, end).append("<br>");
                    start = end;
                }
                end++;
            }
            builder.append(text.substring(start));

            return currentFont.deriveFont(Font.PLAIN);
        }
    }

    private static Font getScaledFontForPanel(String text, int fontStyle, JPanel panel) {
        Font currentFont = new Font(Font.SANS_SERIF, fontStyle, MIN_FONT_SIZE); // Start with the minimum font size
        FontMetrics fontMetrics = panel.getFontMetrics(currentFont);
        int maxWidth = panel.getWidth();
        int maxHeight = panel.getHeight();

        if (fontMetrics.stringWidth(text) <= maxWidth && fontMetrics.getHeight() <= maxHeight) {
            return currentFont;
        } else {
            StringBuilder builder = new StringBuilder("<html>");
            int start = 0;
            int end = 0;
            int lineWidth = 0;

            while (end < text.length()) {
                lineWidth = fontMetrics.stringWidth(text.substring(start, end + 1));
                if (lineWidth > maxWidth) {
                    builder.append(text, start, end).append("<br>");
                    start = end;
                }
                end++;
            }
            builder.append(text.substring(start));

            return currentFont.deriveFont(Font.PLAIN);
        }
    }
}