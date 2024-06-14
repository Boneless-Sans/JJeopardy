package com.boneless.util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JPopUp extends JPanel {
    public static final int BUTTON_INPUT = 0;
    public static final int TEXT_INPUT = 1;
    public static final int MESSAGE = 2;
    public JRoundedButton BUTTON_CANCEL;

    private final int startX;
    private final int startY;
    private final int centerY;

    private String keyPressed;

    private Timer animationTimer;
    private Timer clearTimer;

    private JButton sourceButton;
    private JRoundedButton inputButton;

    private final Container parent;

    public JPopUp(Container parent) {
        this.parent = parent;

        setSize(500,300);
        setLayout(new BorderLayout());

        BUTTON_CANCEL = new JRoundedButton("Cancel");
        BUTTON_CANCEL.addActionListener(e -> hidePopUp());

        //more calculations
        startX = parent.getWidth() / 2 - getWidth() / 2;
        startY = parent.getHeight();
        centerY = parent.getHeight() / 2 - getHeight() / 2;

        setLocation(startX, startY);
    }

    public void showPopUp(String title, String message, JButton sourceButton, int type, JButton... actionButtons) {
        this.sourceButton = sourceButton;

        System.out.println(parent.getLayout());

        removeAll();
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

        switch (type){
            case BUTTON_INPUT: {
                JRoundedButton inputButton = createKeyButton();

                bodyPanel.add(inputButton, gbc);
            }
            case TEXT_INPUT: {
                //
            }
            case MESSAGE: {
                //
            }
        }

        //footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(accentColor);

        for(JButton actionButton : actionButtons){
            footerPanel.add(actionButton);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        animateToPosition(centerY);

    }

    private JRoundedButton createKeyButton() {
        inputButton = new JRoundedButton("Click to Set");
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
                revalidate();
                repaint();
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
        return inputButton;
    }

    public void hidePopUp() {
        inputButton.setFocusable(false);
        inputButton.setEnabled(false);
        animateToPosition(startY);
    }

    private void animateToPosition(int targetY) {
        int duration = 1000; //ani time
        int framesPerSecond = 60;
        int delay = 1000 / framesPerSecond;

        int startY = getY();
        int distance = targetY - startY;

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

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
                setLocation(startX, newY);
                revalidate();
                repaint();
            }
        });

        animationTimer.start();
    }

    private float cubicEaseInOut(float t) {
        if (t < 0.5f) {
            return 4 * t * t * t; //ease in
        } else {
            float f = (t - 1);
            return 1 + 4 * f * f * f; //ease out
        }
    }
}