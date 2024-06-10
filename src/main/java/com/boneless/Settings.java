package com.boneless;

import com.boneless.util.ButtonIcon;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class Settings extends JPanel {
    /* slide bottom to middle
    -General | X
        -Null Safety | X
            -Main File | √
            -Settings | X
        -Exit Confirmation | X
    -Main Body | X
        -JScrollPane | √
        -Rounded Boxes for Key Binds | X
            -Key Bind Text | X
            -Key Bind Setter | X
        -Rounded Boxes for Toggles | X
            -Item Name | X
            -State | X
    -Footer | X
        -Save Button | X
        -Exit Button | √
    -Settings | X
        -Exit | X
        -Advance | X
        -Play Audio, could be moved to start game | X
        -Full Screen, requires restart | X
        -Scroll Bar Sensitivity | X
     */
    private final Color mainColor;
    private final Color accentColor;
    private final Color fontColor;

    private final ArrayList<JRoundedButton> keyBindButtonList = new ArrayList<>();
    private final ArrayList<ButtonIcon> toggleButtonList = new ArrayList<>();

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

        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBounds(0,0,screenWidth, screenHeight);
        masterPanel.setOpaque(false);

        masterPanel.add(header(), BorderLayout.NORTH);
        masterPanel.add(mainBody(), BorderLayout.CENTER);
        masterPanel.add(footer(), BorderLayout.SOUTH);

        add(masterPanel);

        save();
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setPaint(new GradientPaint(0,0,mainColor,getWidth(),getHeight(),accentColor));
                g2d.fillRect(0,0,getWidth(),getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(screenWidth, screenHeight));

        panel.add(sectionLabel("Key Binds"));
        panel.add(createKeyBindPanel("Exit"));
        panel.add(createKeyBindPanel("Advance"));

        panel.add(divider());
        panel.add(sectionLabel("Misc"));
        panel.add(createTogglePanel("Fullscreen"));

        HiddenScroller scroller = new HiddenScroller(panel, false);
        scroller.setBackground(mainColor);
        scroller.getVerticalScrollBar().setUnitIncrement(15);

        return scroller;
    }

    private JPanel divider(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 70));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel sectionLabel(String text){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(1100, 70));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(50));
        label.setForeground(fontColor);

        panel.add(label);

        return panel;
    }

    private JPanel createKeyBindPanel(String item){
        JPanel panel = new SettingsOptionPanel();

        panel.add(new JLabel(item));

        JRoundedButton button = new JRoundedButton(getKeyBindFor(item), item.toLowerCase());
        keyBindButtonList.add(button);

        panel.add(button);

        return panel;
    }

    private JPanel createTogglePanel(String item){
        JPanel panel = new SettingsOptionPanel();

        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setBackground(Color.red);

        JLabel itemText = new JLabel(item);
        itemText.setFont(generateFont(15));
        itemText.setForeground(Color.black);

        leftPanel.add(itemText);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.cyan);

        ButtonIcon button = new ButtonIcon(64, false);
        button.addActionListener(null);

        rightPanel.add(button);

        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
    }

    private JPanel footer(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(accentColor);

        JRoundedButton saveButton = new JRoundedButton("Save");
        saveButton.addActionListener(e -> {
            save();
            System.out.println(settingsFile);
            JsonFile.writeln(settingsFile, "key_binds","Test Bind","Bound Item");
        });

        JRoundedButton exitButton = new JRoundedButton("Exit");
        exitButton.addActionListener(e -> exit());

        panel.add(saveButton);
        panel.add(exitButton);
        return panel;
    }

    private String getKeyBindFor(String id){
        String result = JsonFile.read(settingsFile, "key_binds",id.toLowerCase());
        if(result.toLowerCase().contains("key")){
            return null;
        } else {
            return result;
        }
    }

    private void save(){
        System.out.println("Saving Settings...");

        for(JRoundedButton button : keyBindButtonList){
            JsonFile.writeln(settingsFile, "key_binds",button.getId(),button.getText());
        }

        for(ButtonIcon button : toggleButtonList){
            //
        }
    }

    private void exit(){
        changeCurrentPanel(mainMenu, this, false);
        mainMenu.timer.start();
    }

    private static class JRoundedButton extends JButton{
        private String id;
        private boolean renderBorder = false;

        public JRoundedButton(String text){
            super(text);
            setFocusable(false);
        }

        public JRoundedButton(String text, String id){
            this(text);
            this.id = id;
            renderBorder = true;
        }

        public String getId(){return id;}

        @Override
        protected void paintComponent(Graphics g){
            //super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.white);
            g2d.fillRoundRect(0,0,getWidth(),getHeight(),25,25);

            g2d.setColor(Color.black);
            g2d.setFont(generateFont(15));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = ((getHeight() + textHeight) / 2) - 1;
            g2d.drawString(getText(), x, y);
        }

        @Override protected void paintBorder(Graphics g){
            if(renderBorder){
                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.black);
                g2d.drawRoundRect(0,0,getWidth(),getHeight(), 25, 25);
            }
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
