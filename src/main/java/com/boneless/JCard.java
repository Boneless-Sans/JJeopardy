package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.boneless.util.JsonFile;

public class JCard extends JPanel {
    public boolean isActive = false;
    private final JLabel questionLabel;
    private final JLabel answerLabel;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private int continueKey = KeyEvent.VK_SPACE;

    public JCard(int score, String question, String answer) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        questionLabel = new JLabel("Question: " + question);
        answerLabel = new JLabel("Answer: " + answer);

        add(questionLabel, gbc);
        add(answerLabel, gbc);
        answerLabel.setForeground(new Color(0, 0, 0, 0.0f));

        loadKeybind();

        setupListeners();
    }

    private void loadKeybind() {
        String key = JsonFile.readTwoKeys("settings.json", "keyBinds", "continue");

        System.out.println("Continue key from JSON: " + key);

        if (key != null) {
            switch (key.toUpperCase()) {
                case "SPACE":
                    continueKey = KeyEvent.VK_SPACE;
                    break;
                case "ENTER":
                    continueKey = KeyEvent.VK_ENTER;
                    break;
                case "ESC":
                    continueKey = KeyEvent.VK_ESCAPE;
                    break;
                case "F":
                    continueKey = KeyEvent.VK_F;
                    break;
                default:
                    continueKey = KeyEvent.VK_SPACE;
                    break;
            }
        }
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!hasFaded) {
                    fadeQuestion();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
                if (e.getKeyCode() == continueKey && !hasFaded) {
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

                answerLabel.setForeground(new Color(0, 0, 0, opacity2));
                repaint();
            }
        });
        j.start();
    }
}
