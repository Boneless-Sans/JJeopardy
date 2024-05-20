package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static com.boneless.Team.getTeamCount;
import static com.boneless.util.GeneralUtils.parseColor;
import static com.boneless.util.GeneralUtils.generateFont;

public class GameBoard extends JPanel {
    private final String fileName;
    private final Color mainColor;
    public GameBoard(String fileName){
        this.fileName = fileName;
        this.mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));

        setLayout(new BorderLayout());
        add(headPanel(), BorderLayout.NORTH);
        add(mainBoard(), BorderLayout.CENTER);
        add(createTeamsPanel(), BorderLayout.SOUTH);
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
        int boardY = Integer.parseInt(JsonFile.read(fileName, "data", "rows")) + 1; //add one to add top row panels
        panel.setLayout(new GridLayout(boardY, boardX,1,1));

        for(int i = 0;i < boardX;i++){
            panel.add(createHeaderPanel(i));
        }

        for(int i = 1;i < boardY;i++){
            for(int j = 0;j < boardX;j++){
                panel.add(createBoardButton());
            }
        }

        return panel;
    }
    private JPanel createHeaderPanel(int index){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(0));
        panel.setBackground(mainColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        panel.add(new JLabel(JsonFile.readWithThreeKeys(fileName, "board", "categories", "cat_" + index)), gbc);
        return panel;
    }
    private JButton createBoardButton(){
        JButton button = new JButton("test");
        button.setFocusable(false);
        button.setBackground(mainColor);

        return button;
    }
    private JScrollPane createTeamsPanel(){
        JScrollPane parentPanel = new JScrollPane();
        parentPanel.setPreferredSize(new Dimension(getWidth(), 130));
        parentPanel.setBorder(null);
        parentPanel.setBackground(mainColor);

        for(int i = 0;i < 5;i++){
            parentPanel.add(new Team());
        }

        return parentPanel;
    }
}
