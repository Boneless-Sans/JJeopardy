package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JCard extends JPanel {
    public JCard(){}
    public void createCard(int score, String question, String answer){
        setLayout(new BorderLayout());

        add(header(), BorderLayout.NORTH);
        add(mainContent(), BorderLayout.CENTER);
    }
    private JPanel header(){
        JPanel panel = new JPanel(new FlowLayout());

        return panel;
    }
    private JPanel mainContent(){
        JPanel panel = new JPanel(new GridBagLayout());

        return panel;
    }

    public JCard(int score, String question, String answer) {
        setLayout(new BorderLayout());
        JLabel questionLabel = new JLabel("Question: " + question);
        JLabel answerLabel = new JLabel("Answer: " + answer);
        add(questionLabel, BorderLayout.CENTER);
        add(answerLabel, BorderLayout.SOUTH);
    }
}