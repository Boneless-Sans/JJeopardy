package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Arrays;

/*
√  - √
X  - \bX\b
!! - !!(.*?)!!
 */
public class Main extends JFrame implements KeyListener {
    public static String fileName = "template.json";
    public static String settingsFile;
    public boolean doFullScreen = false;
    public static boolean playAudio = false;

    public static KeyEvent lastKeyPressed;

    //init global panels
    public static MainMenu mainMenu;
    public static GameBoard gameBoard;
    public static JCard jCard;
    public static BoardFactory boardFactory;

    public static int screenWidth, screenHeight;

    public static void main(String[] args) throws IOException {

        //setup settings file
        setupSettings();

        //run program
        SwingUtilities.invokeLater(() -> new Main(args));
    }

    private static void setupSettings(){
        File file = new File(System.getProperty("user.home") + "/settings.json");

        try {
            if (!file.exists() && file.createNewFile()) {
                System.out.println("Settings File Missing, Created New File At: " + file.getAbsolutePath());
                try(FileWriter writer = new FileWriter(file)){
                    writer.write("""
                            {
                              "key_binds": {
                                "exit": "esc",
                                "advance": "space"
                              },
                              "misc": {
                                "fullscreen": "false",
                                "audio": "true"
                              }
                            }
                            """);
                }
            }
        } catch (Exception e){
            System.out.println("Error Creating File: \n" + e);
        }
        settingsFile = file.getAbsolutePath();
    }

    public Main(String... arg){
        setTitle("Jeopardy!");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        screenWidth = 1200;
        screenHeight = 700;

        try {
            if(System.getProperty("os.name").equalsIgnoreCase("windows")) {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } else {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
                case "settings": {
                    add(new Settings());
                    break;
                }
            }
        } else {
            add(mainMenu = new MainMenu(this));
        }

        revalidate();
        repaint();
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
        lastKeyPressed = e;
        //esc handler
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read(settingsFile, "keyBinds", "exit")))) {
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
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read(settingsFile, "keyBinds", "continue")))) {
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