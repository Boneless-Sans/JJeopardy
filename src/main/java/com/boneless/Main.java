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
        -get buttons to create the info card | √
        -have buttons read points from json | X
        -create teams sub panel | X
    Create question card (JCard) | X
        -layout | X
        -key binds | X
        -data from json | X
        -animations | X
    Create board factory | X --not sure if we can do this in time
        -figure out the layout | X
    Implement key binds and have them match settings.json | √
    Fix Json shit | X
 */
public class Main extends JFrame implements KeyListener {
    private static boolean isDev = false;
    public static String fileName;
    private boolean doFullScreen = false;

    //init all the panels
    public static final MainMenu menu = new MainMenu();
    public static final GameBoard gameboard = new GameBoard();
    public static final JCard jCard = new JCard();
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
//    public void setName(String newName){
//        setTitle(newName);
//    }
    private void init(){
        if(!isDev) {
            //add(new MainMenu());
        } else {
            //add(new GameBoard().init("devBoard.json"));
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
        if(!menu.isActive && !jCard.isActive) {
            if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "exit")))) {
                System.exit(0);
            }
        }
        if(e.getKeyChar() == 'r'){
            new Main();
        }
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}