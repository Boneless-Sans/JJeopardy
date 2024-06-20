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
    public static String fileName;
    public static String settingsFile;
    public boolean doFullScreen = false;
    public static boolean playAudio = false;

    public static KeyEvent lastKeyPressed;

    //init global panels
    public static MainMenu mainMenu;
    public static GameBoard gameBoard;
    public static JCard jCard;
    public static BoardFactory boardFactory;
    public static Settings settings;

    public static int frameWidth, frameHeight;
    public static int screenWidth, screenHeight;

    public static void main(String[] args) throws IOException {
        //setup settings file
        checkSettingsFileIntegrity();

        //setup sizes
        frameWidth = Integer.parseInt(JsonFile.read(settingsFile, "screen", "screen_resolution").split("x")[0]);
        String rawHeight = JsonFile.read(settingsFile, "screen", "screen_resolution").split("x")[1];
        frameHeight = rawHeight.contains("(") ? Integer.parseInt(rawHeight.split(" ")[0]) : Integer.parseInt(rawHeight);
        screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //run program
        SwingUtilities.invokeLater(() -> new Main(args));
    }

    private static void checkSettingsFileIntegrity(){
        File file = new File(System.getProperty("user.home") + "/settings.json");

        String[] items = {
                //ensure user has a valid settings file, set value using this format: key#item#default state (i.e. true, false, "name here")
                "key_binds#exit@Esc",
                "key_binds#advance@Space",

                "screen#fullscreen@false",
                "screen#screen_resolution@1600x900 (Default)",
                "screen#always_on_top@false",

                "misc#audio@false",
                "misc#reduce_animations@false",
                "misc#play_animations@true",
                "misc#play_background_ani@true"
        };

        try {
            settingsFile = file.getAbsolutePath();

            if(file.createNewFile()) System.out.println("Settings file created");

            FileReader fr = new FileReader(settingsFile);

            if(String.valueOf(fr.read()).equals("-1")) {
                System.out.println("Ran");
                try (FileWriter fw = new FileWriter(settingsFile)) {
                    fw.write("{}");
                }
            }

            fr.close();

            for (String item : items) {
                String key = item.split("#")[0];
                String itemName =  item.split("@")[0].split("#")[1];
                String bind = item.split("@")[1];

                if(JsonFile.read(settingsFile, key, itemName).contains("-1") || JsonFile.read(settingsFile, key, itemName).contains("key")) {
                    JsonFile.writeln(settingsFile, key, itemName, bind);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Main(String... arg){
        setTitle("Jeopardy!");
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(Boolean.parseBoolean(JsonFile.read(settingsFile, "misc", "always_on_top")));

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
            String file = "temp.json";
            switch (arg.substring(startIndex + 1, endIndex)) {
                case "card": {
                    fileName = file;
                    add(gameBoard = new GameBoard(4, this));
                    break;
                }
                case "board": {
                    add(boardFactory = new BoardFactory(this, fileName));
                    break;
                }
                case "settings": {
                    fileName = file;
                    add(new Settings(this));
                    break;
                }
                case "dev": {
                    fileName = file;
                    add(mainMenu = new MainMenu(this));
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
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read(settingsFile, "key_binds", "exit")))) {
            if(mainMenu.menuIsActive) { //menu
                System.exit(0);
            }
            else if (gameBoard != null && gameBoard.GameIsActive) { //game board
                gameBoard.exit();
            }
            else if(gameBoard != null && gameBoard.jCardIsActive) { //jCard
                jCard.exit();
            }
            else if(boardFactory != null && boardFactory.factoryIsActive) {
                boardFactory.exit(false);
            }
            else if(settings != null && settings.settingsIsActive){
                settings.exit();
            }
        }

        //continue handler - Dante
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read(settingsFile, "key_binds", "continue")))) {
            if(gameBoard.jCardIsActive) {
                jCard.moveQuestion();
            }
        }
    }

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}