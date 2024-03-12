package com.boneless;

import com.boneless.util.FontUtility;
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
    private JPanel questionPanel;
    private JLabel answerText;
    private JLabel lineBreakText;
    private final JButton actButton;
    private String esc = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","exit")));
    private String advance = String.valueOf(parseKeyStrokeInput(JsonFile.read("settings.json","keyBinds","continue")));
    //font values
    //header background
    private final Color headerFontColor;
    //main card text
    private final Color textFontColor;
    //background
    private final Color headerBackgroundColor;
    private final Color backgroundColor;

    public InfoCard(String question, String answer, String fileName, String category, int points, boolean doFullScreen, JButton actButton){
        this.question = question;
        this.answer = answer;
        this.fileName = fileName;
        this.actButton = actButton;
        //setup fonts
        //header
        headerFontColor = parseColor("header_background_color");
        headerBackgroundColor = parseColor("header_background_color");
        //header buttons
        Color headerButtonFontColor = parseColor("header_button_font_color");
        Color headerButtonColor = parseColor("header_background_color");
        //main body
        textFontColor = parseColor("text_font_color");
        backgroundColor = parseColor("background_color");

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

        //scale the infoCard up and move it to stay centered??

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(backgroundColor);

        //JTextArea questionText = new JTextArea(question);
        JLabel questionText = new JLabel(question);
        //questionText.set

        //questionPanel = createPanel(new JLabel(questionText.getText()), gbcQuestion, 0); //alpha 255
        questionPanel = createPanel(questionText, 0);

        answerText = new JLabel(answer);
        JPanel answerPanel = createPanel(answerText, -100);

        lineBreakText = new JLabel("------------------------------------------------------------"); //60 chars :3
        lineBreakText.setFont(testFont(mainPanel,lineBreakText,"text"));

        mainPanel.add(questionPanel);
        mainPanel.add(createPanel(lineBreakText, 0));
        mainPanel.add(answerPanel);

        add(headerPanel(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    @SuppressWarnings("MagicConstant")
    private Font testFont(JComponent parent, JComponent item, String type) {
        // Assuming JsonFile.read() reads font data from a file
        String fontName = JsonFile.read(fileName, "data", type + "_font");
        int fontType = parseFontType(type);
        int textLength = (Objects.equals(getJComponentType(item), "label")) ? ((JLabel) item).getText().length() : ((JButton) item).getText().length();

        textLength = (textLength <= 20) ? 100 : 60;

        return new Font(fontName, fontType, (int)(((parent.getHeight() + item.getHeight()) + ((parent.getWidth() + item.getWidth())))));
    }
    private JPanel createPanel(JLabel label, int posMod) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        int x = (getWidth() - 1200) / 2;
        int y = (getHeight() - 400) / 2;

        panel.setBounds(x, y - posMod, 1200, 400);
        panel.setBackground(Color.red);

        // Set the maximum width of the label to the width of the panel
        label.setMaximumSize(new Dimension(panel.getWidth(), Integer.MAX_VALUE));
        label.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
        //label.setFont(FontUtility.getScaledFont(label.getText(), Font.PLAIN, panel));
        label.setForeground(textFontColor);

        //System.out.println(calcLineBreak(Objects.equals(getJComponentType(label), "label") ? ((JLabel) label).getText() : ((JTextArea) label).getText(), label.getFont().getSize(), panel.getWidth()));
        panel.add(label);

        return panel;
    }
    private String getJComponentType(JComponent object){
        if(object instanceof JLabel){
            return "label";
        }else if(object instanceof JTextArea){
            return "text";
        }else if(object instanceof JButton){
            return "button";
        }else{
            System.err.println("Invalid JComponent Type!");
            return null;
        }
    }
    private String calcLineBreak(String text, int fontSize, int parentWidth){
        //get length, and font size, and do some math with the width of the parent panel
        StringBuilder builder = new StringBuilder();
        int currentLineLength = 0;
        String[] wordArray = text.split(" ");

        for(String word: wordArray){
            int wordWidth = word.length() * fontSize;

            if(currentLineLength + wordWidth > parentWidth){
                builder.append("\n");
                currentLineLength = 0;
            }
            builder.append(word).append(" ");
            currentLineLength += wordWidth + fontSize;
        }
        return builder.toString().trim();
    }
    //todo: remove parseFont in favor of new font system
    @SuppressWarnings("MagicConstant")
    private Font parseFont(JComponent parent, String type){
        String fontName = JsonFile.read(fileName,"data",type + "_font");
        return new Font(fontName, parseFontType(type + "_font_type"),parent.getHeight() / 2);
    }
    private JPanel headerPanel(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(headerBackgroundColor);

        JLabel closeText = new JLabel("Continue");
        closeText.setForeground(headerFontColor);
        closeText.setFont(parseFont(panel,"header"));

        JLabel title = new JLabel(getTitle());
        title.setForeground(headerFontColor);
        title.setFont(parseFont(panel,"header"));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        titlePanel.add(title, gbc);

        JLabel reveal = new JLabel("Reveal Correct Answer");
        reveal.setForeground(headerFontColor);
        reveal.setFont(parseFont(panel,"header"));

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
        button.setFont(parseFont(button,"header_button"));
        button.addActionListener(e -> {
            if(type && !exit) {
                if (question.length() > 100) {
                    movePanel(questionPanel, 150);
                } else {
                    movePanel(questionPanel, 100);
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
    private void movePanel(JPanel panel, int amount) {
        int moveAmount = panel.getY() - amount;
        exit = true;
        Timer timer = new Timer(1, e -> {
            if (panel.getY() > moveAmount) {
                panel.setBounds(panel.getX(), panel.getY() - 10, panel.getWidth(), panel.getHeight()); // Decrease Y position to move up
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer when the movement is completed
                fadeIn(answerText, lineBreakText, textFontColor);
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
                Color fg = new Color(r, g, b, Math.round(alpha * 255));
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
                    movePanel(questionPanel, 150);
                } else {
                    movePanel(questionPanel, 100);
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
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}