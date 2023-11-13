package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;

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

        JPanel gameBoard = new JPanel(new GridLayout());
        gameBoard.setPreferredSize(new Dimension(0,300));
        gameBoard.setBackground(Color.BLUE);

        JPanel teams = createTeamsPanel();

        frame.add(createBoard(fileName), BorderLayout.CENTER);
        frame.add(teams, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private JPanel createBoard(String fileName) {
        JPanel gameBoard = new JPanel();
        //gameBoard.setPreferredSize(new Dimension(0,300));
        //gameBoard.setBackground();
        int sizeX = Integer.parseInt(JsonFile.read(getFileName(), "numColumns", "count"));
        int sizeY = Integer.parseInt(JsonFile.read(getFileName(), "numRows", "count"));

        System.out.println("\n" + sizeX + "\n" + sizeY + "\n");

        GridLayout board = new GridLayout(sizeX, sizeY);
        gameBoard.setLayout(board);

//        for (int i = 1; i <= 30; i++) {
//            JButton button = new JButton("Button " + i);
//            gameBoard.add(button);
//        }

        gameBoard.add(createButton(fileName, sizeX, sizeY));
        return gameBoard;
    }
    private JButton createButton(String filename, int maxColumn, int maxRow){
        JButton button = new JButton();
        for(int i = 1; i <= maxColumn; i++){
            button = new JButton(JsonFile.readWithThreeKeys(filename, "column_1", "questions", "row_" + i));
        }
        System.out.println(JsonFile.readWithThreeKeys(filename, "column_1", "questions", "row_1"));
        return button;
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