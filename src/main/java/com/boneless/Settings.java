package com.boneless;

import com.boneless.util.IconResize;
import com.boneless.util.JsonFile;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.boneless.Launcher.*;

public class Settings extends JFrame{
    private boolean changedSettings = false;
    private final String[] buttonOptions = {"Yes","No","Save"};
    private static JButton exitKeyBindButton = null;
    private static JButton continueKeyBindButton = null;
    private static JButton fullscreenKeyBindButton = null;
    private JCheckBox playAudio;
    public Settings(){
        setSize(500,400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
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

        fullscreenKeyBindButton = createKeyBindButton("fullscreen");
        JPanel fullscreenPanel = createKeyBindPanel("Fullscreen", fullscreenKeyBindButton);
        fullscreenKeyBindButton.addActionListener(keyBindButtonListener("Fullscreen"));

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
                        dispose();
                        changeButtonState(true);
                        break;
                    case 2:
                        save();
                        dispose();
                        changeButtonState(true);
                        break;
                }
            }else {
                changeButtonState(true);
                dispose();
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
    private JPanel createCheckboxPanel(String text, JCheckBox checkBox){
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
    private JCheckBox createCheckbox(String setting){
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(Color.lightGray);
        if(Boolean.parseBoolean(JsonFile.read("settings.json","general", setting))){
            checkBox.setIcon(new IconResize("src/main/resources/assets/textures/check_mark.png", 25,25).getImage());
        }else{
            checkBox.setIcon(new IconResize("src/main/resources/assets/textures/cross_mark.png",25,25).getImage());
        }

        return checkBox;
    }
    private static void setKeyBindButtons(boolean enable){
        exitKeyBindButton.setEnabled(enable);
        continueKeyBindButton.setEnabled(enable);
        fullscreenKeyBindButton.setEnabled(enable);
    }
    private ActionListener checkBoxToggle(JCheckBox checkBox){
        return e -> {
            changedSettings = true;
            if(checkBox.isSelected()){
                checkBox.setIcon(new IconResize("src/main/resources/assets/textures/check_mark.png", 25,25).getImage());
            }else{
                checkBox.setIcon(new IconResize("src/main/resources/assets/textures/cross_mark.png",25,25).getImage());
            }
        };
    }
    private void save(){
        changedSettings = false;
        //Key Binds
        JsonFile.writeln("settings.json","keyBinds","exit",exitKeyBindButton.getText());
        JsonFile.writeln("settings.json","keyBinds","continue",continueKeyBindButton.getText());
        JsonFile.writeln("settings.json","keyBinds","fullscreen",fullscreenKeyBindButton.getText());

        //Check Boxes
        JsonFile.writeln("settings.json","general","play_audio", String.valueOf(playAudio.isSelected()));
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
        @Override
        public void keyPressed(KeyEvent e) {

        }
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
