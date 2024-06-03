package com.boneless;

import javax.swing.*;
import java.awt.*;

public class BoardFactory extends JPanel {
    public BoardFactory(){
        setLayout(new BorderLayout());

        add(boardPanel(), BorderLayout.CENTER);
        add(controlPanel(), BorderLayout.EAST);
    }
    private JPanel boardPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.cyan);
        return panel;
    }
    private JPanel controlPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        return panel;
    }
}
