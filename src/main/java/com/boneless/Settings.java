package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;

public class Settings extends JFrame{
    public Settings(){
        setSize(500,500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JButton exit = new JButton("exit");

        add(exit);
        setVisible(true);
    }
}
