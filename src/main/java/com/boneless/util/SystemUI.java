package com.boneless.util;

import javax.swing.*;

public class SystemUI {
    public static void set() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            UIManager.getSystemLookAndFeelClassName();
        }
    }
}
