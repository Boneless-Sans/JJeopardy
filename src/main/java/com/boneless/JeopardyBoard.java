//package com.boneless;
//
//import com.boneless.GameBoard;
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//
//public class JeopardyBoard extends JFrame {
//    private JPanel mainPanel;
//
//    public JeopardyBoard(List<Point> points) {
//        setTitle("Jeopardy Board");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 600);
//
//        mainPanel = new JPanel(new CardLayout());
//        JPanel buttonPanel = new JPanel(new GridLayout(5, 5)); // Example layout
//
//        for (Point point : points) {
//            BoardButton button = new BoardButton(point.getScore(), point.getQuestion(), point.getAnswer());
//            buttonPanel.add(button);
//        }
//
//        mainPanel.add(buttonPanel, "buttonPanel");
//        add(mainPanel);
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        List<Point> points = JsonFile.readPointsFromFile("points.json");
//        new JeopardyBoard(points);
//    }
//}