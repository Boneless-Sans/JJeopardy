package com.boneless;

import com.boneless.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class Settings extends JPanel {
    private final Color mainColor;
    private final Color accentColor;
    private final Color fontColor;

    private final HashMap<String, JRoundedButton> keyBindButtonList = new HashMap<>();;
    private final HashMap<String, ButtonIcon> toggleButtonList = new HashMap<>();
    private final HashMap<String, JComboBox<String>> dropDownList = new HashMap<>();

    private final JPopUp popup;

    public boolean settingsIsActive;

    private int stackHeight = 0;

    private boolean changesMade = false;

    public Settings(Container parent) {
        settingsIsActive = true;

        setLayout(null);

        if(fileName == null){
            mainColor = new Color(20,20,255);
            fontColor = new Color(255,255,255);
        } else {
            mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
            fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
        }
        accentColor = new Color(clamp(mainColor.getRed() - 50), clamp(mainColor.getGreen() - 50), clamp(mainColor.getBlue() - 50));

        add(popup = new JPopUp(parent));

        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBounds(0,0, frameWidth, frameHeight);
        masterPanel.setOpaque(false);

        masterPanel.add(header(), BorderLayout.NORTH);
        masterPanel.add(footer(), BorderLayout.SOUTH);
        masterPanel.add(mainBody(), BorderLayout.CENTER);

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

                g2d.setPaint(new GradientPaint(0,0, mainColor, getWidth(), getHeight(), accentColor));
                g2d.fillRect(0,0,frameWidth, stackHeight + 100);
            }
        };

        JRoundedButton saveBind = new JRoundedButton("Save");
        saveBind.addActionListener(e -> popup.hidePopUp());

        JRoundedButton exitBindButton = new JRoundedButton(firstUpperCase(JsonFile.read(settingsFile, "key_binds", "exit")), Color.gray, Color.white);
        exitBindButton.addActionListener(e -> popup.showPopUp("Exit Bind", "", exitBindButton, JPopUp.BUTTON_INPUT, createCancelBindButton(exitBindButton), saveBind));
        exitBindButton.setPreferredSize(new Dimension(100,50));
        exitBindButton.setFont(generateFont(20));

        JRoundedButton advanceBindButton = new JRoundedButton(firstUpperCase(JsonFile.read(settingsFile, "key_binds", "advance")), Color.gray, Color.white);
        advanceBindButton.addActionListener(e -> popup.showPopUp("Exit Bind", "", advanceBindButton, JPopUp.BUTTON_INPUT, createCancelBindButton(advanceBindButton), saveBind));
        advanceBindButton.setPreferredSize(new Dimension(100,50));
        advanceBindButton.setFont(generateFont(20));

        String[] resolutions = {
                "placeholder",
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

        for (int i = 0; i < resolutions.length; i++) { //figure out the native, then cut off the rest
            if (resolutions[i].contains(String.valueOf(screenWidth))) {
                resolutions[i] = resolutions[i] + " (Native)";

                String[] newArray = new String[resolutions.length - (resolutions.length - i) + 1];

                for (int k = 0, j = 0; k < newArray.length; k++) {
                    newArray[j++] = resolutions[k];
                }

                resolutions = newArray;

                break;
            }
        }

        resolutions[0] = frameWidth + "x" + frameHeight + " (Current)";

        JComboBox<String> resSizeDropDown = new JComboBox<>(resolutions);
        resSizeDropDown.addActionListener(e -> changesMade = true);
        resSizeDropDown.setFont(generateFont(20));

        panel.add(sectionLabel("Key Binds"));
        panel.add(createKeyBindPanel("Exit", "exit", exitBindButton));
        panel.add(createKeyBindPanel("Advance", "advance", advanceBindButton));

        panel.add(divider());
        panel.add(sectionLabel("Display"));
        panel.add(createDropDownPanel("Screen Size", "screen_resolution", resSizeDropDown));
        panel.add(createTogglePanel("Always On Top", "screen", "always_on_top"));
        panel.add(createTogglePanel("Full screen", "screen", "fullscreen"));

        panel.add(divider());
        panel.add(sectionLabel("Misc"));
        panel.add(createTogglePanel("Play Menu Animation", "misc", "play_background_ani"));
        panel.add(createTogglePanel("Reduce Animations", "misc", "reduce_animations"));
        panel.add(createTogglePanel("Play Question Card Animations", "misc", "play_card_animations"));
        panel.add(createTogglePanel("Play Audio", "misc", "audio"));

        panel.setPreferredSize(new Dimension(frameWidth, stackHeight + 100));

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
        panel.setPreferredSize(new Dimension(900, 70));
        panel.setOpaque(false);

        stackHeight += 70;

        return panel;
    }

    private JPanel sectionLabel(String text){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(frameWidth - frameWidth / 6,60));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(50));
        label.setForeground(fontColor);

        panel.add(label);

        stackHeight += 60;

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

        stackHeight += 100;

        return panel;
    }
    private JPanel createKeyBindPanel(String text, String key, JRoundedButton button){
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        rightPanel.add(button, rightGBC);

        button.addActionListener(e -> changesMade = true);

        keyBindButtonList.put(key, button);

        return createSettingsItem(text, rightPanel);
    }

    private JPanel createTogglePanel(String text, String parentKey, String key){
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        ButtonIcon button = new ButtonIcon(64, Boolean.parseBoolean(JsonFile.read(settingsFile, parentKey, key)));
        button.addActionListener(e -> changesMade = true);

        rightPanel.add(button, rightGBC);

        toggleButtonList.put(key,button);

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
        changesMade = false;

        JsonFile.writeln(settingsFile, "key_binds", "exit", keyBindButtonList.get("exit").getText());
        JsonFile.writeln(settingsFile, "key_binds", "advance", keyBindButtonList.get("advance").getText());

        JsonFile.writeln(settingsFile, "screen", "screen_resolution", (String) dropDownList.get("screen_resolution").getSelectedItem());
        JsonFile.writeln(settingsFile, "screen", "always_on_top", getStringBoolean(toggleButtonList.get("always_on_top").isChecked()));
        JsonFile.writeln(settingsFile, "screen", "fullscreen",getStringBoolean(toggleButtonList.get("fullscreen").isChecked()));
        JsonFile.writeln(settingsFile, "screen", "reduce_animations", getStringBoolean(toggleButtonList.get("reduce_animations").isChecked()));
        JsonFile.writeln(settingsFile, "screen", "play_card_animations",getStringBoolean(toggleButtonList.get("play_card_animations").isChecked()));

        JsonFile.writeln(settingsFile, "misc", "disable_background_scroll", getStringBoolean(toggleButtonList.get("disable_background_scroll").isChecked()));
        JsonFile.writeln(settingsFile, "misc","audio",getStringBoolean(toggleButtonList.get("audio").isChecked()));
    }

    private String getStringBoolean(boolean bool){
        return bool ? "true" : "false";
    }

    public void exit(boolean... skipCheck){
        boolean doSkip = skipCheck.length > 0;

        if(changesMade && !doSkip) {
            changesMade = false;

            JRoundedButton cancel = new JRoundedButton("Continue");
            cancel.addActionListener(e -> popup.hidePopUp());

            JRoundedButton exitSave = new JRoundedButton("Exit and Save");
            exitSave.addActionListener(e -> {
                save();
                exit(true);
            });

            JRoundedButton exitNoSave = new JRoundedButton("Exit Without Saving");
            exitNoSave.addActionListener(e -> exit(true));

            popup.showPopUp("Changes Made", "Do you wish to exit without saving?", null, JPopUp.MESSAGE, cancel, exitSave, exitNoSave);
        } else {
            changeCurrentPanel(mainMenu, this, false);
            mainMenu.timer.start();
        }
    }

    private static class SettingsOptionPanel extends JPanel {
        public SettingsOptionPanel(){
            setPreferredSize(new Dimension(frameWidth - frameWidth / 6, 100));
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