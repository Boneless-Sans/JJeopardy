package com.boneless.util;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class NormalButtons {

    public static void set() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
