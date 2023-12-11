package com.boneless;

import com.boneless.util.SystemUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Launcher {
    public static JButton buttonStart;
    public static JButton buttonLoadData;
    public static JButton buttonCreateBoard;
    public static JButton buttonSettings;
    public static JButton buttonExit;

    public static JFrame frame;
    public static ScrollGridPanel backgroundPanel;  // Use ScrollGridPanel as the background panel

    public static void main(String[] args) {
        SystemUI.set();
        Game game = new Game();

        // Create the frame
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create the background panel (ScrollGridPanel)
        backgroundPanel = new ScrollGridPanel();
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Create UI elements
        JLabel titleText = new JLabel("JJeopardy!");
        titleText.setFont(new Font("Arial", Font.PLAIN, 30));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(new Color(0,0,0,0));
        titlePanel.add(titleText);

        buttonStart = new JButton("Start Game");
        buttonLoadData = new JButton("Load Board File");
        buttonCreateBoard = new JButton("Create Game Board");
        buttonSettings = new JButton("Settings");
        buttonExit = new JButton("Exit");

        buttonStart.setFocusable(false);
        buttonLoadData.setFocusable(false);
        buttonCreateBoard.setFocusable(false);
        buttonSettings.setFocusable(false);
        buttonExit.setFocusable(false);

        buttonStart.setBackground(Color.lightGray);
        buttonLoadData.setBackground(Color.lightGray);
        buttonCreateBoard.setBackground(Color.LIGHT_GRAY);
        buttonSettings.setBackground(Color.lightGray);
        buttonExit.setBackground(Color.lightGray);

        buttonStart.setFont(new Font("Arial", Font.PLAIN, 17));
        buttonLoadData.setFont(new Font("Arial", Font.PLAIN, 17));
        buttonCreateBoard.setFont(new Font("Arial", Font.PLAIN, 17));
        buttonSettings.setFont(new Font("Arial", Font.PLAIN, 17));
        buttonExit.setFont(new Font("Arial", Font.PLAIN, 17));

        // shows file name
        JPanel fileText = new JPanel();
        fileText.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel currentFile = new JLabel("Current File: " + game.getFileName());
        currentFile.setFont(new Font("Arial", Font.PLAIN, 15));

        buttonStart.addActionListener(e -> {
            game.initUI();
            frame.dispose();
        });
        buttonLoadData.addActionListener(e -> {
            changeButtonState(false);
            File file = null;
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                game.setFileName(String.valueOf(file));
            }
            System.out.println(file);
            currentFile.setText("Current File: " + game.getFileName());
            changeButtonState(true);

        });
        buttonCreateBoard.addActionListener(e -> {
            changeButtonState(false);
            new GameBoardFactory();
        });
        buttonSettings.addActionListener(e -> {
            changeButtonState(false);
            new Settings();
        });
        buttonExit.addActionListener(e -> System.exit(1));

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(0,0,0,0));
        buttonsPanel.add(buttonStart);
        buttonsPanel.add(buttonLoadData);
        buttonsPanel.add(buttonCreateBoard);
        buttonsPanel.add(buttonSettings);
        buttonsPanel.add(buttonExit);

        fileText.add(currentFile);

        // Add UI elements on top of the background panel
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
        backgroundPanel.add(buttonsPanel, BorderLayout.CENTER);
        backgroundPanel.add(currentFile, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void changeButtonState(boolean enable) {
        if (enable) {
            buttonStart.setEnabled(true);
            buttonLoadData.setEnabled(true);
            buttonExit.setEnabled(true);
            buttonSettings.setEnabled(true);
        } else {
            buttonStart.setEnabled(false);
            buttonLoadData.setEnabled(false);
            buttonExit.setEnabled(false);
            buttonSettings.setEnabled(false);
        }
    }
}
