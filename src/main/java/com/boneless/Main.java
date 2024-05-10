package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    //link to GDoc https://docs.google.com/document/d/1IFx3SDvnhjzMkc3hN28-G_46JCnie7hxkWVV7ez0ENA/edit?usp=sharing
    private static final int RESX = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int RESY = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static void main(String[] args) {
        new Main(args);
    }
    public Main(String[] args){
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }
    private void init(){
        add(headPanel(), BorderLayout.NORTH);
        add(mainBoard(), BorderLayout.CENTER);
        add(teamPanel(), BorderLayout.SOUTH);
    }
    private Font mainFont(){
        return new Font("Arial", Font.PLAIN, 15);
    }
    //panel to contain controls and any other headder text
    private JPanel headPanel(){
        JPanel panel = new JPanel();


        return panel;
    }
    //panel to contain the main board grid
    private JPanel mainBoard(){
        JPanel panel = new JPanel();

        return panel;
    }
    //panel to contain team panels
    private JPanel teamPanel(){
        JPanel panel = new JPanel();

        return panel;
    }
}