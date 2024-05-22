package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.boneless.util.GeneralUtils.*;

import static com.boneless.Main.GAME_BOARD;

public class JCard extends JPanel {
    private final JLabel questionLabel;
    private final JLabel answerLabel;

    public JCard(int score, String question, String answer, Color mainColor) {
        setLayout(new GridBagLayout());
        //todo (for you bb): set background color with 'mainColor'
        GridBagConstraints gbc = new GridBagConstraints();
        setFocusable(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        questionLabel = new JLabel("Question: " + question);
        questionLabel.setFont(generateFont(15));

        answerLabel = new JLabel("Answer: " + answer);
        answerLabel.setFont(generateFont(15));

        add(questionLabel, gbc);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                advance();
            }
        });
    }

    private boolean hasFaded = false;

    public void advance() {
        if(hasFaded){
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
                       }
                       questionLabel.setForeground(new Color(0, 0, 0, opacity));
                       repaint();
                       }
        });
        q.start();
    }
    public void exit(){
        GAME_BOARD.jCardIsActive = false;
        GAME_BOARD.GameIsActive = true;
        changeCurrentPanel(GAME_BOARD.boardPanel, this);
    }
}
