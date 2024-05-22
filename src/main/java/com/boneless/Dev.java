package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Dev extends JFrame {
    public static void main(String[] args){
        new Dev();
    }
    public Dev(){
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        init();
        setVisible(true);
    }
    private void init() {
        setLayout(new GridLayout(10,10));

        for(int i = 0; i < 100; i++){
            add(mineButton("src/main/resources/icon/icon.png"));
        }
    }
    private JButton mineButton(String pathToImage){
        JButton button = new JButton();

        ImageIcon originalIcon = new ImageIcon(pathToImage);
        Image originalImage = originalIcon.getImage();

        button.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = button.getWidth();
                int height = button.getHeight();

                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);

                button.setIcon(icon);
            }
        });

        return button;
    }
}
