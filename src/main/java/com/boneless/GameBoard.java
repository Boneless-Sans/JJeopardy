package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private String fileName;
    public GameBoard(String fileName){
        this.fileName = fileName;
        SystemUI.set();

        setLayout(new BorderLayout());
        add(headPanel(), BorderLayout.NORTH);
        add(mainBoard(), BorderLayout.CENTER);
        add(teamPanel(), BorderLayout.SOUTH);
    }
    //board header panel
    private JPanel headPanel(){ //main board header
        JPanel panel = new JPanel();
        panel.setBackground(Color.blue);

        return panel;
    }
    //panel to contain the main board grid
    private JPanel mainBoard(){ //the board
        JPanel panel = new JPanel();

        //setup values from json
        int boardX = Integer.parseInt(JsonFile.read(fileName, "data", "categories"));
        int boardY = Integer.parseInt(JsonFile.read(fileName, "data", "rows"));;
        panel.setLayout(new GridLayout(boardY, boardX,0,0));

        for(int i = 0; i < boardX * boardY;i++){
            Color color = switch (i % 6){
                case 0 -> Color.red;
                case 1 -> Color.orange;
                case 2 -> Color.yellow;
                case 3 -> Color.green;
                case 4 -> Color.blue;
                case 5 -> Color.magenta;
                default -> Color.cyan;
            };
            panel.add(createBoardButton(0, "question", "answer", color));
        }

        return panel;
    }
    private JButton createBoardButton(int points, String question, String answer, Color backgroundColor){
        JButton button = new JButton(String.valueOf(points));
        button.setForeground(backgroundColor);
        button.setFocusable(false);
        return button;
    }
    //panel to contain team panels
    private JPanel teamPanel(){ //team panel, south of board todo: create layout for team panel, spacers and everything (god fucking damnit this shit fucking sucks to do)
        JPanel panel = new JPanel();

        return panel;
    }
}
