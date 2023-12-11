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
    private JLabel questionText;
    private JLabel answerText;
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

        questionText = new JLabel(question);
        questionText.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 60));

        answerText = new JLabel(answer);
        answerText.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 60));

        mainPanel.add(questionText, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private void fadeIn(JLabel obj, String textToDisplay, int fadeTime) {
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            private float alpha = 0.0f;
            private long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                float progress = (float) (currentTime - startTime) / fadeTime;

                if (progress >= 1.0f) {
                    progress = 1.0f;
                    ((Timer) e.getSource()).stop();
                }

                alpha = progress;
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
                fadeIn(questionText, answerText, question, answer, 1000);
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
