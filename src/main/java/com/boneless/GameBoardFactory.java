package com.boneless;

import javax.swing.*;

import static com.boneless.Launcher.changeButtonState;

public class GameBoardFactory extends JFrame {
    private String title;
    private int columns;
    private int rows;
    public GameBoardFactory(){
        initUI();
    }
    private void initUI(){
        setSize(1280, 720);
        setLocationRelativeTo(null);


        setVisible(true);
    }
}
