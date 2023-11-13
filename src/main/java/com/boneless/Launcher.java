package com.boneless;

import com.boneless.util.NormalButtons;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class Launcher extends JFrame{
    public static JButton buttonSettings;
    public static void main(String[] args){
        //create frame
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());

        JLabel titleText = new JLabel("Jeopardy!");
        titleText.setFont(new Font("Arial", Font.PLAIN, 30));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(titleText);

        NormalButtons.set();
        JButton buttonStart = new JButton("Start Game");
        JButton buttonLoadData = new JButton("Load Board File");
        JButton buttonPreview = new JButton("Preview Board");
        buttonSettings = new JButton("Settings");

        buttonStart.addActionListener(e -> {
            //
        });
        buttonLoadData.addActionListener(e -> {
            //
        });
        buttonPreview.addActionListener(e -> {
            //
        });
        buttonSettings.addActionListener(e -> {
            buttonSettings.setEnabled(false);
            new Settings();
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(buttonStart);
        buttonsPanel.add(buttonLoadData);
        buttonsPanel.add(buttonPreview);
        buttonsPanel.add(buttonSettings);

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
