package com.boneless;

import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;

import static com.boneless.Launcher.*;

public class Settings extends JFrame{
    public Settings(){
        setSize(500,400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        SystemUI.set();

        JPanel title = new JPanel(new FlowLayout());
        title.setSize(new Dimension());

        JLabel settingsTitle = new JLabel("Settings");
        settingsTitle.setFont(new Font("Arial", Font.PLAIN,25));

        title.add(settingsTitle);

        //Main body
        JPanel mainPanel = new JPanel(new GridLayout(5,1,10,10));
        for(int i = 0;i < 5; i++){
            mainPanel.add(createKeyBindPanel("text " + i));
        }

        JScrollPane mainPane = new JScrollPane(mainPanel);

        JButton exit = new JButton("exit");
        exit.addActionListener(e -> {
            changeButtonState(true);
            dispose();
        });

        add(title, BorderLayout.NORTH);
        add(mainPane, BorderLayout.CENTER);
        add(exit, BorderLayout.SOUTH);
        setVisible(true);
    }
    private JPanel createKeyBindPanel(String text){
        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setPreferredSize(new Dimension(100,100));

        JLabel label = new JLabel(text);

        panel.add(label);

        return panel;
    }
}
