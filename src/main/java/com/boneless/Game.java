package com.boneless;

import com.boneless.util.IconResize;
import com.boneless.util.JsonFile;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JFrame implements KeyListener {
    private static String fileName = "dev_board.json";
    private static boolean canOpen = true;
    private static boolean doFullScreen;
    private int teamCount;
    private int lastCardPoints;
    private String[] teams;
    public static void setDoFullScreen(boolean doFullScreen) {
        Game.doFullScreen = doFullScreen;
    }
    //todo: add saving with hashmaps <JButton, Boolean>
    public void initUI(boolean doFullScreen, int teamCount){
        if(doFullScreen){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            setDoFullScreen(true);
        }else {
            setSize(1600, 900);
            setDoFullScreen(false);
        }
        this.teamCount = teamCount;

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);

        JPanel title = new JPanel(new FlowLayout());
        title.setBackground(parseColor("header_title_background")); //board header

        JLabel titleText = new JLabel();
        titleText.setText(JsonFile.read(getFileName(), "data", "title"));
        titleText.setFont(parseFont(title,25,"header_title"));
        titleText.setForeground(parseColor("header_title_font_color")); //Header Title Font Color
        title.add(titleText);

        JPanel gameBoard = new JPanel(new GridLayout());
        gameBoard.setPreferredSize(new Dimension(0,300));
        gameBoard.setBackground(parseColor("background_color"));

        JScrollPane teams = createTeamsPanel();

        add(title, BorderLayout.NORTH);
        add(createBoard(getFileName()), BorderLayout.CENTER);
        add(teams, BorderLayout.SOUTH);
        setVisible(true);
    }
    private Font parseFont(JComponent parent, int scaleFactor, String type){
        String fontName = JsonFile.read(fileName,"data",type + "_font");
        int fontType = parseFontType(type);
        int fontSize = calcFontSize(parent, scaleFactor);
        return new Font(fontName,fontType,fontSize);
    }
    private int parseFontType(String fontType){
        return switch (JsonFile.read(fileName,"data",fontType)) {
            case "plain" -> 0;
            case "bold" -> 1;
            case "italic" -> 2;
            default -> 99;
        };
    }
    private int calcFontSize(JComponent obj, int a){
        return 25;
    }
    private Color parseColor(String color){
        String initColor = JsonFile.read(fileName, "data",color);
        String[] split = initColor.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    private JPanel createBoard(String fileName) {
        JPanel gameBoard = new JPanel();

        int sizeX = Integer.parseInt(JsonFile.read(fileName, "data", "columns"));
        int sizeY = Integer.parseInt(JsonFile.read(fileName, "data", "rows"));

        GridLayout board = new GridLayout(sizeX, sizeY, 5,5);
        gameBoard.setLayout(board);
        gameBoard.setBackground(parseColor("board_grate_color"));

        JLabel[] cats = createTitles(fileName, sizeX, sizeY);
        JButton[] buttons = createRows(fileName, sizeX, sizeY);

        for (JLabel label : cats) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(parseColor("header_panel_color"));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = 0;

            //label.setFont(parseFont(panel,2,"header_panel"));
            label.setFont(testFont(panel,label,"header_panel"));
            label.setFocusable(false);
            //label.setForeground(parseColor("header_panel_font_color"));

            panel.add(label, gbc);
            gameBoard.add(panel);
        }

        for(JButton button : buttons){ //main board buttons
            button.setFont(testFont(button, button, "board_button"));
            button.setBackground(parseColor("board_button_color"));
            //button.setForeground(parseColor("board_button_font_color")); //fixme <- Color wont set here
            //button.setBorderPainted(false);
            button.setFocusable(false);

            gameBoard.add(button);
        }

        return gameBoard;
    }
    private Font testFont(JComponent parent, JComponent item, String type) {
        // Assuming JsonFile.read() reads font data from a file
        String fontName = JsonFile.read(fileName, "data", type + "_font");
        int fontType = parseFontType(type);

        // Calculate font size based on panel dimensions and text length
        int panelWidth = parent.getWidth();
        int panelHeight = parent.getHeight();
        int textLength = 0;

        if (item instanceof JButton) {
            textLength = ((JButton) item).getText().length();
        } else if (item instanceof JLabel) {
            textLength = ((JLabel) item).getText().length();
        }

        // Adjust this formula as needed
        int fontSize = (int) Math.min(Math.min((double) panelWidth / textLength, (double) panelHeight), 30);

        return new Font(fontName, fontType, fontSize);
    }

    private JScrollPane createTeamsPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(parseColor("footer_background_color"));
        panel.setBorder(null);

        for(int i = 0; i < teamCount; i++){
            panel.add(createGap(80, parseColor("footer_background_color")));
            panel.add(createTeamPanel(new Team("Team " + (i + 1))));
        }
        panel.add(createGap(80, parseColor("footer_background_color")));
        JScrollPane pane = new JScrollPane(panel);
        pane.setPreferredSize(new Dimension(getWidth(),120)); //Height controller
        pane.setBorder(null);
        return pane;
    }
    private JPanel createTeamPanel(Team team){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(parseColor("team_panel_color"));
        panel.setPreferredSize(new Dimension(150,110)); //Panel height controller
        panel.setBorder(null);

        JTextField teamName = new JTextField(team.getTeamName());
        teamName.setPreferredSize(new Dimension(125,25));
        teamName.setBackground(parseColor("team_name_field_color"));
        teamName.setBorder(null);
        teamName.setHorizontalAlignment(JTextField.CENTER);

        JPanel line = new JPanel();
        line.setBackground(parseColor("team_line_color"));
        line.setPreferredSize(new Dimension(130,1));

        JTextField score = new JTextField(String.valueOf(team.getPoints()));
        score.setFont(parseFont(panel,25,"team_font"));
        score.setHorizontalAlignment(JTextField.CENTER);
        score.setBorder(null);
        score.setBackground(parseColor("team_score_background_color"));
        //score.setForeground(parseColor("team_name_font_color"));
        score.setPreferredSize(new Dimension(125,25));

        panel.add(teamName);
        panel.add(line);
        panel.add(score);
        panel.add(createScoreButton("dom.png", true, score));
        panel.add(createGap(25, null));
        panel.add(createScoreButton("dom.png", false, score));
        return panel;
    }
    private JButton createScoreButton(String image, boolean add, JTextField score){
        int buttonSize = 35;
        JButton button = new JButton(new IconResize("dom.png", buttonSize, buttonSize).getImage());
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setFocusable(false);
        button.setFont(parseFont(button,1,"team_panel_color")); //?
        button.addActionListener(e -> {
            int currentScore = Integer.parseInt(score.getText());
            if(add){
                currentScore += lastCardPoints;
                score.setText(String.valueOf(currentScore));
            }else{
                currentScore -= lastCardPoints;
                score.setText(String.valueOf(currentScore));
            }
        });
        return button;
    }
    private JPanel createGap(int size, Color color){
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(size,size));
        return panel;
    }
    private JLabel[] createTitles(String filename, int sizeX, int sizeY) {
        //it adds left to right, so there will need to be some math to calculate when to add titles and questions

        String[] titles = new String[sizeX]; // Declare the array of sizeX elements

        for (int i = 1; i <= sizeX; i++) {
            titles[i-1] = JsonFile.read(filename, "column_" + i, "title"); // Read the title and store it in the array
        }
        //draw each line via their row number
        JLabel[] label = new JLabel[sizeX];
        for(int i = 0; i < sizeX; i++){
            label[i] = new JLabel( "<html>" + titles[i] + "</html>");
        }
        return label;
    }
    public JButton[] createRows(String filename, int sizeX, int sizeY) {
        String[] rowData = new String[sizeX * sizeY];

        for (int row = 1; row <= sizeY; row++) {
            for (int column = 1; column <= sizeX; column++) {
                // Assuming JsonFile.readWithThreeKeys returns a single string for each column and row
                String columnData = JsonFile.readWithThreeKeys(filename, "column_" + column, "points", "row_" + row);

                // Calculate the array index for the current row and column
                int arrayIndex = (row - 1) * sizeX + (column - 1);

                // Ensure that the arrayIndex is within bounds
                if (arrayIndex < rowData.length) {
                    // Do something with rowData (e.g., store it in an array)
                    rowData[arrayIndex] = columnData;
                } else {
                    System.err.println("Index out of bounds: " + arrayIndex);
                }
            }
        }

        JButton[] buttons = new JButton[sizeX * sizeY];
        for (int row = 1; row <= sizeY; row++) {
            for (int column = 1; column <= sizeX; column++) {
                int arrayIndex = (row - 1) * sizeX + (column - 1);
                String buttonName = "Button_" + row + "_" + column;

                buttons[arrayIndex] = new JButton(rowData[arrayIndex]);
                buttons[arrayIndex].setName(buttonName);
                buttons[arrayIndex].addActionListener(new ButtonActionListener(row, column));
                //add the cube thing to the launcher
            }
        }
        return buttons;
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
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // ActionListener for the buttons
    private class ButtonActionListener implements ActionListener {
        private int row;
        private int column;
        public ButtonActionListener(int row, int column) {
            this.row = row;
            this.column = column;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            clickedButton.setEnabled(false);
            lastCardPoints = Integer.parseInt(clickedButton.getText());
            if (canOpen) {
                canOpen = false;
                new InfoCard(
                        JsonFile.readWithThreeKeys(fileName, "column_" + column, "questions", "row_" + row),
                        JsonFile.readWithThreeKeys(fileName, "column_" + column, "answers", "row_" + row),
                        fileName,
                        JsonFile.read(fileName, "column_" + column, "title"),
                        Integer.parseInt(JsonFile.readWithThreeKeys(fileName, "column_" + column, "points", "row_" + row)),
                        doFullScreen,
                        clickedButton);

                // Reset the flag to allow opening new InfoCards
                canOpen = true;
            }
        }
    }
    public void setFileName(String file){
        this.fileName = file;
    }

    public String getFileName(){
        return fileName;
    }
}