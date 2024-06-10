package com.boneless;

import com.boneless.util.ButtonIcon;
import com.boneless.util.GeneralUtils;

import javax.swing.*;
import java.awt.*;

import static com.boneless.util.GeneralUtils.*;

public class Settings extends JPanel {
    /* slide bottom to middle
    -General | X
        -Null Safety | X
        -Exit Confirmation | X
    -Main Body | X
        -JScrollPane
        -Rounded Boxes for Key Binds | X
            -Key Bind Text | X
            -Key Bind Setter | X
        -Rounded Boxes for Toggles | X
            -Item Name | X
            -State | X
    -Footer | X
        -Save Button | X
        -Exit Button | X
     */
    private Color mainColor;
    private Color accentColor;

    public Settings() {
        setLayout(new BorderLayout());

        accentColor = new Color(
                clamp(mainColor.getRed()   - 40),
                clamp(mainColor.getGreen() - 40),
                clamp(mainColor.getBlue()  - 40));
        add(createMainBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JScrollPane createMainBody(){
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setPaint(new GradientPaint(0, 0, mainColor, getWidth() ,getHeight(), accentColor));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.add(new JLabel("Test"));

        return new HiddenScroller(panel, false){
            {
                setBackground(mainColor);
            }
        };
    }

    private JPanel createFooter(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JRoundedButton saveButton = new JRoundedButton("Save");

        JRoundedButton exitButton = new JRoundedButton("Exit");

        panel.add(saveButton);
        panel.add(exitButton);
        return panel;
    }

    private void save(){
        //
    }

    private void exit(){
        //
    }

    private static class JRoundedButton extends JButton{
        public JRoundedButton(String text){
            super(text);
            setFocusable(false);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.lightGray);
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

        @Override protected void paintBorder(Graphics g){} //disable
    }
}
