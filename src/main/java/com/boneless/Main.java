package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.ScrollGridPanel;
import com.boneless.util.SystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
Road map (semi in order) X (incomplete / work in progress) | √ (complete)
    Main menu | √
    Frame changing system | √
    Rework settings | √
    Create main board | X
        -make title header | X
        -change program title name to board name | X
        -get buttons to create the info card | X
        -have buttons read points from json | X
        -launch JCard | X
        -create teams sub panel | X
    Create question card (JCard) | X
        -layout | X
        -key binds | X
        -data from json | X
        -animations | X
    Create board factory | X
        -figure out the layout | X
    Implement key binds and have them match settings.json | √

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
        -Tile overlap in main menu | ? unfixable
 */
public class Main extends JFrame implements KeyListener {
    private static boolean isDev = false;
    public static String fileName;
    private boolean doFullScreen = false;
    public static void main(String[] args) {
        if(args != null && args.length > 0){
            isDev = args[0].contains("dev");
        }
        new Main();
    }
    public Main(){
        setTitle("Jeopardy!");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        init();
        setVisible(true);
        addKeyListener(this);
    }
    private void init(){
        if(!isDev) {
            add(new MainMenu());
        } else {
            add(new GameBoard("devBoard.json"));
        }
    }
    private String parseKeyStrokeInput(String keyStrokeCode){
        return switch (keyStrokeCode){
            case "Esc" -> "\u001B";
            case "Space" -> " ";
            case "Enter" -> "\n";
            case "Backspace" -> "\b";
            default -> keyStrokeCode.toLowerCase();
        };
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "fullscreen")))){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if(doFullScreen){
                doFullScreen = false;
                setLocation((screenSize.width / 2) - 1600 / 2, (screenSize.height / 2) - 900 / 2);
                setSize(1600,900);
            }else{
                doFullScreen = true;
                setLocation(0,0);
                setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            }
        }
        if(String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "exit")))){
            System.exit(0);
        }
        if(e.getKeyChar() == 'r'){
            new Main();
        }
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}