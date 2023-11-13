package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
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

//        for (int i = 1; i <= 30; i++) {
//            JButton button = new JButton("Button " + i);
//            gameBoard.add(button);
//        }

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
    public JButton[] createRows(String filename, int sizeX, int sizeY){
        String[] rowOne = new String[sizeX];
        for(int i = 1; i <= sizeX; i++){
            rowOne[i-1] = JsonFile.readWithThreeKeys(filename, "column_" + i, "questions","row_" + i);
        }

        JButton[] buttons = new JButton[sizeX];
        for(int i = 0; i < sizeX; i++){
            buttons[i] = new JButton(rowOne[i]);
        }
        return buttons;
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