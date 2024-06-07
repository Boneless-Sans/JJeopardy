package com.boneless;

import com.boneless.util.GeneralUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.boneless.util.GeneralUtils.gbc;

public class Dev extends JFrame implements KeyListener {
    public static void main(String[] args){
        new Dev();
    }

    public Dev(){
        setSize(1200,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dev");
        setUndecorated(true);
        //setLayout(null);
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(null);

        JPanel startPanel = new JPanel(new GridBagLayout());
        startPanel.setBackground(Color.cyan);
        startPanel.setBounds(0,0,getWidth(),getHeight());

        JPanel endPanel = new JPanel();
        endPanel.setBackground(Color.red);

        JButton transitionButton = new JButton("Move?");
        transitionButton.addActionListener(e -> movePanels(endPanel, startPanel));

        JButton transitionButton2 = new JButton("Move");
        transitionButton2.addActionListener(e -> movePanels(startPanel,endPanel));

        startPanel.add(transitionButton, gbc);
        endPanel.add(transitionButton2, gbc);



        add(startPanel);
        add(endPanel);
    }

    private JComponent lastMovedPanel;
    private void movePanels(JPanel panelToAdd, JPanel self) {
        // Get the starting and target positions
        int selfStartY = self.getY();
        int selfTargetY;
        int panelToAddStartY;
        int panelToAddTargetY;

        if (lastMovedPanel == self) {
            // If self was the last moved panel, move self down and panelToAdd up
            selfTargetY = getHeight();
            panelToAddStartY = -self.getHeight();
        } else {
            // If self was not the last moved panel, move self up and panelToAdd down
            selfTargetY = -self.getHeight();
            panelToAddStartY = getHeight();
        }
        panelToAddTargetY = selfStartY;

        // Define the duration for the animation in milliseconds
        int duration = 1000; // 1 second
        // The interval for timer events in milliseconds
        int interval = 10;  // Check every 10 ms for smoother animation

        Timer timer = new Timer(interval, null);

        // Record the start time of the animation
        long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            // Calculate the elapsed time
            long elapsed = System.currentTimeMillis() - startTime;
            // Calculate the progress as a value between 0 and 1
            double progress = Math.min(1.0, (double) elapsed / duration);

            // Quadratic easing out function (decelerating to a stop)
            double easeProgress = -Math.pow(progress - 1, 2) + 1;

            // Calculate the new Y positions based on the eased progress
            int selfNewY = (int) (selfStartY + easeProgress * (selfTargetY - selfStartY));
            int panelToAddNewY = (int) (panelToAddStartY + easeProgress * (panelToAddTargetY - panelToAddStartY));

            // Move the panels based on the new calculated Y positions
            self.setBounds(self.getX(), selfNewY, self.getWidth(), self.getHeight());
            panelToAdd.setBounds(panelToAdd.getX(), panelToAddNewY, panelToAdd.getWidth(), panelToAdd.getHeight());

            // Debug output to check the positions
            System.out.println("self Y: " + selfNewY + " panelToAdd Y: " + panelToAddNewY);

            // Stop the timer when the progress reaches 1 (animation completed)
            if (progress >= 1.0) {
                timer.stop();
                lastMovedPanel = self;
            }

            // Ensure the panel updates are reflected immediately
            self.repaint();
            panelToAdd.repaint();
            self.getParent().revalidate();  // Ensure container is revalidated
            self.getParent().repaint();
        });

        // Make sure the parent container has a layout manager that allows absolute positioning
        if (self.getParent().getLayout() != null) {
            self.getParent().setLayout(null);
        }

        // Set initial bounds for panelToAdd if it doesn't have any
        if (panelToAdd.getBounds().isEmpty()) {
            panelToAdd.setBounds(self.getX(), panelToAddStartY, self.getWidth(), self.getHeight());
        }

        // Add the panelToAdd to the parent container if not already added
        if (panelToAdd.getParent() == null) {
            self.getParent().add(panelToAdd);
        }

        // Make sure both panels are visible
        self.setVisible(true);
        panelToAdd.setVisible(true);

        // Start the timer
        timer.start();
    }


    private void fontListTest(){
        System.out.println(System.getProperty("java.io.tmpdir"));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fonts = ge.getAvailableFontFamilyNames();

        JComboBox<String> comboBox = new JComboBox<>(fonts);
        if(fonts[0].contains(".")){ //removes macOS's wierd font '.AppleSystemUIFont'
            comboBox.removeItemAt(0);
        }
        add(comboBox);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.exit(0);
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
