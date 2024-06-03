package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/*
√  - √
X  - \bX\b
!! - !!(.*?)!!
Road map (semi in order) X (incomplete / work in progress) | √ (complete)
    Main menu | √
    Frame changing system | √
    Rework settings | √
    Create main board | √
    Create question card (JCard) | X
        -layout | X?
        -key binds | √
        -data from json | √
        -animations | X
    Create board factory | X
        -figure out the layout | √
        -create context header | X
            -File | X
                -New | X
                -Open | X
                -Save | X
                -Exit | X
            -Help | X
                -Not sure | X
                -Redirect to joke site | X
        -create left board panel | X
            -Emulate GameBoard
            -Functionality with fakeCard
            -Update board when updates are made with right panel
        -create right settings panel | X
            - !!moved to class!!
        -figure out more | √
    Create Application Icon | √ todo: change icon to not use text. for reference, use https://developer.apple.com/design/human-interface-guidelines/app-icons
    Implement key binds and have them match settings.json | √ !!No ARG Only!!
 */
public class Main extends JFrame implements KeyListener {
    private static boolean isDev = false;
    public static String fileName;
    public boolean doFullScreen = false;
    public static boolean playAudio = false;

    //init global panels
    public static MainMenu mainMenu;
    public static GameBoard gameBoard;
    public static JCard jCard;

    public static void main(String[] args) throws IOException {
        if(args != null && args.length > 0){
            isDev = args[0].contains("dev");
        }
        SwingUtilities.invokeLater(Main::new);
    }

    public Main(){
        setTitle("Jeopardy!");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        GeneralUtils.renderIcon();


        //set icon
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("mac")) {
            Taskbar.getTaskbar().setIconImage(GeneralUtils.renderIcon());
        } else {
            setIconImage(GeneralUtils.renderIcon());
        }

        mainMenu = new MainMenu(this);

        init();
        setVisible(true);
        addKeyListener(this);
    }

    public void setName(String newName){
        setTitle(newName);
    }

    private void init(){
        if(!isDev) {
            add(mainMenu);
        } else {
            //add(gameBoard = new GameBoard(4));
            add(new BoardFactory(this));
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
    public void keyTyped(KeyEvent e) {//fullscreen handler
        if(String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "fullscreen")))){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if(doFullScreen){
                doFullScreen = false;
                setLocation((screenSize.width / 2) - 1200 / 2, (screenSize.height / 2) - 720 / 2);
                setSize(1200,720);
            }else{
                doFullScreen = true;
                setLocation(0,0);
                setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            }
        }

        //esc handler
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "exit")))) {
            if(mainMenu.menuIsActive) { //menu
                System.exit(0);
            }
            else if (gameBoard.GameIsActive) { //game board
                gameBoard.exit();
            }
            else if(gameBoard.jCardIsActive) { //jCard
                jCard.exit();
            }
        }

        //continue handler - Dante
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "continue")))) {
            if(gameBoard.jCardIsActive) {
                jCard.advance();
            }
        }

        //reset handler
        if(e.getKeyChar() == 'r'){
            reset();
        }
    }

    public static void reset(){
        mainMenu.menuIsActive = true;
        gameBoard.GameIsActive = false;
        gameBoard.jCardIsActive = false;
        new Main();
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}