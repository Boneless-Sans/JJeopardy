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
        setLayout(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.blue);
        //JLabel label = new JLabel("Boneless");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        int sizeX = getWidth()/2;
        int sizeY = getHeight()/2;
        panel.setBounds((getWidth() / 2) - sizeX / 2, (getHeight() / 2) - sizeY / 2, sizeX, sizeY);

        panel.add(new JLabel("Text"), gbc);

        add(panel);

        Thread thread = new Thread(() -> {
            while(true){
                try {
                    panel.setBounds(panel.getX() - 1, panel.getY() - 1, sizeX, sizeY);
                    Thread.sleep(60); //60 fps
                } catch (InterruptedException e){
                    e.getMessage();
                }
            }
        });
        thread.start();
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
