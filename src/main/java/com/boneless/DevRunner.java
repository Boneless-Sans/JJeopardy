package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DevRunner extends JFrame{
    public static void main(String[] args) {
        boolean launch = false;
        if(launch) {
            Game game = new Game();
            game.initUI(false, 3);
        }else {
            new DevRunner();
        }
    }
    public DevRunner(){
        setSize(200,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField text = new JTextField("this is some really really long text, like you would not BELIEVE how long this text is");
        text.setFocusable(false);
        text.setBorder(null);
        text.setCursor(Cursor.getDefaultCursor());
        text.setHorizontalAlignment(0);

        add(text);
        setVisible(true);
    }
}
