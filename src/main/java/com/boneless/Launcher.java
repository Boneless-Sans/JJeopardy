package com.boneless;

import com.boneless.util.IconResize;
import com.boneless.util.JsonFile;
import com.boneless.util.ScrollGridPanel;
import com.boneless.util.SystemUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Launcher {
    private static JButton buttonStart;
    private static JButton buttonLoadData;
    private static JButton buttonCreateBoard;
    private static JButton buttonSettings;
    private static JButton buttonExit;
    private static JFrame frame;
    private static final Game game = new Game();
    private static Color headerColor = new Color(21, 27, 75);
    private static Color backgroundColor = new Color(42,54,152);
    private static Color textColor = new Color(255,255,255);
    private static final String[] teamDropDown= {
            "1 Team", "2 Teams", "3 Teams", "4 Teams", "5 Teams", "6 Teams", "7 Teams", "8 Teams", "9 Teams", "10 Teams"
    };
    public static void main(String[] args) {
        initUI();
    }
    public static void initUI(){
        headerColor = stringToColor(game.getFileName(), "header_color");
        backgroundColor = stringToColor(game.getFileName(), "background_color");
        textColor = stringToColor(game.getFileName(), "font_color");
        SystemUI.set();

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        ScrollGridPanel backgroundPanel = new ScrollGridPanel();
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Create UI elements
        JLabel titleText = new JLabel("JJeopardy!");
        titleText.setFont(new Font("Arial", Font.PLAIN, 30));
        titleText.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleText);

        buttonStart = new JButton("Start Game");
        buttonLoadData = new JButton("Load Board File");
        buttonCreateBoard = new JButton("Create Game Board");
        buttonSettings = new JButton("Settings");
        buttonExit = new JButton("Exit");

        // Set up buttons
        setupButton(buttonStart);
        setupButton(buttonLoadData);
        setupButton(buttonCreateBoard);
        setupButton(buttonSettings);
        setupButton(buttonExit);

        buttonStart.addActionListener(e -> addTeams());
        buttonLoadData.addActionListener(e -> loadBoardFile());

        buttonCreateBoard.addActionListener(e -> {
            changeButtonState(false);
            new GameBoardFactory();
            frame.dispose();
        });

        buttonSettings.addActionListener(e -> {
            changeButtonState(false);
            new Settings();
        });

        buttonExit.addActionListener(e -> System.exit(0));

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(buttonStart);
        buttonsPanel.add(buttonLoadData);
        buttonsPanel.add(buttonCreateBoard);
        buttonsPanel.add(buttonSettings);
        buttonsPanel.add(buttonExit);

        JPanel fileText = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fileText.setOpaque(false);

        JLabel currentFile = new JLabel("Current File: " + game.getFileName());
        currentFile.setFont(new Font("Arial", Font.PLAIN, 15));
        currentFile.setForeground(Color.white);

        fileText.add(currentFile);

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
        backgroundPanel.add(buttonsPanel, BorderLayout.CENTER);
        backgroundPanel.add(fileText, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    private static Color stringToColor(String fileName, String color){
        String initColor = JsonFile.read(fileName, "data",color);
        String[] split = initColor.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    private static void setupButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(Color.lightGray);
        button.setFont(new Font("Arial", Font.PLAIN, 17));
    }
    private static void loadBoardFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Launcher.game.setFileName(String.valueOf(file));
            System.out.println(file);
            // Update the UI with the new file name
            updateFileNameLabel();
        }

        // Enable buttons after file selection
        changeButtonState(true);
    }
    private static void updateFileNameLabel() {
        JLabel currentFile = new JLabel("Current File: " + Launcher.game.getFileName());
        currentFile.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel fileText = (JPanel) ((JPanel) frame.getContentPane().getComponent(0)).getComponent(2);
        fileText.removeAll();
        fileText.add(currentFile);
        fileText.revalidate();
        fileText.repaint();
    }
    public static void changeButtonState(boolean enable) {
        buttonStart.setEnabled(enable);
        buttonLoadData.setEnabled(enable);
        buttonExit.setEnabled(enable);
        buttonSettings.setEnabled(enable);
        buttonCreateBoard.setEnabled(enable);
    }
    @SuppressWarnings("MagicConstant")
    private static void addTeams(){
        frame.setVisible(false);
        changeButtonState(false);
        JFrame tFrame = new JFrame();
        tFrame.setSize(450,400);
        tFrame.setUndecorated(true);
        tFrame.setLocationRelativeTo(null);
        tFrame.setLayout(new BorderLayout());
        tFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Color fontColor = stringToColor(game.getFileName(), "header_font_color");
        String fontName = JsonFile.read(game.getFileName(), "data","font_name");
        int fontSize = Integer.parseInt(JsonFile.read(game.getFileName(),"data","header_font_size"));
        int fontType = switch (JsonFile.read(game.getFileName(), "data", "header_font_type")) {
            case "Font.BOLD" -> 1;
            case "Font.ITALIC" -> 2;
            default -> 0;
        };
        Font font = new Font(fontName, fontType, fontSize);

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(headerColor);

        JLabel numTeams = new JLabel("<html>" + JsonFile.read(game.getFileName(), "data", "title") + "</html>");
        numTeams.setFont(new Font(fontName, fontType, 30));
        numTeams.setForeground(fontColor);

        titlePanel.add(numTeams);

        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));

        JCheckBox checkBox = new JCheckBox("Fullscreen");
        checkBox.setFont(font);
        checkBox.setFocusable(false);
        checkBox.setBackground(backgroundColor);
        checkBox.setForeground(textColor);
        checkBox.setIcon(new IconResize("cross_mark.png", 35,35).getImage());
        checkBox.addActionListener(e -> {
            if(checkBox.isSelected()){
                checkBox.setIcon(new IconResize("check_mark.png", 35,35).getImage());
            }else{
                checkBox.setIcon(new IconResize("cross_mark.png",35,35).getImage());
            }
        });

        JComboBox<String> dropDown = new JComboBox<>(teamDropDown);
        dropDown.setFont(font);
        dropDown.setFocusable(false);
        dropDown.setPreferredSize(new Dimension(140,34));

        JButton start = new JButton("Start");
        start.setFont(font);
        start.setFocusable(false);
        start.addActionListener(e -> {
            frame.dispose();
            tFrame.dispose();
            game.initUI(checkBox.isSelected(), dropDown.getSelectedIndex() + 1);
        });

        JButton cancel = new JButton("Cancel");
        cancel.setFont(font);
        cancel.setFocusable(false);
        cancel.addActionListener(e -> {
            tFrame.dispose();
            frame.setVisible(true);
            changeButtonState(true);
        });

        String rawKeyBind = JsonFile.read("settings.json","keyBinds", "fullscreen");
        String keyBind = rawKeyBind.substring(0,1).toUpperCase() + rawKeyBind.substring(1);
        JLabel fullscreenText = new JLabel("Or Press \"" + keyBind + "\" To Enter / Exit Fullscreen");
        fullscreenText.setFont(new Font(fontName, Font.PLAIN, 15));
        fullscreenText.setForeground(fontColor);

        mainPanel.add(createBlankPanel(backgroundColor, 40));
        mainPanel.add(dropDown);
        mainPanel.add(start);
        mainPanel.add(cancel);
        mainPanel.add(createBlankPanel(backgroundColor, 40));
        mainPanel.add(createBlankPanel(backgroundColor, 80));
        mainPanel.add(checkBox);
        mainPanel.add(createBlankPanel(backgroundColor, 80));
        mainPanel.add(fullscreenText);

        tFrame.add(titlePanel, BorderLayout.NORTH);
        tFrame.add(mainPanel, BorderLayout.CENTER);
        tFrame.setVisible(true);
    }
    private static JPanel createBlankPanel(Color color, int size){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(size, size));
        panel.setBackground(color);

        return panel;
    }
}
