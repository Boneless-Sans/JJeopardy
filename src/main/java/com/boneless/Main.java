package com.boneless;

import com.boneless.util.JsonFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.boneless.util.GeneralUtils.changeCurrentPanel;

/*
Road map (semi in order) X (incomplete / work in progress) | √ (complete)
    Main menu | √
    Frame changing system | √
    Rework settings | √
    Create main board | X
        -make title header | X
        -get buttons to create the info card | √
        -have buttons read points from json | √
        -create teams sub panel | X
    Create question card (JCard) | X
        -layout | X
        -key binds | X
        -data from json | √
        -animations | X
    Create board factory | X --not sure if we can do this in time
        -figure out the layout | X
    Implement key binds and have them match settings.json | X broken >:(
    Fix Json shit | √ ? forgot why this is here
 */
public class Main extends JFrame implements KeyListener {
    private static boolean isDev = false;
    public static String fileName = "devBoard.json";
    private boolean doFullScreen = false;

    //init global panels
    public static final MainMenu MAIN_MENU = new MainMenu();
    public static final GameBoard GAME_BOARD = new GameBoard();
    public static JCard jCard;
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
        String OS = System.getProperty("os.name").toLowerCase();
        String iconDir = "src/main/resources/icon/icon.png";
        if (OS.contains("windows")) {
            setIconImage(new ImageIcon(iconDir).getImage());
        } else {
            try {
                File imageFile = new File(iconDir);
                Image image = ImageIO.read(imageFile);
                Taskbar.getTaskbar().setIconImage(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        init();
        setVisible(true);
        addKeyListener(this);
    }
    public void setName(String newName){
        setTitle(newName);
    }
    private void init(){
        if(!isDev) {
            add(MAIN_MENU);
        } else {
            add(new GameBoard().init("devBoard.json"));
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
        System.out.println("Key typed: " + e.getKeyChar());
        //fullscreen handler
        if(String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "fullscreen")))){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if(doFullScreen){
                doFullScreen = false;
                setLocation((screenSize.width / 2) - 1200 / 2, (screenSize.height / 2) - 720 / 2);
                setSize(1600,900);
            }else{
                doFullScreen = true;
                setLocation(0,0);
                setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            }
        }
        //esc handler
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "exit")))) {
            if(MAIN_MENU.menuIsActive) { //menu
                System.exit(0);
            }
            else if (GAME_BOARD.GameIsActive) { //game board
                int size = 32;

                BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = bufferedImage.createGraphics();

                g2d.fillRect(0,0,size,size);

                g2d.setColor(Color.red);
                g2d.setFont(g2d.getFont().deriveFont(5f));
                g2d.drawString("?", 0,0);

                g2d.dispose();

                String[] responses = {
                        "Exit","Continue"
                };
                int answer = JOptionPane.showOptionDialog(
                        null,
                        "Change me message",
                        "change me title",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(bufferedImage), responses, 0);
                if (answer == 0) {
                    GAME_BOARD.GameIsActive = false;
                    MAIN_MENU.menuIsActive = true;
                    changeCurrentPanel(MAIN_MENU, GAME_BOARD);
                }
            }
            else if(GAME_BOARD.jCardIsActive) { //jCard
                jCard.exit();
            }
        }
        //reset handler
        if(e.getKeyChar() == 'r'){
            dispose();
            new Main();
        }
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}