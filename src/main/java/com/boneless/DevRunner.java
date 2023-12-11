package com.boneless;

import javax.swing.*;
import java.awt.*;

public class DevRunner extends JFrame{
    public static void main(String[] args) {
        String[] teams = {"team 1","team 2","team 3"};
        Color backgroundColor = new Color(53, 64, 157);
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        frame.setLocationRelativeTo(null);
        frame.setSize(500,500);

        for(String team : teams){
            JPanel panel = new JPanel();
            panel.setBackground(backgroundColor);


            frame.add(panel);
        }
        frame.setVisible(true);
    }
}
