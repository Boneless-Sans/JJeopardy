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
        init();
        setVisible(true);
    }

    private void init() {
        //
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
