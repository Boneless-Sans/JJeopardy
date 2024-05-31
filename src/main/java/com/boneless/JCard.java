package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.boneless.GameBoard.*;
import static com.boneless.GameBoard.HeaderPanel.*;
import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JCard extends JPanel {
    private JLabel questionLabel;
    private JLabel answerLabel;
//    private JLabel questionQuestion;
//    private JLabel answerAnswer;
    private JLabel moron;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private final static Color parseColorFadeComplete = GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
    private final JPanel test;
    private final JPanel test2;

    public JCard(String question, String answer) {
        setLayout(null);

        setBackground(mainColor);

        test = new JPanel(new GridBagLayout());
        test.setBackground(mainColor);

        test2 = new JPanel(new GridBagLayout());
        test2.setBackground(mainColor);

        questionLabel = new JLabel(question);
        questionLabel.setForeground(fontColor);
        questionLabel.setOpaque(false);

        answerLabel = new JLabel(answer);
        answerLabel.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        answerLabel.setOpaque(false);

        moron = new JLabel("you're a moron :)");
        moron.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        moron.setOpaque(false);

        test.add(questionLabel);
        test2.add(answerLabel);
        add(test);
        add(test2);


        centerTestPanel();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerTestPanel();
            }
        });

        setupMouseListeners();
        setUpCharacters();
    }

    private void centerTestPanel() {
        int factor = 2;
        int sizeX2 = getWidth() - (getWidth() / factor);
        int sizeY = (getHeight() - (getHeight() / factor)) / factor;
        int x = (getWidth() / 2) - (sizeX2 / 2);
        int y = (getHeight() / 2) - (sizeY / 2);
        test.setBounds(x, y, sizeX2, sizeY);
        revalidate();
        repaint();
    }

    private void setUpCharacters() {
        questionLabel.setFont(GeneralUtils.generateFont(30));
        answerLabel.setFont(GeneralUtils.generateFont(30));
        moron.setFont(GeneralUtils.generateFont(10));
        setBackground(mainColor);
    }

    public void advance() { //todo: remove, redundant
        moveQuestion();
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!hasFaded) {
                    moveQuestion();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    private void moveQuestion() {
        if (hasFaded) {
            return;
        }
        hasFaded = true;

        int sizeX = 400;
        int sizeY = 200;
        int x = (getWidth() - sizeX) / 2;
        int yQuestion = (getHeight() - sizeY) / 2;
        int targetY = 50; // Target Y position for question label

        Timer q = new Timer(50, null);
        q.addActionListener(new ActionListener() {
            private int currentY = yQuestion;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentY -= 3; // Adjust this value to control the speed of movement
                if (currentY <= targetY) {
                    currentY = targetY;
                    q.stop();
                    fadeInAnswerAndQuestion();
                }
                questionLabel.setBounds(x, currentY, sizeX, sizeY);
                revalidate();
                repaint();
            }
        });
        q.start();
    }

    private void fadeInAnswerAndQuestion() {
        if (!hasFadedIn) {
            return;
        }
        hasFadedIn = false;

        Timer j = new Timer(50, null);
        j.addActionListener(new ActionListener() {
            private float opacity2 = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity2 += 0.05f;
                if (opacity2 >= 1.0f) {
                    opacity2 = 1.0f;
                    j.stop();
                }
                answerLabel.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data","font_color"),(int)(opacity2 * 255)));
                moron.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data","font_color"),(int)(opacity2 * 255)));
                repaint();
            }
        });
        j.start();
    }

    public void exit() {
        leftText.setText("Exit");
        rightPanel.removeAll();
        rightPanel.add(createRightPanel(true));
        rightPanel.revalidate();
        rightPanel.repaint();
        changeCurrentPanel(GAME_BOARD.boardPanel, this);
        GAME_BOARD.jCardIsActive = false;
        GAME_BOARD.GameIsActive = true;
        hasFaded = false;
    }
}
