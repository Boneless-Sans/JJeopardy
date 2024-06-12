package com.boneless;

import com.boneless.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class Settings extends JPanel {
    /* slide bottom to middle
    -General | X
        -Null Safety | √
            -Main File | √
            -Settings | √
        -Exit Confirmation | X
    -Main Body | X
        -JScrollPane | √
        -Rounded Boxes for Key Binds | √
            -Key Bind Text | √
            -Key Bind Setter | √
        -Rounded Boxes for Toggles | √
            -Item Name | √
            -State | √
    -Footer | √
        -Save Button | √
        -Exit Button | √
    -Settings | X
        -Exit | X
        -Advance | √
        -Play Audio, could be moved to start game | X
        -Screen size dropdown | X
        -Always on top | X
        -Scroll Bar Sensitivity | X
     */
    private final Color mainColor;
    private final Color accentColor;
    private final Color fontColor;

    private final HashMap<String, JRoundedButton> keyBindButtonList = new HashMap<>();;
    private final HashMap<String, ButtonIcon> toggleButtonList = new HashMap<>();
    private final HashMap<String, JComboBox<String>> dropDownList = new HashMap<>();

    private final JPopUp popup;

    private boolean changesMade = false;

    public Settings() {
        setLayout(null);

        if(fileName == null){
            mainColor = new Color(20,20,255);
            fontColor = new Color(255,255,255);
        } else {
            mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
            fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
        }
        accentColor = new Color(clamp(mainColor.getRed() - 40), clamp(mainColor.getGreen() - 40), clamp(mainColor.getBlue() - 40));

        add(popup = new JPopUp(this, 500,300));

        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBounds(0,0, frameWidth, frameHeight);
        masterPanel.setOpaque(false);

        masterPanel.add(header(), BorderLayout.NORTH);
        masterPanel.add(mainBody(), BorderLayout.CENTER);
        masterPanel.add(footer(), BorderLayout.SOUTH);

        add(masterPanel);
    }

    private JPanel header(){
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(accentColor);

        JLabel label = new JLabel("Settings");
        label.setForeground(fontColor);
        label.setFont(generateFont(20));

        header.add(label);

        return header;
    }

    private HiddenScroller mainBody(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setPaint(new GradientPaint(0,0,mainColor,getWidth(),getHeight(),accentColor));
                g2d.fillRect(0,0,getWidth(),getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(frameWidth, frameHeight + 300));

        JRoundedButton saveBind = new JRoundedButton("Save");
        saveBind.addActionListener(e -> popup.hidePopUp());

        JRoundedButton exitBindButton = new JRoundedButton(firstUpperCase(JsonFile.read(settingsFile, "key_binds", "exit")), Color.lightGray, Color.white);
        exitBindButton.addActionListener(e -> popup.showPopUp("Exit Bind", "", exitBindButton, JPopUp.BUTTON_INPUT, createCancelBindButton(exitBindButton), saveBind));

        JRoundedButton advanceBindButton = new JRoundedButton(firstUpperCase(JsonFile.read(settingsFile, "key_binds", "advance")), Color.lightGray, Color.white);
        advanceBindButton.addActionListener(e -> popup.showPopUp("Exit Bind", "", advanceBindButton, JPopUp.BUTTON_INPUT, createCancelBindButton(advanceBindButton), saveBind));

        String[] resolutions = {
                "640x360",
                "854x480",
                "1280x720",
                "1366x768",
                "1600x900 (Default)",
                "1680x1050",
                "1920x1080",
                "2048x1152",
                "2560x1440",
                "2880x1620",
                "3200x1800",
                "3440x1440",
                "3840x2160",
                "4096x2304",
                "5120x2880",
                "6016x3384"
        };

        for(String resolution : resolutions){
            resolution = resolution.contains("(") ? resolution.split(",")[0] : resolution;
            boolean nativeCheck = (Integer.parseInt(resolution.split("x")[0]) == screenWidth);
            System.out.println(resolution + " " + nativeCheck + " " + screenWidth);
        }

        JComboBox<String> resSizeDropDown = new JComboBox<>(resolutions);

        panel.add(sectionLabel("Key Binds"));
        panel.add(createKeyBindPanel("Exit", "exit", exitBindButton));
        panel.add(createKeyBindPanel("Advance", "advance", advanceBindButton));

        panel.add(divider());
        panel.add(sectionLabel("Screen"));
        panel.add(createDropDownPanel("Screen Size", "screen_resolution", resSizeDropDown));
        panel.add(createTogglePanel("Always On Top", "always_on_top"));

        panel.add(divider());
        panel.add(sectionLabel("Misc"));
        panel.add(createTogglePanel("Fullscreen", "fullscreen"));
        panel.add(createTogglePanel("Play Audio", "audio"));

        HiddenScroller scroller = new HiddenScroller(panel, false);
        scroller.setBackground(mainColor);
        scroller.getVerticalScrollBar().setUnitIncrement(15);

        return scroller;
    }

    private JRoundedButton createCancelBindButton(JButton source){ //>:(
        String originalBind = source.getText();
        JRoundedButton button = new JRoundedButton("Cancel");
        button.addActionListener(e -> {
            popup.hidePopUp();
            source.setText(originalBind);
        });

        return button;
    }

    private JPanel divider(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 70));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel sectionLabel(String text){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(700,60));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(50));
        label.setForeground(fontColor);

        panel.add(label);

        return panel;
    }

    private final GridBagConstraints leftGBC = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        weightx = 1;
        weighty = 1;
        anchor = GridBagConstraints.WEST;
        insets = new Insets(0,50,0,0);
    }};

    private final GridBagConstraints rightGBC = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        weightx = 1;
        weighty = 1;
        anchor = GridBagConstraints.EAST;
        insets = new Insets(0,0,0,50);
    }};

    private SettingsOptionPanel createSettingsItem(String text, JComponent component){
        SettingsOptionPanel panel = new SettingsOptionPanel();

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);

        JLabel itemText = new JLabel(text);
        itemText.setFont(generateFont(40));

        leftPanel.add(itemText, leftGBC);

        panel.add(leftPanel);
        panel.add(component);

        return panel;
    }
    private JPanel createKeyBindPanel(String text, String key, JRoundedButton button){
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        rightPanel.add(button, rightGBC);

        keyBindButtonList.put(key, button);

        return createSettingsItem(text, rightPanel);
    }

    private JPanel createTogglePanel(String text, String key){
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        ButtonIcon button = new ButtonIcon(64, Boolean.parseBoolean(JsonFile.read(settingsFile, "misc", key)));
        rightPanel.add(button, rightGBC);

        toggleButtonList.put("key",button);

        return createSettingsItem(text, rightPanel);
    }

    private JPanel createDropDownPanel(String text, String key, JComboBox<String> sourceBox){
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        rightPanel.add(sourceBox, rightGBC);

        dropDownList.put(key, sourceBox);

        return createSettingsItem(text, rightPanel);
    }

    private JPanel footer(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(accentColor);

        JRoundedButton saveButton = new JRoundedButton("Save");
        saveButton.addActionListener(e -> save());

        JRoundedButton exitButton = new JRoundedButton("Exit");
        exitButton.addActionListener(e -> exit());

        panel.add(exitButton);
        panel.add(saveButton);
        return panel;
    }

    private String getKeyBindFor(String id){
        String result = JsonFile.read(settingsFile, "key_binds",id.toLowerCase());
        if(result.toLowerCase().contains("invalid")){
            return null;
        } else {
            return result;
        }
    }

    private void save(){
        System.out.println("Saving Settings...");

        JsonFile.writeln(settingsFile, "key_binds", "exit", keyBindButtonList.get("exit").getText());
        JsonFile.writeln(settingsFile, "key_binds", "advance", keyBindButtonList.get("advance").getText());

        JsonFile.writeln(settingsFile, "misc", "screen_resolution", (String) dropDownList.get("screen_resolution").getSelectedItem());
        JsonFile.writeln(settingsFile, "misc", "always_on_top", getStringBoolean(toggleButtonList.get("always_on_top").isChecked()));

        JsonFile.writeln(settingsFile, "misc","fullscreen",getStringBoolean(toggleButtonList.get("fullscreen").isChecked()));
        JsonFile.writeln(settingsFile, "misc","audio",getStringBoolean(toggleButtonList.get("audio").isChecked()));
    }

    private String getStringBoolean(boolean bool){
        return bool ? "true" : "false";
    }

    private void exit(){
        if(changesMade) {
            JRoundedButton cancel = new JRoundedButton("Continue");
            cancel.addActionListener(e -> popup.hidePopUp());

            JRoundedButton exitNoSave = new JRoundedButton("Exit Without Saving");
            exitNoSave.addActionListener(e -> {
                changeCurrentPanel(mainMenu, this, false);
                mainMenu.timer.start();
            });

            JRoundedButton exitSave = new JRoundedButton("Exit and Save");
            exitSave.addActionListener(e -> {
                save();
                changeCurrentPanel(mainMenu, this, false);
                mainMenu.timer.start();
            });

            //popup.showPopUp("Changes Made", "Do you wish to exit without saving?", JPopUp.MESSAGE, null, cancel, exitSave, exitNoSave);
        } else {
            changeCurrentPanel(mainMenu, this, false);
            mainMenu.timer.start();
        }
    }

    private static class SettingsOptionPanel extends JPanel {
        public SettingsOptionPanel(){
            setPreferredSize(new Dimension(700, 100)); //todo: fix centering
            setLayout(new GridLayout(1,2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.white);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(),25,25);
        }

        @Override protected void paintBorder(Graphics g) {} //disable
    }
}
