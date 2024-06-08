package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;

/*
√  - √
X  - \bX\b
!! - !!(.*?)!!
 */
public class Main extends JFrame implements KeyListener {
    public static String fileName;
    public boolean doFullScreen = false;
    public static boolean playAudio = false;

    //init global panels
    public static MainMenu mainMenu;
    public static GameBoard gameBoard;
    public static JCard jCard;
    public static BoardFactory boardFactory;

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> new Main(args));
    }

    public Main(String... arg){
        setTitle("Jeopardy!");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        assert arg != null;
        GeneralUtils.renderIcon(128);

        //set icon
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("mac")) {
            Taskbar.getTaskbar().setIconImage(GeneralUtils.renderIcon(128));
        } else {
            setIconImage(GeneralUtils.renderIcon(128));
        }

        mainMenu = new MainMenu(this);

        init(Arrays.toString(arg));
        setVisible(true);
        addKeyListener(this);
    }

    public void setName(String newName){
        setTitle(newName);
    }

    private void init(String arg){
        int startIndex = arg.indexOf("-");
        int endIndex = arg.indexOf("]");
        if(startIndex != -1 && endIndex != -1) {
            switch (arg.substring(startIndex + 1, endIndex)) {
                case "card": {
                    add(gameBoard = new GameBoard(4));
                    break;
                }
                case "board": {
                    add(boardFactory = new BoardFactory(this));
                    break;
                }
            }
        } else {
            add(mainMenu = new MainMenu(this));
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

            revalidate();
            repaint();
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
            else if(boardFactory.factoryIsActive) {
                boardFactory.exit();
            }
        }

        //continue handler - Dante
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "continue")))) {
            if(gameBoard.jCardIsActive) {
                jCard.moveQuestion();
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