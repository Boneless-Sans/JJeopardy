package com.boneless.util;

import javax.swing.*;
import java.awt.*;

import static com.boneless.util.GeneralUtils.generateFont;

public class JRoundedButton extends JButton {
    private String id;
    private boolean renderBorder = false;

    public JRoundedButton(String text){
        super(text);
        setFocusable(false);
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

        g2d.setColor(Color.white);
        g2d.fillRoundRect(0,0,getWidth(),getHeight(),25,25);

        g2d.setColor(Color.black);
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