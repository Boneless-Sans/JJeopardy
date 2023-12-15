package com.boneless;

import javax.swing.*;
import java.awt.*;

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
        //JTextArea textArea = new JTextArea("Lorem ipsum dolor sit amet, consectetur adipiscing elit...");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20,1,10,10));
        // Create a JScrollPane and add the JTextArea to it

        panel.add(createTemplate("text 1"));
        panel.add(createTemplate("text 2"));
        panel.add(createTemplate("text 3"));
        panel.add(createTemplate("text 4"));
        panel.add(createTemplate("text 5"));

        JScrollPane scrollPane = new JScrollPane(panel);

        // Set the scroll policies (optional)
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the frame
        add(scrollPane);
    }
    private JPanel createTemplate(String text){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel(text);
        panel.add(label);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScrollBarExample());
    }
}
