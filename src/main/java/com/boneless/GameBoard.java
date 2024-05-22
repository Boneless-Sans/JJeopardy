package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class GameBoard extends JPanel {
    public boolean GameIsActive = false;
    public boolean jCardIsActive = false;

    private final Color mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));;
    private final Color fontColor = parseColor(JsonFile.read(fileName, "data","font_color"));
    private final HeaderPanel headerPanel = new HeaderPanel();
    public final JPanel boardPanel = mainBoard();
    public GameBoard() {}
    public JPanel init(String fileName){
        GameIsActive = true;
        MAIN_MENU.menuIsActive = false;

        setLayout(new BorderLayout());
        setBackground(mainColor);
        setFocusable(true);
        add(headerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(createTeamsPanel(), BorderLayout.SOUTH);

        return this;
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
                    panel.add(new BoardButton(score, question, answer, mainColor));

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
    private JPanel createTeamsPanel() {
        JPanel parentPanel = new JPanel();
        parentPanel.setPreferredSize(new Dimension(getWidth(), 130));
        //parentPanel.setBorder(null);
        //parentPanel.setBackground(mainColor);

        for (int i = 0; i < 5; i++) {
            parentPanel.add(new Team());
        }

        return parentPanel;
    }
    private class HeaderPanel extends JPanel{
        private JPanel rightPanel;

        private JButton exitButton;
        private JLabel exitText;

        private final JLabel reveal;

        private final int fontSize = 20;

        public HeaderPanel(){
            setBackground(mainColor);
            setLayout(new GridLayout());

            exitText = new JLabel("Exit");
            exitText.setForeground(fontColor);
            exitText.setFont(generateFont(fontSize));

            exitButton = createHeaderButton("exit", 0);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setOpaque(false);
            leftPanel.add(exitButton);
            leftPanel.add(exitText);

            JLabel title = new JLabel(JsonFile.read(fileName, "data", "board_name"));
            title.setForeground(fontColor);
            title.setFont(generateFont(fontSize));

            JPanel titlePanel = new JPanel(new GridBagLayout());
            titlePanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = 0;

            titlePanel.add(title, gbc);

            reveal = new JLabel("Reveal Correct Answer");
            reveal.setForeground(fontColor);
            reveal.setFont(generateFont(fontSize));

            rightPanel = createBlank();
            rightPanel.setBackground(mainColor);
            rightPanel.setOpaque(false);

            add(leftPanel, BorderLayout.WEST); //left panel
            add(titlePanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);

        }
        private JPanel createBlank(){
            JPanel panel = new JPanel();
            panel.setBackground(mainColor);
            panel.setOpaque(false);
            return panel;
        }
        public void activateJCard(){
            rightPanel = createRightPanel(reveal, createHeaderButton("continue", 1));
            revalidate();
            repaint();
        }
        public void deactivateJCard(){
            rightPanel = createBlank();
            revalidate();
            repaint();
        }
        private JButton createHeaderButton(String text, int UUID){
            String rawKeyBind = JsonFile.read("settings.json","keyBinds", text);
            String keyBind = rawKeyBind.substring(0,1).toUpperCase() + rawKeyBind.substring(1);
            JButton button = new JButton(keyBind);
            button.setFocusable(false);
            button.setFont(generateFont(20));
            button.addActionListener(e -> {
                switch (UUID){
                    case 0: { //board exit
                        //
                    }
                    case 1: { //card exit
                        //
                    }
                    case 2: { //card continue
                        //
                    }
                }
            });

            return button;
        }
        private JPanel createRightPanel(JLabel label, JButton button){
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setOpaque(false);
            panel.add(button);
            panel.add(label);
            return panel;
        }
    }
    //Dante
    private class BoardButton extends JButton {
        private final int score;
        private final String question;
        private final String answer;
        private final Color mainColor;

        public BoardButton(int score, String question, String answer, Color mainColor) {
            this.score = score;
            this.question = question;
            this.answer = answer;
            this.mainColor = mainColor;
            setText(String.valueOf(score));
            setBackground(mainColor);
            setFocusable(false);
            addActionListener(listener());
        }
        private ActionListener listener() {
            return e -> {
                headerPanel.activateJCard();

                JPanel parentPanel = (JPanel) getParent();
                System.out.println(parentPanel);
                jCard = new JCard(score, question, answer, mainColor);
                jCardIsActive = true;
                GameIsActive = false;

                changeCurrentPanel(jCard, parentPanel);
            };
        }
    }
}
