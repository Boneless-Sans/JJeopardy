package com.boneless;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class GridLayoutExample {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("GridLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Create a panel with GridLayout
        JPanel panel = new JPanel();
        GridLayout gridLayout = new GridLayout(2, 3); // 2 rows, 3 columns
        panel.setLayout(gridLayout);

        // Add buttons to the panel
        for (int i = 1; i <= 6; i++) {
            JButton button = new JButton("Button " + i);
            panel.add(button);
        }

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame visibility
        frame.setVisible(true);
    }
}
