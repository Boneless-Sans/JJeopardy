package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TextScalingExample extends JFrame {
    private JPanel panel;
    private JLabel label;

    public TextScalingExample() {
        setTitle("Text Scaling Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw a rectangle to represent the available space
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new BorderLayout());

        label = new JLabel("Resizable Text", SwingConstants.CENTER);
        label.setFont(new Font("Helvetica", Font.PLAIN, 20));
        panel.add(label, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.CENTER);

        // Add a component listener to handle resizing
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeText();
            }
        });
    }

    private void resizeText() {
        // Calculate optimal font size based on panel dimensions
        int width = panel.getWidth();
        int height = panel.getHeight();
        int newFontSize = Math.max(Math.min((width * height) / 5000, 30), 10); // Adjust this formula as needed

        // Set the new font size
        label.setFont(label.getFont().deriveFont(Font.PLAIN, newFontSize));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextScalingExample::new);
    }
}
