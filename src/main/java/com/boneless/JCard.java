package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JCard extends JPanel {
    public boolean isActive = false;
    private JLabel questionLabel;
    private JLabel answerLabel;

    public JCard(int score, String question, String answer) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        questionLabel = new JLabel("Question: " + question);
        answerLabel = new JLabel("Answer: " + answer);

        add(questionLabel, gbc);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeQuestion();
            }
        });
        //commit test niggerrr
    }

    private boolean hasFaded = false;

    private void fadeQuestion() {
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
}
