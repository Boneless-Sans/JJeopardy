package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.ScrollGridPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static com.boneless.Main.fileName;
import static com.boneless.util.GeneralUtils.*;

public class MainMenu extends ScrollGridPanel {
    private final ArrayList<JButton> buttonsList = new ArrayList<>();
    private final JLabel currentFile;
    public MainMenu(){
        //fileName = "devBoard.json";
        setLayout(new BorderLayout());

        //title text, pretty self-explanatory
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        JLabel title = new JLabel("Jeopardy!");
        title.setFont(generateFont(50));
        title.setForeground(Color.white);

        //add(titlePanel, BorderLayout.SOUTH);

        //title.add()
        titlePanel.add(title, gbc);

        //button setup
        JPanel menuParentPanel = new JPanel(new GridBagLayout());
        menuParentPanel.setOpaque(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setOpaque(false);
        buttonsPanel.setPreferredSize(new Dimension(150,250));

        buttonsPanel.add(createMenuButton("Start Game", 0));
        buttonsPanel.add(createMenuButton("Choose Board File", 1));
        buttonsPanel.add(createMenuButton("Board Creator", 2));
        buttonsPanel.add(createMenuButton("Settings", 3));
        buttonsPanel.add(createMenuButton("Exit", 4));

        add(titlePanel, BorderLayout.NORTH);

        menuParentPanel.add(buttonsPanel, gbc);

        add(menuParentPanel, BorderLayout.CENTER);

        currentFile = new JLabel(fileName == null ? "No Board Selected" : "Current Board: " + fileName);
        currentFile.setFont(generateFont(15));
        currentFile.setForeground(Color.white);
        add(currentFile, BorderLayout.SOUTH);
    }
    private JButton createMenuButton(String text, int UUID){
        JButton button = new JButton(text){
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = 170;
                return size;
            }
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isEnabled()) {
                    g.setColor(Color.lightGray);
                    g.setFont(getFont());
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(getText());
                    int textHeight = fm.getAscent();
                    int x = (getWidth() - textWidth) / 2;
                    int y = (getHeight() + textHeight) / 2 - 4;
                    g.drawString(getText(), x, y);
                }
            }
        };
        button.setFocusable(false);
        button.setFont(generateFont(15));
        try { //im not sure why != null isn't working, but whatever this works
            if (UUID == 0 && fileName.isEmpty()) { //disable start button if there is no current board file
                button.setEnabled(false);
            }
        } catch (NullPointerException ignore){
            System.out.println("File is null, using defaults...");
            button.setEnabled(false);
        }
        buttonsList.add(button);
        button.addActionListener(e -> {
            switch (UUID){ //perhaps not the best way of doing this, but it works for now
                case 0: { //start
                    changeCurrentPanel(new GameBoard(fileName), this);
                    break;
                }
                case 1: { //board file
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(new FileNameExtensionFilter("Json File", "json"));

                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        System.out.println(file);
                        changeFileName(String.valueOf(file));
                        changeColor(parseColor(JsonFile.read(fileName, "data", "global_color")));
                    }
                    break;
                }
                case 2: { //board creator
                    changeCurrentPanel(new BoardFactory(), this);
                    break;
                }
                case 3: { //settings
                    changeCurrentPanel(new Settings(), this);
                    break;
                }
                case 4: { //exit
                    System.exit(0);
                    break;
                }
            }
        });

        return button;
    }
    private void changeFileName(String newFile){
        fileName = newFile.substring(newFile.lastIndexOf("\\") + 1);
        currentFile.setText("Current Board: " + fileName);
        buttonsList.get(0).setEnabled(!fileName.isEmpty());
    }
}
