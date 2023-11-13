package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game {
    private String fileName = "main_board.json";
    private int pointsToAdd;
    public Game(){
        initUI();
    }
    public Game(boolean no){
        //
    }
    private void initUI(){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel title = new JPanel(new FlowLayout());
        JLabel titleText = new JLabel(JsonFile.read(getFileName(), "data", "title"));
        titleText.setFont(new Font(JsonFile.read(getFileName(), "data", "font_name"), Font.PLAIN, Integer.parseInt(JsonFile.read(getFileName(), "data", "font_size"))));
        title.add(titleText);

        JPanel gameBoard = new JPanel(new GridLayout());
        gameBoard.setPreferredSize(new Dimension(0,300));
        gameBoard.setBackground(Color.BLUE);

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

        System.out.println("\n" + sizeX + "\n" + sizeY + "\n");

        GridLayout board = new GridLayout(sizeX, sizeY);
        gameBoard.setLayout(board);

        JButton[] buttons = createTitles(fileName, sizeX, sizeY);
        JButton[] rowOne = createRows(fileName, sizeX, sizeY);
        for (JButton button : buttons) {
            gameBoard.add(button);
        }
        for(JButton button : rowOne){
            gameBoard.add(button);
        }
        return gameBoard;
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
            }
        }

        return buttons;
    }

    // ActionListener for the buttons
    private class ButtonActionListener implements ActionListener {
        private int row;
        private int column;

        public ButtonActionListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Open a new JFrame with parameters row and column
            JFrame newFrame = new JFrame("New Frame - Row: " + row + ", Column: " + column);
            newFrame.setSize(300, 200);
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JButton questionText = new JButton(JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "questions", "row_" + row));
            newFrame.add(questionText);
            questionText.addActionListener(a -> {
                JLabel answerText = new JLabel(JsonFile.readWithThreeKeys(getFileName(), "column_" + column, "answers", "row_" + row));

                newFrame.remove(questionText);
            });

            newFrame.setVisible(true);
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