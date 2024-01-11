package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class RandomScalingPanelExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Main Frame");
            mainFrame.setSize(400, 300);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());

            JButton spawnButton = new JButton("Spawn Panel");
            spawnButton.addActionListener(e -> {
                // Create a panel that will be scaled
                JPanel scalingPanel = new JPanel();
                scalingPanel.setBackground(Color.BLUE);
                scalingPanel.setLayout(null); // Allows manual positioning

                // Set the panel to be transparent
                scalingPanel.setOpaque(false);

                // Add the panel to the content pane of the main frame
                mainFrame.add(scalingPanel, BorderLayout.CENTER);

                // Set to undecorated to allow free positioning
                JDialog dummyDialog = new JDialog(mainFrame);
                dummyDialog.setUndecorated(true);

                // Set the initial random location of the panel
                setRandomLocation(mainFrame, scalingPanel);

                // Scale the panel to completely cover the main frame
                Timer scaleTimer = new Timer(5, scaleAction(mainFrame, scalingPanel, dummyDialog));
                scaleTimer.start();
            });

            mainFrame.add(spawnButton, BorderLayout.SOUTH);

            mainFrame.setVisible(true);
        });
    }

    private static void setRandomLocation(JFrame mainFrame, JPanel scalingPanel) {
        Random random = new Random();
        int maxX = mainFrame.getWidth() - scalingPanel.getWidth();
        int maxY = mainFrame.getHeight() - scalingPanel.getHeight();

        int randomX = random.nextInt(maxX + 1);
        int randomY = random.nextInt(maxY + 1);

        scalingPanel.setLocation(randomX, randomY);
    }

    private static AbstractAction scaleAction(JFrame mainFrame, JPanel scalingPanel, JDialog dummyDialog) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scalingPanel.getWidth() < mainFrame.getWidth() || scalingPanel.getHeight() < mainFrame.getHeight()) {
                    scalingPanel.setSize(scalingPanel.getWidth() + 2, scalingPanel.getHeight() + 2);
                } else {
                    ((Timer) e.getSource()).stop(); // Stop the timer when the panel is fully scaled
                    dummyDialog.dispose(); // Dispose the dummy dialog after scaling
                }
            }
        };
    }
}
