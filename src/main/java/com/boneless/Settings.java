package com.boneless;

import com.boneless.util.IconResize;
import com.boneless.util.JsonFile;
import com.boneless.util.SystemUI;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.boneless.Launcher.*;

public class Settings extends JFrame implements ActionListener {
    private boolean changedSettings = false;
    private final String[] buttonOptions = {"Yes","No","Save"};
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

        JTextField exitTextField = createTextField("exit");
        JPanel exitPanel = createTextFieldPanel("Exit",exitTextField);

        exitTextField.getDocument().addDocumentListener(documentListener(exitTextField));

        JTextField continueTextField = createTextField("continue");
        JPanel continuePanel = createTextFieldPanel("Continue", continueTextField);
        continueTextField.getDocument().addDocumentListener(documentListener(continueTextField));

        JTextField fullscreenTextField = createTextField("fullscreen");
        JPanel fullscreenPanel = createTextFieldPanel("Fullscreen", fullscreenTextField);
        fullscreenTextField.getDocument().addDocumentListener(documentListener(fullscreenTextField));

        mainPanel.add(createHeaderText("Key Binds"));
        mainPanel.add(exitPanel);
        mainPanel.add(continuePanel);
        mainPanel.add(fullscreenPanel);

        mainPanel.add(createHeaderText("Audio"));
        mainPanel.add(createCheckboxPanel("Play Audio", "play_audio"));

        JScrollPane mainPane = new JScrollPane(mainPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton save = new JButton("Save Settings");
        save.setFocusable(false);
        save.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton exit = new JButton("Close");
        exit.setFocusable(false);
        exit.setFont(new Font("Arial", Font.PLAIN, 20));
        exit.addActionListener(e -> {
            if(!changedSettings){
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
                        changeButtonState(true);
                        dispose();
                        break;
                    case 1:
                        break;
                    case 2:
                        //TODO implement saving
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
        //panel.setBackground(Color.red);

        JLabel label = new JLabel("<html>" + text + "<br>\n-----------------------------</html>");
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        panel.add(label);
        return panel;
    }
    private JPanel createTextFieldPanel(String text, JTextField textField) {
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

        keyBindPanel.add(textField, gbc);

        panel.add(labelPanel);
        panel.add(keyBindPanel);
        return panel;
    }
    private JTextField createTextField(String keyBind){
        String rawKeyBind = JsonFile.read("settings.json", "keyBinds", keyBind);
        String keyBindText = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);

        JTextField keyBindTextField = new JTextField(keyBindText);
        keyBindTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        keyBindTextField.setPreferredSize(new Dimension(100, 45));
        keyBindTextField.addActionListener(this::textFieldAction);

        return keyBindTextField;
    }
    private DocumentListener documentListener(JTextField textField){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
    }
    private void textFieldAction(ActionEvent e) {
        changedSettings = true;
        JTextField textField = (JTextField) e.getSource();
        String input = textField.getText();
        System.out.println(input);
    }

    private int getTextFieldIndex(String keyBindName) {
        // Map keyBindName to an index for the array
        // You may use a switch statement, a Map, or any other logic
        // to determine the index based on the keyBindName
        int index = 0;
        switch (keyBindName) {
            case "exit":
                index = 0;
                break;
            case "continue":
                index = 1;
                break;
            case "fullscreen":
                index = 2;
                break;
            // Add more cases as needed
        }
        return index;
    }
    private JPanel createCheckboxPanel(String text, String isChecked){
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

        JCheckBox checkBox = new JCheckBox();
        if(Boolean.parseBoolean(JsonFile.read("settings.json","general", isChecked))){
            checkBox.setIcon(new IconResize("src/main/resources/assets/textures/check_mark.png", 25,25).getImage());
        }else{
            checkBox.setIcon(new IconResize("src/main/resources/assets/textures/cross_mark.png",25,25).getImage());
        }
        checkBox.addActionListener(e -> {
            changedSettings = true;
            if(checkBox.isSelected()){
                checkBox.setIcon(new IconResize("src/main/resources/assets/textures/check_mark.png", 25,25).getImage());
            }else{
                checkBox.setIcon(new IconResize("src/main/resources/assets/textures/cross_mark.png",25,25).getImage());
            }
        });

        keyBindPanel.add(checkBox, gbc);

        panel.add(labelPanel);
        panel.add(keyBindPanel);
        return panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
