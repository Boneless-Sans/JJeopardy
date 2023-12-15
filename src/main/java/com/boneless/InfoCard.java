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
    private Color headerColor = new Color(21, 27, 75);
    private Color textColor = new Color(255,255,255);
    private JPanel questionPanel;
    private JLabel answerText;
    private JLabel lineBreakText;
    private String esc = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","exit")));
    private String advance = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","continue")));
    public InfoCard(String question, String answer, String fileName, String category, int points, boolean doFullScreen){
        this.question = question;
        this.answer = answer;
        this.fileName = fileName;
        setTitle(category + " For " + points);
        initUI(doFullScreen);
    }
    @SuppressWarnings("MagicConstant")
    private void initUI(boolean doFullScreen){
        if(doFullScreen){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            System.out.println("fullscreen");
        }else {
            setSize(1280, 720);
        }
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addKeyListener(this);
        SystemUI.set();
        if(Objects.equals(esc, "null")){
            esc = JsonFile.read("settings.json","keyBinds","exit");
        }
        if(Objects.equals(advance, "null")){
            advance = JsonFile.read("settings.json","keyBinds","continue");
        }

        GridBagConstraints gbcQuestion = new GridBagConstraints();
        gbcQuestion.gridx = 0;
        gbcQuestion.gridy = 0;
        if(question.length() > 50){
            gbcQuestion.fill = GridBagConstraints.BOTH;
        }else{
            gbcQuestion.fill = GridBagConstraints.CENTER;
        }
        gbcQuestion.weightx = 1.0;
        gbcQuestion.weighty = 1.0;

        GridBagConstraints gbcAnswer = new GridBagConstraints();
        gbcAnswer.gridx = 0;
        gbcAnswer.gridy = 0;
        gbcAnswer.fill = GridBagConstraints.CENTER;
        gbcAnswer.weightx = 1.0;
        gbcAnswer.weighty = 1.0;

        headerColor = stringToColor(fileName, "header_color");
        Color backgroundColor = stringToColor(fileName, "background_color");
        textColor = stringToColor(fileName, "font_color");

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(backgroundColor);

        String fontName = JsonFile.read(fileName, "data", "font_name");
        int fontSize = Integer.parseInt(JsonFile.read(fileName, "data", "card_font_size"));
        int fontType = switch (JsonFile.read(fileName, "data", "font_size")) {
            case "Font.BOLD" -> 1;
            case "Font.ITALIC" -> 2;
            default -> 0;
        };
        Font font = new Font(fontName, fontType, fontSize);

        JLabel questionText = new JLabel("<html><body>" + question + "</body></html>");

        questionPanel = createPanel(questionText, gbcQuestion, 0, font, 255);
        questionPanel.setBackground(Color.red);

        answerText = new JLabel(answer);
        JPanel answerPanel = createPanel(answerText, gbcAnswer, - 100, font, 0);


        lineBreakText = new JLabel("---------------------------------------------------------");
        lineBreakText.setFont(new Font(JsonFile.read(fileName, "data", "font_name"), Font.ITALIC, 2));

        mainPanel.add(questionPanel);
        mainPanel.add(createPanel(lineBreakText, gbcAnswer, 0, font, 0));
        mainPanel.add(answerPanel);

        add(headerPanel(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private JPanel createPanel(JLabel label, GridBagConstraints gbc, int posMod, Font font, int alpha) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        int x = (getWidth() - 1200) / 2;
        int y = (getHeight() - 400) / 2;

        panel.setBounds(x, y - posMod, 1200, 400);
        //panel.setOpaque(false);
        panel.setBackground(Color.red);

        label.setFont(font);
        label.setForeground(new Color(textColor.getRed(),textColor.getGreen(),textColor.getBlue(),alpha));

        panel.add(label, gbc);

        return panel;
    }
    @SuppressWarnings("MagicConstant")
    private JPanel headerPanel(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(headerColor);

        Color fontColor = stringToColor(fileName, "header_font_color");
        String fontName = JsonFile.read(fileName, "data","font_name");
        int fontSize = Integer.parseInt(JsonFile.read(fileName,"data","header_font_size"));
        int fontType = switch (JsonFile.read(fileName, "data", "header_font_type")) {
            case "Font.BOLD" -> 1;
            case "Font.ITALIC" -> 2;
            default -> 0;
        };
        Font font = new Font(fontName, fontType, fontSize);

        JLabel closeText = new JLabel("Continue");
        closeText.setForeground(fontColor);
        closeText.setFont(font);

        JLabel title = new JLabel(getTitle());
        title.setForeground(fontColor);
        title.setFont(new Font(fontName, Font.BOLD, fontSize));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        titlePanel.add(title, gbc);

        JLabel reveal = new JLabel("Reveal Correct Answer");
        reveal.setForeground(fontColor);
        reveal.setFont(font);

        panel.add(createHeaderPanel(closeText, createHeaderButton("exit", font)));

        panel.add(titlePanel);

        panel.add(createHeaderPanel(reveal, createHeaderButton("continue", font)));

        return panel;
    }
    private JPanel createHeaderPanel(JLabel label, JButton button){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.add(label);
        panel.add(button);
        return panel;
    }
    private JButton createHeaderButton(String text, Font font){
        JButton button = new JButton(parseKeyStroke(JsonFile.read("settings.json","keyBinds",text)));
        button.setFocusable(false);
        button.setFont(font);

        return button;
    }
    private String parseKeyStroke(String keyStrokeCode){
        return switch (keyStrokeCode){
            case "0x1B" -> "Esc";
            case "0x20" -> "Space";
            case "\n" -> "Enter";
            case "0x12" -> "Alt";
            case "\b" -> "Backspace";
            default -> keyStrokeCode;
        };
    }
    private String parseKeyStrokeInput(String keyStrokeCode){
        return switch (keyStrokeCode){
            case "0x1B" -> "\u001B";
            case "0x20" -> " ";
            case "\n" -> "\n";
            case "0x12" -> "0x12";
            default -> "null";
        };
    }
    private void movePanel(JPanel panel, int amount) {
        int moveAmount = panel.getY() - amount;
        exit = true;
        Timer timer = new Timer(1, e -> {
            if (panel.getY() > moveAmount) {
                panel.setBounds(panel.getX(), panel.getY() - 10, panel.getWidth(), panel.getHeight()); // Decrease Y position to move up
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer when the movement is completed
                fadeIn(answerText, lineBreakText, textColor);
            }
        });

        timer.start();
    }
    private void fadeIn(JLabel answerLabel, JLabel lineBreak, Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

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
                answerLabel.setForeground(new Color(r, g, b, Math.round(alpha * 255)));
                lineBreak.setForeground(new Color(r, g, b, Math.round(alpha * 255)));
            }
        });

        fadeInTimer.start();
    }

    private Color stringToColor(String fileName, String color){
        String initColor = JsonFile.read(fileName, "data",color);
        String[] split = initColor.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("Key pressed: " + e.getKeyChar() + " Key expected: " + esc);
        if(String.valueOf(e.getKeyChar()).equals(advance)) {
            if(!exit) {
                if (question.length() > 100) {
                    movePanel(questionPanel, 150);
                } else {
                    movePanel(questionPanel, 100);
                }
            }else{
                dispose();
            }
        }else if(String.valueOf(e.getKeyChar()).equals(esc)){
            dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
