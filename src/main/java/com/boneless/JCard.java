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
    private final JLabel questionLabel;
    private final JLabel answerLabel;
    private final JLabel questionQuestion;
    private final JLabel answerAnswer;
    private final JLabel moron;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;

    public JCard(String question, String answer) {
        setLayout(null);

        int factor = 4;
        int sizeX2 = getWidth() - (getWidth() / factor);
        int sizeY2 = (getHeight() - (getHeight() / factor)) / factor;

        JPanel test = new JPanel(new GridBagLayout());
        test.setBackground(Color.CYAN);
        test.setBounds((getWidth() / 2) - (sizeX2 / 2), (getHeight() / 2) - (sizeY2 / 2), sizeX2, sizeY2);

        JPanel test2 = new JPanel(new GridBagLayout());
        test2.setBackground(Color.CYAN);
        test2.setBounds((getWidth() / 2) - (sizeX2 / 2), (getHeight() / 2) + 30, sizeX2, sizeY2);

        JPanel test3 = new JPanel(new GridBagLayout());
        test3.setBackground(Color.CYAN);
        test3.setBounds((getWidth() / 2) - (sizeX2 / 2), (getHeight() / 2) + 60, sizeX2, sizeY2);

        JPanel test4 = new JPanel(new GridBagLayout());
        test4.setBackground(Color.CYAN);
        test4.setBounds((getWidth() / 2) - (sizeX2 / 2), (getHeight() / 2) + 90, sizeX2, sizeY2);

        JPanel test5 = new JPanel(new GridBagLayout());
        test5.setBackground(Color.CYAN);
        test5.setBounds((getWidth() / 2) - (sizeX2 / 2), (getHeight() / 2) + 120, sizeX2, sizeY2);


        questionLabel = new JLabel("Question: ");
        questionLabel.setForeground(fontColor);
        questionLabel.setOpaque(false);

        questionQuestion = new JLabel(question);
        questionQuestion.setForeground(fontColor);
        questionQuestion.setOpaque(false);

        answerLabel = new JLabel("Answer: ");
        answerLabel.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        answerLabel.setOpaque(false);

        answerAnswer = new JLabel(answer);
        answerAnswer.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        answerAnswer.setOpaque(false);

        moron = new JLabel("you're a moron :)");
        moron.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        moron.setOpaque(false);
        //bruh

        test.add(questionLabel, gbc);
        test2.add(questionQuestion, gbc);
        test3.add(answerLabel, gbc);
        test5.add(answerAnswer, gbc);
        test5.add(moron, gbc);

        add(test);
        add(test2);
        add(test3);
        add(test4);
        add(test5);

        setupMouseListeners();
        setUpCharacters();
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
        int x2 = getWidth() / 2;
        int x4 =  (getWidth() / 2) + ((getWidth() - sizeX) / 2);
        int x6 =  (x4 / 2) + 35;
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
                questionQuestion.setBounds(x, currentY + 30, sizeX, sizeY);
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