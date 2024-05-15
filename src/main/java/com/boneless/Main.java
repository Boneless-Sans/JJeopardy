package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.Print;
import com.boneless.util.ScrollGridPanel;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
Road map (semi in order) X (incomplete / work in progress) | √ (complete)
    Fix frame changing system | X
        >remove all objects | X
        >add new JPanel? | X
        -needs to preserve last state of a panel, else the board gets fucked a little. this might work automatically just bc of how java works. | X
    Rework settings | X
        -make it work with new frame system | X
    Create main board | X
        -make title header | X
        -change program title name to board name | X
        -get buttons to create the info card | X
    Create question card | X
    Create board factory | X
        -figure out the layout | X
        -todo: fill out todo after last todo. then that todo should replace this todo with the new todo and that is quite the todo list of todos
    Implement key binds and have them match settings.json | X

    General Json list           | X
        -get questions          | X
        -get answers            | X
        -get scores             | X
        -get button color       | X
        -get background color   | X
        -get header color       | X
        -get team panel color   | X
        -get font color         | X

    fixme list:
        -Tile overlap in main menu
 */
public class Main extends JFrame {
    public static boolean isDev = false;
    private String fileName = "devBoard.json";
    private static final ArrayList<JButton> menuButtons = new ArrayList<>();
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
        setUndecorated(true);
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
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        JLabel title = new JLabel("Jeopardy!");
        title.setFont(generateFont(50));

        titlePanel.add(title, gbc);

        //button setup
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);

        buttonsPanel.add(createMenuButton("Start Game", 0));
        buttonsPanel.add(createMenuButton("Choose Board File", 1));
        buttonsPanel.add(createMenuButton("Board Creator -fuck me-", 2));
        buttonsPanel.add(createMenuButton("Settings", 3));
        buttonsPanel.add(createMenuButton("Exit", 4));

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        return panel;
    }
    private JButton createMenuButton(String text, int UUID){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(generateFont(15));
        menuButtons.add(button);
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
                    changeCurrentPanel(new Settings());
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
        //removeAll();
        add(panelToSet);
    }
    public static void changeButtonsState(boolean isEnabled){
        for (JButton menuButton : menuButtons) {
            menuButton.setEnabled(isEnabled);
        }
    }
}