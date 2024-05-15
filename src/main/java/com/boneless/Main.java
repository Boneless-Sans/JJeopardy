package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.Print;
import com.boneless.util.ScrollGridPanel;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    //link to GDoc https://docs.google.com/document/d/1IFx3SDvnhjzMkc3hN28-G_46JCnie7hxkWVV7ez0ENA/edit?usp=sharing
    public static boolean isDev = false;
    private String fileName = "devBoard.json";
    public static void main(String[] args) {
        if(args != null && args.length > 0){
            isDev = args[0].contains("dev");
        }
        new Main();
    }
    public Main(){
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }
    private void init(){
        //todo: manage screens via adding and removing jPanels from the main frame 
        SystemUI.set();
        if(!isDev) {
            add(menuPanel());
        } else {
            add(new GameBoard(fileName));
        }
    }
    //Menu Panel
    private JPanel menuPanel(){
        ScrollGridPanel panel = new ScrollGridPanel();
        panel.setLayout(new BorderLayout());

        //todo: add Start, File Chooser, Settings, Creator, Exit Buttons, title, and current file

        //title text, pretty self-explanatory
        JLabel title = new JLabel("Jeopardy!");
        title.setFont(generateFont(25));

        //button setup
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);

        buttonsPanel.add(createMenuButton("Start Game", 0));
        buttonsPanel.add(createMenuButton("Choose Board File", 1));
        buttonsPanel.add(createMenuButton("Board Creator -fuck me-", 2));
        buttonsPanel.add(createMenuButton("Settings", 3));

        buttonsPanel.add(createMenuButton("Exit", 4));

        panel.add(title, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        return panel;
    }
    private JButton createMenuButton(String text, int UUID){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(generateFont(15));
        button.addActionListener(e -> {
            switch (UUID){ //perhaps not the best way of doing this, but it works for now
                case 0: { //start
                    //
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
                    //
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
}