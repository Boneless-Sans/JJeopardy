package com.boneless;

import com.boneless.util.JsonFile;
import com.boneless.util.KeyBindManager;
import com.boneless.util.ScrollGridPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements KeyListener {
    //link to GDoc https://docs.google.com/document/d/1IFx3SDvnhjzMkc3hN28-G_46JCnie7hxkWVV7ez0ENA/edit?usp=sharing
    private final int RESX = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int RESY = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final String FILENAME = "devBoard.json";
    //private final Font mainFont;
    public static void main(String[] args) {
        new Main(args);
    }
    public Main(String[] args){
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        init();
        setVisible(true);
    }
    private void init(){
        //todo: manage screens via adding and removing jPanels from the main frame

        //Menu panel
        add(menuPanel());

        //Main board panel
        //JPanel boardPanel = boardPanel();
    }
    //Menu Panel
    private JPanel menuPanel(){
        JPanel panel = new JPanel();

        panel.add(new ScrollGridPanel());
        return panel;
    }
    //Main board panel
    private JPanel boardPanel(){
        JPanel panel = new JPanel();
        panel.add(headPanel(), BorderLayout.NORTH);
        panel.add(mainBoard(), BorderLayout.CENTER);
        panel.add(teamPanel(), BorderLayout.SOUTH);
        return panel;
    }
    //board header panel
    private JPanel headPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.blue);

        return panel;
    }
    //panel to contain the main board grid
    private JPanel mainBoard(){
        JPanel panel = new JPanel();

        //setup values from json
        int boardX = Integer.parseInt(JsonFile.read(FILENAME, "data", "categories"));
        int boardY = Integer.parseInt(JsonFile.read(FILENAME, "data", "rows"));;
        panel.setLayout(new GridLayout(boardY, boardX,0,0));

        for(int i = 0; i < boardX * boardY;i++){
            Color color = switch (i % 6){
                case 0 -> Color.red;
                case 1 -> Color.orange;
                case 2 -> Color.yellow;
                case 3 -> Color.green;
                case 4 -> Color.blue;
                case 5 -> Color.magenta;
                default -> Color.cyan;
            };
            panel.add(createBoardButton(0, "question", "answer", color));
        }

        return panel;
    }
    private JButton createBoardButton(int points, String question, String answer, Color backgroundColor){
        JButton button = new JButton(String.valueOf(points));
        button.setBackground(backgroundColor);
        return button;
    }
    //panel to contain team panels
    private JPanel teamPanel(){
        JPanel panel = new JPanel();

        return panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("keypressed");
    }
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}