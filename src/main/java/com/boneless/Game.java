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
    private String fileName = "dev_board.json";
    private JFrame frame;
    private static boolean canOpen = true;
    private static final Color[] colors = JsonFile.readColorArray("gameData.json","colors");
    private final String textFont = JsonFile.read(getFileName(), "data", "font_name");

    private static final Color buttonColor = colors != null ? colors[0] : null;
    private static final Color backgroundColor = colors != null ? colors[1] : null;

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

        GridLayout board = new GridLayout(sizeX, sizeY);
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
                // Open a new JFrame with parameters row and column
                JFrame newFrame = new JFrame("New Frame - Row: " + row + ", Column: " + column);
                newFrame.setUndecorated(true);
                newFrame.setLocationRelativeTo(null);
                newFrame.setSize(1280, 720);
                newFrame.setLocation(frame.getX(),frame.getY());
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JButton questionText = new JButton(JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "questions", "row_" + row));
                questionText.setFont(new Font(textFont, Font.ITALIC, 60));
                questionText.setFocusable(false);

                newFrame.add(questionText);
                questionText.addActionListener(a -> {
                    String question = questionText.getText();
                    String lineBreak = "-----------------------------";
                    String answer = JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "answers", "row_" + row);
                    JButton answerText = new JButton("<html><head><style>.center-text{text-align:center;}</style></head><body><div class='center-text'>" +
                            question +
                            "<br />" +
                            lineBreak +
                            "<br />" +
                            answer +
                            "</div></body></html>");
                    answerText.setFont(new Font(textFont, Font.ITALIC, 60));
                    answerText.setFocusable(false);

                    questionText.setFont(new Font(textFont, Font.ITALIC, 60)); //for some reason, this has to be here or else it won't show the answer

                    newFrame.add(answerText);
                    newFrame.remove(questionText);

                    answerText.addActionListener(e1 -> {
                        newFrame.dispose();
                        canOpen = true;
                    });
                });

                newFrame.setVisible(true);
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