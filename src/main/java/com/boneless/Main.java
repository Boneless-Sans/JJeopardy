package com.boneless;

import com.boneless.util.ScrollGridPanel;
import com.boneless.util.SystemUI;

import javax.swing.*;

/*
Road map (semi in order) X (incomplete / work in progress) | √ (complete)
    Main menu | X
        -General layout | X
        -Functionality (frame changing, have bool for disabling ) | √
            -Start          | √
            -board chooser  | √
            -board creator  | √
            -settings       | √
            -exit           | √
    Frame changing system | √
    Rework settings | X
        -make it work with new frame system | √
        -change checkbox system for non png use | X
    Create main board | X
        -make title header | X
        -change program title name to board name | X
        -get buttons to create the info card | X
        -have buttons read points from json
        -launch JCard
    Create question card (JCard) | X
        -layout
        -key binds
        -data from json
    Create board factory | X
        -figure out the layout | X
        -todo: fill out todo after last todo. then that todo should replace this todo with the new todo and that is quite the todo list of todos
    Implement key binds and have them match settings.json | X

    General Json list
        -get questions
        -get answers
        -get scores
        -get button color
        -get background color
        -get header color
        -get team panel color
        -get font color
        -get title color

    fixme list:
        -Tile overlap in main menu | X
 */
public class Main extends JFrame {
    public static boolean isDev = false;
    private ScrollGridPanel panel;
    private MainMenu menu;
    public static void main(String[] args) {
        if(args != null && args.length > 0){
            isDev = args[0].contains("dev");
        }
        new Main();
    }
    public Main(){
        menu = new MainMenu();
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
            add(menu);
        } else {
            //add(new GameBoard(fileName));
            add(new Settings(menu));
        }
    }
}