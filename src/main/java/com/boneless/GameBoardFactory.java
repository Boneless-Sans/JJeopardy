package com.boneless;

import com.boneless.util.IconResize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GameBoardFactory extends JFrame {
    public GameBoardFactory(){
        initUI();
    }
    private void initUI(){
        setSize(1280, 720);
        setLocationRelativeTo(null);
        addWindowListener(closeWindow());


        setVisible(true);
    }
    private void save(){
        //save somehow
    }
    private WindowListener closeWindow(){
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] responses = {
                        "Cancel","Exit Without Saving","Save and Exit"
                };
                IconResize icon = new IconResize("src/main/resources/assets/textures/dom.png");
                int answer = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure you want to exit without saving?",
                        "Unsaved Changes!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        icon.getImage(), responses, 0);
                switch (answer){
                    case 1 -> {
                        save();
                        exitApp();
                    }
                    case 2 -> exitApp();
                    case 3 -> {
                        return;
                    }
                }
            }
        };
    }
    private void exitApp(){
        dispose();
        Launcher.main(null);
    }
}
