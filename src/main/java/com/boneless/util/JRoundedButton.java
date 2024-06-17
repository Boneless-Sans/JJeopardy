package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.boneless.util.GeneralUtils.generateFont;

public class JRoundedButton extends JButton {
    private String id;
    private boolean renderBorder = false;
    private final Color normalColor;
    private Color color;
    private final Color fontColor;

    public JRoundedButton(String text, Color... colors){
        super(text);
        setFocusable(false);

        if (colors.length == 2) {
            normalColor = colors[0];
            color = colors[0];
            fontColor = colors[1];
        } else {
            normalColor = Color.white;
            color = Color.white;
            fontColor = Color.black;
        }

        //listener for changing color
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                color = color.darker();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                color = normalColor;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                color = color.brighter();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                color = normalColor;
                repaint();
            }
        });
    }

    public JRoundedButton(String text, String id){
        this(text);
        this.id = id;
        renderBorder = true;
    }

    public String getId(){return id;}

    @Override
    protected void paintComponent(Graphics g){
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(color);
        g2d.fillRoundRect(0,0,getWidth(),getHeight(),25,25);

        g2d.setColor(fontColor);
        g2d.setFont(generateFont(getFont().getSize()));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = ((getHeight() + textHeight) / 2) - 1;
        g2d.drawString(getText(), x, y);
    }

    @Override protected void paintBorder(Graphics g){
        if(renderBorder){
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.black);
            g2d.drawRoundRect(0,0,getWidth(),getHeight(), 25, 25);
        }
    }
}