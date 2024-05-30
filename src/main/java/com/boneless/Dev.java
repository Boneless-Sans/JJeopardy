package com.boneless;

import com.boneless.util.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Dev extends JFrame {
    public static void main(String[] args){
        new Dev();
    }
    public Dev(){
        setSize(1280,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        setLayout(null);
        init();
        setVisible(true);
    }
    private void init() {
        JPanel testPanel = new JPanel(new BorderLayout());
        testPanel.setBackground(Color.cyan);
        int factor = 10;
        int sizeX = getWidth() - (getWidth() / factor);
        int sizeY = (getHeight() - (getHeight() / factor)) / 2;
        testPanel.setBounds((getWidth() / 2) - (sizeX / 2), (getHeight() / 2) - (sizeY / 2), sizeX, sizeY);

        JLabel label = new JLabel("This is a very very interesting question");
        label.setFont(GeneralUtils.generateFont(50));

        testPanel.add(label);
        add(testPanel);

        System.out.println(testPanel.getY() + " " + getHeight() / 5);
        Thread thread = new Thread(() -> {
            while(testPanel.getY() >= getHeight() / 10){
                try {
                    testPanel.setBounds(testPanel.getX(), testPanel.getY() - 3, testPanel.getWidth(), testPanel.getHeight());
                    Thread.sleep(16); //~60 fps
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
