package com.boneless;

import com.boneless.util.AudioPlayer;
import com.boneless.util.NormalButtons;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Set;

public class Launcher extends JFrame{
    public static JButton buttonSettings;
    public static JFrame frame;
    public static void main(String[] args){
        //create frame
        frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titleText = new JLabel("Jeopardy!");
        titleText.setFont(new Font("Arial", Font.PLAIN, 30));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(titleText);

        JButton buttonStart = new JButton("Start Game");
        JButton buttonLoadData = new JButton("Load Board File");
        JButton buttonExit = new JButton("Exit");
        buttonSettings = new JButton("Settings");

        buttonStart.setFocusable(false);
        buttonLoadData.setFocusable(false);
        buttonExit.setFocusable(false);
        buttonSettings.setFocusable(false);

        buttonStart.setBackground(Color.lightGray);
        buttonLoadData.setBackground(Color.lightGray);
        buttonExit.setBackground(Color.lightGray);
        buttonSettings.setBackground(Color.lightGray);

        buttonStart.addActionListener(e -> {
            new Game();
            frame.dispose();
        });
        buttonLoadData.addActionListener(e -> {
            //
        });
        buttonExit.addActionListener(e -> {
            System.exit(1);
        });
        buttonSettings.addActionListener(e -> {
            buttonSettings.setEnabled(false);
            new Settings();
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(buttonStart);
        buttonsPanel.add(buttonLoadData);
        buttonsPanel.add(buttonSettings);
        buttonsPanel.add(buttonExit);

        //shows file name
        JPanel fileText = new JPanel();
        fileText.setLayout(new FlowLayout());

        Game game = new Game();
        JLabel currentFile = new JLabel("Current File: " + game.getFileName());
        currentFile.setFont(new Font("Arial", Font.PLAIN, 15));

        fileText.add(currentFile, BorderLayout.CENTER);

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(currentFile, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
