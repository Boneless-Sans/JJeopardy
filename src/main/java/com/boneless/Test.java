package com.boneless;

import javax.swing.*;

public class Test {
    private static int labelX;
    private static int labelY;
    private static JFrame frame;
    public static void main(String[] args){
        //new InfoCard("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "answer 1" ,"dev_board.json", null, 100);
        new InfoCard("question 1", "answer 1" ,"dev_board.json", "Java", 100);
        /*
        frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.red);
        label.setBounds(0,0,50,50);
        labelX = label.getX();
        labelY = label.getY();
        moveLabel(label);
        frame.add(label);
        frame.setVisible(true);
         */
    }
//    private static void moveLabel(JLabel label){
//        //35
//
//        Timer timer = new Timer(0, e -> {
//            if(labelX < (frame.getWidth() - 14) - label.getWidth()){
//                labelX += 1;
//            }else if(labelY < (frame.getHeight() - 35) - label.getHeight()){
//                //((Timer) e.getSource()).stop();
//                labelY += 1;
//            } else if(labelX > (frame.getWidth() - 350) - label.getWidth()) {
//                labelX -= 1;
//            }
//            label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
//        });
//        timer.start();
//    }
}