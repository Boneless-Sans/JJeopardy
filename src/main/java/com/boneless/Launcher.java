package com.boneless;

import com.boneless.util.AudioPlayer;
import com.boneless.util.NormalButtons;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Set;

public class Launcher{
    public static JButton buttonStart;
    public static JButton buttonLoadData;
    public static JButton buttonExit;
    public static JButton buttonSettings;

    public static JFrame frame;
    public static void main(String[] args){
        Game game = new Game(false);
        //create frame
        frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleText = new JLabel("Jeopardy!");
        titleText.setFont(new Font("Arial", Font.PLAIN, 30));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(titleText);

        buttonStart = new JButton("Start Game");
        buttonLoadData = new JButton("Load Board File");
        buttonExit = new JButton("Exit");
        buttonSettings = new JButton("Settings");

        buttonStart.setFocusable(false);
        buttonLoadData.setFocusable(false);
        buttonExit.setFocusable(false);
        buttonSettings.setFocusable(false);

        buttonStart.setBackground(Color.lightGray);
        buttonLoadData.setBackground(Color.lightGray);
        buttonExit.setBackground(Color.lightGray);
        buttonSettings.setBackground(Color.lightGray);

        //shows file name
        JPanel fileText = new JPanel();
        fileText.setLayout(new FlowLayout(FlowLayout.CENTER));


        JLabel currentFile = new JLabel("Current File: " + game.getFileName());
        currentFile.setFont(new Font("Arial", Font.PLAIN, 15));

        buttonStart.addActionListener(e -> {
            new Game();
            frame.dispose();
        });
        buttonLoadData.addActionListener(e -> {
            changeButtonState(false);
            File file = null;
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
            if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
                file = chooser.getSelectedFile();
                game.setFileName(String.valueOf(file));
            }
            System.out.println(file);
            currentFile.setText("Current File: " + game.getFileName());
            changeButtonState(true);

        });
        buttonExit.addActionListener(e -> {
            System.exit(1);
        });
        buttonSettings.addActionListener(e -> {
            changeButtonState(false);
            new Settings();
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(buttonStart);
        buttonsPanel.add(buttonLoadData);
        buttonsPanel.add(buttonSettings);
        buttonsPanel.add(buttonExit);

        fileText.add(currentFile);

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(currentFile, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    public static void changeButtonState(boolean enable){
        if(enable){
            buttonStart.setEnabled(true);
            buttonLoadData.setEnabled(true);
            buttonExit.setEnabled(true);
            buttonSettings.setEnabled(true);
        }else{
            buttonStart.setEnabled(false);
            buttonLoadData.setEnabled(false);
            buttonExit.setEnabled(false);
            buttonSettings.setEnabled(false);
        }
    }
}
