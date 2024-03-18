package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DevRunner extends JFrame{
    public static void main(String[] args) {
        boolean launch = true;
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

        JLabel text = new JLabel("<html><body style=color:rgb(0,0,0); opacity:150;>" + "this is text" + "</body></html>");

        add(text);
        setVisible(true);
    }
}
