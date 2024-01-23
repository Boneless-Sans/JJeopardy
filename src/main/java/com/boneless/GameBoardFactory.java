package com.boneless;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameBoardFactory extends JFrame {
    private String title;
    private int columns;
    private int rows;
    private boolean hasChanged;
    public GameBoardFactory(){
        initUI();
    }
    private void initUI(){
        String[] optionPane = {"Yes","No"};
        Icon icon = new ImageIcon("assets/textures/warn.png");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(hasChanged){
                    if(JOptionPane.showOptionDialog(
                            null,
                            "Body Text",
                            "Hello, Title",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            icon.getImage(), //how icon and not image
                            optionPane, 0)){
                        Launcher.initUI();
                        dispose();
                    }
                }
            }
        });
        setSize(1280, 720);
        setLocationRelativeTo(null);

        //create panels
        JFrame colorPallet = new JFrame();
        initColorPallet(colorPallet);

        setVisible(true);
    }
    private void initColorPallet(JFrame frame){
        frame.setSize(300,400);

        setVisible(true);
    }
}
