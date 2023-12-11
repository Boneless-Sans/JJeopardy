package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InfoCard extends JFrame implements KeyListener {
    private String question;
    private String answer;
    private String fileName;
    private JLabel text;
    private Timer timer;
    private boolean actionPerformed = false;
    public InfoCard(String question, String answer, String fileName) {
        this.question = question;
        this.answer = answer;
        this.fileName = fileName;
        initUI();
    }
    private void initUI(){
        setUndecorated(true);
        setSize(1280,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        addKeyListener(this);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;

        text = new JLabel(question);
        text.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 60));

        mainPanel.add(text, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private void fadeInInitialText(JLabel questionLabel, JLabel answerLabel, String initialText, String finalAnswer) {
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            private float alpha = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.02f; // Adjust the fading speed as needed

                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    ((Timer) e.getSource()).stop();

                    // Add a line break and start fading in the finalAnswer
                    questionLabel.setText(initialText);
                    answerLabel.setText("<html><head><style>.center-text{text-align:center;}</style></head><body><div class='center-text'>" +
                            finalAnswer +
                            "</div></body></html>");
                    fadeIn(answerLabel, 0.0f, 1.0f, finalAnswer);
                }

                questionLabel.setText(initialText);
                questionLabel.setForeground(new Color(0, 0, 0, alpha));
                questionLabel.repaint(); // Ensure the changes are immediately visible
            }
        });

        fadeInTimer.start();
    }

    private void fadeIn(JLabel obj, float startAlpha, float endAlpha, String textToDisplay) {
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            private float alpha = startAlpha;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.02f; // Adjust the fading speed as needed

                if (alpha >= endAlpha) {
                    alpha = endAlpha;
                    ((Timer) e.getSource()).stop();
                }

                obj.setText("<html><head><style>.center-text{text-align:center;}</style></head><body><div class='center-text'>" +
                        textToDisplay +
                        "</div></body></html>");
                obj.setForeground(new Color(0, 0, 0, alpha));
                obj.repaint(); // Ensure the changes are immediately visible
            }
        });

        fadeInTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = Character.toLowerCase(e.getKeyChar());
        if (keyChar == ' ') {
            if (true) {
                fadeInInitialText(text, question, answer);
            } else {
                dispose();
            }
        } else if (keyChar == KeyEvent.VK_ESCAPE) {
            dispose();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
