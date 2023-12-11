package com.boneless;

import com.boneless.util.JsonFile;

import javax.imageio.plugins.tiff.TIFFDirectory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;

public class Game {
    private static String fileName = "dev_board.json";
    private JFrame frame;
    private static boolean canOpen = true;
    private final String textFont = JsonFile.read(getFileName(), "data", "font_name");
    private final Color buttonColor = stringToColor("button_color");
    private final Color backgroundColor = stringToColor("background_color");

    public boolean playAudio = true;

    public Game(){

    }
    public void initUI(){
        frame = new JFrame();
        frame.setSize(1280,720);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);

        JPanel title = new JPanel(new FlowLayout());
        JLabel titleText = new JLabel();
        titleText.setText(JsonFile.read(getFileName(), "data", "title"));
        titleText.setFont(new Font(textFont, Font.PLAIN, 25));
        title.add(titleText);

        JPanel gameBoard = new JPanel(new GridLayout());
        gameBoard.setPreferredSize(new Dimension(0,300));
        gameBoard.setBackground(backgroundColor);

        JPanel teams = createTeamsPanel();

        frame.add(title, BorderLayout.NORTH);
        frame.add(createBoard(getFileName()), BorderLayout.CENTER);
        frame.add(teams, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private JPanel createBoard(String fileName) {
        JPanel gameBoard = new JPanel();

        int sizeX = Integer.parseInt(JsonFile.read(fileName, "data", "columns"));
        int sizeY = Integer.parseInt(JsonFile.read(fileName, "data", "rows"));

        GridLayout board = new GridLayout(sizeX, sizeY, 5,5);
        gameBoard.setLayout(board);

        JButton[] buttons = createTitles(fileName, sizeX, sizeY);
        JButton[] rowOne = createRows(fileName, sizeX, sizeY);

        for (JButton button : buttons) {
            button.setFont(new Font(textFont, Font.PLAIN, 20));
            button.setFocusable(false);
            button.addActionListener(new ButtonActionListener());
            gameBoard.add(button);
        }

        for(JButton button : rowOne){
            button.setFont(new Font(textFont,Font.PLAIN,25));
            gameBoard.add(button);
        }

        return gameBoard;
    }
    public JPanel createTeams(String teamName){
        JPanel team = new JPanel(new FlowLayout());


        return team;
    }
    public JButton[] createTitles(String filename, int sizeX, int sizeY) {
        //it adds left to right, so there will need to be some math to calculate when to add titles and questions

        String[] titles = new String[sizeX]; // Declare the array of sizeX elements

        for (int i = 1; i <= sizeX; i++) {
            titles[i-1] = JsonFile.read(filename, "column_" + i, "title"); // Read the title and store it in the array
        }
        //draw each line via their row number
        JButton[] buttons = new JButton[sizeX];
        for(int i = 0; i < sizeX; i++){
            buttons[i] = new JButton(titles[i]);
        }
        return buttons;
    }
    public JButton[] createRows(String filename, int sizeX, int sizeY) {
        String[] rowData = new String[sizeX * sizeY];

        for (int row = 1; row <= sizeY; row++) {
            for (int column = 1; column <= sizeX; column++) {
                // Assuming JsonFile.readWithThreeKeys returns a single string for each column and row
                String columnData = JsonFile.readWithThreeKeys(filename, "column_" + column, "questions", "row_" + row);

                // Calculate the array index for the current row and column
                int arrayIndex = (row - 1) * sizeX + (column - 1);

                // Ensure that the arrayIndex is within bounds
                if (arrayIndex < rowData.length) {
                    // Do something with rowData (e.g., store it in an array)
                    rowData[arrayIndex] = columnData;
                } else {
                    System.err.println("Index out of bounds: " + arrayIndex);
                }
            }
        }

        JButton[] buttons = new JButton[sizeX * sizeY];
        for (int row = 1; row <= sizeY; row++) {
            for (int column = 1; column <= sizeX; column++) {
                int arrayIndex = (row - 1) * sizeX + (column - 1);
                String buttonName = "Button_" + row + "_" + column;

                buttons[arrayIndex] = new JButton(rowData[arrayIndex]);
                buttons[arrayIndex].setName(buttonName);
                buttons[arrayIndex].addActionListener(new ButtonActionListener(row, column));
                //add the cube thing to the launcher
            }
        }
        return buttons;
    }
    private Color stringToColor(String panel){
        String initColor = JsonFile.readTwoKeys(getFileName(), "data", panel);
        String[] split = initColor.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    // ActionListener for the buttons
    private class ButtonActionListener implements ActionListener {
        private int row;
        private int column;
        public ButtonActionListener(){
            //
        }
        public ButtonActionListener(int row, int column) {
            this.row = row;
            this.column = column;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            clickedButton.setEnabled(false);
            if(canOpen) {
                canOpen = false;
                new InfoCard(JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "questions", "row_" + row),
                        JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "answers", "row_" + row), getFileName());
            }
        }
    }


    private JPanel createTeamsPanel(){
        JPanel teams = new JPanel();
        teams.setPreferredSize(new Dimension(0,100));
        teams.setBackground(Color.red);

        return teams;
    }
    public void setFileName(String file){
        this.fileName = file;
    }

    public String getFileName(){
        return fileName;
    }
}