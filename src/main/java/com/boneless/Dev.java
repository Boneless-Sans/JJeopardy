package com.boneless;

import com.boneless.util.GeneralUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.boneless.util.GeneralUtils.*;
import static java.awt.Color.*;

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
        //setLayout(null);
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(null);

        JPanel startPanel = new JPanel(new GridBagLayout());
        startPanel.setBackground(cyan);
        startPanel.setBounds(0,0,getWidth(),getHeight());

        JPanel endPanel = new JPanel(new GridBagLayout());
        endPanel.setBackground(red);

        JPanel testPanel = new JPanel(new GridBagLayout());
        testPanel.setBackground(green);

//        JButton transitionButton = new JButton("Move?");
//        transitionButton.addActionListener(e -> changeCurrentPanel(endPanel, startPanel, false));
//
//        JButton otherTest = new JButton("Test");
//        otherTest.addActionListener(e -> changeCurrentPanel(testPanel, startPanel, true));
//
//        JButton transitionButton2 = new JButton("Move");
//        transitionButton2.addActionListener(e -> changeCurrentPanel(startPanel,endPanel, true));
//
//        startPanel.add(transitionButton, gbc);
//        startPanel.add(otherTest);
//
//        endPanel.add(transitionButton2, gbc);
//        endPanel.add(new JButton("Bruh"){{addActionListener(e -> changeCurrentPanel(testPanel, endPanel, true));}});
//
//        testPanel.add(new JButton("Also a Test"){{addActionListener(e -> changeCurrentPanel(startPanel, testPanel, false));}}, gbc);
//        testPanel.add(new JButton("Also a Test 2"){{addActionListener(e -> changeCurrentPanel(endPanel, testPanel, false));}});



        add(startPanel);
        //add(endPanel);
    }

    private void fontListTest(){
        System.out.println(System.getProperty("java.io.tmpdir"));

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
