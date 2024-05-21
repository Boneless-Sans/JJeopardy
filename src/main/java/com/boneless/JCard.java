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

    public JCard() {}

    public void createCard(int score) { // !! PATH: board > col_# > question_# / answer_# !!
        isActive = true;
        setLayout(new BorderLayout());

        add(header(), BorderLayout.NORTH);
        add(mainContent(), BorderLayout.CENTER);
    }

    private JPanel header() {
        JPanel panel = new JPanel(new FlowLayout());
        return panel;
    }

    private JPanel mainContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        return panel;
    }

    public void advance() {
        System.out.println("Advance");
    }

    public JCard(int score, String question, String answer) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints gbc2 = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc2.gridx = 0;
        gbc2.gridy = 5;
        gbc2.fill = GridBagConstraints.NONE;

        questionLabel = new JLabel("Question: " + question);
        answerLabel = new JLabel("Answer: " + answer);

        add(questionLabel, gbc2);
        add(answerLabel, gbc);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeOutQuestion();
            }
        });
    }

    private void fadeQuestion(){
        Timer q = new Timer(50, null);
        q.addActionListener(e -> {
            float opacity = 1.0f;
            if (questionLabel.isOpaque()) {
                opacity = 0.0f;
            }
        }
    }
    private void fadeOutQuestion() {
        Timer timer = new Timer(50, null);
        timer.addActionListener(new ActionListener() {
            private float opacity = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    timer.stop();
                }
                questionLabel.setForeground(new Color(0, 0, 0, opacity));
                repaint();
            }
        });
        timer.start();
    }
}
