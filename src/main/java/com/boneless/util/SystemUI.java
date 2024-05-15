package com.boneless.util;

import javax.swing.*;

public class SystemUI{
    public static void set(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {

        }
    }
}
