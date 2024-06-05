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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.boneless.util.GeneralUtils.gbc;

public class Dev extends JFrame implements KeyListener {
    public static void main(String[] args){
        new Dev();
    }

    public Dev(){
        setSize(128,128);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        setUndecorated(true);
        //setLayout(null);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(GeneralUtils.renderIcon(),0,0,null);
            }
        };
        add(panel);

        BufferedImage icon = GeneralUtils.renderIcon();
        File output = new File(System.getProperty("user.home") + "/Desktop/icon.png");
        try {
            ImageIO.write(icon, "png", output);
            System.out.println("Image saved!");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
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
