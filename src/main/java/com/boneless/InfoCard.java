package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class InfoCard extends JFrame implements KeyListener {
    private final String question;
    private final String answer;
    private final String fileName;
    private boolean exit = false;
    private JLabel questionText;
    private JLabel answerText;
    private final JButton actButton;
    private String esc = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","exit")));
    private String advance = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","continue")));

    //todo: fix text fading
    public InfoCard(String question, String answer, String fileName, String category, int points, boolean doFullScreen, JButton actButton){
        this.question = question;
        this.answer = answer;
        this.fileName = fileName;
        this.actButton = actButton;

        setTitle(category + " For " + points);
        initUI(doFullScreen);
    }
    private int parseFontType(String fontType){
        return switch (JsonFile.read(fileName,"data",fontType)) {
            case "Font.BOLD" -> 1;
            case "Font.ITALIC" -> 2;
            default -> 0;
        };
    }
    private Color parseColor(String color){
        String initColor = JsonFile.read(fileName, "data",color);
        String[] split = initColor.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    private void initUI(boolean doFullScreen){
        if(doFullScreen){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            System.out.println("fullscreen");
        }else {
            setSize(1600,900);
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        addKeyListener(this);
        setLocationRelativeTo(null);
        SystemUI.set();
        if(Objects.equals(esc, null)){
            esc = JsonFile.read("settings.json","keyBinds","exit");
        }
        if(Objects.equals(advance, null)){
            advance = JsonFile.read("settings.json","keyBinds","continue");
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(parseColor("info_background"));

        questionText = createLabel(question, 0);

        answerText = createLabel(answer, -100);

        mainPanel.add(questionText);
        mainPanel.add(createLabel("------------------------------------------------------------", 0)); //60 chars :3
        mainPanel.add(answerText);

        add(headerPanel(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private JLabel createLabel(String text, int posMod) {
        String color = JsonFile.read(fileName, "data", "text_font_color");
        JLabel label = new JLabel("<html><center><body style=color:rgb(" + color + ")>" + text + "</center></html>", JLabel.CENTER);

        int x = (getWidth() - 1200) / 2;
        int y = (getHeight() - 400) / 2;

        label.setFont(new Font(Font.DIALOG, Font.PLAIN, 60)); //set font size 60
        label.setForeground(parseColor("info_text_color"));
        label.setOpaque(false);
        label.setBounds(x,y - posMod, 1200,400);

        return label;
    }
    @SuppressWarnings("MagicConstant")
    private Font parseFont(JPanel parent, String textType){
        String fontName = JsonFile.read(fileName,"data", textType);
        int fontType = switch (JsonFile.read(fileName,"data",textType + "_font")){
            case "italic" -> 1;
            case "bold" -> 2;
            default -> 0;
        };
        int fontSize = Math.abs((parent.getHeight() / 2));
        return new Font(fontName, fontType, fontSize);
    }
    private JPanel headerPanel(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(parseColor("info_header_background"));

        JLabel closeText = new JLabel("Continue");
        closeText.setForeground(parseColor("info_header_text_font"));
        closeText.setFont();

        JLabel title = new JLabel(getTitle());
        title.setForeground(parseColor("info_header_text_color"));
        title.setFont();

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        titlePanel.add(title, gbc);

        JLabel reveal = new JLabel("Reveal Correct Answer");
        reveal.setForeground(parseColor("info_header_text_color"));
        reveal.setFont();

        panel.add(createHeaderPanel(closeText, createHeaderButton("exit", false)));

        panel.add(titlePanel);

        panel.add(createHeaderPanel(reveal, createHeaderButton("continue", true)));

        return panel;
    }
    private JPanel createHeaderPanel(JLabel label, JButton button){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.add(label);
        panel.add(button);
        return panel;
    }
    private JButton createHeaderButton(String text, boolean type){
        String rawKeyBind = JsonFile.read("settings.json","keyBinds", text);
        String keyBind = rawKeyBind.substring(0,1).toUpperCase() + rawKeyBind.substring(1);
        JButton button = new JButton(keyBind);
        button.setFocusable(false);
        button.setFont();
        button.addActionListener(e -> {
            if(type && !exit) {
                if (question.length() > 100) {
                    movePanel(questionText, 150);
                } else {
                    movePanel(questionText, 100);
                }
            }else{
                dispose();
            }
        });

        return button;
    }
    private String parseKeyStrokeInput(String keyStrokeCode){
        return switch (keyStrokeCode){
            case "Esc" -> "\u001B";
            case "Space" -> " ";
            case "Enter" -> "\n";
            case "Backspace" -> "\b";
            default -> keyStrokeCode;
        };
    }
    private void movePanel(JLabel panel, int amount) {
        int moveAmount = panel.getY() - amount;
        exit = true;
        Timer timer = new Timer(1, e -> {
            if (panel.getY() > moveAmount) {
                panel.setBounds(panel.getX(), panel.getY() - 10, panel.getWidth(), panel.getHeight()); // Decrease Y position to move up
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer when the movement is completed
                fadeIn(answerText, lineBreakText); //todo: remake fade in to work with new HTML code
            }
        });

        timer.start();
    }
    private void fadeIn(JLabel answerLabel, JLabel lineBreak) {
        Timer fadeInTimer = new Timer(10, new ActionListener() {
            private final long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                float progress = (float) (currentTime - startTime) / 1000;

                if (progress >= 1f) {
                    progress = 1f;
                    ((Timer) e.getSource()).stop();
                }

                // Adjust alpha for transparency
                float alpha = progress;

                // Set the text color with adjusted alpha
                Color fg = new Color(255, 255, 255, Math.round(alpha * 255));
                answerLabel.setForeground(fg);
                lineBreak.setForeground(fg);
            }
        });

        fadeInTimer.start();
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(String.valueOf(e.getKeyChar()).equals(advance)) {
            if(!exit) {
                if (question.length() > 100) {
                    movePanel(questionText, 150);
                } else {
                    movePanel(questionText, 100);
                }
            }else{
                dispose();
            }
        }
        if(String.valueOf(e.getKeyChar()).equals(esc)){
            dispose();
            actButton.setEnabled(true);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}