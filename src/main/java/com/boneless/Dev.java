package com.boneless;

import com.boneless.util.JRoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Dev extends JFrame implements KeyListener {
    public static void main(String[] args){
        new Dev();
    }

    public Dev(){
        setSize(1200,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        setUndecorated(true);
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(new FlowLayout());

        JRoundedButton button = new JRoundedButton("Test");
        button.setPreferredSize(new Dimension(100, 50));
        button.addActionListener(e -> System.out.println("Pressed"));

        add(button);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.exit(0);
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
