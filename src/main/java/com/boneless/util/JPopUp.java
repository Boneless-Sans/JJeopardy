package com.boneless.util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JPopUp {
    public static void showButtonInputPopUp(Container parent, String title, JButton sourceButton, JButton... actionButtons) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setSize(500,300);

        int startX = parent.getWidth() / 2 - panel.getWidth() / 2;
        int startY = parent.getHeight();
        int centerY = parent.getHeight() / 2 - panel.getHeight() / 2;

        panel.setLocation(startX, startY);

        Color mainColor;
        Color fontColor;

        if(fileName == null){
            mainColor = new Color(20,20,255);
            fontColor = Color.white;
        } else {
            mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
            fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
        }
        Color accentColor = adjustColor(mainColor);

        //header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(accentColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(generateFont(15));
        titleLabel.setForeground(fontColor);

        headerPanel.add(titleLabel);

        //body
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setBackground(mainColor);

        JRoundedButton inputButton = new JRoundedButton("Click to Set");
        inputButton.setPreferredSize(new Dimension(300,100));
        inputButton.setFocusable(true);
        inputButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 8: inputButton.setText("Backspace"); break;
                    case 10: inputButton.setText("Enter"); break;
                    case 27: inputButton.setText("Esc"); break;
                    case 32: inputButton.setText("Space"); break;
                    case 127: inputButton.setText("Delete"); break;
                    default: inputButton.setText(firstUpperCase(String.valueOf(e.getKeyChar())));
                }
                sourceButton.setText(inputButton.getText());
                panel.revalidate();
                panel.repaint();
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });

        bodyPanel.add(inputButton);

        //footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(accentColor);

        for(JButton actionButton : actionButtons){
            footerPanel.add(actionButton);
        }

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(bodyPanel, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);

        animateToPosition(centerY, startX);
    }

    public static void hidePopUp(int startY) {
        if(inputButton != null) {
            inputButton.setFocusable(false);
            inputButton.setEnabled(false);
        }
        animateToPosition(startY);
    }

    private static void animateToPosition(JPanel self, int targetY, int startX) {
        Timer animationTimer;

        int duration = 1000; //ani time
        int framesPerSecond = 60;
        int delay = 1000 / framesPerSecond;

        int startY = self.getY();
        int distance = targetY - startY;

        animationTimer = new Timer(delay, new ActionListener() {
            long startTime = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }

                long currentTime = System.currentTimeMillis();
                float elapsedTime = (float) (currentTime - startTime) / duration;

                if (elapsedTime > 1.0f) {
                    elapsedTime = 1.0f;
                    ((Timer) e.getSource()).stop();
                }

                // ease in out function
                float easedTime = cubicEaseInOut(elapsedTime);

                int newY = startY + Math.round(distance * easedTime);
                self.setLocation(startX, newY);
                self.revalidate();
                self.repaint();
            }
        });

        animationTimer.start();
    }

    private static float cubicEaseInOut(float t) {
        if (t < 0.5f) {
            return 4 * t * t * t; //ease in
        } else {
            float f = (t - 1);
            return 1 + 4 * f * f * f; //ease out
        }
    }
}