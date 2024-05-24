package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JCard extends JPanel {
    public boolean isActive = false;
    private JLabel questionLabel;
    private JLabel answerLabel;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;

    public static Color mainColor;

    public JCard(int score, String question, String answer, Color mainColor) {
        setLayout(null);

        JCard.mainColor = mainColor;

        questionLabel = new JLabel("Question: " + question, SwingConstants.CENTER);
        //questionLabel.setBackground(Color.cyan);
        questionLabel.setForeground(new Color(0, 0, 0, 1.0f));

        answerLabel = new JLabel("Answer: " + answer, SwingConstants.CENTER);
        //answerLabel.setBackground(Color.cyan);
        answerLabel.setForeground(new Color(0, 0, 0, 0.0f));

        add(questionLabel);
        add(answerLabel);

        setupMouseListeners();
        setFonts();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Center labels
        int sizeX = 400;
        int sizeY = 200;
        int x = (getWidth() - sizeX) / 2;
        int y = (getHeight() - sizeY) / 2;
        int yQuestion = (getHeight() - sizeY) / 2;
        int yAnswer = yQuestion + sizeY - (sizeY / 2);
        questionLabel.setBounds(x, y, sizeX, sizeY);
        answerLabel.setBounds(x, yAnswer, sizeX, sizeY);
    }

    public void advance() {
        // Placeholder
        fadeQuestion();
    }

    private void setFonts() {
        questionLabel.setFont(Font.getFont(JsonFile.read("devBoard.json", "data", "font")));
        answerLabel.setFont(Font.getFont(JsonFile.read("devBoard.json", "data", "font")));
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!hasFaded) {
                    fadeQuestion();
                }
            }
        });


        setFocusable(true);
        requestFocusInWindow();
    }

    private void fadeQuestion() {
        if (hasFaded) {
            return;
        }
        hasFaded = true;

         Timer q = new Timer(50, null);
         q.addActionListener(new ActionListener() {
             private float opacity = 1.0f;

             @Override
             public void actionPerformed(ActionEvent e) {
                 opacity -= 0.05f;
                 if (opacity <= 0.0f) {
                     opacity = 0.0f;
                     q.stop();
                     fadeInAnswer();
                 }
                 questionLabel.setForeground(new Color(0, 0, 0, opacity));
                 repaint();
             }
         });
         q.start();
     }

    private void fadeInAnswer() {

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

                questionLabel.setForeground(new Color(0, 0, 0, opacity2));
                answerLabel.setForeground(new Color(0, 0, 0, opacity2));
                repaint();
            }
        });
        j.start();
    }

    public void exit() {
        GameBoard.HeaderPanel.leftText.setText("Exit");
        changeCurrentPanel(GAME_BOARD.boardPanel, this);
        GAME_BOARD.jCardIsActive = false;
        GAME_BOARD.GameIsActive = true;
        hasFaded = false;
    }
}
