package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DevRunner extends JFrame{
    public static void main(String[] args) {
        Game game = new Game();
        game.initUI(false, 3);
        //new DevRunner();
    }
    private ArrayList<JLabel> textArray = new ArrayList<>();
    private Font font = new Font("Arial",Font.PLAIN,25);
    public DevRunner(){
        setSize(200,200);
        setLocationRelativeTo(null);
        addComponentListener(windowListener());

        font = new Font(font.getName(),font.getStyle(),Toolkit.getDefaultToolkit().getScreenSize().height / 25);

        JLabel text = new JLabel("this is text");
        textArray.add(text);
        text.setFont(font);
        add(text);
        setVisible(true);
    }
    private ComponentAdapter windowListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int windowHeight = e.getComponent().getHeight();
                int windowWidth = e.getComponent().getWidth();
                for (JLabel txt : textArray) {
                    font = new Font(font.getName(), font.getStyle(), (int)(((windowHeight + txt.getHeight()) + ((windowWidth + txt.getWidth()) / 3))/ 15.0));
                    //jank but seems to work
                    txt.setFont(font);
                    txt.revalidate();
                    txt.repaint();
                }
            }
        };
    }
}
