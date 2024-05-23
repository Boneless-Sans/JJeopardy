package com.boneless;

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

        JPanel questionLabel = new JPanel(new GridBagLayout());
        questionLabel.setBackground(Color.cyan);
        JPanel answerLabel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        int sizeX = getWidth() / 2;
        int sizeY = getHeight() / 2;
        answerLabel.setBounds(getWidth() / 2 - sizeX / 2,(getHeight() / 2) - sizeY / 2, sizeX, sizeY);
        questionLabel.setBounds((getWidth() / 2) - sizeX / 2,(getHeight() / 2) - sizeY / 2, sizeX, sizeY);

        questionLabel.add(new JLabel("Question: "), gbc);
        questionLabel.setForeground(new Color(0,0,0,1.0f));
        answerLabel.add(new JLabel("Answer: "), gbc);
        answerLabel.setForeground(new Color(0, 0, 0, 0.0f));

        add(questionLabel);
        add(answerLabel);

        setupListeners();
    }

    public void advance(){
        //
    }

    private void setFonts(){

    }

    private void setupListeners() {
        // Mouse listener to trigger fading on mouse click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!hasFaded) {
                    fadeQuestion();
                }
            }
        });

        // Key listener to trigger fading on key press
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!hasFaded) {
                    fadeQuestion();
                }
            }
        });

        // Ensure the panel is focusable to receive key events
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

                int size = 200;
                questionLabel.setBounds(getWidth() / 2 - size, getHeight() / 2  - size, size, size);
                questionLabel.setForeground(new Color(0,0,0, opacity2));
                answerLabel.setForeground(new Color(0, 0, 0, opacity2));
                repaint();
            }
        });
        j.start();
    }
    public void exit(){
        GameBoard.HeaderPanel.leftText.setText("Exit");
        changeCurrentPanel(GAME_BOARD.boardPanel, this);
        GAME_BOARD.jCardIsActive = false;
        GAME_BOARD.GameIsActive = true;
        hasFaded = false;
    }
}

