package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Game {
    private void initUI(){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gameBoard = new JPanel(new GridLayout());
        gameBoard.setPreferredSize(new Dimension(0,300));
        gameBoard.setBackground(Color.BLUE);

        JPanel teams = createTeamsPanel();

        frame.add(gameBoard, BorderLayout.CENTER);
        frame.add(teams, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private JPanel createBoard(){
        JPanel gameBoard = new JPanel();
        gameBoard.setPreferredSize(new Dimension(0,300));
        //gameBoard.setBackground();
        return gameBoard;
    }
    private JPanel createTeamsPanel(){
        JPanel teams = new JPanel();
        teams.setPreferredSize(new Dimension(0,100));
        teams.setBackground(Color.red);

        return teams;
    }
}