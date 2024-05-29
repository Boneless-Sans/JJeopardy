package com.boneless;

import com.boneless.util.GeneralUtils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

import static com.boneless.Main.*;

public class Team extends JPanel {
    private static int teamCount;
    private int score;
    private final JTextField scoreField;

    public Team(){
        //value setup
        teamCount++;
        score = 0;

        //setup UI
        setBackground(Color.white);
        setPreferredSize(new Dimension(150,110)); //Panel height controller
        setBorder(null);

        //name field
        Color fieldColor = new Color(200,200,200);
        JTextField teamName = new JTextField("Team " + teamCount);
        teamName.setPreferredSize(new Dimension(125,25));
        teamName.setBackground(fieldColor);
        teamName.setBorder(BorderFactory.createEtchedBorder(0));
        teamName.setHorizontalAlignment(JTextField.CENTER);

        //div line
        JPanel line = new JPanel();
        line.setBackground(Color.black);
        line.setPreferredSize(new Dimension(130,1));

        //score field

        scoreField = new JTextField(String.valueOf(this.score));
        scoreField.setFont(GeneralUtils.generateFont(15));
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        scoreField.setBorder(BorderFactory.createEtchedBorder(0));
        scoreField.setBackground(fieldColor);
        scoreField.setPreferredSize(new Dimension(125,25));
        ((AbstractDocument) scoreField.getDocument()).setDocumentFilter(new NumberFilter());

        add(teamName);
        add(line);
        add(scoreField);
        add(new ScoreButton(false));
        add(new ScoreButton(true));
    }
    public void addToScore(int scoreToAdd){
        score += scoreToAdd;
        scoreField.setFocusable(false);
        scoreField.setText(String.valueOf(score));
        scoreField.setFocusable(true);
    }
    private class ScoreButton extends JButton{
        private final Color color;
        private final boolean subtract;
        public ScoreButton(boolean subtract){
            color = subtract ? Color.red : Color.green;
            this.subtract = subtract;

            int size = 40;
            setPreferredSize(new Dimension(size, size));
            addActionListener(e -> {
                if(!subtract){
                    addToScore(GAME_BOARD.scoreToAdd);
                } else {
                    addToScore(-GAME_BOARD.scoreToAdd);
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g){
            //super.paintComponent(g); disable super for complete render control

            Graphics2D g2d = (Graphics2D) g;

            //enable antialiasing, not really needed but cool to have
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int centerX = width / 2;
            int centerY = height / 2;

            int lineThickness = 4; //im not explaining this

            int lineLength = Math.min(width, height) / 4; //sets line length, higher is smaller

            g2d.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            //draw circle
            int ovalDiameter = (Math.min(width, height) / 2) + 10;
            int ovalX = centerX - ovalDiameter /2;
            int ovalY = centerY - ovalDiameter /2;
            g2d.setColor(color);
            g2d.fillOval(ovalX, ovalY, ovalDiameter, ovalDiameter);

            g2d.setColor(Color.white);
            //draw vertical line if subtract is false
            if(!subtract) g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength);
            //horizontal line
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
        }
        @Override
        protected void paintBorder(Graphics g){
            //disable border rendering to remove border
        }
    }
    private static class NumberFilter extends DocumentFilter{
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            if (isNumeric(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            if (isNumeric(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean isNumeric(String text) {
            for (char c : text.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
    }
}
