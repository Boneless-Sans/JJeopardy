package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InfoCard extends JFrame implements KeyListener {
    private String question;
    private String answer;
    private String fileName;
    private JLabel text;
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
    private void fade(JLabel obj, String question, String answer, int time) {
        String lineBreak = "--------------------------------------";
        obj.setText("<html><head><style>.center-text{text-align:center;}</style></head><body><div class='center-text'>" +
                question +
                "<br />" +
                lineBreak +
                "<br />" +
                answer +
                "</div></body></html>");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = Character.toLowerCase(e.getKeyChar());
        if (keyChar == ' ') {
            //change screen to answer, then dispose
            fade(text, question, answer, 1000);
            System.out.println("pressed");
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
