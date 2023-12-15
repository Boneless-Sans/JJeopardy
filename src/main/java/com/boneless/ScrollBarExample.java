package com.boneless;

import javax.swing.*;

public class ScrollBarExample extends JFrame {

    public ScrollBarExample() {
        // Set up the frame
        setTitle("JScrollBar Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Add components to the frame
        addComponents();

        // Make the frame visible
        setVisible(true);
    }

    private void addComponents() {
        JTextArea textArea = new JTextArea("Lorem ipsum dolor sit amet, consectetur adipiscing elit...");

        // Create a JScrollPane and add the JTextArea to it
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set the scroll policies (optional)
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the frame
        add(scrollPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScrollBarExample());
    }
}
