package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.Print;
import com.boneless.util.ScrollGridPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMenu extends ScrollGridPanel {
    private String fileName = "devBoard.json";
    private static final ArrayList<JButton> menuButtons = new ArrayList<>();
    public MainMenu(){
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
        title.setForeground(Color.white); //todo: change to a json read

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
    }
    private JButton createMenuButton(String text, int UUID){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(generateFont(15));
        menuButtons.add(button);
        button.addActionListener(e -> {
            switch (UUID){ //perhaps not the best way of doing this, but it works for now
                case 0: { //start
                    changeCurrentPanel(new GameBoard(fileName));
                    break;
                }
                case 1: { //board file
                    JFileChooser chooser = new JFileChooser();
                    Print.print(chooser.showOpenDialog(null));

                    break;
                }
                case 2: { //board creator
                    //
                    break;
                }
                case 3: { //settings
                    changeCurrentPanel(new Settings(this));
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
    private Font generateFont(int fontSize){
        return new Font(
                JsonFile.read(fileName, "data","font"),
                Font.PLAIN,
                fontSize
        );
    }
    private void setFile(String fileName){
        this.fileName = fileName;
    }
    private void changeCurrentPanel(JPanel panelToSet){
        Container parent = getParent();

        parent.remove(this);
        parent.add(panelToSet);

        parent.revalidate();
        parent.repaint();
    }
}
