package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.Print;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("ExtractMethodRecommender") //shut the fuck up
public class Settings extends JPanel{
    private boolean changedSettings = false;
    private final String[] buttonOptions = {"Yes","No","Save"};
    private static JButton exitKeyBindButton = null;
    private static JButton continueKeyBindButton = null;
    private static JButton fullScreenKeyBindButton = null;
    private JPanel menuPanel;
    private final JButton playAudio;
    public Settings(MainMenu menuPanel){
        this.menuPanel = menuPanel;
        setLayout(new BorderLayout());
        SystemUI.set();

        JPanel title = new JPanel(new FlowLayout());
        title.setSize(new Dimension());

        JLabel settingsTitle = new JLabel("Settings");
        settingsTitle.setFont(new Font("Arial", Font.PLAIN,25));

        title.add(settingsTitle);

        //Main body
        JPanel mainPanel = new JPanel(new GridLayout(0,1,10,10));

        exitKeyBindButton = createKeyBindButton("exit");
        JPanel exitPanel = createKeyBindPanel("Exit",exitKeyBindButton);
        exitKeyBindButton.addActionListener(keyBindButtonListener("Exit"));

        continueKeyBindButton = createKeyBindButton("continue");
        JPanel continuePanel = createKeyBindPanel("Continue", continueKeyBindButton);
        continueKeyBindButton.addActionListener(keyBindButtonListener("Continue"));

        fullScreenKeyBindButton = createKeyBindButton("fullscreen");
        JPanel fullscreenPanel = createKeyBindPanel("Fullscreen", fullScreenKeyBindButton);
        fullScreenKeyBindButton.addActionListener(keyBindButtonListener("Fullscreen"));

        mainPanel.add(createHeaderText("Key Binds"));
        mainPanel.add(exitPanel);
        mainPanel.add(continuePanel);
        mainPanel.add(fullscreenPanel);

        playAudio = createCheckbox("play_audio");
        JPanel playAudioPanel = createCheckboxPanel("Play Audio", playAudio);
        playAudio.addActionListener(checkBoxToggle(playAudio));

        mainPanel.add(createHeaderText("Audio"));
        mainPanel.add(playAudioPanel);

        JScrollPane mainPane = new JScrollPane(mainPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton save = new JButton("Save Settings");
        save.setFocusable(false);
        save.setFont(new Font("Arial", Font.PLAIN, 20));
        save.addActionListener(e -> save());

        JButton exit = new JButton("Close");
        exit.setFocusable(false);
        exit.setFont(new Font("Arial", Font.PLAIN, 20));
        exit.addActionListener(e -> {
            if(changedSettings){
                int input = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure you want to exit without saving?",
                        "Settings have been changed",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        buttonOptions,
                        buttonOptions[0]);
                switch(input){
                    case 0:
                        exitSettings();
                        break;
                    case 2:
                        save();
                        exitSettings();
                        break;
                }
            }else {
                exitSettings();
            }
        });

        bottomPanel.add(save);
        bottomPanel.add(exit);

        add(title, BorderLayout.NORTH);
        add(mainPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    private JPanel createHeaderText(String text){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.setPreferredSize(new Dimension(0,0));

        JLabel label = new JLabel("<html>" + text + "<br>\n-----------------------------</html>");
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        panel.add(label);
        return panel;
    }
    private JPanel createKeyBindPanel(String text, JButton button) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setBackground(Color.lightGray);
        panel.setPreferredSize(new Dimension(0, 50));

        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setBackground(Color.lightGray);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 25));

        labelPanel.add(label, gbc);

        JPanel keyBindPanel = new JPanel(new GridBagLayout());
        keyBindPanel.setBackground(Color.lightGray);

        keyBindPanel.add(button, gbc);

