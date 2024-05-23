package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static com.boneless.GameBoard.HeaderPanel.*;
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

        revalidate();
        repaint();

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
    public void exit(){
        int size = 32;

        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.fillRect(0,0,size,size);

        g2d.setColor(Color.red);

        g2d.setFont(generateFont(50));
        g2d.drawString("fix me",(32 / 2) - 5,(32 / 2) - 5);

        g2d.dispose();

        String[] responses = {
                "Exit","Continue"
        };
        int answer = JOptionPane.showOptionDialog(
                null,
                "Change me message",
                "change me title",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(bufferedImage), responses, 0);
        if (answer == 0) {
            GAME_BOARD.GameIsActive = false;
            MAIN_MENU.menuIsActive = true;
            changeCurrentPanel(MAIN_MENU, GAME_BOARD);
        }
    }
    class HeaderPanel extends JPanel{
        public static JLabel leftText;
        public static JPanel rightPanel;
        public static int fontSize = 20;
        public HeaderPanel(){
            setBackground(mainColor);
            setLayout(new GridLayout());

            leftText = new JLabel("Exit");
            leftText.setForeground(fontColor);
            leftText.setFont(generateFont(fontSize));

            JButton exitButton = createHeaderButton("exit", true);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setOpaque(false);
            leftPanel.add(exitButton);
            leftPanel.add(leftText);

            JLabel title = new JLabel(JsonFile.read(fileName, "data", "board_name"));
            title.setForeground(fontColor);
            title.setFont(generateFont(fontSize));

            JPanel titlePanel = new JPanel(new GridBagLayout());
            titlePanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = 0;

            JLabel reveal = new JLabel("Reveal Correct Answer");
            reveal.setForeground(fontColor);
            reveal.setFont(generateFont(fontSize));

            titlePanel.add(title, gbc);

            rightPanel = createRightPanel(reveal, createHeaderButton("continue", false));
            //rightPanel.setBackground(mainColor);
            //rightPanel.setOpaque(false);

            add(leftPanel, BorderLayout.WEST); //left panel
            add(titlePanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);

        }

        private JPanel createRightPanel(JLabel label, JButton button) {
            JPanel parent = new JPanel(null);
            parent.setBackground(Color.cyan);

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.add(label);
            panel.add(button);
            panel.setBackground(Color.red);

            parent.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int width = parent.getWidth();
                    int height = parent.getHeight();
                    panel.setBounds(panel.getX(), panel.getY(), width, height);
                }
                @Override public void componentMoved(ComponentEvent e) {}
                @Override public void componentShown(ComponentEvent e) {}
                @Override public void componentHidden(ComponentEvent e) {}
            });

            parent.add(panel);
            return parent;
        }

        public static JButton createHeaderButton(String text, boolean isExit){
            String rawKeyBind = JsonFile.read("settings.json","keyBinds", text);
            String keyBind = rawKeyBind.substring(0,1).toUpperCase() + rawKeyBind.substring(1);
            JButton button = new JButton(keyBind);
            button.setFocusable(false);
            button.setFont(generateFont(20));
            button.addActionListener(e -> {
                if(isExit) {
                    if (GAME_BOARD.GameIsActive)
                        GAME_BOARD.exit();
                    else
                        jCard.exit();
                } else {
                    jCard.advance();
                }
            });

            return button;
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
                HeaderPanel.leftText.setText("Back");

                JPanel parentPanel = (JPanel) getParent();
                jCard = new JCard(score, question, answer, mainColor);
                jCardIsActive = true;
                GameIsActive = false;

                changeCurrentPanel(jCard, parentPanel);
            };
        }
    }
}