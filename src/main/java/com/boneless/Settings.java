package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.NormalButtons;

import javax.swing.*;
import java.awt.*;

import static com.boneless.Launcher.buttonSettings;
import static com.boneless.Launcher.frame;

public class Settings extends JFrame{
    public Settings(){
        setSize(500,500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        NormalButtons.set();

        JPanel title = new JPanel(new FlowLayout());
        title.setSize(new Dimension());

        JLabel settingsTitle = new JLabel("Settings");
        settingsTitle.setFont(new Font("Arial", Font.PLAIN,25));

        title.add(settingsTitle);

        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(300,300));
        mainPanel.setBackground(Color.WHITE);

        JButton exit = new JButton("exit");
        exit.addActionListener(e -> {
            buttonSettings.setEnabled(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dispose();
        });

        add(title, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(exit, BorderLayout.SOUTH);
        setVisible(true);
    }
}
