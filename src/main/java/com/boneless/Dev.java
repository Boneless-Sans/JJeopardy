package com.boneless;

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
        File file = new File(String.valueOf(getClass().getClassLoader().getResource("data/template.json")));

        try {
            FileReader reader = new FileReader(file);
            String test = reader.toString();
            System.out.println(test);
            add(new JLabel(test));
        } catch (FileNotFoundException e) {}
        System.out.println();
    }

    private void fontListTest(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fonts = ge.getAvailableFontFamilyNames();
 
        JComboBox<String> comboBox = new JComboBox<>(fonts);
        if(fonts[0].contains(".")){ //removes macOS's wierd font '.AppleSystemUIFont'
            comboBox.removeItemAt(0);
        }
        add(comboBox);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.exit(0);
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
