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
    private final String lineBreak = "............................";
    private JLabel questionText;
    private JLabel answerText;
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

        JPanel mainPanel = new JPanel(null);

        JPanel questionPanel = new JPanel();
        questionPanel.setBounds(50,50,50,50);
        questionPanel.setBackground(Color.red);
        questionText = new JLabel(question);
        questionText.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 60));

        answerText = new JLabel(answer);
        answerText.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 60));
        answerText.setForeground(new Color(0,0,0,0));

        questionPanel.add(questionText);
        mainPanel.add(questionPanel);
        mainPanel.add(answerText);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private void fadeIn(JLabel questionLabel, JLabel answerLabel, String textToDisplay, int fadeTime) {
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

                JComponent what = new JButton();

                questionLabel.setForeground(new Color(0, 0, 0, 0));
                answerLabel.setText(textToDisplay);
                answerLabel.setForeground(new Color(0, 0, 0, alpha));
            }
        });

        fadeInTimer.start();
    }
    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = Character.toLowerCase(e.getKeyChar());
        if (keyChar == ' ') {
            if (true) {
                fadeIn(questionText,answerText, answer, 1000);
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
