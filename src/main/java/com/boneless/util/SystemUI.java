package com.boneless.util;

import javax.swing.*;

public class SystemUI {
    private SystemUI(){
        //nuh uh, no use
    }
//    public static void set(){
//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
//    }
    public static void set(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
