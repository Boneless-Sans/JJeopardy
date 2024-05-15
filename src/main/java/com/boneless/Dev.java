package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Dev extends JFrame {
    public static void main(String[] args){
        new Dev();
    }
    public Dev(){
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        init();
        setVisible(true);
    }
    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        Team.ScoreButton workPanel = new Team.ScoreButton(false);
        workPanel.setPreferredSize(new Dimension(250,250));

        add(workPanel, gbc);
    }
}
