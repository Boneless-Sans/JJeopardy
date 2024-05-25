package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.boneless.util.GeneralUtils.changeCurrentPanel;
import static com.boneless.util.GeneralUtils.generateFont;

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

    public static void main(String[] args) throws IOException {
//            BufferedImage icon = ImageIO.read(new File("icon.png"));
//            BufferedImage rounded = makeRoundedCorner(icon, 20);
//            ImageIO.write(rounded, "png", new File("icon.rounded.png"));

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
            setIconImage(new ImageIcon(createIcon(Color.red, Color.green,"Test", GeneralUtils.generateFont(50))).getImage());
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
            add(new GameBoard());
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
            if(MAIN_MENU.menuIsActive) { //menu
                System.exit(0);
            }
            else if (GAME_BOARD.GameIsActive) { //game board
              GAME_BOARD.exit();
            }
            else if(GAME_BOARD.jCardIsActive) { //jCard
                jCard.exit();
            }
        }

        //continue handler - Dante
        if (String.valueOf(e.getKeyChar()).equals(parseKeyStrokeInput(JsonFile.read("settings.json", "keyBinds", "continue")))) {
            if(GAME_BOARD.jCardIsActive) {
                jCard.advance();
            }
        }

        //reset handler
        if(e.getKeyChar() == 'r'){
            reset();
        }
    }
    public static void reset(){
        MAIN_MENU.menuIsActive = true;
        GAME_BOARD.GameIsActive = false;
        GAME_BOARD.jCardIsActive = false;
        new Main();
    }
    public static BufferedImage createIcon(Color colorGradient1, Color colorGradient2, String text, Font font) {
        // Define text size based on font
        int textSize = 24; // Default text size, you can adjust this if needed

        // Get the preferred size for the text
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempGraphics = tempImage.createGraphics();
        tempGraphics.setFont(font);
        FontMetrics fm = tempGraphics.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        textSize = Math.min((int) (0.8 * textWidth), (int) (0.5 * textHeight)); // Adjust text size if needed
        tempGraphics.dispose();

        // Define image width and height based on text size
        int width = textWidth + 40; // Adjusted to fit the text with some padding
        int height = textHeight + 20; // Adjusted to fit the text with some padding

        // Create a buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(0, 0, colorGradient1, width, height, colorGradient2);
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, 20, 20));

        // Draw text
        g2d.setColor(Color.BLACK); // You can change text color here
        g2d.setFont(font.deriveFont((float) textSize));
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);

        // Cartoon-like text border
        g2d.setColor(Color.BLACK); // You can change the border color here
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    g2d.drawString(text, x + i, y + j);
                }
            }
        }

        g2d.dispose();

        return image;
    }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}