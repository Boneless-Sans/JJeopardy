package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Team extends JPanel {
    private static int teamCount;
    private int score = 0;
    private String name;
    public Team(){
        teamCount++;

        setPreferredSize(new Dimension(0,0));
    }
    public int getScore(){
        return score;
    }
    public String getTeamName(){
        return name;
    }

    public static class ScoreButton extends JButton{
        private final Color color;
        private final boolean subtract;
        public ScoreButton(boolean subtract){
            color = subtract ? Color.red : Color.green;
            this.subtract = subtract;
        }
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            //enable antialiasing, not really needed but cool to have ig
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int centerX = width / 2;
            int centerY = height / 2;

            int lineThickness = 20; //im not explaining this

            int lineLength = Math.min(width, height) / 3; //set rough size

            g2d.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g2d.setColor(color);

            //draw vertical line if subtract is false
            if(!subtract) g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength);
            //horizontal line
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
        }
    }
}
