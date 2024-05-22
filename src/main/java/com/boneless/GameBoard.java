package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static com.boneless.util.GeneralUtils.parseColor;

public class GameBoard extends JPanel {
    public boolean isActive = false;
    private Color mainColor;
    private String fileName;
    private final JPanel headerPanel = headerPanel();
    private final JPanel boardPanel = mainBoard();
    public GameBoard() {}
    public JPanel init(String fileName){
        isActive = true;
        this.fileName = fileName;
        this.mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));

        setLayout(new BorderLayout());
        setBackground(mainColor);
        add(headerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(createTeamsPanel(), BorderLayout.SOUTH);

        return this;
    }
    //board header panel
    private JPanel headerPanel(){ //main board header
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(mainColor);
        panel.setPreferredSize(new Dimension(getWidth(), 100));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton button = new JButton("Exit to Menu");
        button.addActionListener(e -> {
            //code here
        });
        buttonPanel.add(button);

        add(buttonPanel);

        JLabel title = new JLabel(JsonFile.read(fileName, "data", "board_name"));

        add(title);

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
            panel.add(createCatPanel(i));
        }

        for (int i = 0; i < boardY -1; i++) {
            for (int j = 0; j < boardX; j++) {
                try {
                    String scoreString = JsonFile.readWithThreeKeys(fileName, "board", "scores", "row_" + i);
                    int score = Integer.parseInt(scoreString);
                    String question = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "question_" + i);
                    String answer = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "answer_" + i);
                    panel.add(new BoardButton(score, question, answer, mainColor, headerPanel));

                } catch (Exception e) {
                    System.err.println("Invalid score for row_" + i + ": " + e.getMessage());
                }
            }
        }


        return panel;
    }
    private JPanel createCatPanel(int index){
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
    private JPanel createTeamsPanel(){
        JPanel parentPanel = new JPanel();
        parentPanel.setPreferredSize(new Dimension(getWidth(), 130));
        //parentPanel.setBorder(null);
        //parentPanel.setBackground(mainColor);

        for(int i = 0;i < 5;i++){
            parentPanel.add(new Team());
        }

        return parentPanel;
    }

    //Dante
    private static class BoardButton extends JButton {
        private final int score;
        private final String question;
        private final String answer;
        private final Color mainColor;
        private final JPanel headerPanel;

        public BoardButton(int score, String question, String answer, Color mainColor, JPanel headerPanel) {
            this.score = score;
            this.question = question;
            this.answer = answer;
            this.mainColor = mainColor;
            this.headerPanel = headerPanel;
            setText(String.valueOf(score));
            setBackground(mainColor);
            addActionListener(listener());
        }
        private ActionListener listener() {
            return e -> GeneralUtils.changeCurrentPanel(new JCard(score, question, answer, mainColor, headerPanel), (JPanel) getParent());
        }
    }
}