        panel.add(labelPanel);
        panel.add(keyBindPanel);
        return panel;
    }
    private JButton createKeyBindButton(String keyBind){
        String rawKeyBind = JsonFile.read("settings.json", "keyBinds", keyBind);
        String keyBindText = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);

        JButton keyBindButton = new JButton(keyBindText);
        keyBindButton.setFont(new Font("Arial", Font.PLAIN, 15));
        keyBindButton.setPreferredSize(new Dimension(100, 45));
        keyBindButton.setFocusable(false);
        return keyBindButton;
    }
    private ActionListener keyBindButtonListener(String keyBind){
        return e -> {
            changedSettings = true;
            setKeyBindButtons(false);

            JButton button = (JButton) e.getSource();

            new keyBindSet(button, keyBind);
        };
    }
    @SuppressWarnings("SameParameterValue")
    private JPanel createCheckboxPanel(String text, JButton checkBox){
        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.setBackground(Color.lightGray);
        panel.setPreferredSize(new Dimension(0,50));

        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setBackground(Color.lightGray);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        Font font = new Font("Arial", Font.PLAIN, 25);

        JLabel label = new JLabel(text);
        label.setFont(font);

        labelPanel.add(label, gbc);

        JPanel keyBindPanel = new JPanel(new GridBagLayout());
        keyBindPanel.setBackground(Color.lightGray);

        keyBindPanel.add(checkBox, gbc);

        panel.add(labelPanel);
        panel.add(keyBindPanel);
        return panel;
    }
    @SuppressWarnings("SameParameterValue")
    private JButton createCheckbox(String setting){ //todo: replace png system in favor of custom drawn JPanels
        CheckBoxToggle checkBox = new CheckBoxToggle(Boolean.parseBoolean(JsonFile.read("settings.json","general", setting)));
        //checkBox.setBackground(Color.lightGray);

        return checkBox;
    }
    private static void setKeyBindButtons(boolean enable){
        exitKeyBindButton.setEnabled(enable);
        continueKeyBindButton.setEnabled(enable);
        fullScreenKeyBindButton.setEnabled(enable);
    }
    private ActionListener checkBoxToggle(JButton checkBox){
        return e -> {
            changedSettings = true;
            if(checkBox.isSelected()){
                //checkBox.setIcon(new IconResize("src/main/resources/assets/textures/check_mark.png", 25,25).getImage());
            }else{
                //checkBox.setIcon(new IconResize("src/main/resources/assets/textures/cross_mark.png",25,25).getImage());
            }
        };
    }
    private void save(){
        changedSettings = false;
        //Key Binds
        JsonFile.writeln("settings.json","keyBinds","exit",exitKeyBindButton.getText());
        JsonFile.writeln("settings.json","keyBinds","continue",continueKeyBindButton.getText());
        JsonFile.writeln("settings.json","keyBinds","fullScreen",fullScreenKeyBindButton.getText());

        //Check Boxes
        JsonFile.writeln("settings.json","general","play_audio", String.valueOf(playAudio.isSelected()));
    }
    private void exitSettings(){
        Container parent = getParent();
        parent.remove(this);

        parent.add(menuPanel);

        parent.revalidate();
        parent.repaint();
    }
    private static class keyBindSet extends JFrame implements KeyListener {
        private final JLabel keyBindText;
        public keyBindSet(JButton button, String keyBind){
            setTitle("Press any key...");
            setUndecorated(true);
            setSize(300,200);
            addKeyListener(this);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = 0;

            JPanel currentKey = new JPanel(new GridBagLayout());
            currentKey.setBackground(Color.lightGray);

            JLabel currentKeyText = new JLabel("Key bind for \"" + keyBind + "\"");
            currentKeyText.setFont(new Font("Arial",Font.PLAIN,25));

            currentKey.add(currentKeyText, gbc);

            JPanel keyBindPanel = new JPanel(new GridBagLayout());
            keyBindPanel.setBackground(Color.gray);

            keyBindText = new JLabel("Press any Key...");
            keyBindText.setFont(new Font("Arial",Font.PLAIN,25));

            keyBindPanel.add(keyBindText, gbc);

            JPanel buttonsPanel = new JPanel(new FlowLayout());
            buttonsPanel.setBackground(Color.lightGray);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> {
                dispose();
                setKeyBindButtons(true);
            });
            cancelButton.setFocusable(false);

            JButton saveButton = new JButton("Save");
            saveButton.setFocusable(false);
            saveButton.addActionListener(s -> {
                button.setText(keyBindText.getText());
                dispose();
                setKeyBindButtons(true);
            });

            buttonsPanel.add(cancelButton);
            buttonsPanel.add(saveButton);

            add(currentKey, BorderLayout.NORTH);
            add(keyBindPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.SOUTH);
            setVisible(true);
        }
        private String toUpperCase(String text){
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        @Override
        public void keyTyped(KeyEvent e) {
            switch(e.getKeyChar()){
                case 8:
                    keyBindText.setText("Backspace");
                    break;
                case 10:
                    keyBindText.setText("Enter");
                    break;
                case 27:
                    keyBindText.setText("Esc");
                    break;
                case 32:
                    keyBindText.setText("Space");
                    break;
                case 127:
                    keyBindText.setText("Delete");
                    break;
                default:
                    keyBindText.setText(toUpperCase(String.valueOf(e.getKeyChar())));
            }
        }
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }
    private static class CheckBoxToggle extends JButton{
        private boolean isEnabled;
        public CheckBoxToggle(boolean isEnabled){
            this.isEnabled = isEnabled;
        }
        public void toggleEnabled(){
            isEnabled = !isEnabled;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Cast to Graphics2D for better graphics capabilities
            Graphics2D g2d = (Graphics2D) g;

            // Enable antialiasing for smoother lines and shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int centerX = width / 2;
            int centerY = height / 2;

            int lineThickness = 1; // Thickness of the lines

            int lineLength = Math.min(width, height) / 3; // Length of the lines and diameter of the circle

            // Set the stroke for drawing lines
            g2d.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Draw the circle with red color
            g2d.setColor(Color.red);
            int circleDiameter = lineLength * 2;
            int circleX = centerX - lineLength;
            int circleY = centerY - lineLength;
            g2d.fillOval(circleX, circleY, circleDiameter, circleDiameter);

            // Draw the vertical and horizontal lines with cyan color
            g2d.setColor(Color.cyan);
            g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength);
            g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);

        }
        @Override
        protected void paintBorder(Graphics g) {
            // Do not paint the border
        }

        @Override
        protected void paintChildren(Graphics g) {
            // Do not paint children components if any
        }
    }
}